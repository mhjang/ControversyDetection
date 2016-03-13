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

	public static String CLUEWEB1 = CLUEWEB + "queries/cluewebpages/1/";
//	public static String CLUEWEB_TRAIN1 = "queries/cluewebpages/1/train/";
//	public static String CLUEWEB_TEST1 = "queries/cluewebpages/1/test/";

	public static String CLUEWEB2 =CLUEWEB + "queries/cluewebpages/2/";
//	public static String CLUEWEB_TRAIN2 = "queries/cluewebpages/2/train/";
//	public static String CLUEWEB_TEST2 = "queries/cluewebpages/2/test/";

	public static String CLUEWEB3 =CLUEWEB + "queries/cluewebpages/3/";
//	public static String CLUEWEB_TRAIN3 = "queries/cluewebpages/3/train/";
//	public static String CLUEWEB_TEST3 = "queries/cluewebpages/3/test/";

	public static String CLUEWEB4 =CLUEWEB + "queries/cluewebpages/4/";
//	public static String CLUEWEB_TRAIN4 = "queries/cluewebpages/4/train/";
//	public static String CLUEWEB_TEST4 = "queries/cluewebpages/4/test/";

	public static String CLUEWEB5 = CLUEWEB +"queries/cluewebpages/5/";
//	public static String CLUEWEB_TRAIN5 = "queries/cluewebpages/5/train/";
//	public static String CLUEWEB_TEST5 = "queries/cluewebpages/5/test/";

	public static String GENWEB1 = GENWEB + "queries/1/";
	public static String GENWEB2 = GENWEB + "queries/2/";
	public static String GENWEB3 = GENWEB + "queries/3/";
	public static String GENWEB4 = GENWEB + "queries/4/";
	public static String GENWEB5 = GENWEB + "queries/5/";


	public static String[] CLUEWEB_5FOLD = {CLUEWEB1, CLUEWEB2, CLUEWEB3, CLUEWEB4, CLUEWEB5};
	public static String[] GENWEB_5FOLD = {GENWEB1, GENWEB2, GENWEB3, GENWEB4, GENWEB5};


	/*** KNN neighbors ****/
	public static String ALLQUERY = "retrieval/all.txt";
	public static String TILEQUERY = "retrieval/tile.txt";
	public static String TF10QUERY = "retrieval/tf10.txt";

/*** Resources ****/
	public static String CSCORE = "/home/mhjang/controversy_Data/datasets/resources/CScore_lower.index";
	public static String MSCORE = "/home/mhjang/controversy_Data/datasets/resources/MScore.index";
	public static String DSCORE = "/home/mhjang/controversy_Data/datasets/resources/DScore_lower.index";
	public static String REVISION_CNT = "/home/mhjang/controversy_Data/datasets/resources/revision_count.index";
	public static String REVISED_CLIQUE_CSCORE = "/home/mhjang/controversy_Data/datasets/resources/CScore_clique_2014.index";
	public static String REVISED_PAIR_CSCORE = "/home/mhjang/controversy_Data/datasets/resources/CScore_pair_2014.index";
	public static String REVISED_CLIQUE_MSCORE = "/home/mhjang/controversy_Data/datasets/resources/MScore_clique_2014.index";
	public static String REVISED_PAIR_MSCORE = "/home/mhjang/controversy_Data/datasets/resources/MScore_pair_2014.index";


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
