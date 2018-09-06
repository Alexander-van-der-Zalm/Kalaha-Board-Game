package com.alexandervanderzalm.game.controllers;

import com.alexandervanderzalm.game.Model.GameState;
import com.alexandervanderzalm.game.Model.TurnData;
import com.alexandervanderzalm.game.Model.TurnInputData;


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
