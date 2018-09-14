package com.alexandervanderzalm.game.Model.Game;

import com.alexandervanderzalm.game.Model.Turn.TurnData;

public interface IGame {
    TurnData DoTurn(Integer SelectedIndex);       // Only needs an index - it is assumed that the game does its own validation
    TurnData SetupNewGame();                      // Setup a new game with a default field (14 pits, 2 are kalaha the rest have 6 stones
    TurnData SetUpGameFromTurnData(TurnData data);// Set a game up from any given state
}
