package com.alexandervanderzalm.game.Model.Logger;

import com.alexandervanderzalm.game.Model.Pits.IKalahaPit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

interface ILogCollection {
    void Log(ILog logMe);
    List<LogData> GetLogData();
    void ClearLogs();
}

public class LogCollection implements ILogCollection {

    List<ILog> logs = new ArrayList<>();

    @Override
    public void Log(ILog logMe) {
        logs.add(logMe);
    }

    @Override
    public List<LogData> GetLogData() {
        return logs.stream().map((l)-> l.GetLogData()).collect(Collectors.toList());
    }

    @Override
    public void ClearLogs() {
        logs.clear();
    }


}
