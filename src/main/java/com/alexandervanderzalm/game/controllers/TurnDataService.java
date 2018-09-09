package com.alexandervanderzalm.game.controllers;

import com.alexandervanderzalm.game.Model.GameState;
import com.alexandervanderzalm.game.Model.KalahaPitData;
import com.alexandervanderzalm.game.Model.TurnData;
import com.alexandervanderzalm.game.Model.TurnInputData;

import java.util.ArrayList;


public class TurnDataService {

    // Use Repository

    public static TurnData DoTurn(TurnInputData input)
    {
        TurnData turn = new TurnData();
        // TODO Turn Logic
        //turn.func = TurnDataService::TestFunc;// (Boolean in) -> {return in;};
        //turn.Test = turn.func.toString();
        turn.NextTurnState = GameState.TurnP1;
        turn.Pits = new ArrayList<>();
        turn.Pits.add(new KalahaPitData());
        return turn;
    }

    public static Boolean TestFunc(Boolean test)
    {
        return test;
    }

    public static TurnData InitGame()
    {
        TurnData turn = new TurnData();
        turn.NextTurnState = GameState.TurnP1;
        turn.Pits = new ArrayList<>();
        turn.Pits.add(new KalahaPitData());
        turn.Pits.add(new KalahaPitData());
        turn.Pits.add(new KalahaPitData());
        return turn;
    }
}
