package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Utility.IMethodScheduler;
import com.alexandervanderzalm.game.Utility.IProcedureCollection;
import com.alexandervanderzalm.game.Utility.ProcedureCollection;

import java.util.ArrayList;
import java.util.List;

interface IRuleSet<T>{
    void SetupGame(T Game);
    // Turn Structure?
}

interface IKalahaGame{
    IPitCollection<IKalahaPit> Pits();
    ITurn Turn();
}

interface ITurn{
    IMethodScheduler<ITurn> EndOfTurn();
    ITransformCollection Transforms();
    Integer Player();

    IPitCollection<IKalahaPit> pits(); // Should this be in IKalahaTurn?
}

interface ITransformCollection{
    List<ITransform> Transforms();
}

interface ITransform{
    String GetLog();
    void SetLog(String log);
}

interface IKalahaPit extends IObservedPit<Integer>{
    Boolean IsKalaha();
    void MakeKalaha();
    Integer GetPlayer();
    void SetPlayer(Integer player);
}

class KalahaPit extends ObservedPit implements IKalahaPit{

    private Integer player = 0;
    private Boolean isKalaha = false;

    public KalahaPit(IProcedureCollection onChangedHelper) {
        super(onChangedHelper);
    }

    @Override
    public Boolean IsKalaha() {
        return isKalaha;
    }

    @Override
    public void MakeKalaha() {
        isKalaha = true;
    }

    @Override
    public Integer GetPlayer() {
        return player;
    }

    @Override
    public void SetPlayer(Integer player) {
        this.player = player;
    }

    @Override
    public IProcedureCollection OnChanged() {
        return super.OnChanged;
    }
}

class NormalTransform implements ITransform{
    // TODO Make jpa entity
    // TODO transform id
    // TODO turn id
    // TODO game id

    public NormalTransform(String log) {
        this.log = log;
    }

    private String log;

    @Override
    public String GetLog() {
        return log;
    }

    @Override
    public void SetLog(String log) {
        this.log = log;
    }
}

interface IPitTransform extends ITransform{
    IKalahaPit Pit();
}

public class KalahaRuleset<T extends IKalahaGame> implements IRuleSet<T> {

    @Override
    public void SetupGame(T Game) {
        List<IKalahaPit> pits = new ArrayList<>();

        for(int player = 0; player<2; player++) {
            IKalahaPit kalaha = new KalahaPit(new ProcedureCollection());
            kalaha.MakeKalaha();
            kalaha.SetPlayer(player);
            // ### RULE
            // Whenever a kalaha pit gets changed. Schedule a method for the end of the turn that checks
            // if it is the last pit happens to be the players own kalaha. If so that player gets another
            // turn.
            kalaha.OnChanged().Add(
                // Add OwnKalaha check here (slightly optimized).
                () -> {
                    if(OwnKalaha(kalaha)){
                        Game.Turn().EndOfTurn().ScheduleMethod(
                            (turn) ->  ExtraTurn(turn)
                        );
                    }
                }
            );
            // Add to pit collection
            pits.add(kalaha);

            // TODO - Observed collection creation
            // Create all the normal pits
            for(int normalPitIndex = 0; normalPitIndex < 6; normalPitIndex++) {
                // Prepare a normal pit
                IKalahaPit normalPit = new KalahaPit(new ProcedureCollection());
                normalPit.SetPlayer(player);
                // ### RULE
                // When the changed pit is the players own and empty, then schedule
                // a capture action (move the stones of the opposite pit to this pit).
                // TODO check if it is actually the players turn... (I think thats already there)
                normalPit.OnChanged().Add(
                    () -> {
                        if(OwnEmpty(normalPit, Game.Turn().Player())){
                            Game.Turn().EndOfTurn().ScheduleMethod(
                                (turn) -> Capture(normalPit, turn)
                            );
                        }
                    }
                );

                //TODO - Add to observed collection
                pits.add(normalPit);
            }
        }

        // TODO add all pits to the games/turns pit collection
        //Game.Pits().

    }

    // Turn structure
    // Do a streams analysis on transforms.filter(PitTransform p -> p.Amount == 0)
    // If so game is over

    private void Capture(IKalahaPit normalPit, ITurn turn){//IPitCollection pits) {
        turn.pits().Opposite(normalPit);
        // TODO capture logic
        turn.Transforms().Transforms().add(new NormalTransform("Capture"));
    }

    private boolean OwnEmpty(IKalahaPit normalPit, int player) {
        return normalPit.Amount() == 0 && normalPit.GetPlayer() == player;
    }

    // Essentially wrappers?
    // Move to util?
    private Boolean OwnKalaha(IKalahaPit p) {
        return p.IsKalaha();
    }

    private void ExtraTurn(ITurn turn){
        // TODO extra turn logic
        turn.Transforms().Transforms().add(new NormalTransform("Extra turn"));
    }

    // TODO
    // Somehow implement all the logic inside here

    //Setup
    /*
    // Board
    2*{
        //1*Board.Pits.Add().OnLast(Turn turn)->turn.OnTurnComplete.Add(ExtraTurn())
        //6*Board.Pits.Add(6).OnLast(Turn turn)->{if(Empty())
        2*1*Board.Pits.Add()  <- Pit.OnChanged.Add(turn.EndOfTurn.Schedule((turn) -> { if(OwnKalaha(LastPit(turn))) ExtraTurn(turn)});
        2*6*Boards.Pit.Add()  <- Pit.OnChanged.Add(turn.EndOfTurn.Schedule((turn) -> { if(OwnEmpty (CurrentPit(turn))) Capture(Opposite(CurrentPit(turn)});
                              <- PitCollection.OnDirtySchedulars.Add(turn.EndOfTurn);
                              <- PitCollection.OnDirty.Add(turn.EndOfTurn, ###ENDOFGAME? -> ENDGAME/HARVEST

        // TODO
        // V - ObserveChange
        // V - EventScheduler<T> (optional priority sorting?)
        // X - ObserveChangeCollection
        // X - TurnUtil   - CurrentPit,OppositePit (turn) -> use PitCollection & transformdata
        // X - PitUtil    - OwnEmpty, OwnKalaha (pit)
        // X - ActionsService/Util
                            <- PitCollection.OnCha
    }
     */

    // Turn - Turn.Functions & Turn.Data
    /*
    OnTurnStart.Add(() -> Turn.Counter++);
    OnTurn.Add(() -> PickUpAllAndDropOneToTheRightTillEmpty(turn.Pits,int index)
    OnTurn.Add -> Turn.Board@last.OnLast() ==== POTENTIAL SOLUTION
     */
}
