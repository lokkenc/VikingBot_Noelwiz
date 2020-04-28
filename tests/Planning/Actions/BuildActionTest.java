package planning.actions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildActionTest {

    @Test
    void constructorWorks(){
        BuildAction control = new BuildAction();

        assertEquals(control.actionName(), new BuildAction().actionName(), "Default build action should be constructable.");

        assertEquals("BuildAction_pop", new BuildAction("pop").actionName(),
                "pop is a valid option.");

        assertEquals("BuildAction_pop", new BuildAction("_pop").actionName(),
                "_pop is a valid option.");

        assertEquals("BuildAction_pop", new BuildAction("BuildAction_pop").actionName(),
                "A previous build action's name is a valid option.");

        assertEquals("BuildAction_research", new BuildAction("research").actionName(),
                "research is a valid option.");

        assertEquals("BuildAction_train", new BuildAction("train").actionName(), "" +
                "train is a valid option.");

        assertEquals(control.actionName(), new BuildAction("nonsense this is invalid").actionName(),
                "A nonsense option defaults to the base action name.");

        assertEquals(control.actionName(), new BuildAction("").actionName(),
                "The empty string defaults to the base action name.");
    }

}