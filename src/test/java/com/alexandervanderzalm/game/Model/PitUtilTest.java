package com.alexandervanderzalm.game.Model;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PitUtilTest {

    @Test
    void createPits() {
        List<IKalahaPit> pits = PitUtil.CreatePits(14,6);
        Assert.isTrue(pits.get(7).IsKalaha(),"Index 7 should be a kalaha");
        Assert.isTrue(pits.get(0).IsKalaha(),"Index 0 should be a kalaha");
        Assert.isTrue(pits.size() == 14,"Size should be 14");

        pits = PitUtil.CreatePits(6,6);
        Assert.isTrue(pits.get(3).IsKalaha(),"Index 7 should be a kalaha");
        Assert.isTrue(pits.get(0).IsKalaha(),"Index 0 should be a kalaha");
        Assert.isTrue(pits.size() == 6,"Size should be 14");
    }

    @Test
    void right() {
        Assert.isTrue(PitUtil.Right(14,0) == 1, "Right of 0 should be 1");
        Assert.isTrue(PitUtil.Right(14,13) == 0, "Right of 13 should be 0");
    }

    @Test
    void left() {
        Assert.isTrue(PitUtil.Left(14,0) == 13, "Left of 0 should be 13");
        Assert.isTrue(PitUtil.Left(14,13) == 12, "Left of 13 should be 12");
    }

    @Test
    void opposite() {
        Assert.isTrue(PitUtil.Opposite(14,0) == 0, "Opposite of 0 should be 0");
        Assert.isTrue(PitUtil.Opposite(14,7) == 7, "Opposite of 7 should be  7");
        Assert.isTrue(PitUtil.Opposite(14,1) == 13, "Opposite of 1 should be 13");
        Assert.isTrue(PitUtil.Opposite(14,13) == 1, "Opposite of 13 should be 1");
    }

    @Test
    void firstKalaha() {
        Assert.isTrue(PitUtil.FirstKalaha() == 0, "First Kalaha should always be on 0");
    }

    @Test
    void secondKalaha() {
        Assert.isTrue(PitUtil.SecondKalaha(14) == 7, "Second kalaha of 14 pits should be 7");
        Assert.isTrue(PitUtil.SecondKalaha(6) == 3, "Second kalaha of 6 pits should be 3");
    }
}