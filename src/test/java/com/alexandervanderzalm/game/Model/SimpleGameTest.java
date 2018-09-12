package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Model.Turn.TurnData;
import org.junit.Test;
import org.springframework.util.Assert;


public class SimpleGameTest {

    @Test
    public void initializeGame() {
        SimpleGame g = new SimpleGame();
        TurnData d = g.InitializeGame();

        System.out.println(d.Pits);

        Assert.isTrue(d.Pits.size() == 14, "Check for the pit size");
    }

    @Test
    public void DoTurn_InitializedGame_TurnProcessed(){
        SimpleGame g = new SimpleGame();
        TurnData d = g.InitializeGame();

        TurnData d2 = g.DoTurn(1);

        Assert.isTrue(d.Pits.get(1).stones == 0, "Check if the selected pit is empty");
    }
}