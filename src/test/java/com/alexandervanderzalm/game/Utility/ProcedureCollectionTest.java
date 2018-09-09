package com.alexandervanderzalm.game.Utility;

import com.alexandervanderzalm.game.Utility.FunctionalInterfaces.Procedure;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

class ProcedureCollectionTest {

    Integer counter = 0;
    @Test
    void removeProcedure_AddAndRemove_NoProcedureDone() {
        Procedure p = () -> counter += 1;
        IProcedureCollection t = new ProcedureCollection();
        counter = 0;
        t.Add(p);
        t.Remove(p);

        t.Process(); // Shouldn't actually process p

        Assert.isTrue(counter == 0, "Counter should be 0 after no event");
    }
}