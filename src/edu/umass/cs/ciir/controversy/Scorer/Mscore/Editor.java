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

public class Editor {
    public Editor (int inputid, String inputuserName){
        id = inputid;
        userName = inputuserName;
    }

    int id;
    String userName = "";
    ArrayList<Double> dateDouble = new ArrayList<Double>();
    boolean added = false;

    public double getDateDouble (int i){
        return dateDouble.get(i);
    }
    public String getUserName (){
        return userName;
    }

    public void sorting () {
        Collections.sort(dateDouble);
    }

    public void addDate (String inputstamp, Calendar cal) {
        int year=0, month=0, day=0, hour=0, minute=0, second = 0;
        year = Integer.parseInt(inputstamp.toString().substring(0, 4));
        month = Integer.parseInt(inputstamp.toString().substring(5, 7))-1;
        day = Integer.parseInt(inputstamp.toString().substring(8, 10));
        hour = Integer.parseInt(inputstamp.toString().substring(11, 13));
        minute = Integer.parseInt(inputstamp.toString().substring(14, 16));
        second = Integer.parseInt(inputstamp.toString().substring(17, 19));
        cal.set(year, month, day, hour, minute, second);
        dateDouble.add(cal.getTimeInMillis()/1000.0);
    }


    public static Comparator editNrComparator = new Comparator() {
        public int compare(Object editor1, Object editor2) {
            int l1 = ((Editor) editor1).dateDouble.size();
            int l2 = ((Editor) editor2).dateDouble.size();
            if ((l2-l1)>0) return 1;
            else return -1;
        }
    };

    /*public static Comparator EditDateComparator2 = new Comparator() {
        public int compare(Object d1, Object d2) {
            double editDate1 = ((double) d1).dateDouble.get(i);
            double editDate2 = ((double) d2).dateDouble;
            return (int)(editDate1 - editDate2);
        }
    };*/

}
