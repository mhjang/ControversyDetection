package edu.umass.cs.ciir.controversy.data;

import edu.umass.cs.ciir.controversy.experiment.Info;
import edu.umass.cs.ciir.controversy.utils.SimpleFileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mhjang on 12/18/15.
 */
public class AnnotationDatabase {
    HashMap<String, Integer> controversyDB;
    HashMap<String, Double> info;
    double amputatedScore = 2.5;

    public AnnotationDatabase() throws IOException {
        controversyDB = new HashMap<String, Integer>();
        info = new HashMap<String, Double>();
        SimpleFileReader sr = new SimpleFileReader("/home/mhjang/IdeaProjects/ControversyDetectionCIKM/resource/url_rating.txt");
        while(sr.hasMoreLines()) {
            String line = sr.readLine();
            if(line.contains("wiki")) {
                String[] tokens = line.split("\t");
                String wikiTitle = tokens[3].substring(tokens[3].lastIndexOf('/')+1);
  //              System.out.println(wikiTitle + "\t" + tokens[5]);
                controversyDB.put(wikiTitle.toLowerCase(), Integer.parseInt(tokens[5]));
            }
        }
    }

    public HashMap<String, String> computeScore(HashMap<String, String> info, ArrayList<String> wikidocs) {
        Double sumScore = 0.0;
        Integer coveredArticles = 0; // # of annotated articles
        Double coveredAvgScore = 0.0; // total of score sum only for annotated
        Double maxScore = amputatedScore;
        Double minScore = 4.0;
        Double coveredMaxScore = 0.0;
        for(String doc : wikidocs) {
            double score = getControversyScore(doc);
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

    /**
     * return -1 if the wiki title doesn't exist in the annotated articles
     * @param wikiTitle
     * @return
     */
    public int getControversyScore(String wikiTitle) {
        // # of annotated articles
        int coveredArticle = 0;
        double coveredArticleSum = 0.0;
        if(controversyDB.containsKey(wikiTitle)) {
            int score = controversyDB.get(wikiTitle);
            return score;
        }
        else return -1;
    }
}
