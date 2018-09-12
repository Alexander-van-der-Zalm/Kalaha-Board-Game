package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Model.Turn.TurnData;

public interface IGame {
    TurnData DoTurn(Integer SelectedIndex);
    TurnData SetupNewGame();
    //TurnData SetUpGameFromTurnData(TurnData data);
}
