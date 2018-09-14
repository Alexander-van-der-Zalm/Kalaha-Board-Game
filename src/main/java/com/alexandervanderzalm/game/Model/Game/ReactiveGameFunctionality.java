package com.alexandervanderzalm.game.Model.Game;

import com.alexandervanderzalm.game.Model.Turn.TurnData;
import com.alexandervanderzalm.game.Utility.FunctionCollection;
import com.alexandervanderzalm.game.Utility.FunctionalInterfaces.Method;

public class ReactiveGameFunctionality {
    public FunctionCollection<GameData,TurnData> SpecialEndOfTurnScenarios;
    public Method<Integer> TurnProcedure;
}
