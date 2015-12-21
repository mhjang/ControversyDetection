package edu.umass.cs.ciir.controversy.Scorer;

import edu.umass.cs.ciir.controversy.data.AnnotationDatabase;
import edu.umass.cs.ciir.controversy.experiment.Info;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mhjang on 12/18/15.
 */
public class KNNScorer {
    AnnotationDatabase cd;


    /**
     * The default score value to put in to the wikipage that was not annotated
     */
    double amputatedScore = 2.5;
    public KNNScorer(AnnotationDatabase cd_) {
        this.cd = cd_;
    }

    /**
     * compute the aggregated controversy score from annotated wikipedia pages
     * @param wikidocs
     * @return
     */
    public HashMap<String, Double> computeScore(HashMap<String, Double> info, ArrayList<String> wikidocs) {
        double sumScore = 0.0;
        int coveredArticles = 0; // # of annotated articles
        double coveredAvgScore = 0; // total of score sum only for annotated
        double maxScore = amputatedScore;
        double coveredMaxScore = 0.0;
        for(String doc : wikidocs) {
            double score = cd.getControversyScore(doc);
            if(score > 0.0) { // it's annotated
                sumScore += score;
                coveredArticles++;
                coveredAvgScore += score;
                if(maxScore < score)
                    maxScore = score;
                if(coveredMaxScore < score)
                    coveredMaxScore = score;

            }
            else
                sumScore += amputatedScore;

        }

        if(coveredArticles > 0)
            coveredAvgScore /= coveredArticles;
        else {
            coveredAvgScore = 0.0;
        }
        double avgScore = sumScore / (double)(wikidocs.size());
        info.put(Info.COVERED_ARTICLE_NUM, (double)coveredArticles);
        info.put(Info.COVERED_ARTICLE_SCORE_AVG, (double)coveredAvgScore);
        info.put(Info.COVERED_ARTICLE_SCORE_MAX, (double)coveredMaxScore);

        info.put(Info.SCORING_AVG, avgScore);
        info.put(Info.SCORING_MAX, maxScore);
        return info;
    }
}
