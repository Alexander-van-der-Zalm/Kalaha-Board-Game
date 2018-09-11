package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Model.Logger.LogCollection;
import com.alexandervanderzalm.game.Model.Logger.PitLog;
import com.alexandervanderzalm.game.Model.Pits.IKalahaPit;
import com.alexandervanderzalm.game.Model.Pits.PitCollection;
import com.alexandervanderzalm.game.Model.Pits.PitUtil;
import com.alexandervanderzalm.game.Model.Turn.TurnData;

import java.util.stream.Collectors;

public class SimpleGame implements IGame{

    //private List<IKalahaPit> pits;
    private PitCollection<IKalahaPit> pits;
    private GameState nextTurnState;
    private int currentPlayer = 0;
    private int currentTurn = 0;
    private LogCollection logger = new LogCollection();

    public SimpleGame() {
        //this.pits = new ArrayList<>();
    }

    @Override
    public TurnData InitializeGame() {
        int fieldsPerPlayer = 6;
        int startStones = 6;
        pits = new PitCollection<>( PitUtil.CreatePits(14,6));
        currentTurn = 0;
        nextTurnState = GameState.TurnP1;

        // Log all the changes
        pits.pList.stream()
                .filter((p) -> !p.IsKalaha())
                .forEach((p) -> logger.Log(new PitLog(p, pits.pList.indexOf(p), 6, p.Amount())));

        System.out.println("Initialized a kalaha game.");
        return GameToTurnData();
    }

    @Override
    public TurnData DoTurn(Integer SelectedIndex) {

        // Prepare gameState next round & current player index
        currentTurn++;
        currentPlayer = nextTurnState == GameState.TurnP1 ? 0 : 1;
        FlipGameState();

        // Grab all from the currently selected pit
        System.out.println("Grabbed pits @ " + SelectedIndex);
        IKalahaPit current = pits.Get(SelectedIndex);
        Integer hand = current.GrabAll();
        logger.Log(new PitLog(current, pits.IndexOf(current), -hand, current.Amount()));

        // Drop one in the right pit except for the opposite players pit
        while(hand > 0) {
        //for (int i = 0; i < hand; i++) {
            current = pits.Right(current);

            // Skip when landed upon oponents kalaha
            if(current.IsKalaha() && current.GetPlayer() != currentPlayer) {
                System.out.println(String.format("Skipped dropping a stone opponents Kalaha @ %s", pits.pList.indexOf(current)));
                continue;
            }
            if(current.GetPlayer() == currentPlayer && hand == 1)
            {
                // Extra turn
                if(current.IsKalaha()){
                    //Extra turn on last stone in hand drop
                    System.out.println(String.format("Extra turn for %s", (currentPlayer == 0 ? "P1" : "P2")));
                    FlipGameState();
                } // Capture opposite?
                else if(current.Amount() == 0){
                    System.out.println(String.format("Capture from %d opposite @ %d ", pits.pList.indexOf(current), pits.pList.indexOf(pits.Opposite(current))));
                    // add both opposite & the last one into own kalaha
                    IKalahaPit opposite = pits.Opposite(current);
                    int stonesCaptured = opposite.GrabAll();
                    pits.KalahaOfPlayer(currentPlayer).Add(stonesCaptured + 1);
                    logger.Log(new PitLog(opposite, pits.IndexOf(opposite), -stonesCaptured, opposite.Amount()));
                    logger.Log(new PitLog(current, pits.IndexOf(current), stonesCaptured, current.Amount() - 1));
                    logger.Log(new PitLog(current, pits.IndexOf(current), 1, current.Amount()));
                    hand--;
                    continue;
                }
            }

            System.out.println("Dropped a stone @ " + pits.pList.indexOf(current));
            // Drop stone
            current.Add(1);
            hand--;
            logger.Log(new PitLog(current, pits.IndexOf(current), 1, current.Amount()));
        }

        // Check for end of game
        // --One of the sides is empty
        if(pits.pList.stream().filter((p) -> !p.IsKalaha() && p.Amount() == 0 && p.GetPlayer() == 0).count() == 6 || pits.pList.stream().filter((p) ->!p.IsKalaha() && p.Amount() == 0 && p.GetPlayer() == 1).count() == 6) {
            // Add all pits to their respective owners
            pits.pList.stream().filter((p) -> !p.IsKalaha()).forEach((p) -> pits.KalahaOfPlayer(p.GetPlayer()).Add(p.GrabAll()));
            SetWinner();

        }

        // --Unwinnable condition detected
        int pitsInField = pits.pList.stream()
                .filter((p) -> !p.IsKalaha())
                .map((p) -> p.Amount())
                .reduce(0, (x,y) -> x + y);

        if(pits.KalahaOfPlayer1().Amount() + pitsInField < pits.KalahaOfPlayer2().Amount() ||
           pits.KalahaOfPlayer2().Amount() + pitsInField < pits.KalahaOfPlayer1().Amount()) {
            System.out.println("Unwinnable condition detected");
            SetWinner();
        }
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
        System.out.println("We have a winner: " + (currentPlayer == 0 ? "Player1" : "Player2"));
    }

    private void FlipGameState(){
        nextTurnState = nextTurnState == GameState.TurnP1 ? GameState.TurnP2 : GameState.TurnP1;
    }

    private TurnData GameToTurnData(){
        TurnData data = new TurnData();
        // Transform pit data into clean rest data
        // data.Pits = pits.stream().map(x -> new KalahaPitData(x.GetPlayer(),x.IsKalaha(),x.Amount())).collect(Collectors.toList());
        data.Pits = pits.pList.stream().map(x -> x.Data()).collect(Collectors.toList());
        data.NextTurnState = nextTurnState;
        data.Turn = currentTurn;
        data.Player1Score = pits.KalahaOfPlayer1().Amount();//pits.Get(0).Amount();
        data.Player2Score = pits.KalahaOfPlayer2().Amount();// pits.Get(pits.Pits.size()/2).Amount();
        data.Log = logger.GetLogData();
        logger.ClearLogs();
        return data;
    }

}
