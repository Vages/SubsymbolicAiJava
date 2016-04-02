package project2;

public enum AdultSelection {
    FULL_GENERATIONAL_REPLACEMENT, OVER_PRODUCTION, GENERATIONAL_MIXING;

    public static AdultSelection getStrategyFromChar(char c){
        switch (c) {
            case 'f':
                return FULL_GENERATIONAL_REPLACEMENT;
            case 'o':
                return OVER_PRODUCTION;
            case 'g':
                return GENERATIONAL_MIXING;
            default:
                return FULL_GENERATIONAL_REPLACEMENT;
        }
    }
}
