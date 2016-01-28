package edu.umass.cs.ciir.controversy.data;

import edu.umass.cs.ciir.controversy.Scorer.ScoringMethod;
import edu.umass.cs.ciir.controversy.utils.SimpleFileReader;
import org.lemurproject.galago.core.btree.simple.DiskMapReader;
import org.lemurproject.galago.core.btree.simple.DiskMapSortedBuilder;
import org.lemurproject.galago.tupleflow.Utility;
import org.lemurproject.galago.utility.ByteUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by mhjang on 12/20/15.
 */
public class CScoreDatabase {
    HashMap<String, Double> cscoreDB;
    public CScoreDatabase() throws IOException {
        cscoreDB = new HashMap<String, Double>();
        String dir = "/home/mhjang/IdeaProjects/ControversyDetectionCIKM/resource/cscore_full.txt";
        SimpleFileReader sr = new SimpleFileReader(dir);
        while(sr.hasMoreLines()) {
            String line = sr.readLine();
            String[] tokens = line.split("\t");
            Double score = Double.parseDouble(tokens[1]);
            String word = tokens[0];
            cscoreDB.put(word.toLowerCase(), score);
        }
    }


    public void computeScore(HashMap<String, String> info, ArrayList<String> wikidocs, int votingMethod, int topK) {
        Double finalScore = 0.0;
        String maxPage = null;


        if(votingMethod == ScoringMethod.MAX) {
            for (String wiki : wikidocs.subList(0, Math.min(topK, wikidocs.size()))) {
                double score = getScore(wiki);
                System.out.println(info.get("qid") + "\t" + wiki + "\t" + score);
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
        if(cscoreDB.containsKey(word))
            return cscoreDB.get(word);
        else
            return 0.0;
    }
    public void buildIndex() throws IOException {
        /******************************
         * building a B-tree index
         ******************************/

        DiskMapSortedBuilder dmb = new DiskMapSortedBuilder("MScore_index.txt");
        String fileName = "/home/mhjang/IdeaProjects/ControversyDetectionCIKM/resource/sortedMscore_.txt";
        BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
        String line = null;
        while((line = br.readLine()) != null) {
            try{
                StringTokenizer st = new StringTokenizer(line, "\t");
                Double score = Double.parseDouble(st.nextToken());
                String title = st.nextToken();
                byte[] ngram = ByteUtil.fromString(title);
                byte[] frequency = Utility.fromDouble(score);
                dmb.put(ngram, frequency);
           } catch(NumberFormatException e) {
                System.out.println(line);
                e.printStackTrace();
            }
        }
        dmb.close();

     //   DiskMapReader dmr = DiskMapReader.fromMap("ngram_index/1gms0", map);

      //  dmb.close();

        /***
         * loading the index file
         */
        //    File file = new File("ngram_index/1gms");

        /**
         * Build an on-disk map using galago
         */
   /*     DiskMapReader mapReader = new DiskMapReader(file.getAbsolutePath());
        System.out.println(Runtime.getRuntime().maxMemory() / (1024 * 1024 * 1024));
        /*
         * pull keys
         */
  /*      byte[] data = mapReader.get(Utility.fromString("dynamic"));
        if(data != null) {
            String count = Utility.toString(data);
            System.out.println("count: "+ count);
        }

        // java using object equality is dumb
        // assertTrue(memKeys.contains(key)) will fail because it does pointer comparisons...
*/
    }
}
