package planning;

import agents.EconomyAgent;
import agents.IntelligenceAgent;
import agents.StrategyAgent;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.Environment;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import burlap.mdp.singleagent.model.RewardFunction;
import bwapi.Game;
import bwapi.Player;
import bwapi.Race;
import bwapi.UnitType;
import planning.actions.helpers.ActionParserHelper;
import planning.actions.BuildAction;
import planning.actions.TrainAction;
import planning.actions.helpers.ProtossBuildingParserHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class StarcraftEnvironment implements Environment {
    private Race PlayerRace;
    private Race EnemyRace;
    private RewardFunction rewardFunction;
    private double PreviousReward = 0;
    private IntelligenceAgent intelligenceAgent = null;
    private EconomyAgent economyAgent;
    private PriorityQueue<Action> ActionQueue;
    private StarcraftModel model;
    private Game game;
    private Player self;
    private StrategyAgent strategyAgent;


    /**
     * Constructer for the enviorment
     * @param rf the reward function that should be used to calculate rewards
     * @param intelligenceAgent the {@link IntelligenceAgent} associated with the current game
     * @param model the {@link StarcraftModel} that is associated with this environment
     * @param strats The strategy agent for the attacking
     */
    public StarcraftEnvironment(RewardFunction rf, IntelligenceAgent intelligenceAgent, StarcraftModel model, StrategyAgent strats){
        this.intelligenceAgent = intelligenceAgent;
        economyAgent = new EconomyAgent(intelligenceAgent.getGame());
        this.model = model;
        rewardFunction = rf;
        ActionQueue = new PriorityQueue<Action>(new QueueComparator());
        PlayerRace = intelligenceAgent.getPlayerRace();
        EnemyRace = intelligenceAgent.getEnemyRace();
        game = intelligenceAgent.getGame();
        self = intelligenceAgent.getSelf();
        strategyAgent = strats;
    }

    /**
     * Change the reward function used.
     * @param rf the reward function that should be used
     */
    public void UpdateRewardFunction(RewardFunction rf){
        rewardFunction = rf;
    }


    /**
     * Calls some unknown functions in the Agents.IntelligenceAgent to figure
     * out the current state of the game.
     * @return a {@link State} representing the current observation
     */
    public State currentObservation() {
        State retState;
        int numWorkers = intelligenceAgent.getNumWorkers();
        int mineralProductionRate = Math.round(intelligenceAgent.getMineralProductionRate());
        int gasProductionRate = Math.round(intelligenceAgent.getGasProductionRate());
        int numBases = intelligenceAgent.getNumBases();
        int timeSinceLastScout = intelligenceAgent.getTimeSinceLastScout();
        ArrayList<CombatUnitStatus> combatUnitStatuses = intelligenceAgent.getCombatUnitStatuses();
        int numEnemyWorkers = intelligenceAgent.getNumEnemyWorkers();
        int numEnemyBases = intelligenceAgent.getNumEnemyBases();
        UnitType mostCommonCombatUnit = intelligenceAgent.getMostCommonCombatUnit();
        Boolean attackingEnemyBase = intelligenceAgent.attackingEnemyBase();
        Boolean beingAttacked = intelligenceAgent.beingAttacked();
        Race enemyRace = intelligenceAgent.getEnemyRace();
        GameStatus gameStatus = intelligenceAgent.getGameStatus();
        int[][] trainingCapacity = intelligenceAgent.getTrainingCapacity();
        int populationCapacity = intelligenceAgent.getPopulationCapacity();
        int populationUsed = intelligenceAgent.getPopulationUsed();
        HashMap<UnitType, Integer> unitMemory = intelligenceAgent.getUnitMemory();

        retState = new PlanningState(numWorkers, mineralProductionRate, gasProductionRate, numBases, timeSinceLastScout,
                combatUnitStatuses, numEnemyWorkers, numEnemyBases, mostCommonCombatUnit, attackingEnemyBase,
                beingAttacked, PlayerRace, enemyRace, gameStatus, trainingCapacity, populationCapacity, populationUsed,
                unitMemory);

        return retState;
    }

    /**
     * Intercepts the action given by executeAction(Action) and sends it to the bot to be done.
     * @param action the action to be executed
     * @return the {@link EnvironmentOutcome} that results from the action being taken
     */
    public EnvironmentOutcome executeAction(Action action) {
        ActionParserHelper aph = new ActionParserHelper();
        //TODO: interperate actions based on their name.
        String actionName = action.actionName();
        switch (aph.GetActionType(action)) {
            case ATTACK:
                String args[] = actionName.split("_");
                String attackTarget = " ";
                //temp boolean since we either attack with everything, or
                //some combo of units
                boolean attackWith = true;

                for (int i = 0; i < args.length; i++){
                    if(args[i].startsWith("what=")){
                        attackTarget = args[i].substring(5);
                    }else if(args[i].startsWith("unit=")){
                        /*
                        unsued right now.
                        TODO: parse this argument when implemented
                        if(args[i].endsWith("all")) {
                            attackWith = true;
                        } else {
                            attackWith = false;
                        }
                        */

                    }
                }

                switch (attackTarget){
                    case "harass":

                        break;
                    case "base":


                        break;

                    case "army":


                        break;

                    case "defend":


                        break;
                    default:
                        //intelligenceAgent.get
                        break;
                }

                strategyAgent.attackEnemy(IntelligenceAgent.getInstance(game).getCombatUnits(self));

                break;
            case BUILD:
                BuildAction buildAction = (BuildAction) action;
                economyAgent.createBuildingOfType(game, self,
                        ProtossBuildingParserHelper.translateBuilding(buildAction));
                break;
            case EXPAND:

                break;
            case TRAIN:
                TrainAction trainAction = (TrainAction) action;
                if (trainAction.getUnitToTrain().equalsIgnoreCase("worker")) {
                    economyAgent.trainWorker();
                }
                else if (trainAction.getUnitToTrain().equalsIgnoreCase("combatUnit")) {
                    economyAgent.trainCombatUnit();
                }
                break;
            case SCOUT:

                break;

            case GATHER:
                boolean gas = action.actionName().endsWith("_gas");
                //gas is shorter to check, but the functions ask if it's minerals
                strategyAgent.executeGatherAction(!gas);
                break;
            case UPGRADE:

            default:


        }


        //TODO: SEND COMMANDS TO BOT BASED ON THE ACTION.

        //observe results
        State currentstate = currentObservation();
        State resultingstate = predictState(action);
        PreviousReward = rewardFunction.reward(currentstate,action,resultingstate);
        return new EnvironmentOutcome(currentstate, action, /*resulting state*/
                resultingstate,  /*reward*/ PreviousReward, isTerminalState(resultingstate));
    }

    /**
     * @return #PreviousReward
     */
    public double lastReward() {
        return PreviousReward;
    }

    /**
     * Returns whether the environment is in a terminal state that prevents further action by the agent.
     *
     * @return true if the current environment is in a terminal state; false otherwise.
     */
    @Override
    public boolean isInTerminalState() {
        return isTerminalState(currentObservation());
    }

    /**
     * Check if we have no town centers, or if the
     * enemy has no town centers, or if the game is done.
     * @return true if the game is in terminal state, false otherwise
     */
    public boolean isTerminalState(State s) {
        boolean terminal = false;
        if((int) s.get("numBases") < 1 || (int) s.get("numEnemyBases") < 1){
            terminal = true;
        }
        return terminal;
    }


    /**
     * Bot conceads, the game ends, and then
     * maybe a new game is started.
     */
    public void resetEnvironment() {

    }

    /**
     * TODO: predict the resulting state based on an action.
     * @param action the action taken by the ai
     * */
    private State predictState(Action action){
        return currentObservation();
    }
}
