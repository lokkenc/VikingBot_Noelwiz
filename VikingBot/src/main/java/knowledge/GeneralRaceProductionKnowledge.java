package knowledge;

public interface GeneralRaceProductionKnowledge {
    // https://liquipedia.net/starcraft/Mining for numbers
    //TODO: IF WE CONVERT TO JAVA 8, MAKE THESE METHODS STATIC

    /***
     *
     * @param NumWorkers array of ints, each int is the number of workers at a mineral field, the
     *                   length is the number of mineral fields
     * @return the rate of mineral production / {some unit of time}
     */
    float AverageMineralProductionRate(int[] NumWorkers);


    /***
     *
     * @param NumWorkers array of ints, each int is the number of workers at a mineral field, the
     *                   length is the number of mineral fields
     * @return the rate of mineral production / {some unit of time}
     */
    float AverageGasProductionRate(int[] NumWorkers);
}
