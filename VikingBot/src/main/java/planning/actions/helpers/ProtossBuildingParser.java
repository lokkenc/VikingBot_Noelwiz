package planning.actions.helpers;

import bwapi.UnitType;
import planning.actions.BuildAction;

public class ProtossBuildingParser {

    public static UnitType translateBuilding(BuildAction action){
        return translateBuilding(action.actionName().split("_")[1]);
    }

    public static UnitType translateBuilding(String what){
        UnitType unit;
        switch (what){
            case "gas":
                unit = UnitType.Protoss_Assimilator;
                break;
            case "research":
                unit = UnitType.Protoss_Cybernetics_Core;
                break;
            case "train":
                unit = UnitType.Protoss_Gateway;
                break;
            case "pop":
                unit = UnitType.Protoss_Pylon;
                break;
            case "base":
                unit = UnitType.Protoss_Nexus;
                break;
            default:
                unit = UnitType.Unknown;
                break;
        }

        return unit;
    }
}
