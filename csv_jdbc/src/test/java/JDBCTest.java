import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCTest {

    @Test
    public void loadCSV() throws Exception {

        Class.forName("org.neo4j.jdbc.Driver");

        Connection con = DriverManager.getConnection("jdbc:neo4j://localhost:7474/");

        try (Statement stmt = con.createStatement()) {
            //delete all content
            stmt.execute("match (n) optional match (n)-[r]-() delete n, r");
            ResultSet rs = stmt.executeQuery("match (n) return count(*) as count");
            while (rs.next()) {
                System.out.println(rs.getString("count"));
            }
        }
    }
}
