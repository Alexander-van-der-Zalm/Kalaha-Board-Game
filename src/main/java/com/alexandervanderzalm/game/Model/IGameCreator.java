package com.alexandervanderzalm.game.Model;

interface IGameCreator{
    IGame Create();
}

class SimpleGameCreator implements IGameCreator{

    @Override
    public IGame Create() {
        return new SimpleKalahaGame();
    }
}


class ReactiveGameCreator implements IGameCreator{

    @Override
    public IGame Create() {
        return new ReactiveKalahaGame();
    }
}