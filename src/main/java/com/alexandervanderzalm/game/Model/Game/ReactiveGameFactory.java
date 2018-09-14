package com.alexandervanderzalm.game.Model.Game;

import com.alexandervanderzalm.game.Model.ReactiveKalahaGame;

public class ReactiveGameFactory implements IGameFactory {

    @Override
    public IGame Create() {
        return new ReactiveKalahaGame();
    }
}
