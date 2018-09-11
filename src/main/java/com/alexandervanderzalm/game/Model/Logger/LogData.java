package com.alexandervanderzalm.game.Model.Logger;

public class LogData {
    public LogTypes Type;
    public String Log;
    public Integer Index = -1;

    public LogData(LogTypes type) {
        Type = type;
    }

    public LogData(LogTypes type, String log) {
        Type = type;
        Log = log;
    }

    public LogData(LogTypes type, String log, Integer index, Integer amountAdded) {
        Type = type;
        Log = log;
        Index = index;
        AmountAdded = amountAdded;
    }

    public Integer AmountAdded = 0;
}
