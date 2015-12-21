package edu.umass.cs.ciir.controversy.utils;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by mhjang on 2/1/14.
 */
public class StopWordRemover {
    public HashSet<String> stopwords;

    public StopWordRemover() {
        try{
            SimpleFileReader sr =new SimpleFileReader("stopwords.txt");
        //    File stopWordsFile = new File( this.getClass().getResource("./resources/stopwords.txt").toURI());
            stopwords = new HashSet<String>();
            while(sr.hasMoreLines()) {
                String line = sr.readLine().replace("\n","");
                stopwords.add(line.toLowerCase().trim());
            }
            sr.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public LinkedList<String> removeStopwords(LinkedList<String> list) {
        int len = list.size();
        LinkedList<String> stopRemovedList = new LinkedList<String>();
        for(int i=0; i<len; i++) {
            String word = list.get(i).toLowerCase();
            if(!stopwords.contains(word))
                stopRemovedList.add(word);
        }
        list = stopRemovedList;

        return stopRemovedList;
    }

    /**
     * Filters out the stop words AND drops a word that only consists of numbers. ("3 4 5")
     * @param list
     * @return
     */
    public String[] removeStopWords(String[] list) {
        ArrayList<String> l = new ArrayList<String>();
        for(int i=0; i<list.length; i++) {
            String word = list[i].toLowerCase();
            if(!stopwords.contains(word) && !word.matches("[ 0-9]*"))
            {
                l.add(word);
            }
        }
        String[] newlist = new String[l.size()];
        newlist = l.toArray(newlist);
        return newlist;
    }


}
