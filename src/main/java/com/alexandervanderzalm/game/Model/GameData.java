package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Model.Logger.LogCollection;
import com.alexandervanderzalm.game.Model.Pits.KalahaPitData;
import com.alexandervanderzalm.game.Model.Pits.PitCollection;
import com.alexandervanderzalm.game.Model.Turn.TurnData;

import java.util.stream.Collectors;

public class GameData{
    public PitCollection<ReactivePit> Pits;
    public LogCollection Logger;
    public GameState NextTurnState;
    public Integer CurrentTurn;
    public Integer CurrentPlayer;
    public Integer CurrentHand;

    public TurnData ToTurnData() {
        TurnData data = new TurnData();

        // Transform pit data into clean rest data
        data.Pits = Pits.pList.stream().map(x -> new KalahaPitData(x.Data().player, x.Data().isKalaha, x.Amount())).collect(Collectors.toList());
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
