package com.alexandervanderzalm.controllers;

import com.alexandervanderzalm.Model.GameState;
import com.alexandervanderzalm.Model.TurnData;
import com.alexandervanderzalm.Model.TurnInputData;


public class TurnDataService {

    // Use Repository

    public static TurnData DoTurn(TurnInputData input)
    {
        TurnData turn = new TurnData();
        // TODO Turn Logic
        //turn.func = TurnDataService::TestFunc;// (Boolean in) -> {return in;};
        //turn.Test = turn.func.toString();
        turn.StartOfTurnState = GameState.Init;
        turn.EndOfTurnState = GameState.TurnP1;
        return turn;
    }

    public static Boolean TestFunc(Boolean test)
    {
        return test;
    }

    public static TurnData InitGame()
    {
        TurnData turn = new TurnData();

        return turn;
    }
}
