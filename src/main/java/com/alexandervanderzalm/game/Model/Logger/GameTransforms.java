package com.alexandervanderzalm.game.Model.Logger;

import com.alexandervanderzalm.game.Model.Pits.IKalahaPit;

interface ITransform{
    String GetLog();
    void SetLog(String log);
}

class NormalTransform implements ITransform{
    // TODO Make jpa entity
    // TODO transform id
    // TODO turn id
    // TODO game id

    public NormalTransform(String log) {
        this.log = log;
    }

    private String log;

    @Override
    public String GetLog() {
        return log;
    }

    @Override
    public void SetLog(String log) {
        this.log = log;
    }
}

interface IPitTransform extends ITransform{
    IKalahaPit Pit();
}
