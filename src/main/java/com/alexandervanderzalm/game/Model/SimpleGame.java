package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Model.Logger.LogCollection;
import com.alexandervanderzalm.game.Model.Logger.LogUtility;
import com.alexandervanderzalm.game.Model.Pits.*;
import com.alexandervanderzalm.game.Model.Turn.TurnData;

import java.util.stream.Collectors;

/* SimpleGame
    A simple implementation of the kalaha game.

    # TurnData
    The primary job is to produce valid TurnData based on a simple input,
    according to the specified rules.

    # Logging
    Logs all the transformations of the game.
     - PitLogs - Mark actual pit/stone changes. Such as the grabbing of stones
     - TextLogs - Mark important logical events, such as an extra turn or a capture.

    # Setup
    Controls the flow of logic for setting up a game new or from a previous state.

    # Turns
    Controls the flow of logic for doing a turn.

    *** Note this is the simple version using primarily if conditional flow.
    *   This as opposed to the LogicGameFramework & Ruleset variant, which uses a more
    *   Reactive approach.
    *   TODO LogicGameFramework & Ruleset (Currently not fully implemented)
 */
public class SimpleGame implements IGame{

    private PitCollection<IKalahaPit> pits;
    private GameState nextTurnState;
    private int currentPlayer = 0;
    private int currentTurn = 0;
    private LogCollection logger = new LogCollection();

    public SimpleGame() {
        //this.pits = new ArrayList<>();
    }

    @Override
    public TurnData SetupNewGame() {
        pits = new PitCollection<>( PitUtil.CreatePits(14,6));
        currentTurn = 0;
        nextTurnState = GameState.TurnP1;

        // Log all the changes to setup the board
        pits.pList.stream()
                .filter((p) -> !p.IsKalaha())
                .forEach((p) -> Log(p, 6));

        Log("Initialized a new kalaha game.");
        Log(String.format("Turn %d - %s - New Turn",currentTurn + 1, LogPlayer(currentPlayer)));
        return GameToTurnData();
    }

    @Override
    public TurnData SetUpGameFromTurnData(TurnData data) {
        pits = new PitCollection<>(PitUtil.CreatePitsFromTurnData(data));
        nextTurnState = data.NextTurnState;
        currentPlayer = nextTurnState == GameState.TurnP1 ? 0 : 1; // Not really relevant
        currentTurn = data.Turn;
        LogUtility.SetLoggerFromTurnData(logger,data);
        return GameToTurnData();
    }

    @Override
    public TurnData DoTurn(Integer SelectedIndex) {

        // Check for valid input
        TurnData error = InputUtility.IsValidInput(SelectedIndex,GameToTurnData());
        if(error != null)
            return error;

        // Prepare gameState next round & current player index
        currentTurn++;
        currentPlayer = nextTurnState == GameState.TurnP1 ? 0 : 1;
        FlipGameState();

        // Grab all from the currently selected pit
        IKalahaPit current = pits.Get(SelectedIndex);
        Integer hand = current.GrabAll();
        int handBackup = hand;

        // Log pickup
        Log(String.format("Turn %d - %s - Grabbed %d stones from pit %d.",
                currentTurn,
                LogPlayer(currentPlayer),
                hand,
                SelectedIndex
        ));

        Log(current, -hand);

        // Drop one in the right pit except for the opposite players pit
        while(hand > 0) {
            //for (int i = 0; i < hand; i++) {
            current = pits.Right(current);

            // Skip when landed upon oponents kalaha
            if (current.IsKalaha() && current.GetPlayer() != currentPlayer) {
                Log(String.format("Turn %d - Skipped dropping a stone at opponents Kalaha.",currentTurn));
                continue;
            }

            if (current.GetPlayer() == currentPlayer && hand == 1) {
                // #########  Extra turn
                if (current.IsKalaha()) {
                    //Extra turn on last stone in hand drop
                    Log(String.format("Turn %d - %s - Gains an Extra Turn for dropping the last stone in his own Kalaha.",
                            currentTurn,
                            LogPlayer(currentPlayer)
                    ));
                    FlipGameState();

                } // ######## Capture opposite?
                else if (current.Amount() == 0) {
                    // TODO do the proper order....
                    // For logging purposes add first
                    current.Add(1);
                    Log(current, 1);
                    hand--;

                    // Then remove
                    current.GrabAll();
                    Log(current, -1);

                    // add both opposite & the last one into own kalaha
                    IKalahaPit opposite = pits.Opposite(current);
                    // Grab stones of the opposite
                    int stonesCaptured = opposite.GrabAll();
                    if (stonesCaptured > 0) Log(opposite, -stonesCaptured);

                    // Add both the hand and the captured stones to the kalaha
                    IKalahaPit kalaha = pits.KalahaOfPlayer(currentPlayer);
                    kalaha.Add(1);
                    Log(kalaha, 1);

                    kalaha.Add(stonesCaptured);
                    if (stonesCaptured > 0) Log(kalaha, stonesCaptured);

                    Log(String.format("Turn %d - %s - Captured %d stones from opposite pit %d and scored %d.",
                            currentTurn,
                            LogPlayer(currentPlayer),
                            stonesCaptured,
                            pits.IndexOf(opposite),
                            stonesCaptured+1
                    ));

                    continue;
                }
            }

            // Drop stone & log
            current.Add(1);
            hand--;
            Log(current, 1); // No text log of drops (feels to spammy)
        }
        Log(String.format("%sDropped %d stones, clockwise one by one ending at %d.",
                LogUtility.LogStart(currentPlayer,currentTurn),
                handBackup,
                pits.IndexOf(current)
        ));
        // Check for end of game
        // --One of the sides is empty
        if(pits.pList.stream().filter((p) -> !p.IsKalaha() && p.Amount() == 0 && p.GetPlayer() == 0).count() == 6 || pits.pList.stream().filter((p) ->!p.IsKalaha() && p.Amount() == 0 && p.GetPlayer() == 1).count() == 6) {
            // Add all pits to their respective owners
            pits.pList.stream().filter((p) -> !p.IsKalaha()).forEach((p) -> pits.KalahaOfPlayer(p.GetPlayer()).Add(p.GrabAll()));
            SetWinner();
            return GameToTurnData();
        }

        // --Unwinnable condition detected
        int pitsInField = pits.pList.stream()
                .filter((p) -> !p.IsKalaha())
                .map((p) -> p.Amount())
                .reduce(0, (x,y) -> x + y);

        int p1 = pits.KalahaOfPlayer1().Amount();
        int p2 = pits.KalahaOfPlayer2().Amount();
        if(p1 + pitsInField < p2 || p2 + pitsInField < p1) {
            Log(String.format("Turn %d - Unwinnable condition detected. %s: %d Field: %d %s: %d.",currentTurn ,LogPlayer(0),p1,pitsInField,LogPlayer(1),p2));
            SetWinner();
            return GameToTurnData();
        }

        // Log new turn
        Log(String.format("Turn %d - %s - New Turn",currentTurn +1, LogPlayer(nextTurnState == GameState.TurnP1? 0 : 1)));

        return GameToTurnData();
    }

    private void SetWinner(){

        if(pits.KalahaOfPlayer1().Amount() > pits.KalahaOfPlayer2().Amount()) {
            nextTurnState = GameState.WinP1;
            currentPlayer = 0;
        }else {
            nextTurnState = GameState.WinP2;
            currentPlayer = 1;
        }
        Log(String.format("Congratulations %s!", LogPlayer(currentPlayer)));
        Log(String.format("Refresh the page to play again.", LogPlayer(currentPlayer)));
    }

    private void FlipGameState(){
        nextTurnState = nextTurnState == GameState.TurnP1 ? GameState.TurnP2 : GameState.TurnP1;
    }

    private TurnData GameToTurnData(){
        TurnData data = new TurnData();
        // Transform pit data into clean rest data
        data.Pits = pits.pList.stream().map(x -> new KalahaPitData(x.GetPlayer(),x.IsKalaha(),x.Amount())).collect(Collectors.toList());
        //data.Pits = pits.pList.stream().map(x -> x.Data()).collect(Collectors.toList()); // Not a deep copy...
        data.NextTurnState = nextTurnState;
        data.Turn = currentTurn;
        data.Player1Score = pits.KalahaOfPlayer1().Amount();//pits.Get(0).Amount();
        data.Player2Score = pits.KalahaOfPlayer2().Amount();// pits.Get(pits.Pits.size()/2).Amount();
        data.Log = logger.GetLogData();
        logger.ClearLogs();
        return data;
    }

    private void Log(IKalahaPit pit, int amount){
        LogUtility.Log(logger,pits,pit,amount);
    }

    private void Log(String textLog){
        LogUtility.Log(logger,textLog);
    }

    private String LogPlayer(int playerIndex){
        return LogUtility.LogPlayer(playerIndex);
    }
}
