package planning;


import planning.actions.helpers.ActionParserHelper;
import burlap.mdp.core.action.Action;

import java.util.Comparator;

public class QueueComparator implements Comparator {
    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     * <p>
     * In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.<p>
     * <p>
     * The implementor must ensure that <tt>sgn(compare(x, y)) ==
     * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>compare(x, y)</tt> must throw an exception if and only
     * if <tt>compare(y, x)</tt> throws an exception.)<p>
     * <p>
     * The implementor must also ensure that the relation is transitive:
     * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
     * <tt>compare(x, z)&gt;0</tt>.<p>
     * <p>
     * Finally, the implementor must ensure that <tt>compare(x, y)==0</tt>
     * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
     * <tt>z</tt>.<p>
     * <p>
     * It is generally the case, but <i>not</i> strictly required that
     * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking,
     * any comparator that violates this condition should clearly indicate
     * this fact.  The recommended language is "Note: this comparator
     * imposes orderings that are inconsistent with equals."
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the
     * second.
     * @throws NullPointerException if an argument is null and this
     *                              comparator does not permit null arguments
     * @throws ClassCastException   if the arguments' types prevent them from
     *                              being compared by this comparator.
     */
    @Override
    public int compare(Object o1, Object o2) {
        int netdifference = 0;

        //So, this queue is for Actions only
        Action action1 = (Action) o1;
        Action action2 = (Action) o2;

        ActionParserHelper.ActionEnum a1type = ActionParserHelper.GetActionType(action1);
        ActionParserHelper.ActionEnum a2type = ActionParserHelper.GetActionType(action2);

        String a1name;
        String a2name;

        if(a1type == a2type){
            //TODO: Once the bellow are added to the build actions,
            //      then the build action should be prioratized in that order
            //creep/power providers = highest priority
            //population building = next highest (for Zerg, and terran if ever implemented)
            //unique buildings = next highest
            //training buildings = next highest

            a1name = action1.actionName();
            a2name = action2.actionName();

            //prioratizes population buildings above all others.
            if (a1name.equals(a2name)){
                netdifference = 0; //no op
            } else if(a1name.endsWith("_pop")){
                netdifference = -1;
            } else if(a2name.endsWith("_pop")){
                netdifference = +1;
            }


        }else{
            netdifference = ActionTypeValues(a1type) - ActionTypeValues(a2type);
        }

        return netdifference;
    }

    /**
     * Assigns a priority to an action given its type.
     * NOTE: a lower value means a higher priority.
     * @param atype an Action Type Enumerator from the ActionParserHelper class.
     * @return an integer representing the priority of the action for the starcraft bot
     *         to do.
     */
    private static int ActionTypeValues(ActionParserHelper.ActionEnum atype){
        int value = 0;
        switch (atype){
            case UPGRADE:
                value = 20;
                break;
            case TRAIN:
                value = 20;
                break;
            case SCOUT:
                value = 11;
                break;
            case EXPAND:
                value = 20;
                break;
            case BUILD:
                value = 19;
                break;
            case ATTACK:
                value = 10;
                break;
            case GATHER:
                value = 11;
                break;
            case UNKNOWN:
                value = 10000;
                break;
        }

        return value;
    }
}
