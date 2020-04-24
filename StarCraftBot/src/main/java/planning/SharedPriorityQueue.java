package planning;

import burlap.mdp.core.action.Action;

import java.util.PriorityQueue;

/**
 * A priority queue for the ai planning to communicate with the starcraft bot
 *
 * Works with the StarcraftPlanner to decide what to do next.
 */
public class SharedPriorityQueue {
    private PriorityQueue<Action> ActionQueue;
    private StarcraftPlanner Planner;

    public SharedPriorityQueue(StarcraftPlanner planner){
        Planner = planner;
        ActionQueue = new PriorityQueue<Action>(3,new QueueComparator());
    }


    public Action DeQueue(){
        Action result = ActionQueue.poll();

        Planner.SparsePlanStep();

        return result;
    }

    public boolean EnQueue(Action action){
        boolean add = ActionQueue.add(action);

        if(!add){
            System.err.println("Error: failed to add action.");
        }

        return add;
    }


    public Action Peek(){
        return ActionQueue.peek();
    }

    public int size() {
        return ActionQueue.size();
    }
}
