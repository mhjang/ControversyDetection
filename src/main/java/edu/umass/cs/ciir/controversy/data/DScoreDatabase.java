package edu.umass.cs.ciir.controversy.data;

import edu.umass.cs.ciir.controversy.Scorer.Aggregator;
import edu.umass.cs.ciir.controversy.utils.SimpleFileReader;
import edu.umass.cs.ciir.controversy.utils.SimpleFileWriter;
import org.lemurproject.galago.core.btree.simple.DiskMapReader;
import org.lemurproject.galago.tupleflow.Utility;
import org.lemurproject.galago.utility.ByteUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by mhjang on 1/8/16.
 * for dispute tag
 */
public class DScoreDatabase {
    HashSet<String> dscoreDB;
    DiskMapReader reader;
    public DScoreDatabase() throws IOException {
        dscoreDB = new HashSet<String>();
        reader = new DiskMapReader(DataPath.DSCORE);
    }
    private boolean isDisputed(String topic) {
        if(dscoreDB.contains(topic)) return true;
        else return false;
    }

    public void close() throws IOException {
        reader.close();
    }
    public void computeScore(HashMap<String, String> info, ArrayList<String> wikidocs, int votingMethod, int topK)
    throws IOException {
        boolean useSecondList = false;
        if(wikidocs.size() < topK) {
            System.out.println(info.get("qid") + " does not have " + topK + " neighbors, but only " + wikidocs.size() + " docs.");
            topK = wikidocs.size();
        }

        List<String> list;

        list =  wikidocs.subList(0, topK);


        if(votingMethod == Aggregator.MAX) {
            for (String wiki : list) {
                if (isDisputed(wiki)) {
                    info.put("DScore", "1.0");
                    return;
                }
            }

        }
        else {
            int count = 0;

            for (String wiki : list) {
                if (isDisputed(wiki)) {
                    count++;
                }
            }

            if((double)(count)/(double)(list.size()) >= 0.5)
                info.put("DScore", "1.0");

        }

        // implement avg

        info.put("DScore", "0.0");
    }

    public Double getScore(String word) {
        if(reader.containsKey(ByteUtil.fromString(word)))
            return 1.0;
        else
            return 0.0;
    }
}
