package edu.umass.cs.ciir.controversy.experiment;

import edu.umass.cs.ciir.controversy.Scorer.*;
import edu.umass.cs.ciir.controversy.data.*;
import edu.umass.cs.ciir.controversy.utils.DirectoryManager;
import edu.umass.cs.ciir.controversy.utils.SimpleFileReader;
import edu.umass.cs.ciir.controversy.utils.SimpleFileWriter;
import org.lemurproject.galago.core.retrieval.ScoredDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
/**
 * Created by mhjang on 12/18/15.
 */
public class OuputControversyScores {
    public static void main(String[] args) throws IOException {

        CScoreDatabase csd = new CScoreDatabase();
        MScoreDatabase msd = new MScoreDatabase();
        DScoreDatabase dsd = new DScoreDatabase();


        PrintStream console = System.out;

        File file = new File("retrieved_neighbors_Cscore.txt");
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
    //    System.setOut(ps);

        /**
         * 1: Read query webpages
         * */
        String queryDir = "/home/mhjang/controversy_Data/annotated/";
   //     String[] trainingTopics = {"anne_frank", "cancer", "cheese", "intensive_farming", "steve_jobs"};
        String[] trainingTopics = {"anti_americanism", "anne_frank", "medieval_cusine", "uranium", "gay_marriage", "intensive_farming", "native_americans", "steve_jobs", "video_game", "feminism", "berlin_wall", "native_americans", "cancer", "cheese"};
    //    String[] trainingTopics = {"gay_marriage"};
        //    String[] testTopics = {"anti_americanism", "berlin_wall", "feminism", "gay_marriage", "intensive_farming", "native_americans", "steve_jobs", "video_game",
    //    "cancer","anne_frank", "cheese"};
   //     String[] testTopics = {"medieval_cusine"};
        Evaluator eval = new Evaluator(Evaluator.MAX_RATING);
        HashMap<String, HashMap<String, String>> result = new HashMap<String, HashMap<String, String>>();
        WikiRetrieval wr = new WikiRetrieval();
        DocumentQueryGenerator dqg = new DocumentQueryGenerator();
        AnnotationDatabase cd = new AnnotationDatabase();
        KNNScorer scorer = new KNNScorer(cd);
        AggregationParameter aggregationParam = new AggregationParameter.Builder().setAggregationMethod(AggregationParameter.AGGREGATION_OR)
                .setMThreshold(30000).setCScoreThreshold(0.0418).build();
        Aggregator aggregator = new Aggregator(aggregationParam);
        WikiGoldStandard wikiDB = new WikiGoldStandard();


   //     SimpleFileWriter sw = new SimpleFileWriter("query_10.txt");
   //     SimpleFileWriter queryResultWrite = new SimpleFileWriter("retrieved_neighbors.txt");

        SimpleFileReader sr = new SimpleFileReader("query_10.txt");
        HashMap<String, String> generatedQueryMap = new HashMap<String, String>();
        while(sr.hasMoreLines()) {
            String line = sr.readLine();
            String[] tokens = line.split("\t");
            String queryId = tokens[0];
            String query = tokens[1];
            generatedQueryMap.put(queryId, query);
        }

        sr.close();

        HashMap<String, ArrayList<String>> retrieved = new HashMap<String, ArrayList<String>>();
        SimpleFileReader sr2 = new SimpleFileReader("/home/mhjang/IdeaProjects/ControversyDetectionCIKM/resource/rankedlist/allquery_ranking.txt");
        while(sr2.hasMoreLines()) {
            String line = sr2.readLine();
            System.out.println(line);
            String[] tokens = line.split("\t");
            String queryId = tokens[0];
            if(!retrieved.containsKey(queryId))
                retrieved.put(queryId, new ArrayList<String>());
            System.out.println(queryId + "\t" + tokens[1]);
            retrieved.get(queryId).add(tokens[1].toLowerCase());
        }

        for(String topic : trainingTopics) {
            DirectoryManager dm = new DirectoryManager(queryDir + topic);

            for (String queryId : dm.getFileNameList()) {

                /* Query Generation Experiment */



                // /  SimpleFileReader sr = new SimpleFileReader(queryDir + topic + "/" + queryId);
            /*
                StringBuilder builder = new StringBuilder();
                while (sr.hasMoreLines()) {
                    builder.append(sr.readLine());
                }
                String qText = builder.toString();


                 // 2: Generate query from each webpage

                String generatedQuery = dqg.generateQuerybyFrequency(qText, 10);
                System.out.println("query: " + generatedQuery);
    //            sw.writeLine(queryId + "\t" + generatedQuery);


                // 3: Find relevant Wikipedia pages

                // by running queries to the index live
                /*
                List<ScoredDocument> scoredDocuments = wr.runQuery(generatedQuery, 20);
                ArrayList<String> rankedDoc = new ArrayList<String>();
                for (ScoredDocument sd : scoredDocuments) {
                    String wikiTitle = sd.documentName.replace(".html","");
                    rankedDoc.add(wikiTitle.toLowerCase());
    //                queryResultWrite.writeLine(queryId + "\t" + wikiTitle + "\t" + sd.rank);
                }
                */
                // or by loading computed ranked list
               if(!retrieved.containsKey(queryId)) continue;
                  ArrayList<String> rankedDoc = retrieved.get(queryId);


                // Oracle Experiment
                 ArrayList<String> goldDoc = wikiDB.getRelevantPages(queryId, 2.5);
         //       ArrayList<String> rankedDoc = goldDoc;

                // 4: Aggregate controversy score for each set
                HashMap<String, String> info = new HashMap<String, String>();
                Integer size = goldDoc.size();
                info.put("number of relevant oracle docs", size.toString());
                if(size < 5)
                    continue;
                scorer.computeScore(info, rankedDoc);
                info.put("qid", queryId);
                int k = 10;

                csd.computeScore(info, rankedDoc, ScoringMethod.MAX, k);
                msd.computeScore(info, rankedDoc, ScoringMethod.MAX, k);
                dsd.computeScore(info, rankedDoc, ScoringMethod.MAX, k);
                aggregator.aggregate(info);


                // print heading
                result.put(queryId, info);
            }
        }
 //       eval.binaryOracleEvaluate(result);
          eval.binaryAutomaticEvaluate(result, aggregationParam);


  //      sw.close();
  //      queryResultWrite.close();

        /**
         * 5: Output controversy score
         */
    }

}
