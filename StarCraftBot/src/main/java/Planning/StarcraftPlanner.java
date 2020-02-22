package Planning;

import Planning.Actions.*;
import burlap.behavior.policy.GreedyQPolicy;
import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.learning.lspi.SARSCollector;
import burlap.behavior.singleagent.learning.lspi.SARSData;
import burlap.behavior.singleagent.planning.stochastic.montecarlo.uct.UCT;
import burlap.behavior.singleagent.planning.stochastic.sparsesampling.SparseSampling;
import burlap.behavior.singleagent.pomdp.wrappedmdpalgs.BeliefSparseSampling;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.Environment;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.model.SampleModel;
import burlap.statehashing.HashableStateFactory;

public class StarcraftPlanner {
    private Episode ep = new Episode();

    private BeliefSparseSampling beliefPlanner;
    private SparseSampling sparsePlanner;
    private UCT uctPlanner;
    private Environment game;
    private GreedyQPolicy uctPolicy;
    private Policy sparcePolicy;


    /**
     * Function to initalize the two planning functions until we decide on one
     * @param domain the game domain.
     * @param factory a state factory for all game nodes.
     * @param inialValue the value function for reaching the inital goal.
     */
    private void initalizePlanners(SADomain domain, HashableStateFactory factory, ValueFunction inialValue){
        float DiscountFactor = 0.5f;
        uctPlanner = new UCT(domain, DiscountFactor, factory, 5, 10, -1 );

        sparsePlanner = new SparseSampling(domain,DiscountFactor,factory,10,1);
        sparsePlanner.setValueForLeafNodes(inialValue);

        //beliefPlanner = new BeliefSparseSampling(domain,DiscountFactor,factory,10,-1);

        game = new StarcraftEnviorment();
        uctPolicy = uctPlanner.planFromState(game.currentObservation());
        sparcePolicy = new GreedyQPolicy(sparsePlanner);
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

        SampleModel scsample = new StarcraftSampleModle();

        //TODO: MAKE A class that implements SampleModel.
        domain.setModel(scsample);

        HashableStateFactory factory = null;

        ValueFunction initalreward = null;

        initalizePlanners(domain, factory, initalreward);

        sparsePlanner.setForgetPreviousPlanResults(true);
    }

    public void UCTPlanStep(){
        Action todo = uctPolicy.action(game.currentObservation());
        game.executeAction( todo );
    }

    public void SparsePlanStep(){
        Action todo = uctPolicy.action( game.currentObservation());
        game.executeAction( todo );
    }


    public void setGoal(ValueFunction vf){
        sparsePlanner.setValueForLeafNodes(vf);
        uctPolicy = sparsePlanner.planFromState(game.currentObservation());

        //TODO: figure out how to tell the UCT where to go.
    }


}
