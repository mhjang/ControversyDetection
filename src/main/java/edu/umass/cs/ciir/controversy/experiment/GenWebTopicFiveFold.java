package edu.umass.cs.ciir.controversy.experiment;

import edu.umass.cs.ciir.controversy.utils.SimpleFileWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

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

    public static void main(String[] args) throws IOException {


        PrintStream ps_console = System.out;

        File file = new File("/home/mhjang/controversy_Data/datasets/generalweb300/experiments/fivefold/fold5/fold5.sh");
        FileOutputStream fos = new FileOutputStream(file);

        // Create new print stream for file.
        PrintStream ps = new PrintStream(fos);

        // Set file print stream.
        System.setOut(ps);

        System.out.print("declare -a dir=(\"");
        int n = Fold5.train.length;
        for (int i=0; i<n; i++) {
            if(i!= n-1)
                System.out.print(Fold5.train[i] + "\" \"");
            else
                System.out.print(Fold5.train[i] + "\") \n");
        }
        System.out.println("for i in \"${dir[@]}\"\ndo\ncp /home/mhjang/controversy_Data/" +
                "datasets/generalweb300/queries/$i/* train/;\ndone");


        System.out.print("declare -a dir=(\"");
        n = Fold5.test.length;
        for (int i=0; i<n; i++) {
            if(i!= n-1)
                System.out.print(Fold5.test[i] + "\" \"");
            else
                System.out.print(Fold5.test[i] + "\") \n");
        }
        System.out.println("for i in \"${dir[@]}\"\ndo\ncp /home/mhjang/controversy_Data/" +
                "datasets/generalweb300/queries/$i/* test/;\ndone");
    }




}
