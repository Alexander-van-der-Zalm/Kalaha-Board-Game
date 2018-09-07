package com.alexandervanderzalm.game.Utility;

import com.alexandervanderzalm.game.Utility.FunctionalInterfaces.Procedure;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

class TriggerProcedureOnChangeTest {

    Integer counter = 0;
    @Test
    void removeProcedure_AddAndRemove_NoProcedureDone() {
        Procedure p = () -> counter += 1;
        ITriggerProcedureOnChange t = new TriggerProcedureOnChange();
        counter = 0;
        t.AddProcedure(p);
        t.RemoveProcedure(p);

        t.TriggerOnChangedProcedures(); // Shouldn't trigger

        Assert.isTrue(counter == 0, "Counter should be 0 after no event");
    }
}