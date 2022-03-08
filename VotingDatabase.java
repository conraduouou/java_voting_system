import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VotingDatabase {

    // Change according to machine needs
    // Must include
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/elections";
    static final String USER = "root";
    static final String PASS = "iloveGod";

    /**
     * Must be included
     */
    String activeUsername, activeUserId;

    /**
     * Should correspond to each other. Or if you're more comfortable with
     * vectors, feel free to use them.
     */
    String[] candidates;
    String[] candidateIds;

    /**
     * Must be included and be global in the class file. This is to ensure that
     * there will be no data leaks in closing the program. Refer to my
     * DatabaseConn.java file for closing these variables/references using
     * a WindowListener.
     */
    Connection connection = null;
    PreparedStatement statement = null;

    public static void main(String[] args) {
        Date date = new Date();

        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(date));
    }

    /**
     * Returns true or false depending whether the username inputted has a match in
     * the database and the generated hash from the database matches the inputted
     * password.
     * 
     * Depends on the PasswordAuthentication.java for authentication purposes. This
     * is to enhance security by not having plaintext passwords in the database,
     * which is a bad practice in maintaining a server.
     * 
     * Takes two arguments: the username and the password.
     * 
     * Should be inside a try-catch block.
     * 
     * If you have followed my code in having the global connection variable
     * establish a connection upon Window startup, the statement should
     * be working as intended.
     * 
     * Use upon pressing login button.
     * 
     * 
     * @return
     *         Returns true if username is both found and password matches the hash
     *         in the database. Proceed when returned true, show a dialog when
     *         false.
     */
    private int logIn(String user, String pass) throws SQLException {
		PasswordAuthentication auth = new PasswordAuthentication();

		statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
		statement.setString(1, user);

		ResultSet result = statement.executeQuery();

		if (!result.next()) {
			return 3;
		} else {
			do {
				String token = result.getString("hash");
				String type = result.getString("type").toLowerCase();
				boolean isMatch = auth.authenticate(pass.toCharArray(), token);

				if (isMatch) {
					activeUsername = result.getString("username");
					activeUserId = result.getString("id");

					if (type.compareTo("voter") == 0) {
						return 2;
					} else if (type.compareTo("admin") == 0) {
						return 1;
					}
				}
				
			} while (result.next());

			return 3;
		}
	}

    /**
     * Returns false if there's either a user with an existing username or there
     * was an error.
     * 
     * Use this method when upon pressing the register button.
     * 
     * @param user
     *                The user name
     * 
     * @param pass
     *                The password
     * 
     * @param isAdmin
     *                A boolean specifying what radio button is activated--admin or
     *                voter
     * @return
     *         Returns false upon error
     * 
     * @throws SQLException
     *                      Must therefore be wrapped in a try-catch block
     */
    private boolean register(String user, String pass, boolean isAdmin) throws SQLException {
        PasswordAuthentication auth = new PasswordAuthentication();

        statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
        statement.setString(1, user);

        ResultSet result = statement.executeQuery();

        if (result.next()) {
            return false;
        }

        statement = connection.prepareStatement("INSERT INTO users (id, username, hash, type) VALUES (?, ?, ?, ?)");

        String userId = auth.hash((user + " id").toCharArray());
        String hash = auth.hash(pass.toCharArray());

        statement.setString(1, userId);
        statement.setString(2, user);
        statement.setString(3, hash);
        statement.setString(4, isAdmin ? "Admin" : "Voter");

        statement.executeUpdate();

        return true;
    }

    /**
     * Use upon pressing view results button in the admin screen. Should work
     * fine and return data that consists of two columns namely the
     * 
     * @return
     *         Returns a ResultSet object that contains the data of the candidates'
     *         votes ordered by most to least.
     * @throws SQLException
     */
    private ResultSet showResults() throws SQLException {
        statement = connection.prepareStatement(
                "SELECT candidateName AS ?, COUNT(candidateId) AS ? FROM votes ORDER BY COUNT(candidateId) DESC");
        statement.setString(1, "Candidate Name");
        statement.setString(2, "Number of Votes");

        return statement.executeQuery();
    }

    /**
     * Does the following things:
     * 
     * 1) Gets data of candidate chosen through passed candidateId;
     * 2) Gets user id through passed username;
     * 3) Handles all that values and passed it into the INSERT query.
     * 
     * Considering that the candidateId is needed, the IDs need to be recorded
     * upon login. Probably store them in an array. Should also then be global in
     * order to be accessed.
     * 
     * And if you're going to store the candidates' IDs anyway, why not store the
     * userID as well in order to not make the connection do an unnecessary search?
     *  
     * Use upon press of submit vote button.
     * 
     * @param user
     *                    The user
     * @param candidateId
     *                    The id of candidate chosen.
     * @return
     *         Returns true if there were no errors--false if there were.
     * @throws SQLException
     */
    private boolean voteCandidate(String user, String userId, String candidateId) throws SQLException {
        PasswordAuthentication auth = new PasswordAuthentication();

        String candidateName, candidatePos, voteId;
        ResultSet result;
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        statement = connection.prepareStatement("SELECT * FROM candidates WHERE id = ?");
        statement.setString(1, candidateId);

        result = statement.executeQuery();

        if (!result.next()) {
            return false;
        }

        candidateName = result.getString("candidateName");
        candidatePos = result.getString("candidatePos");
        voteId = auth.hash((user + candidateName).toCharArray());

        statement = connection.prepareStatement("INSERT INTO votes (id, userId, candidate, " +
                "candidatePos, candidateId, dateVoted) VALUES (?, ?, ?, ?, ?, ?)");

        statement.setString(1, voteId);
        statement.setString(2, userId);
        statement.setString(3, candidateName);
        statement.setString(4, candidatePos);
        statement.setString(5, candidateId);
        statement.setString(6, date);

        statement.executeUpdate();
        result.close();

        return true;
    }

    /**
     * Must be included. The code that entails login process and actually collects
     * the candidates to view. It's the id you'll be getting from this data that 
     * you'll be passing to the vote process in order to actually simulate the vote.
     * 
     * @throws SQLException
     */
    private void collectCandidates() throws SQLException {
        statement = connection.prepareStatement("SELECT * FROM candidates");
        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.next()) {
            return;
        } else {
            int i = 0;
            do {
                candidates[i] = resultSet.getString("candidateName") + resultSet.getString("candidateLast");
                candidateIds[i] = resultSet.getString("id");
                i++;
            } while (resultSet.next());
        }
    }
}