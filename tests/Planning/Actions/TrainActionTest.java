package Planning.Actions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainActionTest {

    @Test
    void TestConstructer(){

        assertEquals("TrainAction", new TrainAction().actionName(), "Defualt train action should be constructable.");

        assertEquals("TrainAction", new TrainAction("amount=").actionName(),
                "an amount of null is invalid, defaults to base action");

        assertEquals("TrainAction", new TrainAction("amount=0").actionName(),
                "Must train at least one unit, otherwise option is ignored.");

        assertEquals("TrainAction_amount=1", new TrainAction("amount=1").actionName(),
                "an amount of 1 is valid");

        assertEquals("TrainAction_what=worker", new TrainAction("what=worker").actionName(),
                "worker is a valid option for what to train.");

        assertEquals("TrainAction_what=combatUnit", new TrainAction("what=combatUnit").actionName(),
                "combatUnit is a valid option for what to train.");

        assertEquals("TrainAction", new TrainAction("what=").actionName(),
                "a what of null is invalid, defaults to base action");


        assertEquals("TrainAction_what=worker_amount=1", new TrainAction("what=worker_amount=1").actionName(),
                "multiple options are valid and desireable");

        assertEquals("TrainAction", new TrainAction("nonsense this is invalid").actionName(),
                "A nonsense option defaults to the base action name.");

        assertEquals("TrainAction", new TrainAction("").actionName(),
                "the empty string defaults to the base action name.");

    }

}