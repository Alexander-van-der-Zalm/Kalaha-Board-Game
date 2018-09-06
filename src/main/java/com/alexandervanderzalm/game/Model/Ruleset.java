package com.alexandervanderzalm.game.Model;

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
        // - ObserveChange
        // - EventScheduler<T> (optional priority sorting?)
        // - ObserveChangeCollection
        // - TurnUtil   - CurrentPit,OppositePit (turn) -> use PitCollection & transformdata
        // - PitUtil    - OwnEmpty, OwnKalaha (pit)
        // - ActionsService/Util
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
