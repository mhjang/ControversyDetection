package edu.umass.cs.ciir.controversy.experiment;

import edu.umass.cs.ciir.controversy.utils.StrUtil;
import org.lemurproject.galago.core.parse.Document;
import org.lemurproject.galago.core.parse.TagTokenizer;
import org.lemurproject.galago.core.retrieval.Retrieval;
import org.lemurproject.galago.core.retrieval.RetrievalFactory;
import org.lemurproject.galago.core.retrieval.ScoredDocument;
import org.lemurproject.galago.core.retrieval.query.Node;
import org.lemurproject.galago.core.retrieval.query.StructuredQuery;
import org.lemurproject.galago.utility.Parameters;

import java.util.List;


/**
 * Created by mhjang on 1/30/15.
 */
public class WikiRetrieval {
    Parameters p;
    public WikiRetrieval() {
        try {



        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void readByWikiId() throws Exception{
        Document.DocumentComponents dc = new Document.DocumentComponents(true,false,true);
        String jsonConfigFile = "search.params";
        Parameters globalParams = Parameters.parseFile(jsonConfigFile);
        Retrieval retrieval= RetrievalFactory.instance(globalParams);
        Document d = retrieval.getDocument("Model_figure",dc);
        for (int j=0;j<d.terms.size();j++){
            System.out.print(d.terms.get(j) + " ");
        }
    }
    public List<ScoredDocument> runQuery(String query, int requested) {
        try {
            String jsonConfigFile = "search.params";
            query = query.replaceAll("#","");
            query = query.replaceAll("\"","");

         //   System.out.println("query: " + query);
            TagTokenizer tt = new TagTokenizer();
            Document d = tt.tokenize(query);
            Parameters globalParams = Parameters.parseFile(jsonConfigFile);
            Retrieval retrieval= RetrievalFactory.instance(globalParams);

            Parameters p = Parameters.create();
            p.set("startAt", 0);
            p.set("requested", requested);
            p.set("metrics", "map");
            List<ScoredDocument> results = null;

            String tokenizedQuery = StrUtil.join(d.terms, " ");
            Node root = StructuredQuery.parse("#sdm(" + tokenizedQuery + ")");
          //  System.out.println("Query: " + tokenizedQuery);
            Node transformed = retrieval.transformQuery(root, p);
            results = (List<ScoredDocument>) retrieval.executeQuery(transformed, p).scoredDocuments; // issue the query!

        //    for(ScoredDocument sd:results){ // print results
         //       System.out.println(sd.rank+" "+sd.documentName+ " ("+sd.score+")");

         //       Document document = retrieval.getDocument(sd.documentName, new Document.DocumentComponents(true, true, true));


        //    }
            return results;

        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        WikiRetrieval wr = new WikiRetrieval();
     //   wr.runQuery("#combine ( award marley produce 2008 hollywood february marley  announce star bob walk greatest scorsese 2001: rita 11 biopic rock bbc rank roll )");
        wr.readByWikiId();
    }
}
