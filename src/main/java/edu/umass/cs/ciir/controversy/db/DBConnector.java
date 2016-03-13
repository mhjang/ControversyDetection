package edu.umass.cs.ciir.controversy.db;

/**
 * Created by mhjang on 3/4/16.
 */

import java.sql.*;


public class DBConnector {
    String url = "jdbc:mysql://laguna.cs.umass.edu/datamachine";//127.0.0.7
    String id = "jwpl";
    String pw = "Euj3Kabgen";
    Connection con = null;
    Statement stmt = null;
    boolean chk;
    /**
     *
     */
    public DBConnector(){
        //jdbc driver
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }
        try{
            con = DriverManager.getConnection(url, id, pw);
            stmt = con.createStatement();
            System.out.println("DB connected!");
        }catch(SQLException ex){
            ex.printStackTrace();
            return;
        }
    }

    public PreparedStatement getPreparedStatment(String sql) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }


    /**
     */
    public boolean sendQuery(String query){
        try{
            stmt.executeUpdate(query);
            System.out.println(query);
        }catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public ResultSet getQueryResult(String query){
        ResultSet rs = null;
        try{
            rs = stmt.executeQuery(query);

        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return rs;
    }

    /**
     * close DB connection(Statment, Connection)
     * @return close DBC
     */
    public boolean closeConnection(){
        try{
            stmt.close();
            con.close();
        }catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertValue(String into, String value){
        String sql = "insert into "+into+" values "+value+";";
//		System.out.println(sql);
        try{
            stmt.executeUpdate(sql);
//			System.out.println("data inserted");
        }catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }



}