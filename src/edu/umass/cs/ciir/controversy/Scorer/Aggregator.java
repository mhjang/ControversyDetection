package edu.umass.cs.ciir.controversy.Scorer;

import java.util.HashMap;

/**
 * Created by mhjang on 12/21/15.
 */
public class Aggregator {
    AggregationParameter param;
    public Aggregator(AggregationParameter p) {
        this.param = p;
    }

    public void aggregate(HashMap<String, String> info) {
        Integer isControversial = 0;
        if(param.aggregationMethod == AggregationParameter.AGGREGATION_M) {
            if (Double.parseDouble(info.get("MScore")) >= param.MScoreThreshold)
                isControversial = 1;
        }
        else if(param.aggregationMethod == AggregationParameter.AGGREGATION_C) {
            if (Double.parseDouble(info.get("CScore")) >= param.CScoreThreshold)
                isControversial = 1;
        }
        else if(param.aggregationMethod == AggregationParameter.AGGREGATION_D) {
            if(Double.parseDouble(info.get("DScore")) > 0.5)
                isControversial = 1;
        }
        else if(param.aggregationMethod == AggregationParameter.AGGREGATION_MAJORITY) {
            if(Double.parseDouble(info.get("MScore")) >= param.MScoreThreshold &&
                    Double.parseDouble(info.get("CScore")) >= param.CScoreThreshold)
                isControversial = 1;
        }

        else if(param.aggregationMethod == AggregationParameter.AGGREGATION_OR) {
            if(Double.parseDouble(info.get("MScore")) >= param.MScoreThreshold ||
                    Double.parseDouble(info.get("CScore")) >= param.CScoreThreshold ||
                        Double.parseDouble(info.get("DScore")) >= 0.5)
                isControversial = 1;
        }
        info.put("prediction", isControversial.toString());
    }

}
