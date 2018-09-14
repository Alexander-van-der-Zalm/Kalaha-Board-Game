package com.alexandervanderzalm.game.Model.Pits;

import com.alexandervanderzalm.game.Model.Turn.TurnData;
import com.alexandervanderzalm.game.Utility.ProcedureCollection;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PitUtil {

    public static List<KalahaPitData> CreatePitDataList(int normalPitsPerPlayer, int stonesAmount){
        int[] normalPits = new int[normalPitsPerPlayer * 2];
        for (int i = 0; i < normalPitsPerPlayer * 2; i++) {
            normalPits[i] = stonesAmount;
        }
        return CreatePitDataList(0,0,normalPits);
    }

    public static List<KalahaPitData> CreatePitDataList(int p1Score, int p2Score, int[] normalPits){
        // Initialize from normalPits data
        if(normalPits.length % 2 > 0)
            System.out.println("CreatePitDataList - NOTE! - normalPits not an even amount");

        List<KalahaPitData> result = new ArrayList<>();
        // Add normalPits
        Arrays.stream(normalPits).forEach((p) -> result.add(new KalahaPitData(0,false, p)));
        // Add kalahas
        result.add(FirstKalaha(),new KalahaPitData(0,true,p1Score));
        result.add(SecondKalaha(normalPits.length + 2),new KalahaPitData(0,true,p2Score));
        SetPlayer(result);

        return result;
    }

    public static List<IKalahaPit> CreatePitsFromTurnData(TurnData data){
        List<IKalahaPit> pits = new ArrayList<>();
        data.Pits.forEach((p) -> pits.add(new KalahaPit(p, new ProcedureCollection())));
        return pits;
    }

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

    public static void SetPlayer(List<KalahaPitData> d){
        d.forEach((p) -> p.player = GetPlayer(d.size(),d.indexOf(p)));
    }

    public static int GetPlayer(int size, int index){
        return index < SecondKalaha(size) ? 0 : 1;
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
