package Planning.Actions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildActionTest {

    @Test
    void constructorWorks(){
        testingbuildaction control = new testingbuildaction();
        assertEquals(control.actionName(), new BuildAction().actionName(), "Defualt build action should be constructable.");

        assertEquals("BuildAction_pop", new BuildAction("pop").actionName(),
                "pop is a valid option.");

        assertEquals("BuildAction_pop", new BuildAction("_pop").actionName(),
                "_pop is a valid option.");

        assertEquals("BuildAction_pop", new BuildAction("BuildAction_pop").actionName(),
                "a previous build action's name is a valid option.");

        assertEquals("BuildAction_research", new BuildAction("research").actionName(),
                "research is a valid option.");

        assertEquals("BuildAction_train", new BuildAction("train").actionName(), "" +
                "train is a valid option.");

        control = new testingbuildaction();
        assertEquals(control.actionName(), new BuildAction("nonsense this is invalid").actionName(),
                "A nonsense option defaults to the base action name.");

        control = new testingbuildaction();
        assertEquals(control.actionName(), new BuildAction("").actionName(),
                "the empty string defaults to the base action name.");
    }

}