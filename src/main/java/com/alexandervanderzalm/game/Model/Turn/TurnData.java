package com.alexandervanderzalm.game.Model.Turn;


import com.alexandervanderzalm.game.Model.GameState;
import com.alexandervanderzalm.game.Model.Logger.ILog;
import com.alexandervanderzalm.game.Model.Logger.LogData;
import com.alexandervanderzalm.game.Model.Pits.KalahaPitData;
import com.alexandervanderzalm.game.Model.Pits.PitUtil;

import java.util.ArrayList;
import java.util.List;

public class TurnData {
    // Previous TurnData ?
    // Next TurnData ?

    public int Turn = 0;
    public int Player1Score = 0;
    public int Player2Score = 0;
    public GameState NextTurnState = GameState.TurnP1;
    public List<KalahaPitData> Pits;
    public List<LogData> Log;

    public TurnData() {
        Pits = new ArrayList<>();
        Log = new ArrayList<>();
    }

    public TurnData(List<KalahaPitData> pits) {
        this();
        Pits = pits;
        Player1Score = pits.get(PitUtil.FirstKalaha()).stones;
        Player2Score = pits.get(PitUtil.SecondKalaha(pits.size())).stones;
    }

    public TurnData(int turn, GameState nextTurnState, List<KalahaPitData> pits) {
        this(turn,nextTurnState,pits,new ArrayList<>());
    }

    public TurnData(int turn, GameState nextTurnState, List<KalahaPitData> pits, List<LogData> log) {
        this(pits);
        Turn = turn;
        NextTurnState = nextTurnState;
        Log = log;
    }


}
