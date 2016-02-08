package edu.umass.cs.ciir.controversy.data;

/**
 * Created by mhjang on 12/20/15.
 */
public interface DataPath {

/** Basic Dir **/
	public static String CLUEWEB = "/home/mhjang/controversy_Data/datasets/clueweb/";
	public static String GENWEB = "/home/mhjang/controversy_Data/datasets/generalweb300/";


/*** Queries ****/
	public static String CLUEWEB_QUERY = "queries/cluewebpages/";
	public static String GENWEB_QUERY = "queries/";

/*** KNN neighbors ****/
	public static String ALLQUERY = "retrieval/all.txt";
	public static String TILEQUERY = "retrieval/tile.txt";
	public static String TF10QUERY = "retrieval/tf10.txt";

/*** Resources ****/
	public static String CSCORE = "/home/mhjang/controversy_Data/datasets/resources/CScore_lower.index";
	public static String MSCORE = "/home/mhjang/controversy_Data/datasets/resources/MScore_lower.index";
	public static String DSCORE = "/home/mhjang/controversy_Data/datasets/resources/DScore.txt";
	public static String REVISION_CNT = "/home/mhjang/controversy_Data/datasets/resources/revision_count.index";
	public static String REVISED_CLIQUE_CSCORE = "/home/mhjang/controversy_Data/datasets/resources/clique_revised_CSCORE.index";
	public static String REVISED_PAIR_CSCORE = "/home/mhjang/controversy_Data/datasets/resources/pair_revised_CSCORE.index";
	public static String REVISED_CLIQUE_MSCORE = "/home/mhjang/controversy_Data/datasets/resources/clique_revised_MSCORE.index";
	public static String REVISED_PAIR_MSCORE = "/home/mhjang/controversy_Data/datasets/resources/pair_revised_MSCORE.index";


	/** Judgments ***/
	public static String GOLDSTANDARD = "judgments/avg_rating.txt";
	

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
