package com.alexandervanderzalm.game.Utility;

import org.junit.Test;
import org.springframework.util.Assert;
import static org.junit.jupiter.api.Assertions.*;

public class MethodSchedulerPriorityQueueTest {

    Integer counter;
    @Test
    public void processMethods_ScheduleTwoMethods_CountedTwoProcessedMethods(){
        // Setup
        MethodSchedulerPriorityQueue<Turn> scheduler = new MethodSchedulerPriorityQueue<Turn>();
        counter = 0;
        scheduler.ScheduleMethod((turn) -> counter+=1);
        scheduler.ScheduleMethod((turn) -> counter+=10);
        scheduler.ProcessMethods(new Turn());

        // Test
        Assert.isTrue(counter == 11,"Both methods processed");
    }

    @Test
    public void processMethods_ScheduleTwoPriorityMethods_MethodsInOrder(){
        // Setup
        MethodSchedulerPriorityQueue<Turn> scheduler = new MethodSchedulerPriorityQueue<Turn>();
        counter = 10;
        scheduler.ScheduleMethod((turn) -> counter*=10,0);
        scheduler.ScheduleMethod((turn) -> counter+=10, 10);
        scheduler.ScheduleMethod((turn) -> System.out.println("Second"),0);
        scheduler.ScheduleMethod((turn) -> System.out.println("First"), 10);
        scheduler.ProcessMethods(new Turn());

        // Test
        Assert.isTrue(counter == 200,"Both methods processed in order");
    }

    @Test
    public void processMethods_AddOnceProcessTwice_ProcessedOnce(){
        // Setup
        MethodSchedulerPriorityQueue<Turn> scheduler = new MethodSchedulerPriorityQueue<Turn>();
        counter = 10;
        scheduler.ScheduleMethod((turn) -> counter*=10,0);
        scheduler.ScheduleMethod((turn) -> counter+=10, 10);
        scheduler.ScheduleMethod((turn) -> System.out.println("Second"),0);
        scheduler.ScheduleMethod((turn) -> System.out.println("First"), 10);
        scheduler.ProcessMethods(new Turn());   // Should clear out all processes after processed
        scheduler.ProcessMethods(new Turn());

        // Test
        Assert.isTrue(counter == 200,"Both methods processed in order");
    }

}

class Turn{

}