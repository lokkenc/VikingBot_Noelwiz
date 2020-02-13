package Knowledge;

public class GeneralGameKnowledge {

    //int?
    //TODO: CHANGE ARGUMENT TO THE API THING.
    /***
     *
     * @param race the race {the api races, null}. null being unknown
     * @return integer number of frames, expecting 30 frames per second
     * representing the time until a combat unit could be made by a race
     */
    public int FramesTillDanger(int race){
        //TODO: find these numbers
        int TerranTime = 0;
        int ZergTime = 0;
        int Protosstime = 0;

        int time;
        //UNKNOWN RACE
        switch (race){
            case 1:
                time = TerranTime;
                break;
            case 2:
                time = ZergTime;
                break;
            case 3:
                time = ZergTime;
                break;
            default:
                time = Math.min(TerranTime, ZergTime);
                time = Math.min(time, Protosstime);
                break;
        }

        return time;
    }
}
