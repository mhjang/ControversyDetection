package edu.umass.cs.ciir.controversy.data;

import edu.umass.cs.ciir.controversy.utils.SimpleFileReader;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by mhjang on 12/18/15.
 */
public class AnnotationDatabase {
    HashMap<String, Integer> controversyDB;
    HashMap<String, Double> info;
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
