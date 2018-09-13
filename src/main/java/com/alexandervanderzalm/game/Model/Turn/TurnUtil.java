package com.alexandervanderzalm.game.Model.Turn;

import com.alexandervanderzalm.game.Model.GameState;

public class TurnUtil {
    public static int GetPlayer(TurnData d){
        return d.NextTurnState == GameState.TurnP1 ? 0 : 1;
    }
}
