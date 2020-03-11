package Planning.Actions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AttackActionTest {



    @Test
    void actionName() {
        testingattackaction action = new testingattackaction();
        String arguments = "what=base_group=all_amount=all";

        //tests the array based constructor used by string based constructor, and addOptions
        AttackAction otheraction = new AttackAction(arguments);
        assertEquals("AttackAction_what=base_group=all_amount=all",
                otheraction.actionName(),
                "addOptions should work to add all 3 options, and so should the array based constructor");


        assertEquals("AttackAction_what=base_group=all_amount=all",
                otheraction.actionName(),
                "addOptions should work to add all 3 options");

    }

    @Test
    void copy() {
        AttackAction action = new AttackAction();

        assertEquals("AttackAction", action.actionName());

        assertEquals("AttackAction",
                action.copy().actionName(),
                "Copy test attack action: copying an attack should be the same");


        action = new AttackAction("_what=army");
        assertEquals(action.actionName(),
                action.copy().actionName(),
                "Copy test attack action: copying an attack with arguements should be the same");

    }



    @Test
    void testStringContructor(){
        assertEquals("AttackAction",
                new AttackAction().actionName(),
                "this should always pass");

        assertEquals("AttackAction_what=army",
                new AttackAction("what=army").actionName(),
                "what=army is valid");

        assertEquals("AttackAction_what=base",
                new AttackAction("what=base").actionName(),
                "what=base is valid");

        assertEquals("AttackAction_what=defend",
                new AttackAction("what=defend").actionName(),
                "what=defend is valid");

        assertEquals("AttackAction_unit=zergling",
                new AttackAction("unit=zergling").actionName(),
                "unit=zergling is valid, for now.");

        /*
        //TODO: ENABLE THIS TEST WHEN we start parsing if the what is a real unit.
        assertEquals("AttackAction",
                new AttackAction("what=nonsense").actionName(),
                "what=nonsense is invalid, should default to not including this");


        assertEquals("AttackAction_unit=zergling",
                new AttackAction("what=nonsense_unit=zergling").actionName(),
                "what=nonsense is invalid, should default to not including this");

         */
    }
}