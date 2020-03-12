package Planning.Actions;

import burlap.mdp.core.action.SimpleAction;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ActionParserHelperTest {

    @org.junit.jupiter.api.Test
    void getActionTypeShouldBeUnkown() {
        Assertions.assertEquals(ActionParserHelper.ActionEnum.UNKNOWN,
                ActionParserHelper.GetActionType(new SimpleAction("unknownname")),
                "ActionParser  unknownTest 1: a random string should be unknown");

        assertEquals(ActionParserHelper.ActionEnum.UNKNOWN,
                ActionParserHelper.GetActionType(new SimpleAction("asdgbnvngsfhfgs")),
                "ActionParser unknown Test 2: a random string should be unknown");

        assertEquals(ActionParserHelper.ActionEnum.UNKNOWN,
                ActionParserHelper.GetActionType(new SimpleAction("attackaction")),
                "ActionParser unknown Test 3: should not ignore case");


        assertEquals(ActionParserHelper.ActionEnum.UNKNOWN,
                ActionParserHelper.GetActionType(new SimpleAction("scoutaction")),
                "ActionParser unknown test4: should not ignore case");

        assertEquals(ActionParserHelper.ActionEnum.UNKNOWN,
                ActionParserHelper.GetActionType(new SimpleAction("unknownname")),
                "ActionParser Test 1: a random string should be unknown");
    }

    @org.junit.jupiter.api.Test
    void getActionTypeBuildAction() {
        assertEquals(ActionParserHelper.ActionEnum.BUILD,
                ActionParserHelper.GetActionType(new BuildAction("_pop")),
                "ActionParser build Test 1: building with arguments should be known");

        assertEquals(ActionParserHelper.ActionEnum.BUILD,
                ActionParserHelper.GetActionType(new BuildAction()),
                "ActionParser build Test 2: defualt build action should be recognized");

        assertNotEquals(ActionParserHelper.ActionEnum.BUILD,
                ActionParserHelper.GetActionType(new SimpleAction("buildaction_pop")),
                "ActionParser build Test 3: should not ignore case");

        assertNotEquals(ActionParserHelper.ActionEnum.BUILD,
                ActionParserHelper.GetActionType(new SimpleAction("buildaction")),
                "ActionParser build test4: should not ignore case");
    }

    @org.junit.jupiter.api.Test
    void getActionTypeTrainAction() {
        assertEquals(ActionParserHelper.ActionEnum.TRAIN,
                ActionParserHelper.GetActionType(new TrainAction("_what=Worker")),
                "ActionParser build Test 1: building with arguments should be known");

        assertEquals(ActionParserHelper.ActionEnum.TRAIN,
                ActionParserHelper.GetActionType(new TrainAction("what=Worker")),
                "ActionParser build Test 1: building with arguments should be known");

        assertEquals(ActionParserHelper.ActionEnum.TRAIN,
                ActionParserHelper.GetActionType(new TrainAction("what=Worker_amount=1")),
                "ActionParser build Test 2: a train action with multiple args should be recognized");

        assertEquals(ActionParserHelper.ActionEnum.TRAIN,
                ActionParserHelper.GetActionType(new TrainAction("")),
                "ActionParser build Test 2: an empty string build should still be a build");


        assertNotEquals(ActionParserHelper.ActionEnum.TRAIN,
                ActionParserHelper.GetActionType(new SimpleAction("trainaction_what=something")),
                "ActionParser build Test 3: should not ignore case");


        assertNotEquals(ActionParserHelper.ActionEnum.TRAIN,
                ActionParserHelper.GetActionType(new SimpleAction("trainaction")),
                "ActionParser build test4: should not ignore case");
    }

    @org.junit.jupiter.api.Test
    void getActionAttackAction() {
        //TODO: FIX THIS TEST. something with the constructor appears to be not asigning action name at the end

        AttackAction action = new AttackAction("what=harass");
        assertEquals(ActionParserHelper.ActionEnum.ATTACK,
                ActionParserHelper.GetActionType(action),
                "ActionParser attack Test 1: attack with arguments should be known");

        assertEquals(ActionParserHelper.ActionEnum.ATTACK,
                ActionParserHelper.GetActionType(new AttackAction("what=army")),
                "ActionParser attack Test 1: attack with arguments should be known");

        //may or maynot work.
        assertEquals(ActionParserHelper.ActionEnum.ATTACK,
                ActionParserHelper.GetActionType(new AttackAction("what=Worker_amount=all")),
                "ActionParser attack Test 2: a AttackAction action with multiple args should be recognized");

        String[] options = new String[2];
        options[0] = "what=harass";
        options[1] = "amount=all";

        assertEquals(ActionParserHelper.ActionEnum.ATTACK,
                ActionParserHelper.GetActionType(new AttackAction(options)),
                "ActionParser attack Test 3: a AttackAction action with multiple args should be recognized");


        assertEquals(ActionParserHelper.ActionEnum.ATTACK,
                ActionParserHelper.GetActionType(new AttackAction("")),
                "ActionParser attack Test 4: an empty string attack should still be a build");


        assertNotEquals(ActionParserHelper.ActionEnum.ATTACK,
                ActionParserHelper.GetActionType(new SimpleAction("trainaction_what=something")),
                "ActionParser attack Test 5: should not ignore case");


        assertNotEquals(ActionParserHelper.ActionEnum.ATTACK,
                ActionParserHelper.GetActionType(new SimpleAction("trainaction")),
                "ActionParser attack test 6: should not ignore case");
    }

    @org.junit.jupiter.api.Test
    void getActionTypeExpandAction() {
        assertEquals(ActionParserHelper.ActionEnum.EXPAND,
                ActionParserHelper.GetActionType(new SimpleAction("ExpandAction")),
                "ActionParser expand Test 1: building with arguments should be known");

        assertNotEquals(ActionParserHelper.ActionEnum.EXPAND,
                ActionParserHelper.GetActionType(new SimpleAction("expandaction_nonsense")),
                "ActionParser expand Test 2: should not ignore case");

        assertNotEquals(ActionParserHelper.ActionEnum.EXPAND,
                ActionParserHelper.GetActionType(new SimpleAction("expandaction")),
                "ActionParser build test3: should not ignore case");
    }
}