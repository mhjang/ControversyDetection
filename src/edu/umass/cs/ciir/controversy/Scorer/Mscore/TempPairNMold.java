/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author robert
 */
package edu.umass.cs.ciir.controversy.Scorer.Mscore;//0524 tovabbfejlesztese E/N

import java.util.*;

public class TempPairNMold {
    public TempPairNMold (String iname1, String iname2, int id1, int id2){
        names.add(iname1);
        names.add(iname2);
        ids.add(id1);
        ids.add(id2);
    }

    ArrayList<String> names = new ArrayList<String>();
    ArrayList<Integer> ids = new ArrayList<Integer>();
    String article = "";
    int rNr;
    ArrayList<String> dateStrings = new ArrayList<String>();
    ArrayList<Double> dateDoubles = new ArrayList<Double>();
    ArrayList<String> active = new ArrayList<String>();
    boolean mutual = false;
    double rvts = 0;
    int art = 0;

    public static Comparator rvtNrComparator = new Comparator() {
        public int compare(Object pair1, Object pair2) {
            int l1 = ((TempPairNMold) pair1).dateStrings.size();
            int l2 = ((TempPairNMold) pair2).dateStrings.size();
            if ((l2-l1)>0) return 1;
            else return -1;
        }
    };
}
