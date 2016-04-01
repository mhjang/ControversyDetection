package edu.umass.cs.ciir.controversy.experiment;

import edu.umass.cs.ciir.controversy.utils.SimpleFileWriter;

import java.io.IOException;

/**
 * Created by mhjang on 2/8/16.
 *
 *
 *id: # of experiment run
 *dataset: "clueweb" or "genweb"
 *querygen: "all", "tf10", "tile"
 *k: integer, 1 - 20. # of k in KNN neighbors
 *revised: boolean, are we using revised score or not. True: use, False: not use
 *threshold_C: threshold for C Score
 *threshold_M: threshold for M Score
 *aggregation: "max" or "avg"
 *voting: "C", "M", "D", "majority", "AND", "OR", "D_CM"
 */
public class GenerateParams {
    public static void main(String[] args) throws IOException {


        /***
         * ID	QueryMethod	Network
         1	All	No
         2	All	Clique
         3	All	Pair
         4	TF10	No
         5	TF10	Clique
         6	TF10	Pair
         7	Tile	No
         8	Tile	Clique
         9	Tile	Pair
         */

        int i=1;
        String dataset = "clueweb";
    //    String[] querygen = {"all", "tf10", "tile"};
        String[] querygen = {"tile"};
        int[] neighborK = {1, 5, 10, 15, 20};
        boolean revised = true;
    //    String[] networkConstruction = {"clique", "pair"};
        String network = "clique";
        String[] aggregation = {"max", "avg"};
        String[] voting = {"C", "M", "D", "Majority", "AND", "D_CM"};
        int mThreshold = 20000;
        String cThreshold = "0.00418";
        String[][] thresholds = {{"20000", "0.00418"}, {"40000", "0.17"}, {"84930","0.00418"}, {"2850000", "0.17"}};
        int runId = 12;
        // revised == false
        int[] wikifier_k = {1, 5};
        for(int k2 : wikifier_k) {
            for (String q : querygen) {
                for (int k : neighborK) {
                    for (String a : aggregation) {
                        for (String v : voting) {
                            for (String[] t : thresholds) {
                                SimpleFileWriter sw = new SimpleFileWriter("/home/mhjang/controversy_Data/datasets/clueweb/experiments/runs/" + runId + "/trainParam/" + i + ".param");
                                //   SimpleFileWriter sw = new SimpleFileWriter("/home/mhjang/controversy_Data/datasets/expresults/params/" + dataset + "/" + runId + "/trainParam/" + i + ".param");
                                StringBuilder sb = new StringBuilder();
                                sb.append("{");
                                sb.append(addJSonLine("id", i, true));
                                sb.append(addJSonLine("dataset", dataset, true));
                                sb.append(addJSonLine("querygen", q, true));
                                sb.append(addJSonLine("k", k, true));
                                sb.append(addJSonLine("querygen2", "wikifier", true));
                                sb.append(addJSonLine("k2", k2, true));
                                sb.append(addJSonLine("revised", revised, true));
                                if (revised)
                                    sb.append(addJSonLine("network", network, true));
                                sb.append(addJSonLine("M_threshold", t[0], true));
                                sb.append(addJSonLine("C_threshold", t[1], true));
                                sb.append(addJSonLine("aggregation", a, true));
                                sb.append(addJSonLine("voting", v, false));
                                sb.append("}");
                                sw.writeLine(sb.toString());
                                sw.close();

                                i++;
                            }
                        }

                    }
                }
            }


/*
        int i=1;
        String dataset = "genweb";
        String[] querygen = {"all", "tf10", "tile"};
        int[] neighborK = {1, 5, 10, 15};
        boolean revised = false;
        String[] networkConstruction = {"clique", "pair"};
        String[] aggregation = {"max", "avg"};
        String[] voting = {"C", "M", "D", "Majority", "AND", "D_CM"};


        // revised == false
        for(int k : neighborK) {
            for(String a: aggregation) {
                for(String v: voting) {
                    for(String q : querygen) {
                        revised = false;
                        SimpleFileWriter sw = new SimpleFileWriter("/home/mhjang/controversy_Data/datasets/expresults/params/" + dataset.charAt(0)  + i + ".param");
                        StringBuilder sb =  new StringBuilder();
                        sb.append("{");
                        sb.append(addJSonLine("id", i, true));
                        sb.append(addJSonLine("dataset",dataset, true));
                        sb.append(addJSonLine("querygen", q, true));
                        sb.append(addJSonLine("k", k, true));
                        sb.append(addJSonLine("revised", revised, true));
                        sb.append(addJSonLine("aggregation", a, true));
                        sb.append(addJSonLine("M_threshold", mThreshold, true));
                        sb.append(addJSonLine("C_threshold", cThreshold, true));
                        sb.append(addJSonLine("voting", v, false));

                        sb.append("}");
                        sw.writeLine(sb.toString());
                        sw.close();
                        i++;
                        revised = true;
                        sw = new SimpleFileWriter("/home/mhjang/controversy_Data/datasets/expresults/params/" + dataset.charAt(0)  + i + ".param");
                        sb =  new StringBuilder();
                        sb.append("{");
                        sb.append(addJSonLine("id", i, true));
                        sb.append(addJSonLine("dataset",dataset, true));
                        sb.append(addJSonLine("querygen", q, true));
                        sb.append(addJSonLine("k", k, true));
                        sb.append(addJSonLine("revised", revised, true));
                        sb.append(addJSonLine("network", "clique", true));
                        sb.append(addJSonLine("aggregation", a, true));
                        sb.append(addJSonLine("M_threshold", mThreshold, true));
                        sb.append(addJSonLine("C_threshold", cThreshold, true));
                        sb.append(addJSonLine("voting", v, false));
                        sb.append("}");
                        sw.writeLine(sb.toString());
                        sw.close();
                        i++;
                        sw = new SimpleFileWriter("/home/mhjang/controversy_Data/datasets/expresults/params/" + dataset.charAt(0)  + i +  ".param");

                        sb =  new StringBuilder();
                        sb.append("{");
                        sb.append(addJSonLine("id", i, true));
                        sb.append(addJSonLine("dataset",dataset, true));
                        sb.append(addJSonLine("querygen", q, true));
                        sb.append(addJSonLine("k", k, true));
                        sb.append(addJSonLine("revised", revised, true));
                        sb.append(addJSonLine("network", "pair", true));
                        sb.append(addJSonLine("aggregation", a, true));
                        sb.append(addJSonLine("M_threshold", mThreshold, true));
                        sb.append(addJSonLine("C_threshold", cThreshold, true));
                        sb.append(addJSonLine("voting", v, false));
                        sb.append("}");
                        sw.writeLine(sb.toString());
                        sw.close();


                    }
                    i++;

                }
            }
        }
        */
        }

    }

    private static String addJSonLine(String field, String key, boolean comma) {
        StringBuilder sb = new StringBuilder();
        sb.append("\""+ field + "\":\"" + key + "\"");
        if(comma) sb.append(",\n");
        else sb.append("\n");
        return sb.toString();
    }


    private static String addJSonLine(String field, int key, boolean comma) {
        StringBuilder sb = new StringBuilder();
        sb.append("\""+ field + "\":\"" + key + "\"");
        if(comma) sb.append(",\n");
        else sb.append("\n");
        return sb.toString();
    }

    private static String addJSonLine(String field, boolean key, boolean comma) {
        StringBuilder sb = new StringBuilder();
        sb.append("\""+ field + "\":"+ key);
        if(comma) sb.append(",\n");
        else sb.append("\n");
        return sb.toString();
    }
}
