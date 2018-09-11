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
}