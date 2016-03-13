package edu.umass.cs.ciir.controversy.experiment;

/**
 * Created by mhjang on 2/9/16.
 */
public class GenWebTopicFiveFold {
    private static String GlobalWarming = "global_warming";
    private static String NuclearPower = "nuclear_power";

    private static String GayMarriage = "gay_marriage";
    private static String VideoGame = "video_game";

    private static String NativeAmericans = "native_americans";
    private static String AntiAmericanism = "anti_americanism";

    private static String Feminism = "feminism";
    private static String IntensiveFarming = "intensive_farming";

    private static String BerlinWall = "berlin_wall";
    private static String MedievalCuisine = "medieval_cusine";
    private static String SteveJobs = "steve_jobs";

    private static String Uranium = "uranium";

    private static String AnneFrank = "anne_frank";

    private static String Cancer = "cancer";

    private static String Cheese = "cheese";

    public static class Fold1{
        public static String[] train = {GayMarriage, VideoGame, NativeAmericans, AntiAmericanism, Feminism,
                                IntensiveFarming, BerlinWall, Uranium, AnneFrank, Cancer, Cheese};
        public static String[] test = {GlobalWarming, NuclearPower, MedievalCuisine, SteveJobs};

    }


    public static class Fold2{
        public static String[] train = {GlobalWarming, NuclearPower, MedievalCuisine, SteveJobs, 
            NativeAmericans, AntiAmericanism, Feminism, IntensiveFarming, BerlinWall, AnneFrank, Cancer, 
            Cheese};
        public static String[] test = {GayMarriage, VideoGame, Uranium};

    }

    public static class Fold3{
        public static String[] train = {GlobalWarming, NuclearPower, MedievalCuisine, SteveJobs, 
            Feminism, IntensiveFarming, BerlinWall, Cancer, GayMarriage, VideoGame, Uranium, Cheese};
        public static String[] test = {NativeAmericans, AntiAmericanism, AnneFrank};

    }

    public static class Fold4{
        public static String[] train = {GlobalWarming, NuclearPower, MedievalCuisine, SteveJobs, 
            BerlinWall, GayMarriage, VideoGame, Uranium, Cheese, NativeAmericans, AntiAmericanism, AnneFrank};
        public static String[] test = {Feminism, IntensiveFarming, Cancer};

    }

    public static class Fold5{
        public static String[] train = {GlobalWarming, NuclearPower, MedievalCuisine, SteveJobs, 
            GayMarriage, VideoGame, Uranium, NativeAmericans, AntiAmericanism, AnneFrank,
        Feminism, IntensiveFarming, Cancer};
        public static String[] test = {BerlinWall, Cheese};

    }




}
