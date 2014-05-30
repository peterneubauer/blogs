package org.neo4j.jdbctest;

import org.junit.Test;
import org.neo4j.helpers.collection.MapUtil;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class JDBCTest {

    @Test
    public void loadCSV() throws Exception {

        //load the Neo4j JDBC driver class
        Class.forName("org.neo4j.jdbc.Driver");

        //connect to a vanilla default local neo4j server
        Connection con = DriverManager.getConnection("jdbc:neo4j://localhost:7474/");

        try {
            Statement stmt = con.createStatement();
            //delete all existing content
            stmt.execute("match (n) optional match (n)-[r]-() delete n, r");

            //the property map for the "neo4j" node
            final Map neo4jProperties = new HashMap();
            neo4jProperties.put("name", "neo4j");
            neo4jProperties.put("inception", 2007);

            //insert some one-time data
            final PreparedStatement insertNeo4j = con.prepareStatement("create (:Project {1})");
            insertNeo4j.setObject(1, neo4jProperties);
            insertNeo4j.execute();

            //some repetitive query as a prepared statement reusing the neo4j values in the parameters map
            final PreparedStatement insertPeople = con.prepareStatement("" +
                    "MATCH (neo4j:Project{name:{0}.name}) " +
                    "CREATE ({name:{1}})-[:CONTRIBUTES_TO]->(neo4j)");
            insertPeople.setObject(0, neo4jProperties);
            //some bulk-data, possibly read from files or other data sources
            String[] names = {"Michael", "Emil"};
            for (String name : names) {
                insertPeople.setString(1, name);
                insertPeople.execute();
            }

            //some read query
            ResultSet rs = stmt.executeQuery("match (n)-[:CONTRIBUTES_TO]->(:Project) return n.name as name");
            int count = 0;
            while (rs.next()) {
                System.out.println(rs.getString("name"));
                count++;
            }

            assertEquals(2, count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
