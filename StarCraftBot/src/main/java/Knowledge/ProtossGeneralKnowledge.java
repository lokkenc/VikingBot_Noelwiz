package Knowledge;

import Knowledge.GeneralRaceProductionKnowledge;

public class ProtossGeneralKnowledge implements GeneralRaceProductionKnowledge {
    // https://liquipedia.net/starcraft/Mining for numbers
    // https://tl.net/forum/brood-war/89939-ideal-mining-thoughts
    private static float gatherrate = 1.055f;
    private static float gasRatePerWorker = 103f;
    //per minute, so per 30*60 frames        0   3   6   9   12     15    18
    private static float[] ratePerWorkder = {0, 65f,65f,65f,58.5f, 54.6f, 51.7f, 50.1f, 28.6f, 47.6f, 44f};

    //apparently asusmes 9 things of minerals per patch
    /***
     * todo: write this
     * @param NumWorkers array of ints, each int is the number of workers at a mineral field, the
     *                   length is the number of mineral fields
     * @return the rate of mineral production / {some unit of time}
     */
    public float AverageMineralProductionRate(int[] NumWorkers) {
        float production = 0;

        //good candidate for loop unrolling maybe, if used frequently
        for (int patch = 0; patch < NumWorkers.length; patch++){
            production += NumWorkers[patch] * ratePerWorkder[Math.round(NumWorkers[patch]/3)]  ;
        }

        production = production * gatherrate;
        return production;
    }

    /***
     * todo: write this
     * @param NumWorkers array of ints, each int is the number of workers at a geyser, the
     *                   length is the number of geysers
     * @return the rate of mineral production / {some unit of time}
     */
    public float AverageGasProductionRate(int[] NumWorkers) {
        float production = 0;

        for (int geyser = 0; geyser < NumWorkers.length; geyser++) {
            production += NumWorkers[geyser] * gasRatePerWorker;
        }

        return production;
    }
}
