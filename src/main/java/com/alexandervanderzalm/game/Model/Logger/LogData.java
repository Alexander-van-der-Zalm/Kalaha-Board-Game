package com.alexandervanderzalm.game.Model.Logger;

public class LogData {
    public LogTypes Type;
    public String Log;
    public Integer Index = -1;
    public Integer AmountAdded = 0;
    public Integer FinalAmount = 0;

    public LogData(LogTypes type) {
        Type = type;
    }

    public LogData(LogTypes type, String log) {
        Type = type;
        Log = log;
    }

    public LogData(LogTypes type, String log, Integer index, Integer amountAdded, Integer finalAmount) {
        Type = type;
        Log = log;
        Index = index;
        AmountAdded = amountAdded;
        FinalAmount = finalAmount;
    }


}
