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

    String datasetDir, queryDir, retrievedDir, goldstandardDir;

    String runid;
    String querymethod;
    String dataset;
    int neighborK;
    boolean revised;
    double thresold_C;
    int threshold_M;
    String aggregation;
    int votingOption; // C, M, D, majority, AND, OR, D_CM
    int aggregationOption;
    int networkOption;
    int runextension;
    String voting;

    public static int CLIQUE_BASED_NETWORK = 1;
    public static int PAIR_BASED_NETWORK = 2;

    String network;

    HashMap<String, HashMap<String, String>> result;
    HashMap<String, ArrayList<String>> retrieved;
    VotingClassifer votingClassifier;
    Evaluator eval;
    String[] topics;

    String paramFile;

    public static class QueryMethod {
        public static String ALLQUERY = "all";
        public static String TIlEQUERY = "tile";
        public static String TF10QUERY = "tf10";
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


        String dataset = "CLUEWEB";
        String mode = "testParam"; // or "testParam"

        String[] expDataset;
        if(dataset.equals("CLUEWEB"))
            expDataset = DataPath.CLUEWEB_5FOLD;
        else
            expDataset = DataPath.GENWEB_5FOLD;

        for (int k = 1; k <= 5; k++) {
            for (int i = 1; i <= 9; i++) {
                String baseDir = expDataset[k-1];
                String dir = baseDir + "params/" + i + "/" + mode;
                DirectoryManager dm = new DirectoryManager(dir);
                for (String paramFile : dm.getFilePathList()) {
                    if (paramFile.endsWith(".param")) {
                        OutputControversyScores classifier = new OutputControversyScores(baseDir, paramFile, false, k);
                        classifier.run();
                    }
                }
            }
        }
    }

    public OutputControversyScores(String dataFoldDir, String p, boolean isTrain, int runset) throws IOException {
        initialize(dataFoldDir, p, isTrain, runset);

    }
    private void initialize(String dataFoldDir, String paramFile, boolean isTrain, int runset) throws IOException {

        Parameters p = Parameters.parseFile(paramFile);
        System.out.println(paramFile);
        this.paramFile = paramFile;
        runid = p.get("id", "0");



        /******************** Dataset ***********************/

        dataset = "clueweb";
     //   dataset = p.get("dataset", "clueweb");

        if(dataset.equals("clueweb")) {
            datasetDir = DataPath.CLUEWEB;
            if(isTrain) {
                queryDir = dataFoldDir + "train/";
            }
            else {
                queryDir = dataFoldDir + "test/";
            }

        }
        else if(dataset.equals("genweb")) {
            datasetDir = DataPath.GENWEB;
            queryDir = datasetDir + DataPath.GENWEB_QUERY;
            if(isTrain) {
                if (runset == 1) {
                    topics = GenWebTopicFiveFold.Fold1.train;
              //      topics = new String[]{"anti_americanism"};
                }
                if (runset == 2)
                    topics = GenWebTopicFiveFold.Fold2.train;
                if (runset == 3)
                    topics = GenWebTopicFiveFold.Fold3.train;
                if (runset == 4)
                    topics = GenWebTopicFiveFold.Fold4.train;
                if (runset == 5)
                    topics = GenWebTopicFiveFold.Fold5.train;
            }
            else{
                if (runset == 1)
                    topics = GenWebTopicFiveFold.Fold1.test;
                if (runset == 2)
                    topics = GenWebTopicFiveFold.Fold2.test;
                if (runset == 3)
                    topics = GenWebTopicFiveFold.Fold3.test;
                if (runset == 4)
                    topics = GenWebTopicFiveFold.Fold4.test;
                if (runset == 5)
                    topics = GenWebTopicFiveFold.Fold5.test;
            }

     //       topics = new String[]{ControversialTopic.Feminism, NonControversialTopic.AnneFrank};
     //       topics = (String[]) ArrayUtils.addAll(ControversialTopic.all, NonControversialTopic.all);
     //       topics = TestTopics.all;
        }
        else {
            System.out.println("Error! Wrong dataset parameter");
            System.out.println(dataset);
            return;
        }
        eval = new Evaluator(datasetDir + DataPath.GOLDSTANDARD);

        /**************** Query Method ******************/

        querymethod = p.get("querygen", "tf10");

        if(querymethod.equals(QueryMethod.ALLQUERY))
            retrievedDir = datasetDir + DataPath.ALLQUERY;
        else if(querymethod.equals(QueryMethod.TF10QUERY))
            retrievedDir = datasetDir + DataPath.TF10QUERY;
        else if(querymethod.equals(QueryMethod.TIlEQUERY))
            retrievedDir = datasetDir + DataPath.TILEQUERY;
        else {
            System.out.println("Error! Wrong querymethod parameter");
            return;
        }


        /********** How many k neighbors do we use? *******/

        neighborK = Integer.parseInt(p.get("k",  "20"));

        /**************** Aggregation ******************/
        aggregation = p.get("aggregation", "max");
        if(aggregation.equals("max"))
            aggregationOption = Aggregator.MAX;
        else if(aggregation.equals("avg"))
            aggregationOption = Aggregator.AVG;


        /**************** Thresholding *****************/
        thresold_C = Double.parseDouble(p.get("C_threshold", "0.00418"));
        threshold_M = Integer.parseInt(p.get("M_threshold", "20000"));

        runextension = 1;

        /**************** Do we use revised score? ***********/
        revised = p.get("revised", false);
        if(revised) {
            network = p.get("network", "clique");
            if(network.equals("clique"))
                networkOption = CLIQUE_BASED_NETWORK;
            else if(network.equals("pair"))
                networkOption = PAIR_BASED_NETWORK;
            else{
                System.out.println("Error! Wrong network option!");
                return;
            }
        }


        /*************** Voting Option ***************/
        voting = p.get("voting", "C"); // C, M, D, majority, AND, OR, D_CM
        if(voting == "C")
            votingOption = VotingParameter.ISOLATION_C;
        else if(voting.equals("M"))
            votingOption = VotingParameter.ISOLATION_M;
        else if(voting.equals("D"))
            votingOption = VotingParameter.ISOLATION_D;
        else if(voting.equals("Majority"))
            votingOption = VotingParameter.MAJORITY;
        else if(voting.equals("D_CM"))
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

        SimpleFileWriter logWriter = new SimpleFileWriter("error.log");
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

        if (dataset.equals("genweb")) {
            for (String topic : topics) {
                DirectoryManager dm = new DirectoryManager(queryDir + topic);
                for (String queryId : dm.getFileNameList()) {
                    detectControversy(queryId, neighborK);

                }
            }
        } else {
            DirectoryManager dm = new DirectoryManager(queryDir);
            for (String queryId : dm.getFileNameList()) {
                detectControversy(queryId, neighborK);
            }
        }
        logWriter.close();

        boolean debugMode = false;
        HashMap<String, Double> performance = eval.binaryAutomaticEvaluate(result, debugMode);
        SimpleFileWriter sw2 = new SimpleFileWriter(paramFile.replace(".param", ".output"));
        ArrayList<String> pageList = new ArrayList<String>(result.keySet());
        Collections.sort(pageList, ALPHABETICAL_ORDER);
        for (String docName : pageList) {
                HashMap<String, String> info = result.get(docName);
                // 1: Controversial   0: Non-controversial
                sw2.writeLine(docName + "\t" + info.get("prediction"));
        }
        sw2.close();

        csd.close();
        dsd.close();
        msd.close();
   //     if(performance == null) return;

        SimpleFileWriter sw = new SimpleFileWriter(paramFile.replace(".param", ".result")  + runextension);
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.0000");
        if(!revised)
            network = "N/A";
        sb.append(runid + "\t" + querymethod + "\t" + neighborK + "\t" + revised + "\t" + network + "\t" + aggregation
                + "\t" + thresold_C + "\t" + threshold_M + "\t" + voting + "\t" + df.format(performance.get("precision")) + "\t"
                + df.format(performance.get("recall")) + "\t" + df.format(performance.get("specificity")) + "\t" +
                df.format(performance.get("f_1")) + "\t" + df.format(performance.get("f_half")) + "\t" + df.format(performance.get("accuracy")));
        sw.writeLine(sb.toString());
        sw.close();

    }


    private void detectControversy(String queryId, int k) throws IOException {
        if(!retrieved.containsKey(queryId)) {
            System.out.println("Error: " +  queryId + " doesn't have the retrieval result");
            return;
        }

        ArrayList<String> rankedDoc = retrieved.get(queryId);
        // Oracle Experiment
        /**
          ArrayList<String> goldDoc = wikiDB.getRelevantPages(queryId, 2.5);
          ArrayList<String> rankedDoc = goldDoc;
        **/
        HashMap<String, String> info = new HashMap<String, String>();
        info.put("qid", queryId);



        csd.computeScore(info, rankedDoc, aggregationOption, neighborK);
        msd.computeScore(info, rankedDoc, aggregationOption, neighborK);
        dsd.computeScore(info, rankedDoc, aggregationOption, neighborK);
        votingClassifier.classify(info);

        // print heading
        result.put(queryId, info);

    }

}
