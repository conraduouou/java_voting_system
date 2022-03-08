import java.sql.*;

public class VotingDatabaseCreate {

    // Change according to machine needs
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String LOCAL_URL = "jdbc:mysql://localhost:3306/";
    static final String DB_URL = "jdbc:mysql://localhost:3306/elections";
    static final String USER = "root";
    static final String PASS = "iloveGod";

    public static void main(String[] args) {
        Connection conn = null;
        Statement statement = null;
        PreparedStatement pStatement = null;

        try {
            Class.forName(JDBC_DRIVER);

            // !!!!!! IMPORTANT !!!!!!!
            // Steps to create database and tables
            //
            // 1. Uncomment the following lines of code and run each method ONE AT A TIME,
            // NOT in one go.
            //
            // That means running this file one by one until all methods are run.

            // createDatabase(conn, statement);
            createTables(conn, statement, pStatement);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
            }

            try {
                if (pStatement != null) {
                    pStatement.close();
                }
            } catch (Exception e) {
            }

            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Here's the error\n");
                e.printStackTrace();
            }
        }
    }

    private static void createDatabase(Connection con, Statement stmt) throws SQLException {
        con = DriverManager.getConnection(LOCAL_URL, USER, PASS);
        System.out.println("Connected to database.");
        stmt = con.createStatement();
        stmt.executeUpdate("CREATE DATABASE elections");
        System.out.println("Database created successfully.");
    }

    private static void createTables(Connection con, Statement stmt, PreparedStatement pStmt) throws SQLException {
        con = DriverManager.getConnection(DB_URL, USER, PASS);
        System.out.println("Connected to elections database.");
        stmt = con.createStatement();
        stmt.executeUpdate("CREATE TABLE users " +
                "(id VARCHAR(60) not NULL, " +
                "username VARCHAR(25) not NULL, " +
                "hash VARCHAR(60) not NULL, " +
                "type VARCHAR(25) DEFAULT 'Voter')");
        System.out.println("USERS table created.");
        stmt.executeUpdate("CREATE TABLE candidates " +
                "(id VARCHAR(60) not NULL, " +
                "candidateName VARCHAR(25) not NULL, " +
                "candidateLast VARCHAR(25) not NULL, " +
                "candidatePos VARCHAR(25) not NULL)");
        System.out.println("CANDIDATES table created.");
        stmt.executeUpdate("CREATE TABLE votes " +
                "(id VARCHAR(60) not NULL," +
                "userId VARCHAR(60) not NULL, " +
                "candidate VARCHAR(25) not NULL, " +
                "candidatePos VARCHAR(25) not NULL, " +
                "candidateId VARCHAR(60) not NULL, " +
                "dateVoted DATE not NULL)");
        System.out.println("VOTES table created.");

        PasswordAuthentication auth = new PasswordAuthentication();

        String user = "root";
        String pass = "pass";

        String hash = auth.hash(pass.toCharArray());
        String id = auth.hash((user + " id").toCharArray());

        pStmt = con.prepareStatement("INSERT INTO users (id, username, hash, type) VALUES (?, ?, ?, ?)");
        pStmt.setString(1, id);
        pStmt.setString(2, user);
        pStmt.setString(3, hash);
        pStmt.setString(4, "Admin");

        pStmt.executeUpdate();

        for (int i = 1; i <= 4; i++) {
            String candidate = "Candidate " + i;
            String candidateId = auth.hash((candidate + " id").toCharArray());

            pStmt = con.prepareStatement(
                    "INSERT INTO candidates (id, candidateName, candidateLast, candidatePos) VALUES (?, ?, ?, ?)");
            pStmt.setString(1, candidateId);
            pStmt.setString(2, "Candidate");
            pStmt.setString(3, Integer.toString(i));
            pStmt.setString(4, "President");

            pStmt.executeUpdate();
        }
    }
}
