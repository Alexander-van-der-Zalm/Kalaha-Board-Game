package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Model.Pits.PitUtil;
import com.alexandervanderzalm.game.Model.Turn.TurnData;
import org.junit.Test;
import org.springframework.util.Assert;
import static org.junit.Assert.*;

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
        // Setup
        IGame g = new SimpleGame();
        // -->                             first row     second row (clockwise ie inverted order)
        int[] mockNormalFields = new int[]{1,0,0,0,0,0,  9,11,0,0,0,0};
        TurnData t0Setup = new TurnData(PitUtil.CreatePitDataList(1,10,mockNormalFields));
        TurnData t0 = g.SetUpGameFromTurnData(t0Setup);

        // Test everything as expected so far
        assertSetup(t0Setup);
        assertSetup(t0);

        TurnData t1 = g.DoTurn(1);

        // Player 2 has won with a score of 30
        assertTrue(t1.NextTurnState == GameState.WinP2);
        assertEquals(t1.Player2Score, 30);
        assertEquals(t1.Player1Score, 2);
    }

    private void assertSetup(TurnData d){
        assertEquals((long)d.Pits.get(1).stones, 1);
        assertEquals((long)d.Pits.get(8).stones, 9);
        assertEquals((long)d.Pits.get(9).stones, 11);
        assertEquals((long)d.Player1Score, 1);
        assertEquals((long)d.Player2Score, 10);
    }
}