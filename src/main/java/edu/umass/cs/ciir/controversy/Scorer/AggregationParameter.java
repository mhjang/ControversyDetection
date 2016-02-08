package edu.umass.cs.ciir.controversy.Scorer;

/**
 * Created by mhjang on 12/21/15.
 */
public class AggregationParameter {
    public static int AGGREGATION_M = 1;
    public static int AGGREGATION_C = 2;
    public static int AGGREGATION_D = 3;
    public static int AGGREGATION_MAJORITY = 4;
    public static int AGGREGATIoN_AND = 5;
    public static int AGGREGATION_D_OVER_CM = 6;
    public static int AGGREGATION_OR = 7;

    public int MScoreThreshold;
    public double CScoreThreshold;
    public int k;
    public int votingMethod;
    public int aggregationMethod;

    public static class Builder {
        private int MScoreThreshold = 2850000;
        private double CScoreThreshold = 0.0418;
        private int k = 8;
        private int aggregationMethod = AGGREGATION_M;

        public Builder setMThreshold(int threshold) {
            this.MScoreThreshold = threshold;
            return this;
        }

        public Builder setCScoreThreshold(double threshold) {
            this.CScoreThreshold = threshold;
            return this;
        }

        public Builder setKNeighbors(int k_) {
            this.k = k_;
            return this;
        }

        public Builder setAggregationMethod(int aggregation) {
            this.aggregationMethod = aggregation;
            return this;
        }
        public AggregationParameter build() {
            return new AggregationParameter(this);
        }
    }


    public AggregationParameter(Builder builder) {
        this.aggregationMethod = builder.aggregationMethod;
        this.MScoreThreshold = builder.MScoreThreshold;
        this.CScoreThreshold = builder.CScoreThreshold;
        this.k = builder.k;

    }

    public void printParameter() {
        System.out.println("MScoreThreshold: " + this.MScoreThreshold + "\t" +
        "CScoreThreshold: " + this.CScoreThreshold + "\t K: " + this.k +
                "\t aggregationMethod: " + this.aggregationMethod);
    }

}
