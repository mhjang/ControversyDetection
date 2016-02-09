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
                        SimpleFileWriter sw = new SimpleFileWriter("/home/mhjang/controversy_Data/datasets/expresults/params/" + dataset.charAt(0)  + i +  "_" + q + ".param");
                        StringBuilder sb =  new StringBuilder();
                        sb.append("{");
                        sb.append(addJSonLine("id", i, true));
                        sb.append(addJSonLine("dataset",dataset, true));
                        sb.append(addJSonLine("querygen", q, true));
                        sb.append(addJSonLine("k", k, true));
                        sb.append(addJSonLine("revised", revised, true));
                        sb.append(addJSonLine("aggregation", a, true));
                        sb.append(addJSonLine("voting", v, false));
                        sb.append("}");
                        sw.writeLine(sb.toString());
                        sw.close();
                        revised = true;
                        sw = new SimpleFileWriter("/home/mhjang/controversy_Data/datasets/expresults/params/" + dataset.charAt(0) + i +  "_" + q +  "_clique_revised.param");
                        sb =  new StringBuilder();
                        sb.append("{");
                        sb.append(addJSonLine("id", i, true));
                        sb.append(addJSonLine("dataset",dataset, true));
                        sb.append(addJSonLine("querygen", q, true));
                        sb.append(addJSonLine("k", k, true));
                        sb.append(addJSonLine("revised", revised, true));
                        sb.append(addJSonLine("network", "clique", true));
                        sb.append(addJSonLine("aggregation", a, true));
                        sb.append(addJSonLine("voting", v, false));
                        sb.append("}");
                        sw.writeLine(sb.toString());
                        sw.close();

                        sw = new SimpleFileWriter("/home/mhjang/controversy_Data/datasets/expresults/params/" + dataset.charAt(0) + i +  "_" + q +  "_pair_revised.param");
                        sb =  new StringBuilder();
                        sb.append("{");
                        sb.append(addJSonLine("id", i, true));
                        sb.append(addJSonLine("dataset",dataset, true));
                        sb.append(addJSonLine("querygen", q, true));
                        sb.append(addJSonLine("k", k, true));
                        sb.append(addJSonLine("revised", revised, true));
                        sb.append(addJSonLine("network", "pair", true));
                        sb.append(addJSonLine("aggregation", a, true));
                        sb.append(addJSonLine("voting", v, false));
                        sb.append("}");
                        sw.writeLine(sb.toString());
                        sw.close();


                    }
                    i++;

                }
            }
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
