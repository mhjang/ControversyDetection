package edu.umass.cs.ciir.controversy.experiment;

import edu.umass.cs.ciir.controversy.Scorer.Evaluator;
import edu.umass.cs.ciir.controversy.Scorer.KNNScorer;
import edu.umass.cs.ciir.controversy.Scorer.ScoringMethod;
import edu.umass.cs.ciir.controversy.data.AnnotationDatabase;
import edu.umass.cs.ciir.controversy.data.CScoreDatabase;
import edu.umass.cs.ciir.controversy.data.MScoreDatabase;
import edu.umass.cs.ciir.controversy.utils.DirectoryManager;
import edu.umass.cs.ciir.controversy.utils.SimpleFileReader;
import org.lemurproject.galago.core.retrieval.ScoredDocument;

import java.io.IOException;
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

        /**
         * 1: Read query webpages
         * */
        String queryDir = "/home/mhjang/controversy_Data/annotated/";
   //     String[] testTopics = {"cancer", "video_game", "global_warming", "feminism", "medieval_cusine"};
        String[] testTopics = {"cancer", "medieval_cusine"};
        Evaluator eval = new Evaluator(Evaluator.MAX_RATING);
        HashMap<String, HashMap<String, Double>> result = new HashMap<String, HashMap<String, Double>>();
        for(String topic : testTopics) {
            DirectoryManager dm = new DirectoryManager(queryDir + topic);
            for (String queryId : dm.getFileNameList()) {
                SimpleFileReader sr = new SimpleFileReader(queryDir + topic + "/" + queryId);
                StringBuilder builder = new StringBuilder();
                while (sr.hasMoreLines()) {
                    builder.append(sr.readLine());
                }
                String qText = builder.toString();


                /**
                 * 2: Generate query from each webpage
                 */
                DocumentQueryGenerator dqg = new DocumentQueryGenerator();
                String generatedQuery = dqg.generateQuerybyFrequency(qText, 10);
                System.out.println("query: " + generatedQuery);

                /**
                 * 3: Find relevant Wikipedia pages
                 */
                WikiRetrieval wr = new WikiRetrieval();
                List<ScoredDocument> scoredDocuments = wr.runQuery(generatedQuery, 10);
                ArrayList<String> rankedDoc = new ArrayList<String>();
                for (ScoredDocument sd : scoredDocuments) {
                    rankedDoc.add(sd.documentName.replace(".html", ""));
                }

                /**
                 * 4: Aggregate controversy score for each set
                 */

                AnnotationDatabase cd = new AnnotationDatabase();
                KNNScorer scorer = new KNNScorer(cd);
                HashMap<String, Double> info = new HashMap<String, Double>();
                scorer.computeScore(info, rankedDoc);

                csd.computeScore(info, rankedDoc, ScoringMethod.MAX);
                msd.computeScore(info, rankedDoc, ScoringMethod.MAX);



                // print heading
                result.put(queryId, info);
            }
        }
        eval.binaryEvaluate(result);




        /**
         * 5: Output controversy score
         */
    }

}
