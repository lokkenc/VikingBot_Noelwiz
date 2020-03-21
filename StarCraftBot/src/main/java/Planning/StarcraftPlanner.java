package Planning;

import Planning.Actions.*;
import burlap.behavior.policy.GreedyQPolicy;
import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.planning.stochastic.sparsesampling.SparseSampling;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.ReflectiveHashableStateFactory;
import src.main.java.IntelligenceAgent;

public class StarcraftPlanner {
    private Episode ep = new Episode();

    private SparseSampling sparsePlanner;
    private StarcraftEnvironment game;
    private Policy sparcePolicy;
    private IntelligenceAgent intelligenceAgent;
    private SharedPriorityQueue Actions;

    public StarcraftPlanner(IntelligenceAgent intelligenceAgent) {
        this.intelligenceAgent = intelligenceAgent;

    }

    /**
     * intialize everything to use the ai planning.
     * @param queue queue shared between the planner and the bot
     */
    public void Initalize(SharedPriorityQueue queue){
        this.Actions = queue;

        SADomain domain = new SADomain();
        //add actions to the domain
        domain.addActionType(new AttackActionType());
        domain.addActionType(new BuildActionType());
        domain.addActionType(new ExpandActionType());
        domain.addActionType(new ScoutActionType());
        domain.addActionType(new TrainActionType());
        domain.addActionType(new UpgradeActionType());

        RewardFunction initalreward = new PlanningRewardFunction(GameStatus.EARLY);


        StarcraftModel model = new StarcraftModel(initalreward);
        domain.setModel(model);

        HashableStateFactory factory = new ReflectiveHashableStateFactory();

        //TODO: fill in this. a QFunction implementation
        ValueFunction valuefunction = new DummyQValue();

        //TODO: make sure the enviorment is initalized with everything it needs or something

        game = new StarcraftEnvironment(initalreward, intelligenceAgent, model);

        //NOTE TO FUTURE SELVES: consider adjusting the discount factor.
        float DiscountFactor = 0.5f;
        sparsePlanner = new SparseSampling(domain,DiscountFactor,factory,10,1);
        sparsePlanner.setValueForLeafNodes(valuefunction);

        //get inital policy for planning
        sparcePolicy = new GreedyQPolicy(sparsePlanner);

        //NOTE TO FUTURE SELVES: consider adjusting this.
        sparsePlanner.setForgetPreviousPlanResults(true);
        sparsePlanner.setComputeExactValueFunction(true);

        //enqueue 3 actions
        SparsePlanStep();
        SparsePlanStep();
        SparsePlanStep();
    }

    /**
     * Exicutes an action in the enviorment with the spare planner.
     */
    public void SparsePlanStep(){
        if (roomInQueue()) {
            Action todo = sparcePolicy.action(game.currentObservation()); //ERROR HERE: INFINITE LOOP OF ACTIONS
            Actions.EnQueue(todo);
        }
    }

    /**
     *
     * @param vf A value funtion for evaluating
     */
    public void setGoal(ValueFunction vf){
        sparsePlanner.setValueForLeafNodes(vf);
        sparcePolicy = new GreedyQPolicy(sparsePlanner);

        game.UpdateRewardFunction(rf);
    }


    public void ExecuteAction(){
        game.executeAction(Actions.DeQueue());
    }

    public Boolean roomInQueue() {
        if (Actions.size() > 10)
            return false;
        return true;
    }
}
