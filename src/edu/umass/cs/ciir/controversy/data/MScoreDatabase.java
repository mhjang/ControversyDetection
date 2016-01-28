package edu.umass.cs.ciir.controversy.data;

import edu.umass.cs.ciir.controversy.Scorer.ScoringMethod;
import edu.umass.cs.ciir.controversy.utils.SimpleFileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mhjang on 12/20/15.
 */
public class MScoreDatabase {
    HashMap<String, Double> mscoreDB;
    public MScoreDatabase() throws IOException {
        mscoreDB = new HashMap<String, Double>();
        String dir = "/home/mhjang/IdeaProjects/ControversyDetectionCIKM/resource/MScore.txt";
        SimpleFileReader sr = new SimpleFileReader(dir);
        while(sr.hasMoreLines()) {
            String line = sr.readLine();
            String[] tokens = line.split("\t");
            Double score = Double.parseDouble(tokens[0]);
            String word = tokens[1];
            mscoreDB.put(word.toLowerCase(), score);
        }
    }


    public void computeScore(HashMap<String, String> info, ArrayList<String> wikidocs, int votingMethod, int topK) {
        Double finalScore = 0.0;

        String maxPage = null;
        if(votingMethod == ScoringMethod.MAX) {
            for (String wiki : wikidocs.subList(0, Math.min(topK, wikidocs.size()))) {
                double score = getScore(wiki);
                if(finalScore < score) {
                    finalScore = score;
                    maxPage = wiki;
                }
            }
            info.put("MScoreMaxPage", maxPage);
        }
        else { // votingMethod == ScoringMethod.AVG
            for (String wiki : wikidocs.subList(0, Math.min(topK, wikidocs.size()))) {
                double score = getScore(wiki);
                finalScore += score;
            }
            finalScore /= (double)(wikidocs.size());
        }

        info.put("MScore", finalScore.toString());
    }


    public Double getScore(String word) {
        if(mscoreDB.containsKey(word))
            return mscoreDB.get(word);
        else
            return 0.0;
    }

}
