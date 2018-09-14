package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Model.Game.*;
import com.alexandervanderzalm.game.Model.Logger.LogUtility;
import com.alexandervanderzalm.game.Model.Pits.PitUtil;
import com.alexandervanderzalm.game.Model.Turn.TurnData;

import java.util.List;
import java.util.stream.Collectors;

public class ReactiveKalahaGame implements IGame {

    public ReactiveGameFunctionality Functionality;
    public GameData Data;

    public ReactiveKalahaGame() {
        Functionality = new ReactiveGameFunctionality();
    }

    @Override
    public TurnData SetupNewGame() {
        // Setup field
        TurnData setup = new TurnData(PitUtil.CreatePitDataList(6,6));

        // Wire functions & do setup via SetUpGameFromTurnData
        return SetUpGameFromTurnData(setup);
    }

    @Override
    public TurnData SetUpGameFromTurnData(TurnData data) {
        // Setup game from data
        Data = new GameData(data);

        ReactiveGameRules.WireReactiveKalahaGameRules(this);

        // Add the logs of setup phase placing the stones
        Data.Pits.pList.stream().filter((p) -> !p.Data().isKalaha)
                .forEach((p) -> LogUtility.Log(Data.Logger,Data.Pits,p,6));

        LogUtility.Log(Data.Logger,"Set up a new *Reactive* Kalaha Game!");
        LogUtility.Log(Data.Logger, String.format("%sNew Turn",LogUtility.LogStart(Data.CurrentPlayer, Data.CurrentTurn +1)));
        return this.Data.ToTurnData();
    }


    @Override
    public TurnData DoTurn(Integer SelectedIndex) {
        // Check for valid input
        TurnData error = InputUtility.IsValidInput(SelectedIndex,Data.ToTurnData());
        if(error != null)
            return error;

        // Update Turncounter & flip
        GameUtil.NewTurnSetup(Data);

        // Do the core turn procedure
        if(Functionality.TurnProcedure != null)
            Functionality.TurnProcedure.Process(SelectedIndex);

        // Special end of turn scenarios (such as winning)
        List<TurnData> endScenarios = Functionality.SpecialEndOfTurnScenarios.Process(Data).stream().filter((end) -> end!=null).collect(Collectors.toList());
        if(endScenarios.size() > 0)
            return endScenarios.get(0);

        return Data.ToTurnData();
    }
}


