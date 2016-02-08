package edu.umass.cs.ciir.controversy.Scorer.Mscore;//0524 tovabbfejlesztese E/N
import java.io.*;
import java.util.*;
import java.text.*;


public class Mt1026{
    public static void main(String args[]) {

        DecimalFormat tizedes = new DecimalFormat("#0.000000");//0.00
        DecimalFormat noDec = new DecimalFormat("0");

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        int year=0, month=0, day=0, hour=0, minute=0, second = 0;
        year = 2001;
        month = 0;
        day = 15;
        hour = 0;
        minute = 0;
        second = 0;
        cal.set(year, month, day, hour, minute, second);
        double startDouble = cal.getTimeInMillis()/1000.0;
        year = 2001;
        month = 0;
        day = 16;
        hour = 0;
        minute = 0;
        second = 0;
        cal.set(year, month, day, hour, minute, second);
        double afterStart = cal.getTimeInMillis()/1000.0;
        double dayDouble = afterStart - startDouble;
        year = 2010;
        month = 0;
        day = 30;
        hour = 0;
        minute = 0;
        second = 0;
        cal.set(year, month, day, hour, minute, second);
        double tendDouble = cal.getTimeInMillis()/1000.0;
        int dayNr = 0;
        String filename="";
        for (double t = startDouble; t < tendDouble; t = t + dayDouble) {
            dayNr++;
        }
        dayNr--;
        double endDouble = startDouble+dayNr*dayDouble;
        System.out.println(startDouble+"\t"+endDouble+"\t"+dayDouble);
        String ddaatteeS="";

        String s = "";
        int firstIndex;
        int secondIndex;
        int thirdIndex;
        int fourthIndex;
        int fifthIndex;
        int isrev;
        String cim="";
        String cim11="";
        int atagNr;
        int length;
        int version;
        String uname="";
        String dS = "";
        double firstDateDouble = 0;
        double MFinal = 0;
        File file = new File("");
        String path = file.getAbsolutePath();
        ArrayList<Double> ttt = new ArrayList<Double>();
        ArrayList<Double> MMM = new ArrayList<Double>();

        try{
            FileReader fr1 = new FileReader("enbot.txt");
            BufferedReader br1 = new BufferedReader(fr1);
            while((s = br1.readLine()) != null) {
                if (bots.indexOf(s) == -1)
                    bots.add(s);
            }
        }catch (IOException e) {
            System.out.println("IO Exception bot");
        }

        try{
            FileReader fr1 = new FileReader("legujabbcimek.txt");
            BufferedReader br1 = new BufferedReader(fr1);
            while((s = br1.readLine()) != null) {
                //cim = s.replace( ' ', '_' );
                cimlista.add(s);
            }
        }catch (IOException e) {
            System.out.println("IO Exception bot");
        }

        try{
            FileReader fr1 = new FileReader("enwiki.txt");
            BufferedReader br1 = new BufferedReader(fr1);
            while((s = br1.readLine()) != null) {
                if (s.indexOf("^^^ ")==0) {
//System.out.println(s);
                    //beolvas
                    firstIndex =  s.indexOf(" && ");
                    secondIndex =  s.indexOf(" && ", firstIndex+4);
                    thirdIndex =  s.indexOf(" && ", secondIndex+4);
                    fourthIndex =  s.indexOf(" && ", thirdIndex+4);
                    fifthIndex =  s.indexOf(" && ", fourthIndex+4);
                    dS = s.substring(4,firstIndex);
                    isrev = Integer.parseInt(s.substring(firstIndex+4,secondIndex));
                    version = Integer.parseInt(s.substring(secondIndex+4,thirdIndex));
                    uname = s.substring(thirdIndex+4, fourthIndex);
                    atagNr = Integer.parseInt(s.substring(fourthIndex+4,fifthIndex));
                    length = (int)Double.parseDouble(s.substring(fifthIndex+4));

                    //if (bots.indexOf(uname)==-1){
                    id = users.indexOf(uname);
                    if (id == -1) {
                        users.add(uname);
                        id = users.size()-1;
                        editor.add(new Editor(id, uname));
                    }
                    revisions.add(new Revision(id, dS, cal, (isrev==1), version, atagNr, length));
                    editor.get(id).addDate(dS, cal);
                    //}
                }
                else {
                    //szamol
                    //cim11 = s.replace( ' ', '_' );//System.out.println(cim);
                    //cim = cim11.replace( '/', '_' );
                    //cim = s.replace( ' ', '_' );
                    cim = s;
                    double measure = 0;
                    int revSize = revisions.size();
                    int inNr=0;
                    firstDateDouble = revisions.get(0).dateDouble;

                    //az eredeti valtozatokat is revertnek veszem
	/*for (int i = 0; i < revisions.size(); i++) {
		if (revisions.get(i).isTextRevert){
			for (int j = 0; j < i; j++) {
				if (revisions.get(i).version==revisions.get(j).version)
					revisions.get(j).isTextRevert = true;
			}
		}
        }*/

                    //a bank paget is szamolom
                    for (int i = 0; i < revisions.size(); i++) {
                        if (revisions.get(i).version==-1)
                            revisions.get(i).isTextRevert=true;
                    }

	/*for (int i = 0; i < revisions.size(); i++) {
		if (!revisions.get(i).isTextRevert){
			revisions.remove(i);
			i--;
		}
        }*/

                    int tempPairIndex;
                    boolean vitas = false;
                    if (revisions.size()>1){
                        //kikeresem a legaktivabbakat, ehhez letrehozom a parokat
                        for (int i = 1; i < revisions.size(); i++) {
                            for (int j=(i-1); j >=0; j--) {
                                //for (int j=0; j < i; j++) {
                                if (revisions.get(i).version==revisions.get(j).version){
                                    if ((!editor.get(revisions.get(i).id).userName.equalsIgnoreCase(editor.get(revisions.get(j+1).id).userName))) {
                                        tempPairIndex = -1;
                                        for (int k = 0; k < tempPairs.size(); k++) {
                                            if ( (tempPairs.get(k).names.indexOf(editor.get(revisions.get(i).id).userName)!=-1)&&
                                                    (tempPairs.get(k).names.indexOf(editor.get(revisions.get(j+1).id).userName)!=-1) ){
                                                tempPairIndex = k;
                                                break;
                                            }
                                        }
                                        if (tempPairIndex == -1) {
                                            tempPairs.add(new TempPairNMold (editor.get(revisions.get(i).id).userName, editor.get(revisions.get(j+1).id).userName, revisions.get(i).id, revisions.get(j+1).id));
                                            tempPairs.get(tempPairs.size()-1).article=cim;
                                            tempPairs.get(tempPairs.size()-1).rNr = 1;
                                            tempPairs.get(tempPairs.size()-1).dateStrings.add(revisions.get(i).dateString);
                                            tempPairs.get(tempPairs.size()-1).dateDoubles.add(revisions.get(i).dateDouble);
                                            tempPairs.get(tempPairs.size()-1).active.add(editor.get(revisions.get(i).id).userName);
                                            //System.out.println(tempPairs.get(tempPairs.size()-1).article+" && "+revisions.get(i).dateString+" && "+tempPairs.get(tempPairs.size()-1).names.get(0)+" && "+tempPairs.get(tempPairs.size()-1).names.get(1)+" && "+editor.get(revisions.get(i).id).userName);
                                        } else {
                                            tempPairs.get(tempPairIndex).rNr++;
                                            tempPairs.get(tempPairIndex).dateStrings.add(revisions.get(i).dateString);
                                            tempPairs.get(tempPairIndex).dateDoubles.add(revisions.get(i).dateDouble);
                                            tempPairs.get(tempPairIndex).active.add(editor.get(revisions.get(i).id).userName);
                                            //System.out.println(tempPairs.get(tempPairIndex).article+" && "+revisions.get(i).dateString+" && "+tempPairs.get(tempPairIndex).names.get(0)+" && "+tempPairs.get(tempPairIndex).names.get(1)+" && "+editor.get(revisions.get(i).id).userName);
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        ArrayList<Integer> invUsers = new ArrayList<Integer>();//user idkat tartalmaz, nem ismetlodve
                        //kiszamolom a vegso M-et
                        MFinal = 0;
                        //ellenorzom h kolcsonos legyen a revert
                        for (int i = 0; i < tempPairs.size(); i++) {
                            if ( (tempPairs.get(i).active.indexOf(tempPairs.get(i).names.get(0))==-1) || (tempPairs.get(i).active.indexOf(tempPairs.get(i).names.get(1))==-1) ) {
                                //System.out.println("-- "+tempPairs.get(i).dateStrings.get(tempPairs.get(i).dateStrings.size()-1));
                                tempPairs.remove(i);
                                i--;
                            }
                        }
                        //endof //ellenorzom h kolcsonos legyen a revert
                        if (tempPairs.size()>0){
                            Collections.sort(tempPairs, TempPairNMold.rvtNrComparator);
                            //System.out.println(tempPairs.get(0).names.get(0)+" "+tempPairs.get(0).names.get(1));
                        }
                        //revertek keresese

                        //for (int i = 1; i < tempPairs.size(); i++) {//a ket legaktivabb kimarad
                        for (int i = 0; i < tempPairs.size(); i++) {//a ket legaktivabb nem marad ki
                            if (invUsers.indexOf(tempPairs.get(i).ids.get(0))==-1)
                                invUsers.add(tempPairs.get(i).ids.get(0));
                            if (invUsers.indexOf(tempPairs.get(i).ids.get(1))==-1)
                                invUsers.add(tempPairs.get(i).ids.get(1));
                            if (editor.get(tempPairs.get(i).ids.get(0)).dateDouble.size()<editor.get(tempPairs.get(i).ids.get(1)).dateDouble.size())
                                MFinal = MFinal + editor.get(tempPairs.get(i).ids.get(0)).dateDouble.size()*tempPairs.get(i).dateDoubles.size();
                            else
                                MFinal = MFinal + editor.get(tempPairs.get(i).ids.get(1)).dateDouble.size()*tempPairs.get(i).dateDoubles.size();
                        }
                        MFinal = MFinal * invUsers.size();
                        invUsers.clear();
//if (cimlista.indexOf(cim)!=-1) {
                        if ((MFinal > 1000000)&&(MFinal < 10000000)){
//System.out.println(cim);
                            double t;
                            String fn ="";
                            s=cim;
                            cim11 = s.replace( ' ', '_' );
                            cim=cim11.replace( '/', '_' );
                            fn = "/big1/robi/nmt/1026/"+cim+".txt";
                            fwMMM = new FileWriter(fn);
                            bwMMM = new BufferedWriter(fwMMM);
                            pwMMM = new PrintWriter(bwMMM);
                            double[] MM = new double [6];
                            int N;

                            for (int nnn=(revisions.size()-1); nnn>=0 ; nnn--) {
                                t = revisions.get(nnn).dateDouble;
                                //if ((firstDateDouble-t)<dayDouble) {
                                //csokkentjuk a szerkesztesek szamat
                                for (int i = 0; i < tempPairs.size(); i++) {
                                    while (tempPairs.get(i).dateDoubles.get(tempPairs.get(i).dateDoubles.size()-1) > t) {
                                        tempPairs.get(i).dateDoubles.remove(tempPairs.get(i).dateDoubles.size()-1);//System.out.println("- "+tempPairs.get(i).dateStrings.get(tempPairs.get(i).dateStrings.size()-1));
                                        tempPairs.get(i).dateStrings.remove(tempPairs.get(i).dateStrings.size()-1);
                                        tempPairs.get(i).active.remove(tempPairs.get(i).active.size()-1);
                                        if (tempPairs.get(i).dateDoubles.size()==0){
                                            tempPairs.remove(i);
                                            i--;
                                            break;
                                        }
                                    }
                                }
                                //ellenorzom h kolcsonos legyen a revert
                                for (int i = 0; i < tempPairs.size(); i++) {
                                    if ( (tempPairs.get(i).active.indexOf(tempPairs.get(i).names.get(0))==-1) || (tempPairs.get(i).active.indexOf(tempPairs.get(i).names.get(1))==-1) ) {
                                        //System.out.println("-- "+tempPairs.get(i).dateStrings.get(tempPairs.get(i).dateStrings.size()-1));
                                        tempPairs.remove(i);
                                        i--;
                                    }
                                }
                                //endof //ellenorzom h kolcsonos legyen a revert
	/*for (int i = 0; i < tempPairs.size(); i++) {
		for (int j = 0; j < tempPairs.get(i).dateDoubles.size(); j++)
			System.out.println("temppairmaradt "+tempPairs.get(i).dateDoubles.get(j));
	}*/

                                N = editor.size();
                                for (int i = 0; i < editor.size(); i++) {
                                    if (editor.get(i).dateDouble.size()>0){
                                        while (editor.get(i).dateDouble.get(editor.get(i).dateDouble.size()-1) > t) {
                                            editor.get(i).dateDouble.remove(editor.get(i).dateDouble.size()-1);
                                            if (editor.get(i).dateDouble.size()==0){//System.out.println("--- "+editor.get(i).id);
                                                //editor.remove(i);
                                                N--;
                                                i--;
                                                break;
                                            }
                                        }
                                    }
                                    else
                                        N--;
                                }

                                if (revisions.size()>0){//??
                                    while (revisions.get(revisions.size()-1).dateDouble > t) {
                                        revisions.remove(revisions.size()-1);
                                        if (revisions.size()==0)
                                            break;
                                    }
                                }
                                //end of csokkentjuk a szerkesztesek szamat
                                for (int i = 0; i < 6; i++) {
                                    MM[i]=0;
                                }
                                if (tempPairs.size()>0){
                                    Collections.sort(tempPairs, TempPairNMold.rvtNrComparator);
                                    //System.out.println(tempPairs.get(0).names.get(0)+" "+tempPairs.get(0).names.get(1));
                                }
                                //revertek keresese
                                //ArrayList<Integer> invUsers = new ArrayList<Integer>();//user idkat tartalmaz, nem ismetlodve
                                //System.out.println(editor.size());
                                //for (int i = 1; i < tempPairs.size(); i++) {//a ket legaktivabb kimarad
                                for (int i = 0; i < tempPairs.size(); i++) {//a ket legaktivabb nem marad ki
                                    if (invUsers.indexOf(tempPairs.get(i).ids.get(0))==-1)
                                        invUsers.add(tempPairs.get(i).ids.get(0));
                                    if (invUsers.indexOf(tempPairs.get(i).ids.get(1))==-1)
                                        invUsers.add(tempPairs.get(i).ids.get(1));
                                    if (editor.get(tempPairs.get(i).ids.get(0)).dateDouble.size()<editor.get(tempPairs.get(i).ids.get(1)).dateDouble.size()){
                                        MM[5] = MM[5] + editor.get(tempPairs.get(i).ids.get(0)).dateDouble.size()*tempPairs.get(i).dateDoubles.size();
                                        if (i<5)
                                            MM[i] = MM[i] + editor.get(tempPairs.get(i).ids.get(0)).dateDouble.size()*tempPairs.get(i).dateDoubles.size();
                                    }
                                    else{
                                        MM[5] = MM[5] + editor.get(tempPairs.get(i).ids.get(1)).dateDouble.size()*tempPairs.get(i).dateDoubles.size();
                                        if (i<5)
                                            MM[i] = MM[i] + editor.get(tempPairs.get(i).ids.get(1)).dateDouble.size()*tempPairs.get(i).dateDoubles.size();
                                    }
                                }
                                //MM = MM * invUsers.size();
                                if (MM[5]>0){
                                    pwMMM.println(t+"\t"+nnn+"\t"+invUsers.size()+"\t"+N+"\t"+MM[5]+"\t"+MM[0]+"\t"+MM[1]+"\t"+MM[2]+"\t"+MM[3]+"\t"+MM[4]);
                                }
                                invUsers.clear();
                            }
                            pwMMM.close();
                        }//endof MFinal>

                        revisions.clear();
                        users.clear();
                        editor.clear();
                        tempPairs.clear();
                    }
                    else {
                        revisions.clear();
                    }
                }
            }
        }catch (IOException e) {
            System.out.println("IO Exception enwiki");
        }
    }//main end

    static ArrayList<Revision> revisions = new ArrayList<Revision>();
    static ArrayList<String> users = new ArrayList<String>();
    static int id;
    static ArrayList<Editor> editor = new ArrayList<Editor>();
    static ArrayList<String> cimlista = new ArrayList<String>();
    static String cim = "";
    NumberFormat tizedes = new DecimalFormat("#0.0000000");//0.00
    static FileWriter fw3;
    static BufferedWriter bw3;
    static PrintWriter pw3;
    static ArrayList<String> bots = new ArrayList<String>();
    static ArrayList<TempPairNMold> tempPairs = new ArrayList<TempPairNMold>();
    static FileWriter fwMMM;
    static BufferedWriter bwMMM;
    static PrintWriter pwMMM;
}
