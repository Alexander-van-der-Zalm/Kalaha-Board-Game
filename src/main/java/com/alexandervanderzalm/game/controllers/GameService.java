package com.alexandervanderzalm.game.controllers;

import com.alexandervanderzalm.game.Model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GameService {

    // Use Repository
    // Change this to a gameRepo
    private IGame game;

    public TurnData InitGame() {

        //if(game == null){
            game = new SimpleGame();
        //}

        return game.InitializeGame();
    }

    public TurnData DoTurn(TurnInputData input) {
        // Validate valid input

        // If so do the actual turn
        return game.DoTurn(input.SelectedBucket);
    }

    private TurnData CreateFauxData(){
        TurnData turn = new TurnData();
        turn.NextTurnState = GameState.TurnP1;
        turn.Pits = new ArrayList<>();
        turn.Pits.add(new KalahaPitData());
        turn.Pits.add(new KalahaPitData());
        turn.Pits.add(new KalahaPitData());
        return turn;
    }

}
