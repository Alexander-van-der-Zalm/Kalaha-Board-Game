package com.alexandervanderzalm.controllers;

import com.alexandervanderzalm.Model.TurnData;
import com.alexandervanderzalm.Model.TurnInputData;

public class TurnDataService {

    // Use Repository

    public static TurnData DoTurn(TurnInputData input)
    {
        TurnData turn = new TurnData();
        // TODO Turn Logic


        return turn;
    }

    public static TurnData InitGame()
    {
        TurnData turn = new TurnData();

        return turn;
    }
}
