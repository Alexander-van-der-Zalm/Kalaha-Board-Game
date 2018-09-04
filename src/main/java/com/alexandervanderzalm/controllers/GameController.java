package com.alexandervanderzalm.controllers;

import com.alexandervanderzalm.Model.TurnData;
import com.alexandervanderzalm.Model.TurnInputData;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {

    @RequestMapping("/game")
    public TurnData InitializeGame() {
        return TurnDataService.InitGame();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/game")
    public TurnData DoTurn(@RequestBody TurnInputData input){
        return TurnDataService.DoTurn(input);
    }

    @RequestMapping("/sample")
    public TurnInputData GetSample() {
        return new TurnInputData();
    }
}
