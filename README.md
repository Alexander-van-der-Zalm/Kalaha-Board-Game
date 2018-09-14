# Kalaha-Board-Game 
**Web technology based game.**<br/>
**Back-End: Java Spring Boot (starter-web & devtools), Rest API, MVC architecture, JUnit, Gradle, (TomCat local server) -- Intellij** <br/>
**Front-End: HTML, CSS, Vanilla Javascript -- VisualCode**<br/>
<a href="https://alexandervanderzalm.com/">Made by Alexander van der Zalm</a> - <a href="https://github.com/Bahamutho/Kalaha-Board-Game">Github</a>

## Setup

1. Clone the git/unzip to a local directory
2. Run the Application found in Root/src/main/java/com.alexandervanderzalm.game/Application.java
3. Open browser and go to <a href="http://localhost:8080/">http://localhost:8080/</a>  *~Tested primarily in Chrome (Firefox looks good too)*
4. *Optionally change the \src\main\java\com.alexandervanderzalm.game\Controllers\GameService.java file to run the simple variant.*

## Basic architecture
The game is implemented using a basic MVC like architecture with a Rest API.
### Controller
**Rest API via GameController -> GameService -> IGame (Model)**

A spring boot rest service that via the service interacts with the actual model/game logic. </br>
The Rest API receives: TurnDataInput (a selected index) and returns TurnData. Requests mapping:
* Get to /game - Sets up a new game and returns a TurnData json representation of it.
* Post to /game - Expects a json representing TurnDataInput, does the turn based on the index in TurnDataInput and returns a TurnData json representation of the resolved turn.

**TurnData**

A simple data object that hold the minimal representation of a game at any given state. Note that this should be sufficient information for the "IGame" implementation to create a fully functional game at a random state. This is received by the view to create a visual representation of the game at that state. 

```java
public class TurnData {
    public int Turn = 0;
    public int Player1Score = 0;
    public int Player2Score = 0;
    public GameState NextTurnState = GameState.TurnP1;      // What will the turn be for the view?
    public List<KalahaPitData> Pits;                        // The data stored in the pits (stones, player, isKalaha)
    public List<LogData> Log;                               // Stores all the pit changes & text events (extra_turn, capture, etc.)
}
```

**IGame**

This is the interface which a game should conform to. 

```Java
  public interface IGame {
    TurnData DoTurn(Integer SelectedIndex);       // Only needs an index - it is assumed that the game does its own validation
    TurnData SetupNewGame();                      // Setup a new game with a default field (14 pits, 2 are kalaha the rest have 6 stones
    TurnData SetUpGameFromTurnData(TurnData data);// Set a game up from any given state
}
```
### Model

The model consists of an implementation of IGame of which there are two and a few support packages (logger, pits, turn and game). The two IGame implementations are the simple one (SimpleKalahaGame) and the reactive one (ReactiveKalahaGame). 

**SimpleKalahaGame**

The simple one is the result of quick coding to get it working as quickly as possible (made in more or less 2-3 hours), over time used more utility, but primarily it has quite a lot of responsibilities:

* Controlling the turn flow 
* Implementing the rules
* Creating the turnData from the games variables
* Logging changes
* Setting up a game anew and from a turn

**ReactiveKalahaGame**

The basic idea is to separate some of the earlier mentioned responsibilities to their own classes to minimize the responsibilities per class. This is done by essentially having a skeleton **ReactiveKalahaGame** class, which has various (reactive) method hooks in **ReactiveGameFunctionality** and **ReactivePit** (OnGrab/OnAdd) and holds the *runtime* data in **GameData** (which is responsible for changing to and from TurnData). 

```Java
public class ReactiveKalahaGame implements IGame {

    public ReactiveGameFunctionality Functionality;   // Has various hooks for functionality
    public GameData Data;                             // Holds the game runtime data (pits, currenturn, currentplayer, etc.)
    ...
```

The functionality is wired in ReactiveGameRules, like so:

```Java
// Add basic pit logging capability (used for view animation for example)
game.Data.Pits.Stream().forEach((pit) ->{
    pit.OnAdd.Add((stones) -> LogUtility.LogPit(game.Data, pit, stones));
    pit.OnGrab.Add((stones) -> LogUtility.LogPit(game.Data, pit, stones));
});

// Add extra turn rule check only to Kalahas
game.Data.Pits.Stream()
    .filter((p) -> p.Data().isKalaha)
    .forEach((pit) ->{
        pit.OnAdd.Add((stones) -> {
            // Check if the hand is almost empty && if the kalaha is of the current player
            if(game.Data.CurrentHand == 1 && pit.Data().player == game.Data.CurrentPlayer){

                // Add an entry to the logger
                LogUtility.Log(game.Data.Logger,String.format("%sGains an Extra Turn for dropping the last stone in his own Kalaha.",
                        LogUtility.LogStart(game.Data.CurrentPlayer ,  game.Data.CurrentTurn)
                ));
                
                // Change the gameState to the same player
                GameUtil.FlipGameState(game.Data);
            }
        });
});
```

**Support packages**

* Model/Game - Consists of the earlier mentioned separated aspects, a GameFactory (for testing) and GameUtility (for general game functionality).
* Model/Logger - Log changes in PitLog (for pit transforms) and TextLog (for general text messages) to note the important changes of the game. Based on the simple data type LogData.
* Model/Pits - Contains a few pit variations (some with functional hooks and some without), pitcollection & utility and the simple datatype PitData.
* Model/Turn - The simple datatype TurnData and some utility
* Utility - General non app specific method collections and method schedulers to make it easier to work reactively.

### View

<img src="https://alexandervanderzalm.com/wp-content/uploads/2018/09/Kalaha-Board-Game-Alexander-van-der-Zalm.png" data-canonical-src="https://alexandervanderzalm.com/wp-content/uploads/2018/09/Kalaha-Board-Game-Alexander-van-der-Zalm.png" width="600" />

The view was made with HTML,CSS, and Vanilla Javascript. The javascript contains:
* Rest API Get & Post calls to /game on refresh and on pit.onclick.
* A simple delayed animation system to show whats happening pit transformation wise. 
* A simple log system to further make it clear what is happening.

*I know the view was not important. However it is nice to note that I in fact do enjoy working front-end as well and am particularly interested in Typescript, WebGL and WebAssembly.*

The html file can be found in: /Root/src/main/resources/public/index.html.

## Test Driven Development

During the development more tests were added as the game got together. Once the simplegame variant was made a tweak to the original SimpleGameTest made it so that both the new ReactiveGame and the old SimpleGame implementation could directly be tested with all these tests. Then it was simply making sure all the tests passed and developing accordingly. With actual testing a few bugs were identified and added to the test catalog, which would then again also run and test the old implementation. 

Of particular note is the Model/GameTest test file which tests most of the rules and interactions.

In this project tests were made primarily for the more error prone parts of the project and has decent coverage in that regard.

### Running the JUNIT4 Tests
Via Intellij right click on the /Root/test/ folder and run all tests.

## What is relatively easy to do from here?

**Make variants via ReactiveGameRules**

With the current separation comes relatively ease to extend and iterate over the functionality of the game, in the case that this was an actual iterative product. A game variant pretty much comes down to writing hookup logic in a new entry into ReactiveGameRules class.

**Serve games from a repository**

By adding a repository that implements a database for the following entities:
* Game Entities (GameID, TurnDataID) -> A pointer to the most recent turnData.
* TurnData Entities (GameID, TurnDataID, TurnData) -> The actual turn data.

**Replay system**

Since IGame can be initialized from a random turnData state, it is relatively easy to change to any given turn. Hence you could easily query turns from a game to view and play onwards from.

**Online lobbies**

Admittedly this will take a little more work, but should be feasible to do from here on out. It would consist of creating a lobby view, the above mentioned game/turn repositories, a few changes to the logic (a non-interactive gameState otherPlayer) and a way to push changes to the client once another player has finished their turn.
