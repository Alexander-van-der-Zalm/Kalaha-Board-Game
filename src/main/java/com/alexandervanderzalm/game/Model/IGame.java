package com.alexandervanderzalm.game.Model;

public interface IGame {
    TurnData DoTurn(Integer SelectedIndex);
    TurnData InitializeGame();
}
