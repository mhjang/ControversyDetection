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
    public HashMap<String, String> computeScore(HashMap<String, String> info, ArrayList<String> wikidocs) {
        Double sumScore = 0.0;
        Integer coveredArticles = 0; // # of annotated articles
        Double coveredAvgScore = 0.0; // total of score sum only for annotated
        Double maxScore = amputatedScore;
        Double minScore = 4.0;
        Double coveredMaxScore = 0.0;
        for(String doc : wikidocs) {
            double score = cd.getControversyScore(doc);
            if(score > 0.0) { // it's annotated
                sumScore += score;
                coveredArticles++;
                coveredAvgScore += score;
                if(maxScore < score)
                    maxScore = score;
                if(minScore > score)
                    minScore = score;
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
        Double avgScore = sumScore / (double)(wikidocs.size());
        info.put(Info.COVERED_ARTICLE_NUM, coveredArticles.toString());
        info.put(Info.COVERED_ARTICLE_SCORE_AVG, coveredAvgScore.toString());
        info.put(Info.COVERED_ARTICLE_SCORE_MAX, coveredMaxScore.toString());

        info.put(Info.ORACLE_SCORING_AVG, avgScore.toString());
        info.put(Info.ORACLE_SCORING_MAX, maxScore.toString());
        info.put(Info.ORACLE_SCORING_MIN, minScore.toString());
        return info;
    }
}
