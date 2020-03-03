package Knowledge;

import bwapi.Race;

public class GeneralGameKnowledge {

    //int?
    //TODO: CHANGE ARGUMENT TO THE API THING.
    /***
     *
     * @param race the race {the api races, null}. null being unknown
     * @return integer number of frames, expecting 30 frames per second
     * representing the time until a combat unit could be made by a race
     */
    public int FramesTillDanger(Race race){
        //TODO: find these numbers, or estimate
        //meant to be earliest possible time the race can attack us on a map.
        // So like, train a zergling and send it over, or zelot, or marine ect.
        int TerranTime = 0;
        int ZergTime = 0;
        int Protosstime = 0;

        int time;
        //UNKNOWN RACE
        switch (race){
            case Terran:
                time = TerranTime;
                break;
            case Zerg:
                time = ZergTime;
                break;
            case Protoss:
                time = Protosstime;
                break;
            default:
                time = Math.min(TerranTime, ZergTime);
                time = Math.min(time, Protosstime);
                break;
        }

        return time;
    }
}
