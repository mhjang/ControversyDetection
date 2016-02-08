package edu.umass.cs.ciir.controversy.Scorer;

import edu.umass.cs.ciir.controversy.data.AnnotationDatabase;
import edu.umass.cs.ciir.controversy.experiment.Info;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mhjang on 12/18/15.
 */
public class KNNScorer {
    AnnotationDatabase cd;


    /**
     * The default score value to put in to the wikipage that was not annotated
     */
    public KNNScorer(AnnotationDatabase cd_) {
        this.cd = cd_;
    }

    /**
     * compute the aggregated controversy score from annotated wikipedia pages
     * @param wikidocs
     * @return
     */

}
