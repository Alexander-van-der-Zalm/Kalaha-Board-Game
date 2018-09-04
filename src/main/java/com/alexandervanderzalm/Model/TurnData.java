package com.alexandervanderzalm.Model;

enum GameState{
    Init, TurnP1, TurnP2, TurnOtherPlayer, WinP1, WinP2, WinOtherPlayer, Disconnect, Error
}

public class TurnData {
    // Previous
    // Next
    public int Player1Score;
    public int Player2Score;
    public GameState StartOfTurnState; // Get this from previous?
    public GameState EndOfTurnState;
    // public int TurnCount??
    // BucketResult
    // Transformation sequence
}
