package com.alexandervanderzalm.game.Model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleGame implements IGame{

    //private List<IKalahaPit> pits;
    private PitCollection<IKalahaPit> pits;
    private GameState nextTurnState;
    private int currentPlayer = 0;

    public SimpleGame() {
        //this.pits = new ArrayList<>();
    }

    @Override
    public TurnData InitializeGame() {

        pits = new PitCollection<>( PitUtil.CreatePits(14,6));

        nextTurnState = GameState.TurnP1;
        System.out.println("Initialized a kalaha game.");
        return GameToTurnData();
    }

    @Override
    public TurnData DoTurn(Integer SelectedIndex) {
        // Prepare gameState next round & current player index
        currentPlayer = nextTurnState == GameState.TurnP1 ? 0 : 1;
        FlipGameState();

        // Grab all from the currently selected pit
        System.out.println("Grabbed pits @ " + SelectedIndex);
        IKalahaPit current = pits.Get(SelectedIndex);
        Integer hand = current.GrabAll();

        // Drop one in the right pit except for the opposite players pit
        while(hand > 0) {
        //for (int i = 0; i < hand; i++) {
            current = pits.Right(current);

            // Skip when landed upon oponents kalaha
            if(current.IsKalaha() && current.GetPlayer() != currentPlayer) {
                System.out.println(String.format("Skipped dropping a stone opponents Kalaha @ %s", pits.Pits.indexOf(current)));
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
                    System.out.println(String.format("Capture from %d opposite @ %d ", pits.Pits.indexOf(current), pits.Pits.indexOf(pits.Opposite(current))));
                    // add both opposite & the last one into own kalaha
                    pits.KalahaOfPlayer(currentPlayer).Add(pits.Opposite(current).GrabAll() + 1);
                    //pits.KalahaOfPlayer(currentPlayer).Add();
                    hand--;
                    continue;
                }
            }

            System.out.println("Dropped a stone @ " + pits.Pits.indexOf(current));
            // Drop stone
            current.Add(1);
            hand--;
        }

        // Check for end of game
        //Stream<IKalahaPit> filtered = pits.Pits.stream().filter((p) -> !p.IsKalaha() && p.Amount() == 0);
        if(pits.Pits.stream().filter((p) -> !p.IsKalaha() && p.Amount() == 0 && p.GetPlayer() == 0).count() == 6 || pits.Pits.stream().filter((p) ->!p.IsKalaha() && p.Amount() == 0 && p.GetPlayer() == 1).count() == 6) {
            // Add all pits to their respective owners
            pits.Pits.stream().filter((p) -> !p.IsKalaha()).forEach((p) -> pits.KalahaOfPlayer(p.GetPlayer()).Add(p.GrabAll()));

            if(pits.KalahaOfPlayer1().Amount() > pits.KalahaOfPlayer2().Amount())
                nextTurnState = GameState.WinP1;
            else
                nextTurnState = GameState.WinP2;

        }
        return GameToTurnData();
    }

    private void FlipGameState(){
        nextTurnState = nextTurnState == GameState.TurnP1 ? GameState.TurnP2 : GameState.TurnP1;
    }

    private TurnData GameToTurnData(){
        TurnData data = new TurnData();
        // data.Pits = pits.stream().map(x -> new KalahaPitData(x.GetPlayer(),x.IsKalaha(),x.Amount())).collect(Collectors.toList());
        data.Pits = pits.Pits.stream().map(x -> x.Data()).collect(Collectors.toList());
        data.NextTurnState = nextTurnState;
        data.Player1Score = pits.KalahaOfPlayer1().Amount();//pits.Get(0).Amount();
        data.Player2Score = pits.KalahaOfPlayer2().Amount();// pits.Get(pits.Pits.size()/2).Amount();
        return data;
    }

}
