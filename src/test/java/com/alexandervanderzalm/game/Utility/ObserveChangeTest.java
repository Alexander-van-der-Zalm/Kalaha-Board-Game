package com.alexandervanderzalm.game.Utility;

import com.alexandervanderzalm.game.Model.Pit;
import org.junit.Test;
import org.springframework.util.Assert;

import javax.validation.constraints.AssertTrue;
import java.util.List;

public class ObserveChangeTest {

    int TestLocal = 0;

    @Test
    public void Set_AddedOnChangedIntCounter_IntegerMatchesChangeCount() {
        ObserveChange<Integer> number = new ObserveChange<Integer>();
        TestLocal = 0;
        number.OnChangeAdd(() -> TestLocal += 1);
        number.Set(1);
        number.Set(2);

        Assert.isTrue(number.Get() == 2, "Number has been correctly set");
        Assert.isTrue(TestLocal == 2, "After two changes (each change adds 1) - testNumber == 2");
    }

/*    @Test
    public void Set_AddedOnChangedPitCounter_IntegerMatchesChangeCount(){
        ObserveChange<Pit> pit = new ObserveChange<Pit>();
        TestLocal = 0;
        pit.OnChangeAdd(() -> TestLocal += 1);
        pit.Get().Add(2);
        pit.Get().Add(2);
        //Assert.isTrue(TestLocal == 2, "After two changes (each change adds 1) - testNumber == 2");
    }*/
}