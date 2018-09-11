package com.alexandervanderzalm.game.Model.Logger;

import com.alexandervanderzalm.game.Model.Pits.IKalahaPit;

interface IPitLog extends  ILog {
    IKalahaPit Pit();
    Integer Index();
    Integer AmountAdded();
}

public class PitLog extends LogData implements IPitLog  {

    IKalahaPit pit;

    public PitLog(IKalahaPit pit, int index, int amountAdded, int finalAmount) {
        super(LogTypes.PitLog);
        this.pit = pit;
        super.Index = index;
        super.AmountAdded = amountAdded;
        super.FinalAmount = finalAmount;
    }

    @Override
    public IKalahaPit Pit() {
        return null;
    }

    @Override
    public Integer Index() {
        return null;
    }

    @Override
    public Integer AmountAdded() {
        return null;
    }

    @Override
    public LogData GetLogData() {
        return this;
    }
}
