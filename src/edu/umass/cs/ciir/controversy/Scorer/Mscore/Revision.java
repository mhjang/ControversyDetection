/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rsumi
 */
package edu.umass.cs.ciir.controversy.Scorer.Mscore;//0524 tovabbfejlesztese E/N

import java.util.*;

public class Revision {
    public Revision (int inputId, String inputstamp, Calendar cal, boolean inputisTR, int inputversion, int itagNr, int ilength) {
        id = inputId;
        int year=0, month=0, day=0, hour=0, minute=0, second = 0;
        year = Integer.parseInt(inputstamp.toString().substring(0, 4));
        month = Integer.parseInt(inputstamp.toString().substring(5, 7))-1;
        day = Integer.parseInt(inputstamp.toString().substring(8, 10));
        hour = Integer.parseInt(inputstamp.toString().substring(11, 13));
        minute = Integer.parseInt(inputstamp.toString().substring(14, 16));
        second = Integer.parseInt(inputstamp.toString().substring(17, 19));
        cal.set(year, month, day, hour, minute, second);
        dateDouble = cal.getTimeInMillis()/1000.0;
        isTextRevert = inputisTR;
        dateString = inputstamp;
        version = inputversion;
        tagNr = itagNr;
        length = ilength;
    }

    int id;
    double dateDouble;String dateString;
    int version;
    boolean isTextRevert;
    int tagNr;
    int length;

    public static Comparator EditDateComparator = new Comparator() {
        public int compare(Object e1, Object e2) {
            double editDate1 = ((Revision) e1).dateDouble;
            double editDate2 = ((Revision) e2).dateDouble;
            return (int)(editDate1 - editDate2);
        }
    };
}
