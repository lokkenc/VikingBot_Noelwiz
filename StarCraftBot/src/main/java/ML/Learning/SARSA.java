package src.main.java.ML.Learning;

import src.main.java.ML.Actions.Action;
import src.main.java.ML.QTable;
import src.main.java.ML.States.State;
import src.main.java.ML.States.StateSpaceManager;
import src.main.java.ML.RewardFunction;
import bwapi.UnitType;

import java.io.*;
import java.util.Map;
import java.io.Serializable;

public class SARSA implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final double LEARNING_FACTOR = .5;
    private static final double DISCOUNT_FACTOR = .6;
    private static final double INITIAL_QVAL = Double.NEGATIVE_INFINITY;

    private UnitType type;
    private QTable qTable;
    private StateSpaceManager spaceManager;

    public SARSA(UnitType type, StateSpaceManager spaceManager) {
        this.type = type;
        this.spaceManager = spaceManager;
        this.qTable = new QTable(this.spaceManager.getStateSet(), this.spaceManager.getActionList());
    }

    public double computerQValue(State current, Action action, State next, double reward) {
        // SARSA => q-value = q-value + LEARNING_FACTOR * (reward + (DISCOUNT_FACTOR * next_q-value) - q-value)
        double qvalue = qTable.get(current).get(action);
        double errorvalue = LEARNING_FACTOR * (reward + (DISCOUNT_FACTOR * qTable.getMaxValue(next)) - qvalue);

        return qvalue + errorvalue;
    }

    public void updateQTable(State current, Action action, State next) {
        // Get the reward and calculate the qvalue given the reward
        double reward = RewardFunction.getRewardValue(current, action, next);
        double qvalue = computerQValue(current, action, next, reward);

        // Get the current qvalue and update it with the new qvalue
        Map<Action, Double> actionDoubleMap = qTable.get(current);
        actionDoubleMap.put(action, qvalue);
        qTable.put(current, actionDoubleMap);
    }

    public void loadQTable() {
        FileInputStream fis = null;
        try {
            File f = new File("TrainingFiles/Tables/" + type.toString() + "Table.ser");
            fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            qTable = (QTable) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fis != null) {
                    fis.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public void storeQTable() {
        FileOutputStream fos = null;
        try {
            File f = new File("TrainingFiles/Tables/" + type.toString() + "Table.ser");
            f.getParentFile().mkdirs();
            fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(qTable);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fos != null) {
                    fos.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public UnitType getType() {
        return type;
    }

    public QTable getQTable() {
        return qTable;
    }

    public StateSpaceManager getSpaceManager() {
        return spaceManager;
    }
}
