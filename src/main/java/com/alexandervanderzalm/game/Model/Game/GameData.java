package com.alexandervanderzalm.game.Model.Game;

import com.alexandervanderzalm.game.Model.Logger.LogCollection;
import com.alexandervanderzalm.game.Model.Logger.LogUtility;
import com.alexandervanderzalm.game.Model.Pits.PitData;
import com.alexandervanderzalm.game.Model.Pits.PitCollection;
import com.alexandervanderzalm.game.Model.Pits.ReactivePit;
import com.alexandervanderzalm.game.Model.Turn.TurnData;
import com.alexandervanderzalm.game.Model.Turn.TurnUtil;

import java.util.stream.Collectors;

public class GameData{
    public PitCollection<ReactivePit> Pits;
    public LogCollection Logger;
    public GameState NextTurnState = GameState.TurnP1;
    public Integer CurrentTurn = 0;
    public Integer CurrentPlayer = 0;
    public Integer CurrentHand = 0;

    public GameData() {
        Pits = new PitCollection<>();
        Logger = new LogCollection();
    }

    public GameData(TurnData data) {
        this();
        data.Pits.forEach((pit) -> Pits.pList.add(new ReactivePit(pit)));
        NextTurnState = data.NextTurnState;
        CurrentPlayer = TurnUtil.GetPlayer(data);
        CurrentTurn = data.Turn;
        LogUtility.SetLoggerFromTurnData(Logger,data);
    }

    public TurnData
    ToTurnData() {
        TurnData data = new TurnData();

        // Transform pit data into clean rest data
        data.Pits = Pits.pList.stream().map(x -> new PitData(x.Data().player, x.Data().isKalaha, x.Amount())).collect(Collectors.toList());
        //data.Pits = pits.pList.stream().map(x -> x.Data()).collect(Collectors.toList()); // Not a deep copy...
        data.NextTurnState = NextTurnState;
        data.Turn = CurrentTurn;
        data.Player1Score = Pits.KalahaOfPlayer1().Amount();//pits.Get(0).Amount();
        data.Player2Score = Pits.KalahaOfPlayer2().Amount();// pits.Get(pits.Pits.size()/2).Amount();
        data.Log = Logger.GetLogData();
        Logger.ClearLogs();
        return data;
    }
}
