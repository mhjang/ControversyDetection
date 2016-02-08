package edu.umass.cs.ciir.controversy.data;

import edu.umass.cs.ciir.controversy.Scorer.ScoringMethod;
import edu.umass.cs.ciir.controversy.utils.*;
import org.lemurproject.galago.core.btree.simple.DiskMapReader;
import org.lemurproject.galago.core.btree.simple.DiskMapSortedBuilder;
import org.lemurproject.galago.tupleflow.Utility;
import org.lemurproject.galago.utility.ByteUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by mhjang on 12/20/15.
 */
public class CScoreDatabase {
    HashMap<String, Double> cscoreDB;
    DiskMapReader reader;
    DiskMapReader revisedReader;
    boolean revise = false;

    public CScoreDatabase(boolean r) throws IOException {
        reader = new DiskMapReader(DataPath.CSCORE);
        this.revise = r;
        if(this.revise)
            revisedReader = new DiskMapReader(DataPath.REVISED_CLIQUE_CSCORE);

/*      Constructing hashmap, but this is replaced with index file

        cscoreDB = new HashMap<String, Double>();
        SimpleFileReader sr = new SimpleFileReader(dir);
        while(sr.hasMoreLines()) {
            String line = sr.readLine();
            String[] tokens = line.split("\t");
            Double score = Double.parseDouble(tokens[1]);
            String word = tokens[0];
            cscoreDB.put(word.toLowerCase(), score);
        }
*/
    }


    public void computeScore(HashMap<String, String> info, ArrayList<String> wikidocs, int votingMethod, int topK) {
        Double finalScore = 0.0;
        String maxPage = null;


        if(votingMethod == ScoringMethod.MAX) {
            for (String wiki : wikidocs.subList(0, Math.min(topK, wikidocs.size()))) {
                double score = getScore(wiki);
        //        System.out.println(info.get("qid") + "\t" + wiki + "\t" + score);
                if(finalScore < score) {
                    finalScore = score;
                    maxPage = wiki;
                }
            }
            info.put("CScoreMaxPage", maxPage);
        }
        else { // votingMethod == ScoringMethod.AVG
            for (String wiki : wikidocs.subList(0, Math.min(topK, wikidocs.size()))) {
                double score = getScore(wiki);
                finalScore += score;

            }
            finalScore /= (double)(wikidocs.size());
        }
        info.put("CScore", finalScore.toString());

    }
    private Double getScore(String word) {
        if(revise) {
            if(revisedReader.containsKey(ByteUtil.fromString(word))) {
                return Utility.toDouble(revisedReader.get(ByteUtil.fromString(word)));
            }
        }
        if(reader.containsKey(ByteUtil.fromString(word)))
            return Utility.toDouble(reader.get(ByteUtil.fromString(word)));
        else
            return 0.0;
    }

}
