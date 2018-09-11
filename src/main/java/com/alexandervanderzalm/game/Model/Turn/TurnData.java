package com.alexandervanderzalm.game.Model.Turn;


import com.alexandervanderzalm.game.Model.GameState;
import com.alexandervanderzalm.game.Model.Logger.ILog;
import com.alexandervanderzalm.game.Model.Logger.LogData;
import com.alexandervanderzalm.game.Model.Pits.KalahaPitData;

import java.util.List;

public class TurnData {
    // Previous TurnData
    // Next TurnData

    public int Turn;
    public int Player1Score;
    public int Player2Score;
    public GameState NextTurnState;
    public List<KalahaPitData> Pits;
    public List<LogData> Log;
    //public Function<Boolean,Boolean> func;

    // public int TurnCount??
    // BucketResult
    // Transformation sequence
}
