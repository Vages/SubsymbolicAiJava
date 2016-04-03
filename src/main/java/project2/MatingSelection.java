package project2;

public enum MatingSelection {
    FITNESS_PROPORTIONATE, SIGMA_SCALING, TOURNAMENT_SELECTION, BOLTZMANN_SCALING;

    public static MatingSelection getStrategyFromChar(char c){
        switch (c) {
            case 'f':
                return FITNESS_PROPORTIONATE;
            case 's':
                return SIGMA_SCALING;
            case 't':
                return TOURNAMENT_SELECTION;
            case 'b':
                return BOLTZMANN_SCALING;
            default:
                return FITNESS_PROPORTIONATE;
        }
    }
}
