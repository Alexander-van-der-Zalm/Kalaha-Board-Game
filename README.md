# Kalaha-Board-Game 
**Web technology based game.**
**Back-End: Java Spring Boot (starter-web & devtools), Rest API, MVC architecture, JUnit, Gradle, (TomCat local server) -- Intellij** <br/>
**Front-End: HTML, CSS, Vanilla Javascript -- VisualCode**<br/>
<a href="https://alexandervanderzalm.com/">Made by Alexander van der Zalm</a> - <a href="https://github.com/Bahamutho/Kalaha-Board-Game">Github</a>

## Setup

1. Clone the git/unzipping to a local directory
2. Run the Application found in Root/src/main/java/com.alexandervanderzalm.game/Application
3. Open browser and go to <a href="http://localhost:8080/">http://localhost:8080/</a>  *~Tested primarily in Chrome (Firefox looks good too)*

## Basic architecture
The game is implemented using a basic MVC like architecture with a Rest API.
### Controller
**Rest API via GameController -> GameService -> IGame (Model)**

A spring boot rest service that via the service interacts with the actual model/game logic. </br>
The Rest API receives: TurnDataInput (a selected index) and returns TurnData. 

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

The model consists of an implementation of IGame of which there are two and a few support packages (logger, pits, turn and game). A simple one (SimpleKalahaGame) and a reactive one (ReactiveKalahaGame). 

**SimpleKalahaGame**

The simple one is the result of quick coding to get it working as quickly as possible (made in more or less 2-3 hours), over time used more utility, but primarily it has quite a lot of responsibilities:

* Controlling the turn flow 
* Implementing the rules
* Creating the turnData from the games variables
* Logging changes
* Setting up a game anew and from a turn

**ReactieKalahaGame**

The basic idea is to separate some of those responsibilities to their own classes to minimize the responsibilities per class. This is done by essentially having a skeleton **ReactieKalahaGame** class, which has various (reactive) method hooks in **ReactiveGameFunctionality** and **ReactivePit** (OnGrab/OnAdd) and holds the *runtime* data in **GameData** (which is responsible for changing to and from TurnData). 

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

## Test Driven Development

## What is relatively easy to do from here?

**Make variants via ReactiveGameRules**

With the current separation comes relatively ease to extend and iterate over the functionality of the game, in the case that this was an actual iterative product. A game variant pretty much comes down to writing a new entry into ReactiveGameRules class.

**Serve games from a repository**

By adding a repository that implements a database for the following entities:
* Game Entities (GameID, TurnDataID) -> A pointer to the most recent turnData.
* TurnData Entities (GameID, TurnDataID, TurnData) -> The actual turn data.

**Replay system**

Since IGame can be initialized from a random turnData state, it is relatively easy to change to any given turn. Hence you could easily query turns from a game to view and play onwards from.

**Online lobbies**

Admittedly this will take a little more work, but should be feasible to do from here on out. It would consist of creating a lobby view, the above mentioned game/turn repositories, a few changes to the logic (a non-interactive gameState otherPlayer) and a way to push changes to the client once another player has finished their turn.
