package edu.umass.cs.ciir.controversy.data;

import edu.umass.cs.ciir.controversy.Scorer.Aggregator;
import edu.umass.cs.ciir.controversy.experiment.OutputControversyScores;
import edu.umass.cs.ciir.controversy.utils.SimpleFileWriter;
import org.lemurproject.galago.core.btree.simple.DiskMapReader;
import org.lemurproject.galago.tupleflow.Utility;
import org.lemurproject.galago.utility.ByteUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mhjang on 12/20/15.
 */
public class CScoreDatabase {
    HashMap<String, Double> cscoreDB;
    DiskMapReader reader;
    DiskMapReader revisedReader;
    boolean revise = false;
    SimpleFileWriter logWriter;
    public CScoreDatabase(boolean r, int networkOption, SimpleFileWriter logWriter_) throws IOException {
        reader = new DiskMapReader(DataPath.CSCORE);
        logWriter = logWriter_;
        this.revise = r;
        if(this.revise) {
            if (networkOption == OutputControversyScores.CLIQUE_BASED_NETWORK)
                revisedReader = new DiskMapReader(DataPath.REVISED_CLIQUE_CSCORE);
            else
                revisedReader = new DiskMapReader(DataPath.REVISED_PAIR_CSCORE);
        }
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
                double score = getScore(wiki);
                if(finalScore < score) {
                    finalScore = score;
                    maxPage = wiki;
                }
            }
            info.put("CScoreMaxPage", maxPage);
        }
        else if(votingMethod == Aggregator.AVG) {
            for (String wiki : wikidocs.subList(0, topK)) {
                double score = getScore(wiki);
                finalScore += score;

            }
            finalScore /= (double)(topK);
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
