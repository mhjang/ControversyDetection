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

/**
 * Created by mhjang on 1/8/16.
 * for dispute tag
 */
public class DScoreDatabase {
    HashSet<String> dscoreDB;
    SimpleFileWriter logWriter;
    DiskMapReader reader;
    public DScoreDatabase(SimpleFileWriter logWriter_) throws IOException {
        dscoreDB = new HashSet<String>();
        reader = new DiskMapReader(DataPath.DSCORE);
    }
    private boolean isDisputed(String topic) {
        if(dscoreDB.contains(topic)) return true;
        else return false;
    }

    public void computeScore(HashMap<String, String> info, ArrayList<String> wikidocs, int votingMethod, int topK)
    throws IOException {
        Double finalScore = 0.0;
        String maxPage = null;
        if(wikidocs.size() < topK) {
            logWriter.writeLine(info.get("qid") + " does not have " + topK + " neighbors, but only " + wikidocs.size() + " docs.");
            topK = wikidocs.size();
        }

        if(votingMethod == Aggregator.MAX) {
            for (String wiki : wikidocs.subList(0, topK)) {
                if (isDisputed(wiki)) {
                    info.put("DScore", "1.0");
                    return;
                }
            }
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
