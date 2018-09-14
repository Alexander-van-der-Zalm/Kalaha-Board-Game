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

        // Add the logs of the stones
        Data.Pits.pList.stream().filter((p) -> !p.Data().isKalaha)
                .forEach((p) -> LogUtility.Log(Data.Logger,Data.Pits,p,6));

        LogUtility.Log(Data.Logger,"Set up a new Kalaha Game!");
        LogUtility.Log(Data.Logger, String.format("%sNew Turn",LogUtility.LogStart(Data.CurrentPlayer, Data.CurrentTurn +1)));
        return this.Data.ToTurnData();
    }


    @Override
    public TurnData DoTurn(Integer SelectedIndex) {
        // Check for valid input
        TurnData error = InputUtility.IsValidInput(SelectedIndex,Data.ToTurnData());
        if(error != null)
            return error;

        // Update Turncounter & flip
        GameUtil.NewTurnSetup(Data);

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

        // Log for new turn
        game.Functionality.SpecialEndOfTurnScenarios.Add((d) ->{
            LogUtility.Log(game.Data.Logger, String.format("%sNew Turn",LogUtility.LogStart(GameUtil.GetPlayerNextTurn(d),game.Data.CurrentTurn + 1)));
            return null;
        });

        // Add basic pit logging capability
        game.Data.Pits.pList.stream().forEach((pit) ->{
            pit.OnAdd.Add((stones) -> LogUtility.LogPit(game.Data, pit, stones));
            pit.OnGrab.Add((stones) -> LogUtility.LogPit(game.Data, pit, stones));
        });

        // Add extra turn rule check only to Kalahas
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
        game.Data.Pits.pList.stream()
                .filter((p) -> !p.Data().isKalaha)
                .forEach((pit) ->{
            pit.OnAdd.Add((changed) ->{
                if(game.Data.CurrentHand == 1 && pit.Amount()-changed == 0 && pit.Data().player == game.Data.CurrentPlayer){

                    ReactivePit opposite = game.Data.Pits.Opposite(pit);
                    ReactivePit kalaha = game.Data.Pits.KalahaOfPlayer(game.Data.CurrentPlayer);

                    // To prevent triggering of next turn on scoring of kalaha
                    game.Data.CurrentHand=0;
                    // Grab the one from the pit that was just dropped
                    pit.GrabAll();
                    // Capture opposite stones
                    Integer capturedStones = opposite.GrabAll();

                    kalaha.Add(1);
                    kalaha.Add(capturedStones);

                    // Log the event
                    LogUtility.Log(game.Data.Logger,String.format("%sCaptured %d stones from opposite pit %d and scored %d.",
                            LogUtility.LogStart(game.Data.CurrentPlayer ,  game.Data.CurrentTurn),
                            capturedStones,
                            game.Data.Pits.IndexOf(opposite),
                            capturedStones + 1
                    ));

                }
            });
        });


        // The core turn procedure definition
        game.Functionality.TurnProcedure = (index) -> {
            ReactivePit current = game.Data.Pits.Get(index);
            game.Data.CurrentHand = current.GrabAll();

            // Log pickup
            LogUtility.Log(game.Data.Logger,
                String.format("%sGrabbed %d stones from pit %d.",
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

                // Add to current pit and then remove from hand (to triggering conditional bugs for extra turn)
                current.Add(1);
                game.Data.CurrentHand--;
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
        data = new KalahaPitData();
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


