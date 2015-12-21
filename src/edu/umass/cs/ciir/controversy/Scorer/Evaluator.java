package edu.umass.cs.ciir.controversy.Scorer;

import edu.umass.cs.ciir.controversy.data.DataPath;
import edu.umass.cs.ciir.controversy.experiment.Info;
import edu.umass.cs.ciir.controversy.utils.SimpleFileReader;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by mhjang on 12/20/15.
 */
public class Evaluator {
    /**
     * for the ratings rated by more than one annotator, we will take
     * 1. MAX: less controversial
     * 2. MIN: more controversial
     * 3. Average
     * rating
     */
    public static int MAX_RATING= 1;
    public static int MIN_RATING = 2;
    public static int AVG_RATING = 3;

    HashMap<String, Double> goldStandard;

    public Evaluator(int scoring) throws IOException {
        String dir;
        goldStandard = new HashMap<String, Double>();
        if(scoring == MAX_RATING) {
            dir = DataPath.GOLDSTANDARD_TOPIC_MAX;
        }
        else if(scoring == MIN_RATING)
            dir = DataPath.GOLDSTANDARD_TOPIC_MIN;
        else
            dir = DataPath.GOLDSTANDARD_TOPIC_AVG;
        SimpleFileReader sr = new SimpleFileReader(dir);
        while(sr.hasMoreLines()) {
            String line  = sr.readLine();
            System.out.println(line);
            String[] tokens = line.split("\t");
            goldStandard.put(tokens[0], Double.parseDouble(tokens[1]));
        }
    }
    public void binaryEvaluate(HashMap<String, HashMap<String, Double>> result) {
        boolean prediction, truth;
        int correct = 0, wrong = 0;
        System.out.println("querypage \t covered_articles \t covered_avg_score \t covered_max_score \t all_avg_Score \t all_max_score \t result");

        for(String docName : result.keySet()) {
            if(goldStandard.containsKey(docName)) {
                HashMap<String, Double> info = result.get(docName);
                if(info.get(Info.SCORING_MAX) >= 2.5)
                    prediction = false;
                else
                    prediction = true;
                if(goldStandard.get(docName) >= 2.5)
                    truth = false;
                else
                    truth = true;
                if(prediction == truth) {
                    correct++;
                }
                else {
                    wrong++;
                }
                System.out.println(docName + "\t" + info.get(Info.COVERED_ARTICLE_NUM) + "\t" +
                        info.get(Info.COVERED_ARTICLE_SCORE_AVG) + "\t" + info.get(Info.COVERED_ARTICLE_SCORE_MAX) + "\t"
                        + info.get(Info.SCORING_AVG) + "\t" + info.get(Info.SCORING_MAX) + "\t" + (prediction == truth));


            }
        }
        System.out.println("Binary Classifciaton Judgments: " + (double)(correct)/(double)(correct + wrong));
    }

}
