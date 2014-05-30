import org.junit.Test;

import java.sql.*;

public class JDBCTest {

    @Test
    public void loadCSV() throws Exception {

        Class.forName("org.neo4j.jdbc.Driver");

        Connection con = DriverManager.getConnection("jdbc:neo4j://localhost:7474/");

        try (Statement stmt = con.createStatement()) {
            //delete all existing content
            stmt.execute("match (n) optional match (n)-[r]-() delete n, r");

            //insert some one-time data
            stmt.execute("create (:Project{name:'neo4j'})");

            //some repetitive query as a prepared statement
            final PreparedStatement insertPeople = con.prepareStatement("" +
                    "MATCH (neo4j:Project{name:'neo4j'}) " +
                    "CREATE ({name:{1}})-[:CONTRIBUTES_TO]->(neo4j)");

            //some bulk-data, possibly read from files or other data sources
            String[] names = {"Michael", "Emil"};
            for(String name : names) {
                insertPeople.setString(1, name);
                insertPeople.execute();
            }

            //some read query
            ResultSet rs = stmt.executeQuery("match (n)-[:CONTRIBUTES_TO]->(:Project) return n.name as name");
            while (rs.next()) {
                System.out.println(rs.getString("name"));
            }
        }
    }
}
