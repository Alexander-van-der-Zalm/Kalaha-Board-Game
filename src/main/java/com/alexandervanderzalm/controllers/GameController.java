package com.alexandervanderzalm.controllers;

import com.alexandervanderzalm.Model.TurnData;
import com.alexandervanderzalm.Model.TurnDataFromClient;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {

    @GetMapping("/")
    public String getHelloWorldMessage() {
        return "YOYO";
    }

    @RequestMapping("/game")
    public TurnData InitializeGame() {
        return new TurnData();
        // Change to Service
    }

    @RequestMapping(method = RequestMethod.POST, value = "/game")
    public TurnData DoTurn(@RequestBody TurnDataFromClient input){
        TurnData test = new TurnData();
        test.Player1Score = input.PlayerID;
        return test;
        // Change to Service
    }

    @RequestMapping("/sample")
    public TurnDataFromClient GetSample() {
        return new TurnDataFromClient();
    }
}
