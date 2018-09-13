package com.alexandervanderzalm.game.Controllers;

import com.alexandervanderzalm.game.Model.*;
import com.alexandervanderzalm.game.Model.Turn.TurnData;
import com.alexandervanderzalm.game.Model.Turn.TurnInputData;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    // Use Repository
    // Change this to a gameRepo
    private IGame game;

    // TODO serve multiple game instances(requires hash?)
    public TurnData StartNewGame() {
        game = new SimpleGame();

        return game.SetupNewGame();
    }

    // TODO serve multiple game instances(requires hash?)
    public TurnData DoTurn(TurnInputData input) {
        return game.DoTurn(input.SelectedBucket);
    }
}
