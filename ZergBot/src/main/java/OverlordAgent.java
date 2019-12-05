import bwapi.*;
import bwta.Chokepoint;

public class OverlordAgent{
    /**This class will take command of an overlord for positioning.
     * currently just holds which chokepoint the overlord is asigned
     * to watch
     *
     * TODO: make this lisen to the game, so it can change state maybe
     * + move movement commands to here from ZergBot
     */

    public static enum OverlordMode{watchChokePoint, hide, traveling}
    private Unit controlled;
    private Chokepoint assignedCP; //for now, assumed to be one.
    private OverlordMode mode = OverlordMode.watchChokePoint;

    public OverlordAgent(Unit overlord, Chokepoint cp){
        assert(overlord.getType() == UnitType.Zerg_Overlord);
        controlled = overlord;
        assignedCP = cp;
        if(cp!=null){
            mode = OverlordMode.watchChokePoint;
        } else {
            mode = OverlordMode.hide;
        }
    }



    public Chokepoint getChokePoint(){
        return this.assignedCP;
    }

    public Unit getUnit(){
        return controlled;
    }
}
