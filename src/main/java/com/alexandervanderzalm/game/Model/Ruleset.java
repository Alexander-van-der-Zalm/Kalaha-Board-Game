package com.alexandervanderzalm.game.Model;

interface  IRuleSet{
    void SetupGame();
    // Turn Structure?
}

public class Ruleset {

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
        // X - EventScheduler<T> (optional priority sorting?)
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
