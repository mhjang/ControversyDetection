package edu.umass.cs.ciir.controversy.experiment;


import edu.umass.cs.ciir.controversy.Scorer.*;
import edu.umass.cs.ciir.controversy.data.*;
import edu.umass.cs.ciir.controversy.utils.DirectoryManager;
import edu.umass.cs.ciir.controversy.utils.SimpleFileReader;
import edu.umass.cs.ciir.controversy.utils.SimpleFileWriter;
import org.apache.commons.lang3.ArrayUtils;
import org.lemurproject.galago.utility.Parameters;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
/**
 * Created by mhjang on 12/18/15.
 */
public class OutputControversyScores {

    CScoreDatabase csd;
    MScoreDatabase msd;
    DScoreDatabase dsd;
    AnnotationDatabase and;

    String datasetDir, queryDir, retrievedDir, retrievedDir2, goldstandardDir;

    String runid;
    String querymethod, querymethod2;
    String dataset;
    int neighborK, neighborK2 = 0;
    boolean revised;
    double thresold_C;
    int threshold_M;
    String aggregation;
    int votingOption; // C, M, D, majority, AND, OR, D_CM
    int aggregationOption;
    int networkOption;
    int runextension;
    String voting;

    String resultDir;
    int fold;

    public static int CLIQUE_BASED_NETWORK = 1;
    public static int PAIR_BASED_NETWORK = 2;

    String network;

    HashMap<String, HashMap<String, String>> result;
    HashMap<String, ArrayList<String>> retrieved;
    HashMap<String, ArrayList<String>> retrieved2;
    VotingClassifer votingClassifier;
    Evaluator eval;
    String[] topics;

    boolean isTrain = true;
    String paramFile;

    boolean useWikifier = false;
    int wikifier_k = 0;

    public static class QueryMethod {
        public static String ALLQUERY = "all";
        public static String TIlEQUERY = "tile";
        public static String TF10QUERY = "tf10";
        public static String WIKIFIER = "wikifier";
    }


    public static class TrainingTopics {
        public static String GlobalWarming = "global_warming";
        public static String NuclearPower = "nuclear_power";
        public static String GayMarriage = "gay_marriage";
        public static String VideoGame = "video_game";
        public static String MedievalCuisine = "medieval_cusine";
        public static String SteveJobs = "steve_jobs";
        public static String Uranium = "uranium";
        public static String[] all = {GlobalWarming, NuclearPower, GayMarriage, VideoGame, MedievalCuisine, SteveJobs, Uranium};

    }

    public static class TestTopics {
        public static String NativeAmericans = "native_americans";
        public static String AntiAmericanism = "anti_americanism";
        public static String Feminism = "feminism";
        public static String IntensiveFarming = "intensive_farming";
        public static String BerlinWall = "berlin_wall";
        public static String AnneFrank = "anne_frank";
        public static String Cancer = "cancer";
        public static String Cheese = "cheese";

        public static String[] all = {NativeAmericans, AntiAmericanism, Feminism, IntensiveFarming, BerlinWall, AnneFrank, Cancer, Cheese};

    }

    public static class ControversialTopic {
        public static String GlobalWarming = "global_warming";
        public static String NuclearPower = "nuclear_power";
        public static String GayMarriage = "gay_marriage";
        public static String VideoGame = "video_game";
        public static String NativeAmericans = "native_americans";
        public static String AntiAmericanism = "anti_americanism";
        public static String Feminism = "feminism";
        public static String IntensiveFarming = "intensive_farming";
        public static String BerlinWall = "berlin_wall";
        public static String[] all = {GlobalWarming, NuclearPower, GayMarriage, VideoGame, NativeAmericans, AntiAmericanism, Feminism, IntensiveFarming, BerlinWall};

    }

    public static class NonControversialTopic {
        public static String MedievalCuisine = "medieval_cusine";
        public static String SteveJobs = "steve_jobs";
        public static String Uranium = "uranium";
        public static String AnneFrank = "anne_frank";
        public static String Cancer = "cancer";
        public static String Cheese = "cheese";
        public static String[] all = {MedievalCuisine, SteveJobs, Uranium, AnneFrank, Cancer, Cheese};
    }

    public static void main(String[] args) throws IOException {


        /******** User-set parameters ***************/
        String dataset = "GENWEB";
        String learningMode = "testParam"; // or "testParam"
        int runExtension = 2014;
        /**********************************************/

        String baseDir = null;
        boolean isTrain = true;
        String outputMode = null;

        if (learningMode.equals("testParam")) {
            isTrain = false;
            outputMode = "testResults";
        } else
            outputMode = "trainResults";

        String[] expDataset;
        if (dataset.equals("CLUEWEB")) {
            expDataset = DataPath.CLUEWEB_5FOLD;
            baseDir = DataPath.CLUEWEB;
        } else {
            expDataset = DataPath.GENWEB_5FOLD;
            baseDir = DataPath.GENWEB;
        }


        for (int i = 1; i <= 9; i++) {
            String paramDir = baseDir + "experiments/runs/" + i + "/" + learningMode;   // retrieve ith run. Learning mode determines whether it's test or train mode

            for (int k = 1; k <= 5; k++) {
                String resultDir = baseDir + "experiments/runs/" + i + "/results/fold" + k + "/" + outputMode;
                if (!isTrain)
                    paramDir = resultDir;
                String queryBaseDir = expDataset[k - 1]; // retrieve kth fold
                DirectoryManager dm = new DirectoryManager(paramDir);
                for (String paramFile : dm.getFilePathList()) {
                    if (paramFile.endsWith(".param")) {
                        OutputControversyScores classifier = new OutputControversyScores(queryBaseDir, resultDir, paramFile, isTrain, k, runExtension);
                        classifier.run();
                    }
                }
            }
        }
    }

    public OutputControversyScores(String dataFoldDir, String resultDir, String p, boolean isTrain, int runset, int runExtension) throws IOException {
        initialize(dataFoldDir, resultDir, p, isTrain, runset, runExtension);

    }

    private void initialize(String dataFoldDir, String resultDir, String paramFile, boolean isTrain, int runset, int runExtension) throws IOException {

        Parameters p = Parameters.parseFile(paramFile);
        System.out.println(paramFile);
        this.paramFile = paramFile;
        this.resultDir = resultDir;
        runid = p.get("id", "0");
        this.fold = runset;

        this.isTrain = isTrain;

        /******************** Dataset ***********************/

        // dataset = "clueweb";
        dataset = p.get("dataset", "null");

        if (dataset.equals("clueweb")) {
            datasetDir = DataPath.CLUEWEB;
        }
        else if(dataset.equals("genweb")){
            datasetDir = DataPath.GENWEB;
        }
        else {
            System.err.print("Wrong dataset!");
            return;
        }


        if (isTrain)
            queryDir = dataFoldDir + "train/";
        else
            queryDir = dataFoldDir + "test/";



        eval = new Evaluator(datasetDir + DataPath.GOLDSTANDARD);

        /**************** Query Method ******************/

        querymethod = p.get("querygen", "tf10");

        if (querymethod.equals(QueryMethod.ALLQUERY))
            retrievedDir = datasetDir + DataPath.ALLQUERY;
        else if (querymethod.equals(QueryMethod.TF10QUERY))
            retrievedDir = datasetDir + DataPath.TF10QUERY;
        else if (querymethod.equals(QueryMethod.TIlEQUERY))
            retrievedDir = datasetDir + DataPath.TILEQUERY;
        else if (querymethod.equals(QueryMethod.WIKIFIER))
            retrievedDir = datasetDir + DataPath.WIKIFIERQUERY;
        else {
            System.out.println("Error! Wrong querymethod parameter");
            return;
        }

        querymethod2 = p.get("querygen2", "null");
        if(!querymethod2.equals("null")) {
            retrievedDir2 = datasetDir + DataPath.WIKIFIERQUERY;
        }
        /********** How many k neighbors do we use? *******/

        neighborK = Integer.parseInt(p.get("k", "20"));
        neighborK2 = Integer.parseInt(p.get("k2", "0"));

        /**************** Aggregation ******************/
        aggregation = p.get("aggregation", "max");
        if (aggregation.equals("max"))
            aggregationOption = Aggregator.MAX;
        else if (aggregation.equals("avg"))
            aggregationOption = Aggregator.AVG;


        /**************** Thresholding *****************/
        thresold_C = Double.parseDouble(p.get("C_threshold", "0.00418"));
        threshold_M = Integer.parseInt(p.get("M_threshold", "20000"));

        this.runextension = runExtension;

        /**************** Do we use revised score? ***********/
        revised = p.get("revised", false);
        if (revised) {
            network = p.get("network", "clique");
            if (network.equals("clique"))
                networkOption = CLIQUE_BASED_NETWORK;
            else if (network.equals("pair"))
                networkOption = PAIR_BASED_NETWORK;
            else {
                System.out.println("Error! Wrong network option!");
                return;
            }
        }


        /*************** Voting Option ***************/
        voting = p.get("voting", "C"); // C, M, D, majority, AND, OR, D_CM
        if (voting == "C")
            votingOption = VotingParameter.ISOLATION_C;
        else if (voting.equals("M"))
            votingOption = VotingParameter.ISOLATION_M;
        else if (voting.equals("D"))
            votingOption = VotingParameter.ISOLATION_D;
        else if (voting.equals("Majority"))
            votingOption = VotingParameter.MAJORITY;
        else if (voting.equals("D_CM"))
            votingOption = VotingParameter.D_CM;
        VotingParameter votingParam = new VotingParameter.Builder().setVotingMethod(votingOption)
                .setMThreshold(threshold_M).setCScoreThreshold(thresold_C).build();
        votingClassifier = new VotingClassifer(votingParam);


        goldstandardDir = datasetDir + DataPath.GOLDSTANDARD;
    }

    private static Comparator<String> ALPHABETICAL_ORDER = new Comparator<String>() {
        public int compare(String str1, String str2) {
            int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
            if (res == 0) {
                res = str1.compareTo(str2);
            }
            return res;
        }
    };

    public void run() throws IOException {

        csd = new CScoreDatabase(revised, networkOption);
        msd = new MScoreDatabase(revised, networkOption);
        dsd = new DScoreDatabase();
        //     and = new AnnotationDatabase();


        result = new HashMap<String, HashMap<String, String>>();

        retrieved = new HashMap<String, ArrayList<String>>();
        SimpleFileReader sr2 = new SimpleFileReader(retrievedDir);
        while (sr2.hasMoreLines()) {
            String line = sr2.readLine();
            String[] tokens = line.split("\t");
            String queryId = tokens[0];
            if (!retrieved.containsKey(queryId))
                retrieved.put(queryId, new ArrayList<String>());
            retrieved.get(queryId).add(tokens[1].toLowerCase());
        }
        sr2.close();

        if(retrievedDir2 != null) {
            retrieved2 = new HashMap<String, ArrayList<String>>();
            sr2 = new SimpleFileReader(retrievedDir2);
            while (sr2.hasMoreLines()) {
                String line = sr2.readLine();
                String[] tokens = line.split("\t");
                String queryId = tokens[0];
                if (!retrieved2.containsKey(queryId))
                    retrieved2.put(queryId, new ArrayList<String>());
                retrieved2.get(queryId).add(tokens[1].toLowerCase());
            }
            sr2.close();
        }


        System.out.println(queryDir);
        DirectoryManager dm = new DirectoryManager(queryDir);
        for (String queryId : dm.getFileNameList()) {
                detectControversy(queryId);
        }


        boolean debugMode = false;
        HashMap<String, Double> performance = eval.binaryAutomaticEvaluate(result, debugMode);


        if (!isTrain) {
            SimpleFileWriter sw2 = new SimpleFileWriter(paramFile.replace(".param", ".output"));
            SimpleFileWriter sw3 = new SimpleFileWriter(paramFile.replace(".param", ".error"));

            ArrayList<String> pageList = new ArrayList<String>(result.keySet());
            Collections.sort(pageList, ALPHABETICAL_ORDER);
            for (String docName : pageList) {
                HashMap<String, String> info = result.get(docName);
                // 1: Controversial   0: Non-controversial
                String prediction = info.get("prediction");
                String binaryanswer = info.get("binary_rating");
                String rawanswer = info.get("raw_rating");
                /* Warning: /home/mhjang/controversy_Data/datasets/clueweb/experiments/runs/compSignifiance.sh depends on this output. **/
                sw2.writeLine(docName + "\t" + prediction + "\t" + binaryanswer);

                if (!prediction.equals(binaryanswer))
                    sw3.writeLine(docName + "\t" + prediction + "\t" + binaryanswer + "\t" + rawanswer);


            }
            sw2.close();
            sw3.close();
        }

        csd.close();
        dsd.close();
        msd.close();
        //     if(performance == null) return;

        String paramFileName = paramFile.substring(paramFile.lastIndexOf("/") + 1, paramFile.length()).replace(".param", ".result") + runextension;
        SimpleFileWriter sw = new SimpleFileWriter(resultDir + "/" + paramFileName);
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.0000");
        if (!revised)
            network = "N/A";
        sb.append(runid + "\t" + querymethod + "\t" + neighborK + "\t" + revised + "\t" + network + "\t" + aggregation
                + "\t" + thresold_C + "\t" + threshold_M + "\t" + voting + "\t" + df.format(performance.get("precision")) + "\t"
                + df.format(performance.get("recall")) + "\t" + df.format(performance.get("specificity")) + "\t" +
                df.format(performance.get("f_1")) + "\t" + df.format(performance.get("f_half")) + "\t" + df.format(performance.get("accuracy")));
        sw.writeLine(sb.toString());
        sw.close();

    }




    private void detectControversy(String queryId) throws IOException {
        if(!retrieved.containsKey(queryId)) {
            System.out.println("Error: " +  queryId + " doesn't have the retrieval result");
            return;
        }

        ArrayList<String> rankedDoc = retrieved.get(queryId);
        ArrayList<String> rankedDoc2 = null;
        if(retrieved2 != null) {
            if (retrieved2.containsKey(queryId))
                rankedDoc2 = retrieved2.get(queryId);
        }
        // Oracle Experiment
        /**
          ArrayList<String> goldDoc = wikiDB.getRelevantPages(queryId, 2.5);
          ArrayList<String> rankedDoc = goldDoc;
        **/
        HashMap<String, String> info = new HashMap<String, String>();
        info.put("qid", queryId);



        csd.computeScore(info, rankedDoc, rankedDoc2, aggregationOption, neighborK, neighborK2);
        msd.computeScore(info, rankedDoc, rankedDoc2, aggregationOption, neighborK, neighborK2);
        dsd.computeScore(info, rankedDoc, rankedDoc2, aggregationOption, neighborK, neighborK2);
        votingClassifier.classify(info);

        // print heading
        result.put(queryId, info);

    }

}
