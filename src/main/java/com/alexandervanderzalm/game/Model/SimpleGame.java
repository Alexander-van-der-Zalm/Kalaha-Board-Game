package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Utility.MethodCollection;
import com.alexandervanderzalm.game.Utility.ProcedureCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleGame implements IGame{

    private List<IKalahaPit> pits;
    private GameState nextTurnState;

    public SimpleGame() {
        this.pits = new ArrayList<>();
    }

    @Override
    public TurnData InitializeGame() {

        pits = PitUtil.CreatePits(14,6);
//        int boardSize = 14;
//        for (int i = 0; i < boardSize; i++) {
//            pits.add(new KalahaPit(new ProcedureCollection()));
//            if(i == 0 || i == 7)
//                pits.get(i).MakeKalaha();
//            else
//                pits.get(i).Add(6);
//        }
//        for (int i = 7; i < boardSize; i++) {
//            pits.get(i).SetPlayer(1);
//        }

        nextTurnState = GameState.TurnP1;

        return GameToTurnData();
    }

    @Override
    public TurnData DoTurn(Integer SelectedIndex) {
        // Simple switch gameState
        nextTurnState = nextTurnState == GameState.TurnP1 ? GameState.TurnP1 : GameState.TurnP2;

        // Grab all from the current
        Integer hand = pits.get(SelectedIndex).GrabAll();
        for (int i = 0; i < hand; i++) {
            int index = SelectedIndex + i % pits.size();

        }

        // Check for end of game

        return GameToTurnData();
    }

    private TurnData GameToTurnData(){
        TurnData data = new TurnData();
        // data.Pits = pits.stream().map(x -> new KalahaPitData(x.GetPlayer(),x.IsKalaha(),x.Amount())).collect(Collectors.toList());
        data.Pits = pits.stream().map(x -> x.Data()).collect(Collectors.toList());
        data.NextTurnState = nextTurnState;
        data.Player1Score = pits.get(0).Amount();
        data.Player2Score = pits.get(pits.size()/2).Amount();
        return data;
    }

}
