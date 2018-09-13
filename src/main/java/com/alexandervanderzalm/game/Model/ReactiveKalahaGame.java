package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Model.Logger.LogUtility;
import com.alexandervanderzalm.game.Model.Pits.IPit;
import com.alexandervanderzalm.game.Model.Pits.KalahaPitData;
import com.alexandervanderzalm.game.Model.Turn.TurnData;
import com.alexandervanderzalm.game.Utility.FunctionCollection;
import com.alexandervanderzalm.game.Utility.MethodCollection;

import java.util.List;
import java.util.stream.Collectors;

public class ReactiveKalahaGame implements IGame {

    public ReactiveGameData Settings;
    public GameData Data;

    @Override
    public TurnData SetupNewGame() {
        return null;
    }

    @Override
    public TurnData SetUpGameFromTurnData(TurnData data) {
        return null;
    }

    // TODO Make a OnGrab, OnAdd methodCollection that logs
    // That logs
    @Override
    public TurnData DoTurn(Integer SelectedIndex) {
        // Check for valid input
        TurnData error = InputUtility.IsValidInput(SelectedIndex,Data.ToTurnData());
        if(error != null)
            return error;

        // 

        // Special end of turn scenarios (such as winning)
        List<TurnData> endScenarios = Settings.SpecialEndOfTurnScenarios.Process(Data).stream().filter((end) -> end!=null).collect(Collectors.toList());
        if(endScenarios.size() > 0)
            return endScenarios.get(0);

        return Data.ToTurnData();
    }


}

class WireReactiveGame{
    public static void WireGame(ReactiveKalahaGame game){

        // Normal End Game - Priority 1
        game.Settings.SpecialEndOfTurnScenarios.Add((d) ->{
            return GameUtil.EmptyFieldsWinCondition(d);
        });

        // Unwinnable - Priority 2
        game.Settings.SpecialEndOfTurnScenarios.Add((d) ->{
            return GameUtil.UnwinnableWinCondition(d);
        });

        // Add logging capability
        ReactivePit pit = game.Data.Pits.Get(0);
        pit.OnAdd.Add((stones) -> LogUtility.LogPit(game.Data, pit, stones));
        pit.OnGrab.Add((stones) -> LogUtility.LogPit(game.Data, pit, stones));
    }
}

class ReactivePit implements IPit<Integer>{

    public MethodCollection<Integer> OnAdd = new MethodCollection<>();
    public MethodCollection<Integer> OnGrab = new MethodCollection<>();

    private KalahaPitData data;

    @Override
    public void Add(Integer stones) {
        data.stones += stones;
        OnAdd.Process(stones);
    }

    @Override
    public int Amount() {
        return data.stones;
    }

    @Override
    public Integer GrabAll() {
        Integer result = data.stones;
        data.stones = 0;
        OnGrab.Process(-result);
        return result;
    }

    public KalahaPitData Data(){
        return data;
    }
}

class ReactiveGameData{
    public FunctionCollection<GameData,TurnData> SpecialEndOfTurnScenarios;
}
