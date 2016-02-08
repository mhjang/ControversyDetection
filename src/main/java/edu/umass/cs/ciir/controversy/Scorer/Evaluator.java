package edu.umass.cs.ciir.controversy.Scorer;

import edu.umass.cs.ciir.controversy.data.DataPath;
import edu.umass.cs.ciir.controversy.experiment.Info;
import edu.umass.cs.ciir.controversy.utils.SimpleFileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by mhjang on 12/20/15.
 */
public class Evaluator {
    /**
     * for the ratings rated by more than one annotator, we will take
     * 1. MAX: less controversial
     * 2. MIN: more controversial
     * 3. Average
     * rating
     */
    public static int MAX_RATING= 1;
    public static int MIN_RATING = 2;
    public static int AVG_RATING = 3;

    HashMap<String, Double> controveryGoldstandard;

    public Evaluator(int scoring, String dir) throws IOException {
        controveryGoldstandard = new HashMap<String, Double>();

        /*
        if(scoring == MAX_RATING) {
            dir = DataPath.GOLDSTANDARD_TOPIC_MAX;
        }
        else if(scoring == MIN_RATING)
            dir = DataPath.GOLDSTANDARD_TOPIC_MIN;
        else
            dir = DataPath.GOLDSTANDARD_TOPIC_AVG;
         */
        SimpleFileReader sr = new SimpleFileReader(dir);
        while(sr.hasMoreLines()) {
            String line  = sr.readLine();
    //        System.out.println(line);
            String[] tokens = line.split("\t");
            controveryGoldstandard.put(tokens[0], Double.parseDouble(tokens[1]));
        }



    }



    /**
     * for oracle experiment
     * @param result
     */
    public void binaryOracleEvaluate(HashMap<String, HashMap<String, String>> result) {
        boolean prediction, truth;
        int correct = 0, wrong = 0;
        int truePositive = 0;
        int precisionDenom = 0, recallDenom = 0;
        System.out.println("querypage \t covered_articles \t covered_avg_score \t covered_max_score \t all_avg_Score \t all_max_score \t result");

        for(String docName : result.keySet()) {
            if (controveryGoldstandard.containsKey(docName)) {
                HashMap<String, String> info = result.get(docName);
            //    if (Integer.parseInt(info.get(Info.COVERED_ARTICLE_NUM)) > 0) {
                    if (Double.parseDouble(info.get(Info.ORACLE_SCORING_MIN)) >= 2.5)
                        prediction = false;
                    else
                        prediction = true;
                    if (controveryGoldstandard.get(docName) >= 2.5)
                        truth = false;
                    else
                        truth = true;
                    if (prediction == true) {
                        precisionDenom++;
                        if (truth == true) {
                            truePositive++;
                        }
                    }
                    if (truth == true)
                        recallDenom++;
                    System.out.println(docName +"\t" + info.get(Info.COVERED_ARTICLE_NUM) + "\t" +
                            info.get(Info.ORACLE_SCORING_MIN) + "\t" + controveryGoldstandard.get(docName) + "\t" + ((prediction==truth)?"Correct":"Wrong"));

        //        }

            }


         /*       System.out.println(docName + "\t" + info.get(Info.COVERED_ARTICLE_NUM) + "\t" +
                        info.get(Info.COVERED_ARTICLE_SCORE_AVG) + "\t" + info.get(Info.COVERED_ARTICLE_SCORE_MAX) + "\t"
                        + info.get(Info.ORACLE_SCORING_AVG) + "\t" + info.get(Info.ORACLE_SCORING_MIN) + "\t" + (prediction == truth));
            */



            }
        double precision = (double) truePositive / (double) precisionDenom;
        double recall = (double) truePositive / (double) recallDenom;
        double f1 = precision * recall * 2 / (precision + recall);
        System.out.println(truePositive + "\t" + precisionDenom + "\t" + recallDenom);
        System.out.println("Precision \t" + precision + "\t Recall \t" + recall + "\t F1-measure \t" + f1);
        }


    private static Comparator<String> ALPHABETICAL_ORDER = new Comparator<String>() {
        public int compare(String str1, String str2) {
            int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
            if (res == 0) {
                res = str1.compareTo(str2);
            }
            return res;
        }
    };


    /**
     * for oracle experiment
     * @param result
     */
    public void binaryAutomaticEvaluate(HashMap<String, HashMap<String, String>> result, AggregationParameter param) {
        boolean prediction, truth;
        int truePositive = 0;
        int precisionDenom = 0, recallDenom = 0;
        System.out.println("querypage \t MScoreMaxPage \t Max_MScore \t Controversial? \t CScoreMaxPage \t Max_CScore \t Controversial? \t # of Rel Docs \t FinalResult \t ground_truth");





        ArrayList<String> pageList = new ArrayList<String>(result.keySet());
        double allTruthBaseLine = 0;
        int allTruthBaselineDenom = 0, allTruthBaseLinePositive = 0;

        int tp=0, tn=0, fp=0, fn=0, p=0;
        int numOfTruth = 0;
        int N = pageList.size();

        Collections.sort(pageList, ALPHABETICAL_ORDER);
        for (String docName : pageList) {
            if (controveryGoldstandard.containsKey(docName)) {
                HashMap<String, String> info = result.get(docName);

                // 1: Controversial   0: Non-controversial
                if (Integer.parseInt(info.get("prediction")) == 0) // 0
                    prediction = false;
                else
                    prediction = true;

                if (controveryGoldstandard.get(docName) >= 2.5)
                    truth = false;
                else {
                    truth = true;
                    numOfTruth++;
                }

                if(prediction) {
                    if(truth) tp++;
                    else fp++;
                }
                else {
                    if(truth) fn++;
                    else tn++;
                }

                double mscore = Double.parseDouble(info.get("MScore"));
                double cscore = Double.parseDouble(info.get("CScore"));

                System.out.println(docName + "\t" + info.get("MScoreMaxPage") + "\t" + mscore + "\t" +
                         info.get("CScoreMaxPage") + "\t" + cscore + "\t" + info.get("number of relevant oracle docs") +"\t" +(prediction?"1":"0")+ "\t"+ (truth?"1":"0") + "\t" + (prediction==truth));


    //                System.out.println(docName + "\t" + info.get("MScoreMaxPage") + "\t" + mscore + "\t" +
     //                       (mscore > param.MScoreThreshold ? "O" : "X") + "\t" + info.get("CScoreMaxPage") + "\t" +
      //                      cscore + "\t" + ((cscore > param.CScoreThreshold) ? "O" : "X") + "\t" + info.get("number of relevant oracle docs") + "\t" + prediction + "\t" + truth);
                }
            }


            double recall = (double)(tp) / (double)(tp + fn);
            double precision = (double)(tp) / (double)(tp + fp);
            double specificity = (double)(tn) / (double)(tn + fn);
            double accuracy = (double)(tp + tn) / (double)(N);
            double f1 = precision * recall * 2 / (precision + recall);
            double f_half = (1 + 0.5*0.5)*(precision * recall) / ((0.5*0.5*precision) + recall);


            double allTruthBaselinePrecision = (double)(numOfTruth) / (double)(N);
            double allTruthBaselineRecall  = (double)(numOfTruth) / (double)(numOfTruth);
            double allTruthBaselineSpecificity = 0.0;
            double allTruthBaselineAccuracy = allTruthBaselinePrecision;
            double allTruthF1 = allTruthBaselinePrecision * allTruthBaselineRecall * 2 / (allTruthBaselinePrecision + allTruthBaselineRecall);
            double allTruthBaselineF_half = (1 + 0.5*0.5)*(allTruthBaselinePrecision * allTruthBaselineRecall) / ((0.5*0.5*allTruthBaselinePrecision) + allTruthBaselineRecall);

        //       System.out.println(truePositive + "\t" + precisionDenom + "\t" + recallDenom);
        System.out.println("# of truth \t" + numOfTruth);
        System.out.println("Prec. \t Recall \t Accuracy \t Specificity \t F1 \t F_0.5");
        System.out.println(precision + "\t" + recall + "\t" + accuracy + "\t" + specificity + "\t" + f1 + "\t" + f_half);
        System.out.println(allTruthBaselinePrecision + "\t" + allTruthBaselineRecall + "\t" + allTruthBaselineSpecificity + "\t" + allTruthBaselineAccuracy + "\t" + allTruthF1 + "\t" + allTruthBaselineF_half);


    }

    }

