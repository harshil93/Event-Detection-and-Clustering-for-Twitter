import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import org.joda.time.DateTime;


public class Timeline {

	/**
	 * @param args
	 */
	
	public static class CustomComparator implements Comparator<Tweet> {
	    @Override
	    public int compare(Tweet o1, Tweet o2) {
	        return o1.getTimestamp().compareTo(o2.getTimestamp());
	    }
	}
	public static ArrayList<ArrayList<Tweet>> partition(ArrayList<Tweet> tweets, int noOfDays) {
		ArrayList<ArrayList<Tweet>> m = new ArrayList<>();
		Collections.sort(tweets,new CustomComparator());
		
		m.add(new ArrayList<Tweet>());
		Calendar cal = Calendar.getInstance();
		cal.setTime(tweets.get(0).getTimestamp());
		
		java.util.Date start = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, noOfDays);
		java.util.Date end = cal.getTime();
		
		for(Tweet t: tweets){
			if( t.getTimestamp().getTime() >= start.getTime() && t.getTimestamp().getTime() < end.getTime() ){
				m.get(m.size() - 1).add(t);
			}else{
				m.add(new ArrayList<Tweet>());
				start = end;
				cal.add(Calendar.DAY_OF_MONTH, noOfDays);
				end = cal.getTime();
				m.get(m.size() - 1).add(t);
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
