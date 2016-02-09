package edu.umass.cs.ciir.controversy.Scorer;

/**
 * Created by mhjang on 12/21/15.
 */
public class VotingParameter {
    public static int ISOLATION_M = 1;
    public static int ISOLATION_C = 2;
    public static int ISOLATION_D = 3;
    public static int MAJORITY = 4;
    public static int OR = 5;
    public static int D_CM = 6;

    public int MScoreThreshold;
    public double CScoreThreshold;
    public int k;
    public int votingMethod;

    public static class Builder {
        private int MScoreThreshold = 2850000;
        private double CScoreThreshold = 0.0418;
        private int k = 8;
        private int votingMethod = ISOLATION_C;
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

        public Builder setVotingMethod(int voting) {
            this.votingMethod = voting;
            return this;
        }
        public VotingParameter build() {
            return new VotingParameter(this);
        }
    }


    public VotingParameter(Builder builder) {
        this.votingMethod = builder.votingMethod;
        this.MScoreThreshold = builder.MScoreThreshold;
        this.CScoreThreshold = builder.CScoreThreshold;
        this.k = builder.k;

    }

    public void printParameter() {
        System.out.println("MScoreThreshold: " + this.MScoreThreshold + "\t" +
        "CScoreThreshold: " + this.CScoreThreshold + "\t K: " + this.k +
                "\t aggregationMethod: " + this.votingMethod);
    }

}
