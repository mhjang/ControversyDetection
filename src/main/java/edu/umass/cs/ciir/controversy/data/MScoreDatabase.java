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
import java.util.List;

/**
 * Created by mhjang on 12/20/15.
 */
public class MScoreDatabase {
    HashMap<String, Double> mscoreDB;
    DiskMapReader reader;

    DiskMapReader revisedReader;
    boolean revise = false;

    public MScoreDatabase(boolean r, int networkOption) throws IOException {
        reader = new DiskMapReader(DataPath.MSCORE);
        this.revise = r;

        if (this.revise) {
            if (networkOption == OutputControversyScores.CLIQUE_BASED_NETWORK)
                revisedReader = new DiskMapReader(DataPath.REVISED_CLIQUE_MSCORE);
            else
                revisedReader = new DiskMapReader(DataPath.REVISED_PAIR_MSCORE);
        }


        /*
        mscoreDB = new HashMap<String, Double>();
        SimpleFileReader sr = new SimpleFileReader(dir);
        while(sr.hasMoreLines()) {
            String line = sr.readLine();
            String[] tokens = line.split("\t");
            Double score = Double.parseDouble(tokens[0]);
            String word = tokens[1];
            mscoreDB.put(word.toLowerCase(), score);
        }
        */
    }
    public void close() throws IOException {
        reader.close();
        if(revisedReader != null)
            revisedReader.close();
    }

    public void computeScore(HashMap<String, String> info, ArrayList<String> wikidocs, int votingMethod, int topK)
    throws IOException {
        Double finalScore = 0.0;

        String maxPage = null;

        if(wikidocs.size() < topK) {
            System.out.println(info.get("qid") + " does not have " + topK + " neighbors, but only " + wikidocs.size() + " docs.");
            topK = wikidocs.size();
        }

        List<String> list;

        list =  wikidocs.subList(0, topK);


        if(votingMethod == Aggregator.MAX) {
            for (String wiki : list) {
                double score = getScore(wiki);
                if(finalScore < score) {
                    finalScore = score;
                    maxPage = wiki;
                }
            }
            info.put("MScoreMaxPage", maxPage);
        }
        else { // votingMethod == Aggregator.AVG
            for (String wiki : list) {
                double score = getScore(wiki);
                finalScore += score;
            }
            finalScore /= (double)(list.size());
        }

        info.put("MScore", finalScore.toString());
    }


    public Double getScore(String word) {
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
