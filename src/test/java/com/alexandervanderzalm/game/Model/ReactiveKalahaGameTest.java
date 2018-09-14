package com.alexandervanderzalm.game.Model;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReactiveKalahaGameTest {

    int changes;
    @Test
    public void TestReactivePitFunctionality(){
        changes = 0;
        ReactivePit p = new ReactivePit();
        p.OnAdd.Add((c) -> changes++);
        p.OnAdd.Add((c) -> System.out.println(c));
        p.OnGrab.Add((c) -> changes++);

        p.Add(2);
        p.Add(4);

        assertEquals(2,changes);

        changes = 0;

        p.GrabAll();

        assertEquals(1,changes);
    }
}
