package edu.umass.cs.ciir.controversy.data;

/**
 * Created by mhjang on 12/20/15.
 */
public interface DataPath {
    public static String TOPIC_GOLDSTANDARD = "/home/mhjang/controversy_Data/duplicate_score_resolve/";
    public static String GOLDSTANDARD_TOPIC_MIN = TOPIC_GOLDSTANDARD + "min_topic_controversy";
    public static String GOLDSTANDARD_TOPIC_MAX = TOPIC_GOLDSTANDARD + "max_topic_controversy";
    public static String GOLDSTANDARD_TOPIC_AVG = TOPIC_GOLDSTANDARD + "avg_page_controversy";

    public static String GOLDSTANDARD_PAGE_MIN = TOPIC_GOLDSTANDARD + "min_page_controversy";
    public static String GOLDSTANDARD_PAGE_MAX = TOPIC_GOLDSTANDARD + "max_page_controversy";
    public static String GOLDSTANDARD_PAGE_AVG = TOPIC_GOLDSTANDARD + "avg_page_controversy";

    public static String WIKI_GOLDSTANDARD = "/home/mhjang/controversy_Data/duplicate_rating_resolve/";
    public static String GOLDSTANDARD_WIKI_MAX = WIKI_GOLDSTANDARD + "max_rating.tsv";

}
