package com.alexandervanderzalm.game.Model.Logger;

public class PitLog extends LogData implements ILog {

    public PitLog(int index, int amountAdded, int finalAmount) {
        super(LogTypes.PitLog);
        super.Index = index;
        super.AmountAdded = amountAdded;
        super.FinalAmount = finalAmount;
    }

    public PitLog(LogData l){
        super(LogTypes.PitLog);
        super.Index = l.Index;
        super.AmountAdded = l.AmountAdded;
        super.FinalAmount = l.FinalAmount;
        super.Log = l.Log;
    }

    @Override
    public LogData GetLogData() {
        return this;
    }
}
