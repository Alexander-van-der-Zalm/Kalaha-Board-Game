package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Model.Logger.LogUtility;
import com.alexandervanderzalm.game.Model.Pits.IPit;
import com.alexandervanderzalm.game.Model.Pits.KalahaPitData;
import com.alexandervanderzalm.game.Model.Pits.PitUtil;
import com.alexandervanderzalm.game.Model.Turn.TurnData;
import com.alexandervanderzalm.game.Utility.FunctionCollection;
import com.alexandervanderzalm.game.Utility.FunctionalInterfaces.Method;
import com.alexandervanderzalm.game.Utility.MethodCollection;

import java.util.List;
import java.util.stream.Collectors;

public class ReactiveKalahaGame implements IGame {

    public ReactiveGameFunctionality Functionality;
    public GameData Data;

    public ReactiveKalahaGame() {
        Functionality = new ReactiveGameFunctionality();
    }

    @Override
    public TurnData SetupNewGame() {
        // Setup field
        TurnData setup = new TurnData(PitUtil.CreatePitDataList(6,6));

        // Wire functions & do setup via SetUpGameFromTurnData
        return SetUpGameFromTurnData(setup);
    }

    @Override
    public TurnData SetUpGameFromTurnData(TurnData data) {
        // Setup game from data
        Data = new GameData(data);

        WireReactiveGame.WireReactiveKalahaGameRules(this);

        return this.Data.ToTurnData();
    }


    @Override
    public TurnData DoTurn(Integer SelectedIndex) {
        // Check for valid input
        TurnData error = InputUtility.IsValidInput(SelectedIndex,Data.ToTurnData());
        if(error != null)
            return error;

        // TODO update Turncounter & flip -- see simple

        // Do the core turn procedure
        if(Functionality.TurnProcedure != null)
            Functionality.TurnProcedure.Process(SelectedIndex);

        // Special end of turn scenarios (such as winning)
        List<TurnData> endScenarios = Functionality.SpecialEndOfTurnScenarios.Process(Data).stream().filter((end) -> end!=null).collect(Collectors.toList());
        if(endScenarios.size() > 0)
            return endScenarios.get(0);

        return Data.ToTurnData();
    }
}

class WireReactiveGame{
    public static void WireReactiveKalahaGameRules(ReactiveKalahaGame game){

        // Special turn ending scenarios (override a normal to TurnData)
        game.Functionality.SpecialEndOfTurnScenarios = new FunctionCollection<>();

        // Normal End Game - Priority 1
        game.Functionality.SpecialEndOfTurnScenarios.Add((d) ->{
            return GameUtil.EmptyFieldsWinCondition(d);
        });

        // Unwinnable - Priority 2
        game.Functionality.SpecialEndOfTurnScenarios.Add((d) ->{
            return GameUtil.UnwinnableWinCondition(d);
        });

        // Add logging capability
        game.Data.Pits.pList.stream().forEach((pit) ->{
            pit.OnAdd.Add((stones) -> LogUtility.LogPit(game.Data, pit, stones));
            pit.OnGrab.Add((stones) -> LogUtility.LogPit(game.Data, pit, stones));
        });

        // Add extra turn rule
        game.Data.Pits.pList.stream()
            .filter((p) -> p.Data().isKalaha)
            .forEach((pit) ->{
                pit.OnAdd.Add((stones) -> {
                    if(game.Data.CurrentHand == 1 && pit.Data().player == game.Data.CurrentPlayer){
                        LogUtility.Log(game.Data.Logger,String.format("%sGains an Extra Turn for dropping the last stone in his own Kalaha.",
                                LogUtility.LogStart(game.Data.CurrentPlayer ,  game.Data.CurrentTurn)
                        ));
                        GameUtil.FlipGameState(game.Data);
                    }
                });
        });

        // Add capture rule
        game.Data.Pits.pList.stream().forEach((pit) ->{
            pit.OnAdd.Add((stones) -> LogUtility.LogPit(game.Data, pit, stones));
        });


        // The core turn procedure definition
        game.Functionality.TurnProcedure = (index) -> {
            ReactivePit current = game.Data.Pits.Get(index);
            game.Data.CurrentHand = current.GrabAll();

            // Log pickup
            LogUtility.Log(game.Data.Logger,
                String.format("%s- Grabbed %d stones from pit %d.",
                    LogUtility.LogStart(game.Data.CurrentPlayer ,  game.Data.CurrentTurn),
                    game.Data.CurrentHand,
                    index
                )
            );

            // The main drop it one by one loop
            while(game.Data.CurrentHand > 0){
                // Move one to the right (counter-clockWise)
                current = game.Data.Pits.Right(current);

                // Skip when landed upon oponents kalaha
                if (current.Data().isKalaha && current.Data().player != game.Data.CurrentPlayer) {
                    LogUtility.Log(game.Data.Logger, String.format("Turn %d - Skipped dropping a stone at opponents Kalaha.", game.Data.CurrentTurn));
                    continue;
                }

                // Remove from hand and add to the current pit
                game.Data.CurrentHand--;
                current.Add(1);
            }
        };
    }
}

class ReactiveGameFunctionality {
    public FunctionCollection<GameData,TurnData> SpecialEndOfTurnScenarios;
    public Method<Integer> TurnProcedure;
}

class ReactivePit implements IPit<Integer>{

    public MethodCollection<Integer> OnAdd = new MethodCollection<>();
    public MethodCollection<Integer> OnGrab = new MethodCollection<>();

    private KalahaPitData data;

    public ReactivePit(KalahaPitData data) {
        this.data = data;
    }

    public ReactivePit() {
    }

    @Override
    public void Add(Integer stones) {
        data.stones += stones;
        OnAdd.Process(stones);
    }

    @Override
    public int Amount() {
        return data.stones;
    }

    @Override
    public Integer GrabAll() {
        Integer result = data.stones;
        data.stones = 0;
        OnGrab.Process(-result);
        return result;
    }

    public KalahaPitData Data(){
        return data;
    }
}


