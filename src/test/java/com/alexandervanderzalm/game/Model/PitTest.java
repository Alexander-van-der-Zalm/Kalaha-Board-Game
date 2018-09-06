package com.alexandervanderzalm.game.Model;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

class PitTest {

    @Test
    void add_AddedTwoStones_AmountIsTwo() {
        Pit p = new Pit();
        p.Add(2);
        Assert.isTrue(p.Amount() == 2, "AddedTwoStones_PitAmountIsTwo");
    }

    @Test
    void grabAll_AddTwoGrabAll_AmountIsZero() {
        Pit p = new Pit();
        p.Add(2);
        Assert.isTrue(p.Amount() == 2, "AddedTwoStones_PitAmountIsTwo");
        Integer hand = p.GrabAll();
        Assert.isTrue(p.Amount() == 0, "GrabbedAllStones_PitAmountIsZero");
        Assert.isTrue(hand == 2, "GrabbedAllStones_HandHasTwoStones");
    }
}