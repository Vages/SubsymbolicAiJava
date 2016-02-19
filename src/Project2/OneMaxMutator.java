package Project2;

public class OneMaxMutator {

    public static int[] mutate(int[] genome){

        double mutateThreshold = 0.01;
        double numberOfMutations = 6;
        if (Math.random() > mutateThreshold)
            return genome;

        int[] copiedGenome = new int[genome.length];
        System.arraycopy(genome, 0, copiedGenome, 0, genome.length);

        for (int i = 0; i < numberOfMutations; i++){
            int position = (int) (Math.random()*genome.length);
            int ith_digit = genome[position];
            if (ith_digit == 0){
                copiedGenome[position] = 1;
            } else {
                copiedGenome[position] = 0;
            }
        }

        return copiedGenome;
    }
}
