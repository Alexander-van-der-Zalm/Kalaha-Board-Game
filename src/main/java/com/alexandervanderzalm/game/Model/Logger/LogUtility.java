package com.alexandervanderzalm.game.Model.Logger;

import com.alexandervanderzalm.game.Model.GameData;
import com.alexandervanderzalm.game.Model.GameState;
import com.alexandervanderzalm.game.Model.Pits.IPit;
import com.alexandervanderzalm.game.Model.Pits.IPitCollection;
import com.alexandervanderzalm.game.Model.Turn.TurnData;

public class LogUtility {

    public static void Log(LogCollection logger, IPitCollection pits, IPit pit, int amount){
        logger.Log(new PitLog(pits.IndexOf(pit), amount, pit.Amount()));
    }

    public static void LogPit(GameData data, IPit pit, int amount){
        Log(data.Logger,data.Pits,pit,amount);
    }

    public static void Log(LogCollection logger, String textLog){
        logger.Log(new TextLog(textLog));
        System.out.println(textLog);
    }

    public static String LogStartGameData(GameData data){
        return LogStart(data.CurrentPlayer, data.CurrentTurn);
    }

    public static String LogStart(TurnData data){
        return LogStart(data.NextTurnState == GameState.TurnP1 ? 0 : 1, data.Turn);
    }

    public static String LogStart(int playerIndex, int turn){
        return String.format("Turn %d - %s - ",turn,LogPlayer(playerIndex));
    }

    public static String LogPlayer(int playerIndex){
        return  playerIndex == 0 ? "<span class = 'Player1Log'>Player1</span>" : "<span class = 'Player2Log'>Player2</span>";
    }

    public static void SetLoggerFromTurnData(LogCollection logger, TurnData data){
        logger.ClearLogs();
        if(data.Log.size() == 0)
            return;
        data.Log.forEach((l)->{
            switch (l.Type){
                case PitLog:
                    logger.Log(new PitLog(l));
                break;
                case TextLog:
                    logger.Log(new TextLog(l.Log));
                break;
                default:
                    // Do nothing
                    System.out.println("SetLoggerFromTurnData didnt find the right type");
                break;
            }
        });
    }
}
