package com.alexandervanderzalm.game.Model.Pits;

import com.alexandervanderzalm.game.Utility.ProcedureCollection;

import java.util.ArrayList;
import java.util.List;

public class PitUtil {

    public static List<IKalahaPit> CreatePits(int boardSize, int stonesAmount){
        return CreatePits(boardSize,stonesAmount,null);
    }

    public static List<IKalahaPit> CreatePits(int boardSize, int stonesAmount, ProcedureCollection onChanged){
        List<IKalahaPit> pits = new ArrayList<>();

        int secondKalaha = boardSize / 2;

        for (int i = 0; i < boardSize; i++) {
            if(onChanged == null)
                pits.add(new KalahaPit(new ProcedureCollection()));
            else
                pits.add(new KalahaPit(onChanged));

            if(i == 0 || i == secondKalaha)
                pits.get(i).MakeKalaha();
            else
                pits.get(i).Add(stonesAmount);
        }
        for (int i = secondKalaha; i < boardSize; i++) {
            pits.get(i).SetPlayer(1);
        }

        return pits;
    }

    public static int Right(int size, int index){
        return ((index - 1 + size)%size);
    }

    public static int Left(int size, int index){
        return ((index + 1 + size)%size);
    }

    public static int Opposite(int size, int index){
        return (size - index)%size;
    }

    public static int FirstKalaha(){
        return 0;
    }

    public static int SecondKalaha(int size){
        return size/2;
    }
}
