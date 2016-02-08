package edu.umass.cs.ciir.controversy.experiment;

/**
 * Created by mhjang on 2/8/16.
 *
 *
 *
 id: # of experiment run
 dataset: "clueweb" or "genweb"
 querygen: "all", "tf10", "tile"
 k: integer, 1 - 20. # of k in KNN neighbors
 revised: boolean, are we using revised score or not. True: use, False: not use
 threshold_C: threshold for C Score
 threshold_M: threshold for M Score
 aggregation: "max" or "avg"
 voting: "C", "M", "D", "majority", "AND", "OR", "D_CM"
 */
public class GenerateParams {
    public static void main(String[] args) {
        int i=1;
        String dataset = "clueweb";
        String[] querygen = {"all", "tf10", "tile"};
        int k=1;
        boolean revised = false;

    }
}
