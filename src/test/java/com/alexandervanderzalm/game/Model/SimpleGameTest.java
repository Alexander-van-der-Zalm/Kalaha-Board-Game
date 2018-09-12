package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Model.Turn.TurnData;
import org.junit.Test;
import org.springframework.util.Assert;


public class SimpleGameTest {

    @Test
    public void initializeGame() {
        IGame g = new SimpleGame();
        TurnData d = g.SetupNewGame();

        System.out.println(d.Pits);

        Assert.isTrue(d.Pits.size() == 14, "Check for the pit size");
    }

    @Test
    public void DoTurn_InitializedGame_TurnProcessed(){
        IGame g = new SimpleGame();
        TurnData d = g.SetupNewGame();

        TurnData d2 = g.DoTurn(1);

        Assert.isTrue(d.Pits.get(1).stones == 0, "Check if the selected pit is empty");
    }

    @Test
    public void DoTurn_EndGame_GameFinished(){
        IGame g = new SimpleGame();
        int[] mockState = new int[]{10, 0,0,0,0,0,0, 10,
                                        0,0,0,0,0,0     };

        // TODO implement from mock state
    }
}