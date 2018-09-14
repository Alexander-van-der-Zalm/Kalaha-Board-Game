package com.alexandervanderzalm.game.Model.Turn;


import com.alexandervanderzalm.game.Model.Game.GameState;
import com.alexandervanderzalm.game.Model.Logger.LogData;
import com.alexandervanderzalm.game.Model.Pits.PitData;
import com.alexandervanderzalm.game.Model.Pits.PitUtil;

import java.util.ArrayList;
import java.util.List;

/* TurnData
    Data class that gets send to the client and Holds the simplest representation of the game state
    Used by view and throughout the model for things such as input validation and testing.

    # Simple Data
    # Snapshot of the pits
    # Log changes (of pits, rules and exceptions)
 */
public class TurnData {
    public int Turn = 0;
    public int Player1Score = 0;
    public int Player2Score = 0;
    public GameState NextTurnState = GameState.TurnP1;      // What will the turn be for the view?
    public List<PitData> Pits;                        // The data stored in the pits (stones, player, isKalaha)
    public List<LogData> Log;                               // Stores all the pit changes & text events (extra_turn, capture, etc.)

    public TurnData() {
        Pits = new ArrayList<>();
        Log = new ArrayList<>();
    }

    public TurnData(List<PitData> pits) {
        this();
        Pits = pits;
        Player1Score = pits.get(PitUtil.FirstKalaha()).stones;
        Player2Score = pits.get(PitUtil.SecondKalaha(pits.size())).stones;
    }

    public TurnData(int turn, GameState nextTurnState, List<PitData> pits) {
        this(turn,nextTurnState,pits,new ArrayList<>());
    }

    public TurnData(int turn, GameState nextTurnState, List<PitData> pits, List<LogData> log) {
        this(pits);
        Turn = turn;
        NextTurnState = nextTurnState;
        Log = log;
    }
}
