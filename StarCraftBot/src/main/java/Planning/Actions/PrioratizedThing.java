package Planning.Actions;

import java.util.Comparator;

/**
 * maybe this can be implemented by all actions
 */
public interface PrioratizedThing extends Comparator {
    int priority = 0;
    public int GetPriority();
    void SetPriority();

    //public int compare(PrioratizedThing o1, PrioratizedThing o2){
    //    return o1.GetPriority() - o2.GetPriority();
    //}
}
