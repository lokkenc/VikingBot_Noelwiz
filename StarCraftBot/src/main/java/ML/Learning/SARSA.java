package ML.Learning;

import ML.Actions.Action;
import ML.Data.DataManager;
import ML.QTable;
import ML.States.State;
import ML.States.StateSpaceManager;
import ML.RewardFunction;
import bwapi.UnitType;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.Map;
import java.io.Serializable;

public class SARSA implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final double LEARNING_RATE = .1;
    private static final double DISCOUNT_FACTOR = .6;

    private DataManager dataManager;
    private UnitType type;
    private QTable qTable;
    private StateSpaceManager spaceManager;

    public SARSA(UnitType type, StateSpaceManager spaceManager) {
        this.type = type;
        dataManager = new DataManager(type);
        this.spaceManager = spaceManager;
        this.qTable = new QTable(this.spaceManager.getStateSet(), this.spaceManager.getActionList());
    }

    public double computerQValue(State current, Action action, State next, double reward) {
        // SARSA => q-value = q-value + LEARNING_FACTOR * (reward + (DISCOUNT_FACTOR * next_q-value) - q-value)
        double qvalue = qTable.get(current).get(action);
        double errorvalue = LEARNING_RATE * (reward + (DISCOUNT_FACTOR * qTable.getMaxValue(next)) - qvalue);

        return qvalue + errorvalue;
    }

    public void updateQTable(State current, Action action, State next) {
        // Get the reward and calculate the qvalue given the reward
        double reward = RewardFunction.getRewardValue(current, action, next);
        double qvalue = computerQValue(current, action, next, reward);

        dataManager.addDataPoint(current, action, next, reward, qvalue);
        dataManager.addState(current);

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

    public void loadDataManager() {
        FileInputStream fis = null;
        try {
            File f = new File("TrainingFiles/Tables/" + type.toString() + "Data.ser");
            fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            dataManager = (DataManager) ois.readObject();
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

    public void storeDataManager() {
        FileOutputStream fos = null;
        try {
            File f = new File("TrainingFiles/Tables/" + type.toString() + "Data.ser");
            f.getParentFile().mkdirs();
            fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dataManager);
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

    public DataManager getDataManager() {
        return dataManager;
    }
}
