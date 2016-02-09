package edu.umass.cs.ciir.controversy.data;

import edu.umass.cs.ciir.controversy.utils.SimpleFileReader;
import org.lemurproject.galago.core.btree.simple.DiskMapReader;
import org.lemurproject.galago.tupleflow.Utility;
import org.lemurproject.galago.utility.ByteUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Created by mhjang on 2/7/16.
 */
public class NetworkInferredScore {
    static DiskMapReader revisionReader;
    static DiskMapReader cscoreReader;
    static class WikiNode {
        String title;
        int revisionCount;
        double score;
        public WikiNode(String t, int r, double s) {
            title = t;
            revisionCount = r;
            score = s;

        }
    }

    public static void main(String[] args) throws IOException {
         revisionReader = new DiskMapReader(DataPath.REVISION_CNT);
         cscoreReader = new DiskMapReader(DataPath.MSCORE);


    /*    String dir = DataPath.REVISION_CNT;
        DiskMapReader reader = new DiskMapReader(dir);
        SimpleFileReader sr = new SimpleFileReader("/home/mhjang/controversy_Data/datasets/resources/network.txt");
        while(sr.hasMoreLines()) {
            String line = sr.readLine();
            String query = line.split("\t")[0];
            if(reader.containsKey(ByteUtil.fromString(query)))
                System.out.println(query +"\t" + Utility.toDouble(reader.get(ByteUtil.fromString(query))));
        }
    */
        reviseScore();

    }

    private static int getRevisionScore(String query) throws IOException {
        if(revisionReader.containsKey(ByteUtil.fromString(query)))
            return (int)(Utility.toDouble(revisionReader.get(ByteUtil.fromString(query))));
        else return 0;
    }


    private static double getCScore(String query) throws IOException {
        if(cscoreReader.containsKey(ByteUtil.fromString(query)))
            return Utility.toDouble(cscoreReader.get(ByteUtil.fromString(query)));
        else return 0.0;
    }

    public static void reviseScore() throws IOException {
        System.setOut(new PrintStream(new FileOutputStream("/home/mhjang/controversy_Data/datasets/resources/pairbasednetwork/revised.MSCORE")));

        SimpleFileReader sr = new SimpleFileReader("/home/mhjang/controversy_Data/datasets/resources/pairbasednetwork.txt");
  //      System.out.println("Query \t #_of_Neighbors \t Original_Score \t Revised_Score \t MostInfluential");
        while(sr.hasMoreLines()) {
            String line = sr.readLine();
            String[] tokens = line.split("\t");
     //       System.out.println(line);
            int revisionCount = getRevisionScore(tokens[0]);
            WikiNode queryNode = new WikiNode(tokens[0], revisionCount, getCScore(tokens[0]));
            ArrayList<WikiNode> influentialNeighbors = new ArrayList<WikiNode>();

            int revisionSum = revisionCount;
            WikiNode mostInfluential = queryNode;
            for(int i=1; i<tokens.length; i++) {
                int cnt = getRevisionScore(tokens[i]);
                if(cnt > revisionCount) {
                    WikiNode neighbor = new WikiNode(tokens[i], cnt, getCScore(tokens[i]));
                    influentialNeighbors.add(neighbor);
                    revisionSum+= cnt;
                    if(cnt > mostInfluential.revisionCount) {
                        mostInfluential = neighbor;
                    }

                }
            }
            double revisedScore = (double)(queryNode.score) * (double)(queryNode.revisionCount) / (double)(revisionSum);
            for(WikiNode w : influentialNeighbors) {
                revisedScore += (double)(w.score)*(double)(w.revisionCount)/(double)(revisionSum);
            }
        //    System.out.println(queryNode.title + "(" + queryNode.revisionCount + ", " + queryNode.score + ") \t" + influentialNeighbors.size() + "\t" + queryNode.score + "\t" + revisedScore + "\t" + mostInfluential.title + "(" + mostInfluential.revisionCount + ", " + mostInfluential.score + ")");
            System.out.println(queryNode.title + "\t" + revisedScore);
        }
    }
}
