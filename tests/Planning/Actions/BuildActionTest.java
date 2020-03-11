package Planning.Actions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildActionTest {

    @Test
    void constructorWorks(){
        testingbuildaction control = new testingbuildaction();
        assertEquals(control.actionName(), new BuildAction().actionName(), "Defualt build action should be constructable.");

        control = new testingbuildaction();
        control.SetActionName("BuildAction_pop");
        assertEquals(control.actionName(), new BuildAction("pop").actionName(), "pop is a valid option.");

        control = new testingbuildaction();
        control.SetActionName("BuildAction_pop");
        assertEquals(control.actionName(), new BuildAction("_pop").actionName(), "_pop is a valid option.");

        control = new testingbuildaction();
        control.SetActionName("BuildAction_pop");
        assertEquals(control.actionName(), new BuildAction("BuildAction_pop").actionName(), "a previous build action's name is a valid option.");

        control = new testingbuildaction();
        control.SetActionName("BuildAction_research");
        assertEquals(control.actionName(), new BuildAction("research").actionName(), "research is a valid option.");

        control = new testingbuildaction();
        control.SetActionName("BuildAction_train");
        assertEquals(control.actionName(), new BuildAction("train").actionName(), "train is a valid option.");

        control = new testingbuildaction();
        assertEquals(control.actionName(), new BuildAction("nonsense this is invalid").actionName(), "A nonsense option defaults to the base action name.");

        control = new testingbuildaction();
        assertEquals(control.actionName(), new BuildAction("").actionName(), "the empty string defaults to the base action name.");
    }

}