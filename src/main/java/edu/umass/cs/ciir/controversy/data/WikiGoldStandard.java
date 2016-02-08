package edu.umass.cs.ciir.controversy.data;

import edu.umass.cs.ciir.controversy.utils.SimpleFileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by mhjang on 12/21/15.
 */
public class WikiGoldStandard {
    HashMap<String, HashSet<WikiPage>> goldstandard;
    public WikiGoldStandard() {
        goldstandard = new HashMap<String, HashSet<WikiPage>>();
        String dir = DataPath.GOLDSTANDARD_WIKI_MAX;
        try {
            SimpleFileReader sr = new SimpleFileReader(dir);
            while (sr.hasMoreLines()) {
                String line = sr.readLine();
       //         System.out.println(line);
                String[] tokens = line.split("\t");
                String queryPage = tokens[0];
                String title = tokens[1];
                int rating = Integer.parseInt(tokens[2]);
                if(!goldstandard.containsKey(queryPage))
                    goldstandard.put(queryPage, new HashSet<WikiPage>());
                goldstandard.get(queryPage).add(new WikiPage(title.toLowerCase(), rating));

            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    class WikiPage {
        String title;
        int rating;

        public WikiPage(String t, int r) {
            this.title = t;
            this.rating = r;
        }
    }

    public ArrayList<String> getRelevantPages(String queryId, double threshold) {
        HashSet<WikiPage> ratedSet = goldstandard.get(queryId);
        ArrayList<String> relevantSet = new ArrayList<String>();
        System.out.println(queryId);
        for(WikiPage wp : ratedSet) {
            if(wp.rating < threshold) {
                relevantSet.add(wp.title);
            }
        }
        return relevantSet;
    }
}
