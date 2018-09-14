package com.alexandervanderzalm.game.Controllers;

import com.alexandervanderzalm.game.Model.*;
import com.alexandervanderzalm.game.Model.Game.IGame;
import com.alexandervanderzalm.game.Model.Turn.TurnData;
import com.alexandervanderzalm.game.Model.Turn.TurnInputData;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    // Use Repository
    // Change this to a gameRepo
    private IGame game;

    // TODO serve multiple game instances(requires gameID?)
    public TurnData StartNewGame() {
        game = new ReactiveKalahaGame();
        //game = new SimpleKalahaGame();  // Uncomment this to run the simple version
        return game.SetupNewGame();
    }

    // TODO serve multiple game instances
    public TurnData DoTurn(TurnInputData input) {
        return game.DoTurn(input.SelectedBucket);
    }
}
