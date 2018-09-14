package com.alexandervanderzalm.game.Model.Game;

import com.alexandervanderzalm.game.Model.Logger.LogUtility;
import com.alexandervanderzalm.game.Model.Pits.ReactivePit;
import com.alexandervanderzalm.game.Model.ReactiveKalahaGame;
import com.alexandervanderzalm.game.Utility.FunctionCollection;

public class ReactiveGameRules {
    public static void WireReactiveKalahaGameRules(ReactiveKalahaGame game){

        // Special turn ending scenarios (override a normal to TurnData)
        game.Functionality.SpecialEndOfTurnScenarios = new FunctionCollection<>();

        // Normal End Game - Priority 1
        game.Functionality.SpecialEndOfTurnScenarios.Add((d) ->{
            return GameUtil.EmptyFieldsWinCondition(d);
        });

        // Unwinnable - Priority 2
        game.Functionality.SpecialEndOfTurnScenarios.Add((d) ->{
            return GameUtil.UnwinnableWinCondition(d);
        });

        // Log for new turn
        game.Functionality.SpecialEndOfTurnScenarios.Add((d) ->{
            LogUtility.Log(game.Data.Logger, String.format("%sNew Turn",LogUtility.LogStart(GameUtil.GetPlayerNextTurn(d),game.Data.CurrentTurn + 1)));
            return null;
        });

        // Add basic pit logging capability  (used for view animation for example)
        game.Data.Pits.Stream().forEach((pit) ->{
            pit.OnAdd.Add((stones) -> LogUtility.LogPit(game.Data, pit, stones));
            pit.OnGrab.Add((stones) -> LogUtility.LogPit(game.Data, pit, stones));
        });

        // Add extra turn rule check only to Kalahas
        game.Data.Pits.Stream()
            .filter((p) -> p.Data().isKalaha)
            .forEach((pit) ->{
                pit.OnAdd.Add((stones) -> {
                    // Check if the hand is almost empty && if the kalaha is of the current player
                    if(game.Data.CurrentHand == 1 && pit.Data().player == game.Data.CurrentPlayer){

                        // Add an entry to the logger
                        LogUtility.Log(game.Data.Logger,String.format("%sGains an Extra Turn for dropping the last stone in his own Kalaha.",
                                LogUtility.LogStart(game.Data.CurrentPlayer ,  game.Data.CurrentTurn)
                        ));

                        // Change the gameState to the same player
                        GameUtil.FlipGameState(game.Data);
                    }
                });
        });

        // Add capture rule
        game.Data.Pits.pList.stream()
                .filter((p) -> !p.Data().isKalaha)
                .forEach((pit) ->{
            pit.OnAdd.Add((changed) ->{
                if(game.Data.CurrentHand == 1 && pit.Amount()-changed == 0 && pit.Data().player == game.Data.CurrentPlayer){

                    ReactivePit opposite = game.Data.Pits.Opposite(pit);
                    ReactivePit kalaha = game.Data.Pits.KalahaOfPlayer(game.Data.CurrentPlayer);

                    // To prevent triggering of next turn on scoring of kalaha
                    game.Data.CurrentHand=0;
                    // Grab the one from the pit that was just dropped
                    pit.GrabAll();
                    // Capture opposite stones
                    Integer capturedStones = opposite.GrabAll();

                    kalaha.Add(1);
                    kalaha.Add(capturedStones);

                    // Log the event
                    LogUtility.Log(game.Data.Logger,String.format("%sCaptured %d stones from opposite pit %d and scored %d.",
                            LogUtility.LogStart(game.Data.CurrentPlayer ,  game.Data.CurrentTurn),
                            capturedStones,
                            game.Data.Pits.IndexOf(opposite),
                            capturedStones + 1
                    ));

                }
            });
        });


        // The core turn procedure definition
        game.Functionality.TurnProcedure = (index) -> {
            ReactivePit current = game.Data.Pits.Get(index);
            game.Data.CurrentHand = current.GrabAll();

            // Log pickup
            LogUtility.Log(game.Data.Logger,
                String.format("%sGrabbed %d stones from pit %d.",
                    LogUtility.LogStart(game.Data.CurrentPlayer ,  game.Data.CurrentTurn),
                    game.Data.CurrentHand,
                    index
                )
            );

            // The main drop it one by one loop
            while(game.Data.CurrentHand > 0){
                // Move one to the right (counter-clockWise)
                current = game.Data.Pits.Right(current);

                // Skip when landed upon oponents kalaha
                if (current.Data().isKalaha && current.Data().player != game.Data.CurrentPlayer) {
                    LogUtility.Log(game.Data.Logger, String.format("Turn %d - Skipped dropping a stone at opponents Kalaha.", game.Data.CurrentTurn));
                    continue;
                }

                // Add to current pit and then remove from hand (to triggering conditional bugs for extra turn)
                current.Add(1);
                game.Data.CurrentHand--;
            }
        };
    }
}
