package com.alexandervanderzalm.game.Model.Logger;

interface ITextLog extends ILog {
    String GetLog();
}

public class TextLog implements ITextLog {

    private String log;

    public TextLog(String log) {
        this.log = log;
    }

    @Override
    public String GetLog() {
        return log;
    }

    @Override
    public LogData GetLogData() {
        return new LogData(LogTypes.TextLog,log);
    }
}



