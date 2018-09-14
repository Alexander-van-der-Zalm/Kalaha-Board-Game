package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Model.Logger.LogData;
import com.alexandervanderzalm.game.Model.Logger.LogTypes;
import com.alexandervanderzalm.game.Model.Pits.PitUtil;
import com.alexandervanderzalm.game.Model.Turn.TurnData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class GameTest {

    // Setup up so that it can create multiple interface implementations
    private IGameCreator GameCreator;

    public GameTest(IGameCreator game) {
        this.GameCreator = game;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters(){
        return Arrays.asList(new Object[][]{
                { new SimpleGameCreator() },
                { new ReactiveGameCreator() }
        });
    }

    // Initialize a game
    @Test
    public void SetupNewGame_NewGame_Has14PitsAnd6Stones() {
        IGame g = GameCreator.Create();
        TurnData t0 = g.SetupNewGame();

        System.out.println(t0.Pits);

        Assert.isTrue(t0.Pits.size() == 14, "Check for the pit size");
        // Check if all the pits that are not kalaha have 6 stones in them
        t0.Pits.stream().filter((p) -> !p.isKalaha).forEach((p) -> assertEquals((long)p.stones,6));
    }

    @Test
    public void DoTurn_InitializedGame_TurnProcessed(){
        IGame g = GameCreator.Create();
        TurnData t0 = g.SetupNewGame();

        assertEquals(6,(long)t0.Pits.get(1).stones );//, "Check if the selected pit is empty");

        TurnData t1 = g.DoTurn(1);

        // This should fail --> make proper copies?
        Assert.isTrue(t0.Pits.get(1).stones != 0, "Check if the selected pit is empty");
        Assert.isTrue(t1.Pits.get(1).stones == 0, "Check if the selected pit is empty");
    }

    @Test
    public void DoTurn_EndGame_GameFinished(){
        // Setup
        IGame g = GameCreator.Create();
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
        IGame g = GameCreator.Create();
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
        IGame g = GameCreator.Create();
        // -->                             first row     second row (clockwise ie inverted order)
        int[] mockNormalFields = new int[]{1,0,1,0,0,0,  9,11,0,1,0,0};
        TurnData t0Setup = new TurnData(PitUtil.CreatePitDataList(22,0,mockNormalFields));
        TurnData t0 = g.SetUpGameFromTurnData(t0Setup);

        // Do the turn
        TurnData t1 = g.DoTurn(1);

        // Win detected
        assertEquals(t0.NextTurnState, GameState.TurnP1);
        assertEquals(t1.NextTurnState, GameState.WinP1);
    }

    // Skip opponents kalaha
    @Test public void DoTurn_GoRoundWithALotOfStones_SkipAndNoExtraInOpponentsKalaha(){
        // Setup
        IGame g = GameCreator.Create();
        // -->                             first row     second row (clockwise ie inverted order)
        int[] mockNormalFields = new int[]{13,0,1,0,0,0,  9,11,0,1,0,0};
        TurnData t0Setup = new TurnData(PitUtil.CreatePitDataList(22,0,mockNormalFields));
        TurnData t0 = g.SetUpGameFromTurnData(t0Setup);

        // Do the turn
        TurnData t1 = g.DoTurn(1);

        // No extra points for player 2
        assertEquals(t0.Player2Score, 0);
        assertEquals(t1.Player2Score, 0);
    }

    //Capture
    @Test public void DoTurn_LandInOwnEmpty_TransferOpponentsPitToKalaha(){
        // Setup
        IGame g = GameCreator.Create();
        // -->                             first row     second row (clockwise ie inverted order)
        int[] mockNormalFields = new int[]{0,1,0,0,1,0,  9,11,0,1,0,20};
        TurnData t0Setup = new TurnData(PitUtil.CreatePitDataList(0,0,mockNormalFields));
        TurnData t0 = g.SetUpGameFromTurnData(t0Setup);

        // Do the turn
        TurnData t1 = g.DoTurn(2);

        // Should be 20 + 1 afer turn
        assertEquals(t0.Player1Score, 0);
        assertEquals(t1.Player1Score, 21);
    }

    // Faulty input
    @Test public void DoTurn_FaultyInputInputEmptyIndex_NoTurnProcessed(){
        // Setup
        IGame g = GameCreator.Create();
        // -->                             first row     second row (clockwise ie inverted order)
        int[] mockNormalFields = new int[]{0,1,0,0,1,0,  9,11,0,1,0,20};
        TurnData t0Setup = new TurnData(PitUtil.CreatePitDataList(0,0,mockNormalFields));
        TurnData t0 = g.SetUpGameFromTurnData(t0Setup);

        // Do the turn
        TurnData t1 = g.DoTurn(1);

        // No turn progression because faulty input
        assertEquals(t0.Turn, 0);
        assertEquals(t1.Turn, 0);
    }

    // Faulty input
    @Test public void DoTurn_FaultyInputInputTwiceSameIndex_OneTurnProcessed(){
        // Setup
        IGame g = GameCreator.Create();
        // -->                             first row     second row (clockwise ie inverted order)
        int[] mockNormalFields = new int[]{1,1,0,0,1,0,  9,11,0,1,0,20};
        TurnData t0Setup = new TurnData(PitUtil.CreatePitDataList(0,0,mockNormalFields));
        TurnData t0 = g.SetUpGameFromTurnData(t0Setup);

        // Do the turn
        TurnData t1 = g.DoTurn(2);
        TurnData t2 = g.DoTurn(2);

        // No second turn progression because faulty input
        assertEquals(t0.Turn, 0);
        assertEquals(t1.Turn, 1);
        assertEquals(t2.Turn, 1);
    }

    // Faulty input
    @Test public void DoTurn_FaultyInputWrongIndexForActivePlayer_NoTurnProcessed(){
        // Setup
        IGame g = GameCreator.Create();
        // -->                             first row     second row (clockwise ie inverted order)
        int[] mockNormalFields = new int[]{0,1,0,0,1,0,  9,11,0,1,0,20};
        TurnData t0Setup = new TurnData(PitUtil.CreatePitDataList(0,0,mockNormalFields));
        TurnData t0 = g.SetUpGameFromTurnData(t0Setup);

        // Do the turn
        TurnData t1 = g.DoTurn(8); // Index of Player2

        // No turn processed and turn is still for player 1
        assertEquals(t0.Turn, 0);
        assertEquals(t0.NextTurnState,  GameState.TurnP1);
        assertEquals(t1.Turn, 0);
        assertEquals(t1.NextTurnState,  GameState.TurnP1);
    }

    // Check correct dropping of stones
    @Test public void DoTurn_DropAFewStones_CorrectlyDropped(){
        // Setup
        IGame g = GameCreator.Create();
        int[] mockNormalFields = new int[]{0,0,0,0,0,12,  0,0,0,0,0,0};
        TurnData t0Setup = new TurnData(PitUtil.CreatePitDataList(0,0,mockNormalFields));
        TurnData t0 = g.SetUpGameFromTurnData(t0Setup);

        // Do the turn
        TurnData t1 = g.DoTurn(6); // Has 12 stones

        // No turn processed and turn is still for player 1
        t1.Pits.stream().filter((p) ->  // Skip the opponents kalaha & index 6
                !(p.isKalaha && p.player == 1)
                        && t1.Pits.indexOf(p) != 6
        ).forEach((p) ->{
            System.out.println( t1.Pits.indexOf(p) + " " + p.stones);
            assertEquals(1,(long)p.stones);
        });
    }

    // Check integrity of transforms
    @Test public void DoTurn_AFewTurn_BoardStateAndPitTransformsPlusOriginalAreEqual(){
        // Setup a normal game
        IGame g = GameCreator.Create();
        TurnData t0 = g.SetupNewGame();
        List<LogData> log = new ArrayList<>();
        int[] inputSet = {6,5,13,6};
        List<TurnData> turns = new ArrayList<>();

        // Do a few turns
        Arrays.stream(inputSet).forEach((i)->{
            TurnData turn = g.DoTurn(i);
            log.addAll(turn.Log);
            turns.add(turn);
        });

        // Apply all the changes to turn0
        log.forEach((l)->{
            if(l.Type == LogTypes.PitLog){
                System.out.println(l.Index + " " + t0.Pits.get(l.Index).stones + " -> +" + l.AmountAdded + " " + l.FinalAmount);
                assertEquals((long)l.FinalAmount, t0.Pits.get(l.Index).stones + l.AmountAdded);
                t0.Pits.get(l.Index).stones = l.FinalAmount;
            }
        });

        final TurnData finalTurn = turns.get(turns.size()-1);
        t0.Pits.forEach((p) -> assertEquals(p.stones, finalTurn.Pits.get(t0.Pits.indexOf(p)).stones));
    }
}


