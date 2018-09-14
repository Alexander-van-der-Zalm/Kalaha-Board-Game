package com.alexandervanderzalm.game.Model.Game;

import com.alexandervanderzalm.game.Model.SimpleKalahaGame;

public class SimpleGameFactory implements IGameFactory {

    @Override
    public IGame Create() {
        return new SimpleKalahaGame();
    }
}
