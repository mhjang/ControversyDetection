package edu.umass.cs.ciir.controversy.experiment;


import edu.umass.cs.ciir.controversy.Scorer.*;
import edu.umass.cs.ciir.controversy.data.*;
import edu.umass.cs.ciir.controversy.utils.DirectoryManager;
import edu.umass.cs.ciir.controversy.utils.SimpleFileReader;
import jdk.nashorn.internal.objects.Global;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hdgf.chunks.ChunkHeader;
import org.apache.poi.hssf.record.formula.functions.Na;
import org.lemurproject.galago.utility.Parameters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
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
    HashMap<String, HashMap<String, String>> result;
    HashMap<String, ArrayList<String>> retrieved;
    Aggregator aggregator;

    String[] topics;

    public static int CLUEWEB = 1;
    public static int GENWEB = 0;
    public int dataset;

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

        Parameters p = Parameters.parseArgs(args);
        String runid = p.get("id", "0");
        String dataset = p.get("dataset", "clueweb");
        String querymethod = p.get("querygen", "tf10");
        String neighborK = p.get("k",  "20");
        boolean revised = p.get("revised", false);
        String thresold_C = p.get("C_threshold", "0.00418");
        String threshold_M = p.get("M_threshold", "84930");
        String aggregation = p.get("aggregation", "max");
        String voting = p.get("voting", "C"); // C, M, D, majority, AND, OR, D_CM


        OutputControversyScores classifier = new OutputControversyScores();
        classifier.run();
    }


    public OutputControversyScores() {
        initialize(GENWEB);
    }
    private void initialize(int option) {

        dataset = option;
        if(dataset == CLUEWEB) {
            datasetDir = DataPath.CLUEWEB;
            queryDir = datasetDir + DataPath.CLUEWEB_QUERY;
        }
        else {
            datasetDir = DataPath.GENWEB;
            queryDir = datasetDir + DataPath.GENWEB_QUERY;
            topics = (String[]) ArrayUtils.addAll(ControversialTopic.all, NonControversialTopic.all);

        }
        retrievedDir = datasetDir + DataPath.TILEQUERY;
        goldstandardDir = datasetDir + DataPath.GOLDSTANDARD;
    }

    public void run() throws IOException {

        csd = new CScoreDatabase(true);
        msd = new MScoreDatabase(true);
        dsd = new DScoreDatabase(DataPath.DSCORE);
   //     and = new AnnotationDatabase();


        Evaluator eval = new Evaluator(Evaluator.MAX_RATING, datasetDir + DataPath.GOLDSTANDARD);
        result = new HashMap<String, HashMap<String, String>>();


        // 1: Read query webpages
//        DocumentQueryGenerator dqg = new DocumentQueryGenerator();
        AggregationParameter aggregationParam = new AggregationParameter.Builder().setAggregationMethod(AggregationParameter.AGGREGATION_OR)
                .setMThreshold(84930).setCScoreThreshold(0.0418).build();
        aggregator = new Aggregator(aggregationParam);
 //       WikiGoldStandard wikiDB = new WikiGoldStandard();


        retrieved = new HashMap<String, ArrayList<String>>();
        SimpleFileReader sr2 = new SimpleFileReader(retrievedDir);
        while(sr2.hasMoreLines()) {
            String line = sr2.readLine();
    //        System.out.println(line);
            String[] tokens = line.split("\t");
            String queryId = tokens[0];
            if(!retrieved.containsKey(queryId))
                retrieved.put(queryId, new ArrayList<String>());
    //        System.out.println(queryId + "\t" + tokens[1]);
            retrieved.get(queryId).add(tokens[1].toLowerCase());
        }

        if(dataset == GENWEB) {
            for (String topic : topics) {
                DirectoryManager dm = new DirectoryManager(queryDir + topic);
                for (String queryId : dm.getFileNameList()) {
                    detectControversy(queryId, 10);

                }
            }
        }
        else {
            DirectoryManager dm = new DirectoryManager(queryDir);
            for (String queryId : dm.getFileNameList()) {
                detectControversy(queryId, 10);
            }
        }

        eval.binaryAutomaticEvaluate(result, aggregationParam);

    }



    private void detectControversy(String queryId, int k) {
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

        csd.computeScore(info, rankedDoc, ScoringMethod.MAX, k);
        msd.computeScore(info, rankedDoc, ScoringMethod.MAX, k);
        dsd.computeScore(info, rankedDoc, ScoringMethod.MAX, k);
        aggregator.aggregateAndClassify(info);

        // print heading
        result.put(queryId, info);

    }

}
