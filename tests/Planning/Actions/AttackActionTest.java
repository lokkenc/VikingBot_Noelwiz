package Planning.Actions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AttackActionTest {



    @Test
    void actionName() {
        testingattackaction action = new testingattackaction();
        String[] arguments = {"what=base", "group=all", "amount=all"};

        action.SetActionName("AttackAction_what=base_group=all_amount=all");


        //tests the array based constructor used by string based constructor, and addOptions
        AttackAction otheraction = new AttackAction(arguments);
        assertEquals(action.actionName(),
                otheraction.actionName(),
                "addOptions should work to add all 3 options, and so should the array based constructor");

        otheraction = new AttackAction("_what=base_group=all_amount=all");
        assertEquals(action.actionName(),
                otheraction.actionName(),
                "addOptions should work to add all 3 options");

    }

    @Test
    void copy() {
        AttackAction action = new AttackAction();
        assertEquals(action.actionName(),
                action.copy().actionName(),
                "Copy test attack action: copying an attack should be the same");


        action = new AttackAction("_what=army");
        assertEquals(action.actionName(),
                action.copy().actionName(),
                "Copy test attack action: copying an attack with arguements should be the same");

    }



    @Test
    void testSTringContructor(){
        testingattackaction action = new testingattackaction();
        action.SetActionName("AttackAction");

        assertEquals(action.actionName(),
                new AttackAction().actionName(),
                "this should always pass");
    }
}