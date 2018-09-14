package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Model.Logger.LogUtility;
import com.alexandervanderzalm.game.Model.Turn.TurnData;

public class GameUtil {

    public static TurnData UnwinnableWinCondition(GameData data){
        // --Unwinnable condition detected
        int pitsInField = data.Pits.pList.stream()
                .filter((p) -> !p.Data().isKalaha)
                .map((p) -> p.Amount())
                .reduce(0, (x,y) -> x + y);

        int p1 = data.Pits.KalahaOfPlayer1().Amount();
        int p2 = data.Pits.KalahaOfPlayer2().Amount();
        if(p1 + pitsInField < p2 || p2 + pitsInField < p1) {
            LogUtility.Log(data.Logger, String.format("Turn %d - Unwinnable condition detected. %s: %d Field: %d %s: %d.",
                    data.CurrentTurn,
                    LogUtility.LogPlayer(0),
                    p1,
                    pitsInField,
                    LogUtility.LogPlayer(1),
                    p2));

            return EndOfGame(data);
        }
        // Condition does not apply
        return null;
    }

    public static TurnData EmptyFieldsWinCondition(GameData d) {
        // Check
        if(d.Pits.pList.stream().filter((p) -> !p.Data().isKalaha && p.Amount() == 0 && p.Data().player == 0).count() == 6
                || d.Pits.pList.stream().filter((p) ->!p.Data().isKalaha && p.Amount() == 0 && p.Data().player == 1).count() == 6) {

            // Add all pits to their respective owners
            d.Pits.pList.stream().filter((p) -> !p.Data().isKalaha).forEach((p) -> d.Pits.KalahaOfPlayer(p.Data().player).Add(p.GrabAll()));

            return EndOfGame(d);
        }
        return null;
    }

    public static TurnData EndOfGame(GameData d){
        if(d.Pits.KalahaOfPlayer1().Amount() > d.Pits.KalahaOfPlayer2().Amount()) {
            d.NextTurnState = GameState.WinP1;
            d.CurrentPlayer = 0;
        }else {
            d.NextTurnState = GameState.WinP2;
            d.CurrentPlayer = 1;
        }
        LogUtility.Log(d.Logger, String.format("Congratulations %s!", LogUtility.LogPlayer(d.CurrentPlayer)));
        LogUtility.Log(d.Logger, String.format("Refresh the page to play again.", LogUtility.LogPlayer(d.CurrentPlayer)));
        return d.ToTurnData();
    }

    public static void FlipGameState(GameData d){
        d.NextTurnState = d.NextTurnState == GameState.TurnP1 ? GameState.TurnP2 : GameState.TurnP1;
    }

    public static void NewTurn(GameData d){
        d.CurrentTurn++;
        d.CurrentPlayer = d.NextTurnState == GameState.TurnP1 ? 0 : 1;
        FlipGameState(d);
    }
}
