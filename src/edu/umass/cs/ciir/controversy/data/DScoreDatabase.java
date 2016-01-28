package edu.umass.cs.ciir.controversy.data;

import edu.umass.cs.ciir.controversy.Scorer.ScoringMethod;
import edu.umass.cs.ciir.controversy.utils.SimpleFileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by mhjang on 1/8/16.
 * for dispute tag
 */
public class DScoreDatabase {
    HashSet<String> dscoreDB;
    public DScoreDatabase() throws IOException {
        dscoreDB = new HashSet<String>();
        String dir = "/home/mhjang/IdeaProjects/ControversyDetectionCIKM/resource/all-4sets-titlesDisputed.txt";
        SimpleFileReader sr = new SimpleFileReader(dir);
        while(sr.hasMoreLines()) {
            String line = sr.readLine();
            dscoreDB.add(line.toLowerCase());
        }
    }
    private boolean isDisputed(String topic) {
        if(dscoreDB.contains(topic)) return true;
        else return false;
    }

    public void computeScore(HashMap<String, String> info, ArrayList<String> wikidocs, int votingMethod, int topK) {
        Double finalScore = 0.0;

        String maxPage = null;
        if(votingMethod == ScoringMethod.MAX) {
            for (String wiki : wikidocs.subList(0, Math.min(topK, wikidocs.size()))) {
                if (isDisputed(wiki)) {
                    info.put("DScore", "1.0");
         //           System.out.println(wiki);
                    return;
                }
            }
        }

        info.put("DScore", "0.0");
    }
}
