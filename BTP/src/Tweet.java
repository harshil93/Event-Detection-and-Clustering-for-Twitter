import java.util.ArrayList;
import java.util.List;


public class Tweet {
	private Integer tweetID;
	private Integer anon1;
	private Integer anon2;
//	private java.sql.Timestamp timestamp;
	private String timestamp ;
	private String label;
	private String username;
	private String tweet;
	private ArrayList<String> hashtags;
	
	Tweet()
	{
		hashtags = new ArrayList<String>();
	}
	public Integer getAnon1() {
		return anon1;
	}
	public void setAnon1(Integer anon1) {
		this.anon1 = anon1;
	}
	public Integer getAnon2() {
		return anon2;
	}
	public void setAnon2(Integer anon2) {
		this.anon2 = anon2;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTweet() {
		return tweet;
	}
	public void setTweet(String tweet) {
		this.tweet = tweet;
	}
	public Integer getTweetID() {
		return tweetID;
	}
	public void setTweetID(Integer tweetID) {
		this.tweetID = tweetID;
	}
	public List<String> getHashtags() {
		return hashtags;
	}
	public void setHashtags(ArrayList<String> hashtags) {
		this.hashtags = hashtags;
	}
	public void addHashtags(String h) {
		this.hashtags.add(h);
	}
	
}
