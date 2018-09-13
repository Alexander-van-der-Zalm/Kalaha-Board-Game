package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Model.Pits.PitUtil;
import com.alexandervanderzalm.game.Model.Turn.TurnData;
import org.junit.Test;
import org.springframework.util.Assert;
import static org.junit.Assert.*;

public class SimpleGameTest {

    // Initialize a game
    @Test
    public void SetupNewGame_NewGame_Has14PitsAnd6Stones() {
        IGame g = new SimpleGame();
        TurnData t0 = g.SetupNewGame();

        System.out.println(t0.Pits);

        Assert.isTrue(t0.Pits.size() == 14, "Check for the pit size");
        // Check if all the pits that are not kalaha have 6 stones in them
        t0.Pits.stream().filter((p) -> !p.isKalaha).forEach((p) -> assertEquals((long)p.stones,6));
    }

    @Test
    public void DoTurn_InitializedGame_TurnProcessed(){
        IGame g = new SimpleGame();
        TurnData t0 = g.SetupNewGame();

        Assert.isTrue(t0.Pits.get(1).stones == 6, "Check if the selected pit is empty");

        TurnData t1 = g.DoTurn(1);

        // This should fail --> make proper copies?
        Assert.isTrue(t0.Pits.get(1).stones != 0, "Check if the selected pit is empty");
        Assert.isTrue(t1.Pits.get(1).stones == 0, "Check if the selected pit is empty");
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

    @Test public void DoTurn_ExtraTurnExpected_TurnReceived(){
        // Setup
        IGame g = new SimpleGame();
        // -->                             first row     second row (clockwise ie inverted order)
        int[] mockNormalFields = new int[]{1,5,0,0,0,0,  9,11,0,0,0,0};
        TurnData t0Setup = new TurnData(PitUtil.CreatePitDataList(1,10,mockNormalFields));
        TurnData t0 = g.SetUpGameFromTurnData(t0Setup);

        // Test everything as expected so far
        assertSetup(t0Setup);
        assertSetup(t0);
        assertEquals(t0.NextTurnState, GameState.TurnP1);

        TurnData t1 = g.DoTurn(1);

        // Player1 has another turn
        assertEquals(t1.NextTurnState, GameState.TurnP1);
    }


    // unwinnable condition
    @Test public void DoTurn_UnwinnableEndGame_GameFinished(){
        // Setup
        IGame g = new SimpleGame();
        // -->                             first row     second row (clockwise ie inverted order)
        int[] mockNormalFields = new int[]{1,0,1,0,0,0,  9,11,0,1,0,0};
        TurnData t0Setup = new TurnData(PitUtil.CreatePitDataList(22,0,mockNormalFields));
        TurnData t0 = g.SetUpGameFromTurnData(t0Setup);

        // Do the turn
        TurnData t1 = g.DoTurn(1);

        // Player 2 has won with a score of 30
        assertEquals(t0.NextTurnState, GameState.TurnP1);
        assertEquals(t1.NextTurnState, GameState.WinP1);
    }

    // Skip opponents kalaha
    @Test public void DoTurn_GoRoundWithALotOfStones_SkipAndNoExtraInOpponentsKalaha(){
        // Setup
        IGame g = new SimpleGame();
        // -->                             first row     second row (clockwise ie inverted order)
        int[] mockNormalFields = new int[]{13,0,1,0,0,0,  9,11,0,1,0,0};
        TurnData t0Setup = new TurnData(PitUtil.CreatePitDataList(22,0,mockNormalFields));
        TurnData t0 = g.SetUpGameFromTurnData(t0Setup);

        // Do the turn
        TurnData t1 = g.DoTurn(1);

        // Player 2 has won with a score of 30
        assertEquals(t0.Player2Score, 0);
        assertEquals(t1.Player2Score, 0);
    }

    //Capture
    @Test public void DoTurn_LandInOwnEmpty_TransferOpponentsPitToKalaha(){
        // Setup
        IGame g = new SimpleGame();
        // -->                             first row     second row (clockwise ie inverted order)
        int[] mockNormalFields = new int[]{0,1,0,0,1,0,  9,11,0,1,0,20};
        TurnData t0Setup = new TurnData(PitUtil.CreatePitDataList(0,0,mockNormalFields));
        TurnData t0 = g.SetUpGameFromTurnData(t0Setup);

        // Do the turn
        TurnData t1 = g.DoTurn(2);

        // Player 2 has won with a score of 30
        assertEquals(t0.Player1Score, 0);
        assertEquals(t1.Player1Score, 21);
    }


    // TODO faulty input
    // TODO check for transform integrity after a bunch of moves

    // TODO check other implementation
}