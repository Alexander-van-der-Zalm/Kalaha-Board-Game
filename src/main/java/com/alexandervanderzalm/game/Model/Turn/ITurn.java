package com.alexandervanderzalm.game.Model.Turn;

import com.alexandervanderzalm.game.Model.Logger.ITransformCollection;
import com.alexandervanderzalm.game.Model.Pits.IKalahaPit;
import com.alexandervanderzalm.game.Model.Pits.IPitCollection;
import com.alexandervanderzalm.game.Utility.IMethodScheduler;


public interface ITurn{
    // Functionality
    IMethodScheduler<ITurn> EndOfTurn();


    // Actual Data Object
    //TurnData Data();

    // Shortcuts -- Maybe move to Data?
    //IKalahaActions Actions();
    ITransformCollection Transforms();
    Integer Player();
    IPitCollection<IKalahaPit> pits(); // Should this be in IKalahaTurn? -> maybe move to data?
}
