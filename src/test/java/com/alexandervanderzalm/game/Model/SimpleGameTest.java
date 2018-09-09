package com.alexandervanderzalm.game.Model;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

class SimpleGameTest {

    @Test
    void initializeGame() {
        SimpleGame g = new SimpleGame();
        TurnData d = g.InitializeGame();

        System.out.println(d.Pits);

        Assert.isTrue(d.Pits.size() == 12, "Check for the pit size");
    }
}