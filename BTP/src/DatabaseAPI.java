import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAPI {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/twitter_test";

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "root";
	static private Connection conn;
	
	public static void establishConnection() {
		//Statement stmt = null;
		conn = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
		}// end try
	}
	
	public static void closeConnection()
	{
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Tweet fetchTweet(Integer tweetID, Boolean hasht)
	{
		Statement stmt = null;
		Tweet tweet = new Tweet();
		try {
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM tweets where tweetID="+tweetID;
			ResultSet rs = stmt.executeQuery(sql);
			// STEP 5: Extract data from result set
			while (rs.next()) {
				// Retrieve by column name
				tweet.setTweetID(rs.getInt("tweetid"));
				tweet.setAnon1(rs.getInt("anon1"));
				tweet.setAnon2(rs.getInt("anon2"));
				tweet.setTimestamp(rs.getString("timestamp"));
				tweet.setLabel(rs.getString("label"));
				tweet.setUsername(rs.getString("username"));
				tweet.setTweet(rs.getString("tweet"));					
				}
			if (hasht) {
				sql = "SELECT hashtag FROM hashtags where tweetID="+tweetID;
				rs = stmt.executeQuery(sql);
				while(rs.next())
				{
					tweet.addHashtags(rs.getString("hashtag"));
				}
			}
			// STEP 6: Clean-up environment
			rs.close();
			stmt.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}// nothing we can do
		}// end try
		
		return tweet;
	}
	
	public static ArrayList<Tweet> extractTaggedTweets()
	{
//		establishConnection();
		Statement stmt = null;
		ArrayList<Tweet> ans = new ArrayList<Tweet>();
		try {
			Tweet tweet = new Tweet();
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT DISTINCT(tweetid) from hashtags";
			ResultSet rs = stmt.executeQuery(sql);
			ArrayList<Integer> ids = new ArrayList<Integer>();
			// STEP 5: Extract data from result set
			while (rs.next()) {
				ids.add(rs.getInt("tweetid"));
			}
			for (Integer id : ids) {
				tweet = fetchTweet(id, true);
				ans.add(tweet);
			}
			// STEP 6: Clean-up environment
			rs.close();
			stmt.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}// nothing we can do
		}// end try

		return ans;
	}
	
	public static ArrayList<Tweet> extractUntaggedTweets()
	{
//		establishConnection();
		Statement stmt = null;
		ArrayList<Tweet> ans = new ArrayList<Tweet>();
		try {
			Tweet tweet = new Tweet();
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT tweetid from tweets where tweetid NOT IN " +
					"(SELECT DISTINCT(tweetid) from hashtags)";
			ResultSet rs = stmt.executeQuery(sql);
			ArrayList<Integer> ids = new ArrayList<Integer>();
			// STEP 5: Extract data from result set
			while (rs.next()) {
				ids.add(rs.getInt("tweetid"));
			}
			for (Integer id : ids) {
				tweet = fetchTweet(id, true);
				ans.add(tweet);
			}
			// STEP 6: Clean-up environment
			rs.close();
			stmt.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}// nothing we can do
		}// end try

		return ans;
	}
	
	public static void main(String[] args)
	{
		establishConnection();
		Tweet tweet = new Tweet();
		tweet = fetchTweet(1, false);
		ArrayList<Tweet> taggedTweets = new ArrayList<Tweet>();
		taggedTweets = extractTaggedTweets();
		System.out.println(tweet.getTweet());
	}
	   	
}
