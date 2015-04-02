import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class Timeline {

	/**
	 * @param args
	 */
	public static ArrayList<ArrayList<Tweet>> partition(ArrayList<Tweet> tweets, int noOfDays) {
		ArrayList<ArrayList<Tweet>> m = new ArrayList<>();
		
		m.add(new ArrayList<Tweet>());
		String ts = tweets.get(0).getTimestamp().toString();
		int prevDay = Integer.parseInt(ts.split(" ")[0].split("-")[2]);
		 
		for (Tweet tweet : tweets) {
			ts = tweet.getTimestamp().toString();
			int day = Integer.parseInt(ts.split(" ")[0].split("-")[2]);
			if (day - prevDay <= noOfDays) {
				m.get(m.size()-1).add(tweet);
			}
			else {
				m.add(new ArrayList<Tweet>());
				m.get(m.size()-1).add(tweet);
				prevDay = day;
			}
						
		}
		
		return m;
	}
	public static void main(String[] args) {
		ArrayList<Tweet> tweets = new ArrayList<>();
		Tweet tweet=new Tweet();
		DatabaseAPI.establishConnection();
		tweets=DatabaseAPI.extractUntaggedTweets();
		System.out.println("Done");
		ArrayList<ArrayList<Tweet>> m = partition(tweets,1);
	}

}
