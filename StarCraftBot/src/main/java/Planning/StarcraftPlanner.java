package Planning;

import Planning.Actions.*;
import burlap.behavior.policy.GreedyQPolicy;
import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.planning.stochastic.sparsesampling.SparseSampling;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.ReflectiveHashableStateFactory;
import src.main.java.IntelligenceAgent;

public class StarcraftPlanner {
    private Episode ep = new Episode();

    private SparseSampling sparsePlanner;
    private StarcraftEnviorment game;
    private Policy sparcePolicy;
    private IntelligenceAgent intelligenceAgent;

    public StarcraftPlanner(IntelligenceAgent intelligenceAgent) {
        this.intelligenceAgent = intelligenceAgent;
    }

    /**
     * intialize everything to use the ai planning.
     * @param initalstate (for now) a state representing the start of the game
     *                    so that stuff can be initalized.
     */
    public void Initalize(State initalstate){
        SADomain domain = new SADomain();
        //add actions to the domain
        domain.addActionType(new AttackActionType());
        domain.addActionType(new BuildActionType());
        domain.addActionType(new ExpandActionType());
        domain.addActionType(new ScoutActionType());
        domain.addActionType(new TrainActionType());
        domain.addActionType(new UpgradeActionType());

        RewardFunction initalreward = new PlanningRewardFunction(GameStatus.EARLY);

        domain.setModel(new StarcraftModel(initalreward));

        HashableStateFactory factory = new ReflectiveHashableStateFactory();

        //TODO: fill in this. a QFunction implementation
        ValueFunction valuefunction = null;

        //TODO: make sure the enviorment is initalized with everything it needs or something
        game = new StarcraftEnviorment(initalreward, intelligenceAgent);

        //NOTE TO FUTURE SELVES: consider adjusting the discount factor.
        float DiscountFactor = 0.5f;
        sparsePlanner = new SparseSampling(domain,DiscountFactor,factory,10,1);
        sparsePlanner.setValueForLeafNodes(valuefunction);

        //get inital policy for planning
        sparcePolicy = new GreedyQPolicy(sparsePlanner);

        //NOTE TO FUTURE SELVES: consider adjusting this.
        sparsePlanner.setForgetPreviousPlanResults(true);
    }

    /**
     * Exicutes an action in the enviorment with the spare planner.
     */
    public void SparsePlanStep(){
        Action todo = sparcePolicy.action( game.currentObservation());
        game.executeAction( todo );
    }

    /**
     *
     * @param vf A value funtion for evaluating
     */
    public void setGoal(ValueFunction vf, RewardFunction rf){
        sparsePlanner.setValueForLeafNodes(vf);
        sparcePolicy = new GreedyQPolicy(sparsePlanner);

        game.UpdateRewardFunction(rf);
    }
}
