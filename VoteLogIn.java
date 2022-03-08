import java.sql.*;
import java.util.Date;
import java.util.Vector;
import java.text.SimpleDateFormat;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.SystemColor;

public class VoteLogIn extends JFrame {
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/elections";
	static final String USER = "root";
	static final String PASS = "iloveGod";

	String activeUsername, activeUserId;

	private String[] candidates = new String[4];
	private String[] candidateIds = new String[4];
	private String selectedCandidateId;

	private Connection connection = null;
	private PreparedStatement statement = null;

	// jtextfields
	private JTextField textUser;
	private JTextField textPass;
	private boolean isAdmin;

	// jbuttons
	private JButton registerButton;
	private JButton LogIn;
	private JButton registerUserButton;
	private JButton loginReturnButton;
	private JButton voteSubmit;
	private JButton viewResultsButton;

	// radiobuttons
	private JRadioButton candidate1button;
	private JRadioButton candidate2button;
	private JRadioButton candidate3button;
	private JRadioButton candidate4button;
	private JRadioButton voterButton;
	private JRadioButton adminButton;

	// labels
	private JLabel userlbl;
	private JLabel voting;
	private JLabel passlbl;
	private JLabel plsvote;
	private JLabel plslogin;
	private JLabel register;
	private JLabel adminlbl;

	char[] password;
	String token;
	private JTable table_1;
	private JScrollPane scrollPane;

	// others needed

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VoteLogIn window = new VoteLogIn();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public VoteLogIn() {
		System.out.println("Log in page");
		initComponents();

		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

		userlbl = new JLabel("User:");
		userlbl.setBounds(248, 110, 102, 30);
		userlbl.setFont(new Font("STZhongsong", Font.PLAIN, 16));
		contentPane.add(userlbl);

		voting = new JLabel("VOTING SYSTEM");
		voting.setBounds(216, 10, 230, 44);
		voting.setFont(new Font("Times New Roman", Font.BOLD, 20));
		voting.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(voting);

		textPass = new JTextField();
		textPass.setBounds(330, 150, 102, 19);
		textPass.setColumns(10);
		contentPane.add(textPass);

		passlbl = new JLabel("Password:");
		passlbl.setBounds(248, 144, 102, 30);
		passlbl.setFont(new Font("STZhongsong", Font.PLAIN, 16));
		contentPane.add(passlbl);

		plsvote = new JLabel("Please vote for you candidate...");
		plsvote.setBounds(248, 38, 152, 36);
		plsvote.setHorizontalAlignment(SwingConstants.CENTER);
		plsvote.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		plsvote.setVisible(false);
		contentPane.add(plsvote);

		plslogin = new JLabel("Please log in...");
		plslogin.setBounds(248, 38, 152, 36);
		plslogin.setHorizontalAlignment(SwingConstants.CENTER);
		plslogin.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		contentPane.add(plslogin);
		plslogin.setHorizontalAlignment(SwingConstants.CENTER);
		plslogin.setFont(new Font("Times New Roman", Font.PLAIN, 12));

		textUser = new JTextField();
		textUser.setBounds(330, 118, 102, 19);
		contentPane.add(textUser);
		textUser.setColumns(10);

		candidate1button = new JRadioButton(candidates[0]);
		candidate1button.setBounds(148, 98, 131, 30);
		candidate1button.setBackground(SystemColor.info);
		candidate1button.setFont(new Font("STZhongsong", Font.PLAIN, 14));
		candidate1button.setVisible(false);
		contentPane.add(candidate1button);

		candidate2button = new JRadioButton(candidates[1]);
		candidate2button.setBounds(372, 98, 131, 30);
		candidate2button.setFont(new Font("STZhongsong", Font.PLAIN, 14));
		candidate2button.setBackground(SystemColor.info);
		candidate2button.setVisible(false);
		contentPane.add(candidate2button);

		candidate3button = new JRadioButton(candidates[2]);
		candidate3button.setBounds(148, 178, 131, 30);
		candidate3button.setFont(new Font("STZhongsong", Font.PLAIN, 14));
		candidate3button.setBackground(SystemColor.info);
		candidate3button.setVisible(false);
		contentPane.add(candidate3button);

		candidate4button = new JRadioButton(candidates[3]);
		candidate4button.setBounds(372, 178, 131, 30);
		candidate4button.setFont(new Font("STZhongsong", Font.PLAIN, 14));
		candidate4button.setBackground(SystemColor.info);
		candidate4button.setVisible(false);
		contentPane.add(candidate4button);

		ButtonGroup v = new ButtonGroup();
		v.add(candidate1button);
		v.add(candidate2button);
		v.add(candidate3button);
		v.add(candidate4button);
		candidate1button.setSelected(true);

		voteSubmit = new JButton("Submit Vote");
		voteSubmit.setBounds(273, 281, 130, 30);
		voteSubmit.setVisible(false);
		contentPane.add(voteSubmit);

		registerButton = new JButton("Register");
		registerButton.setBounds(299, 250, 85, 21);
		contentPane.add(registerButton);

		LogIn = new JButton("Log In");
		LogIn.setBounds(299, 214, 85, 21);
		contentPane.add(LogIn);

		registerUserButton = new JButton("Register User");
		registerUserButton.setBounds(299, 222, 85, 21);
		registerUserButton.setFont(new Font("Tahoma", Font.PLAIN, 8));
		registerUserButton.setVisible(false);
		contentPane.add(registerUserButton);

		loginReturnButton = new JButton("Return to Log In");
		loginReturnButton.setBounds(299, 253, 85, 21);
		loginReturnButton.setFont(new Font("Tahoma", Font.PLAIN, 6));
		loginReturnButton.setVisible(false);
		contentPane.add(loginReturnButton);

		register = new JLabel("Please register your User and Password");
		register.setBounds(205, 50, 241, 13);
		register.setHorizontalAlignment(SwingConstants.CENTER);
		register.setVisible(false);
		contentPane.add(register);

		voterButton = new JRadioButton("Voter Account");
		voterButton.setBounds(199, 317, 151, 21);
		voterButton.setBackground(SystemColor.info);
		voterButton.setVisible(false);
		contentPane.add(voterButton);

		adminButton = new JRadioButton("Admin Account");
		adminButton.setBounds(380, 317, 152, 21);
		adminButton.setBackground(SystemColor.info);
		adminButton.setVisible(false);
		contentPane.add(adminButton);

		ButtonGroup r = new ButtonGroup();
		r.add(adminButton);
		r.add(voterButton);
		voterButton.setSelected(true);

		adminlbl = new JLabel("Admin's Page");
		adminlbl.setBounds(291, 50, 93, 13);
		adminlbl.setHorizontalAlignment(SwingConstants.CENTER);
		adminlbl.setVisible(false);
		contentPane.add(adminlbl);

		viewResultsButton = new JButton("View Results");
		viewResultsButton.setBounds(35, 317, 102, 21);
		viewResultsButton.setVisible(false);
		contentPane.add(viewResultsButton);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(45, 27, 592, 258);
		contentPane.add(scrollPane);

		table_1 = new JTable();
		scrollPane.setViewportView(table_1);
		table_1.setModel(
				new DefaultTableModel(new Object[][] {},
						new String[] {
								"Candidate Name", "Number of Votes",
						}) {
					Class<?>[] columnTypes = new Class<?>[] {
							String.class, String.class,
					};

					public Class<?> getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
				});

		scrollPane.setVisible(false);

		addListeners();
	}

	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(SystemColor.info);
		setBounds(100, 100, 698, 408);

		WindowListener taskStarter = new WindowAdapter() {
			public void windowOpened(WindowEvent event) {
				try {
					Class.forName(JDBC_DRIVER);

					connection = DriverManager.getConnection(DB_URL, USER, PASS);
					System.out.println("Connected to database.");

					collectCandidates();

					candidate1button.setText(candidates[0]);
					candidate2button.setText(candidates[1]);
					candidate3button.setText(candidates[2]);
					candidate4button.setText(candidates[3]);
		
					selectedCandidateId = candidateIds[0];

				} catch (SQLException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			public void windowClosing(WindowEvent event) {
				System.out.println("is closing...");
				try {
					if (connection != null) {
						connection.close();
						System.out.println("successfully closed connection.");
					}

					if (statement != null) {
						statement.close();
						System.out.println("successfully closed statement.");
					}
				} catch (SQLException e) {
					System.out.println("Here's the error\n");
					e.printStackTrace();
				}
			}
		};

		this.addWindowListener(taskStarter);
	}

	private void addListeners() {
		candidate1button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Candidate 1 picked");
				selectedCandidateId = candidateIds[0];
			}
		});

		candidate2button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Candidate 2 picked");
				selectedCandidateId = candidateIds[1];
			}
		});

		candidate3button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Candidate 3 picked");
				selectedCandidateId = candidateIds[2];
			}
		});

		candidate4button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Candidate 4 picked");
				selectedCandidateId = candidateIds[3];
			}
		});

		LogIn.addActionListener(new ActionListener() { // log into voting
			public void actionPerformed(ActionEvent e) {
				String user = textUser.getText();
				String pass = textPass.getText();

				try {

					int resultInt = logIn(user, pass);

					if (resultInt == 2) {
						// loginset
						LogIn.setVisible(false);
						plslogin.setVisible(false);
						textUser.setVisible(false);
						textPass.setVisible(false);
						userlbl.setVisible(false);
						passlbl.setVisible(false);
						scrollPane.setVisible(false);
						registerButton.setVisible(false);
						// voteset
						plsvote.setVisible(true);
						candidate1button.setVisible(true);
						candidate2button.setVisible(true);
						candidate3button.setVisible(true);
						candidate4button.setVisible(true);
						voteSubmit.setVisible(true);
					} else if (resultInt == 1) {
						System.out.println("Admin Page");
						LogIn.setVisible(false);
						plslogin.setVisible(false);
						textUser.setVisible(false);
						textPass.setVisible(false);
						userlbl.setVisible(false);
						passlbl.setVisible(false);
						scrollPane.setVisible(true);
						registerButton.setVisible(false);
						adminlbl.setVisible(true);
						viewResultsButton.setVisible(true);
						loginReturnButton.setVisible(true);

						tableUpdate();
					} else {
						JOptionPane.showMessageDialog(LogIn, "Please enter valid password and user", "ERROR",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (HeadlessException | SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		registerButton.addActionListener(new ActionListener() { // sends user to registration page
			public void actionPerformed(ActionEvent e) {
				LogIn.setVisible(false);
				plslogin.setVisible(false);
				registerUserButton.setVisible(true);
				loginReturnButton.setVisible(true);
				adminButton.setVisible(true);
				voterButton.setVisible(true);
				registerButton.setVisible(false);
				register.setVisible(true);

				System.out.println("Register page");
			}
		});

		adminButton.addActionListener(new ActionListener() {// sets user as an admin
			public void actionPerformed(ActionEvent e) {
				isAdmin = true;
			}
		});

		voterButton.addActionListener(new ActionListener() {// sets user as an admin
			public void actionPerformed(ActionEvent e) {
				isAdmin = false;
			}
		});

		registerUserButton.addActionListener(new ActionListener() {// registers a new user into the
			public void actionPerformed(ActionEvent e) {
				String user = textUser.getText();
				String pass = textPass.getText();
				try {
					boolean doesNotYetExist = register(user, pass, isAdmin);

					if (!doesNotYetExist) {
						JOptionPane.showMessageDialog(null, "There is already an account with this username", "ERROR",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				System.out.println("User Registered");
				System.out.println(isAdmin);
				JOptionPane.showMessageDialog(LogIn, "Please Return to Log In page");
			}
		});

		voteSubmit.addActionListener(new ActionListener() { // submit the vote
			public void actionPerformed(ActionEvent e) {
				try {
					voteCandidate(activeUsername, activeUserId, selectedCandidateId);

					System.out.println("VOTE SUBMITTED for candidate:");
					System.out.println(selectedCandidateId);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		loginReturnButton.addActionListener(new ActionListener() { // returns user to login page
			public void actionPerformed(ActionEvent e) {
				LogIn.setVisible(true);
				plslogin.setVisible(true);
				registerUserButton.setVisible(false);
				loginReturnButton.setVisible(false);
				registerButton.setVisible(true);
				adminButton.setVisible(false);
				voterButton.setVisible(false);
				register.setVisible(false);
				textUser.setVisible(true);
				textPass.setVisible(true);
				userlbl.setVisible(true);
				viewResultsButton.setVisible(false);
				passlbl.setVisible(true);
				adminlbl.setVisible(false);
				System.out.println("Log In Page");
			}
		});

		viewResultsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableUpdate();
			}
		});
	}

	// checks to see if user is registered
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

	// registers user
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
	
	private ResultSet showResults() throws SQLException {
		statement = connection.prepareStatement(
				"SELECT candidate AS ?, COUNT(candidateId) AS ? FROM votes GROUP BY candidate ORDER BY COUNT(candidateId) DESC");
		statement.setString(1, "name");
		statement.setString(2, "votes");

		return statement.executeQuery();
	}

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
		
		candidateName = result.getString("candidateName") + " " + result.getString("candidateLast");
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
		
		System.out.println(voteId.length());
		
		statement.executeUpdate();
		result.close();
		
		return true;
	}
	
	private void collectCandidates() throws SQLException {// gets candidates
		statement = connection.prepareStatement("SELECT * FROM candidates");
		ResultSet resultSet = statement.executeQuery();

		if (!resultSet.next()) {
			return;
		} else {
			int i = 0;
			do {
				candidates[i] = resultSet.getString("candidateName") + " " + resultSet.getString("candidateLast");
				candidateIds[i] = resultSet.getString("id");
				i++;
			} while (resultSet.next());
		}
	}

	private void tableUpdate() {
		try {
			ResultSet results = showResults();

			DefaultTableModel dtm = (DefaultTableModel) table_1.getModel();
			dtm.setRowCount(0);

			while (results.next()) {
				Vector<String> v2 = new Vector<>();

				v2.add(results.getString("name"));
				v2.add(results.getString("votes"));

				dtm.addRow(v2);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
