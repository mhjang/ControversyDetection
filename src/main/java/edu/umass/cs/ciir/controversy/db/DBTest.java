package edu.umass.cs.ciir.controversy.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by mhjang on 3/4/16.
 */
public class DBTest {
    public static void main(String[] args) throws SQLException {
        DBConnector db = new DBConnector();
        ResultSet rs = db.getQueryResult("select * from page_outlinks;");
        while(rs.next()) {
            System.out.println(rs.getInt("id"));

        }

    }
}
