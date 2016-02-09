package edu.umass.cs.ciir.controversy.Scorer;

import java.util.HashMap;

/**
 * Created by mhjang on 12/21/15.
 */
public class VotingClassifer {
    VotingParameter param;
    public VotingClassifer(VotingParameter p) {
        this.param = p;
    }


    public void classify(HashMap<String, String> info) {
        boolean mscore = Double.parseDouble(info.get("MScore"))>param.MScoreThreshold?true:false;
        boolean cscore = Double.parseDouble(info.get("CScore"))>param.CScoreThreshold?true:false;
        boolean dscore = Double.parseDouble(info.get("DScore"))>0.5?true:false;


        Integer isControversial = 0;
        if(param.votingMethod == VotingParameter.ISOLATION_M) {
            if (mscore) isControversial = 1;
        }
        else if(param.votingMethod == VotingParameter.ISOLATION_C) {
            if (cscore) isControversial = 1;
        }
        else if(param.votingMethod == VotingParameter.ISOLATION_D) {
            if(dscore) isControversial = 1;
        }
        else if(param.votingMethod == VotingParameter.MAJORITY) {
            int count = 0;
            if(mscore) count++;
            if(cscore) count++;
            if(dscore) count++;
            if(count>=2) isControversial = 1;
        }

        else if(param.votingMethod == VotingParameter.OR) {
            if(mscore || cscore || dscore) isControversial = 1;
        }
        else if(param.votingMethod == VotingParameter.D_CM) {
            if(dscore || (cscore && dscore)) isControversial = 1;
        }
        info.put("prediction", isControversial.toString());
    }


}
