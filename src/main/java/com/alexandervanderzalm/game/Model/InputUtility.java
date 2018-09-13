package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Model.Logger.LogUtility;
import com.alexandervanderzalm.game.Model.Logger.TextLog;
import com.alexandervanderzalm.game.Model.Pits.PitUtil;
import com.alexandervanderzalm.game.Model.Turn.TurnData;
import com.alexandervanderzalm.game.Model.Turn.TurnUtil;

public class InputUtility {

    public static TurnData IsValidInput(int selectedIndex, TurnData data){
        int currentPlayer = TurnUtil.GetPlayer(data);

        // Out of index
        if(selectedIndex > data.Pits.size() || selectedIndex < 0)
            return InvalidInput(data, String.format("%s Error!! - Index %d Out of Bounds",
                    LogUtility.LogStart(data),
                    selectedIndex
            ));

        // Not valid for currentPlayer
        if(PitUtil.GetPlayer(data.Pits.size(),selectedIndex) != currentPlayer)
            return InvalidInput(data, String.format("%s Error!! - Index %d does not belong to %s",
                    LogUtility.LogStart(data),
                    selectedIndex,
                    LogUtility.LogPlayer(currentPlayer)
            ));

        // Selected index is empty
        if(data.Pits.get(selectedIndex).stones == 0)
            return InvalidInput(data, String.format("%s Error!! - Index %d does not have any stones!",
                    LogUtility.LogStart(data),
                    selectedIndex
            ));

        return null;// return null if everything is good!
    }

    private static TurnData InvalidInput(TurnData data, String message){
        data.Log.clear();
        data.Log.add(new TextLog(message).GetLogData());
        System.out.println(message);
        return data;
    }
}
