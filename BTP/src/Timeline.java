import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.joda.time.DateTime;


public class Timeline {

	/**
	 * @param args
	 */
	private ArrayList<TimeSegment> timesegments = new ArrayList<TimeSegment>();
	
	public void addSegment(TimeSegment t){
		timesegments.add(t);
	}
	
	public void calcTfIdf(){
		Map<String,Integer> globalEntityTopicCount = new HashMap<String, Integer>();
		for(TimeSegment t:timesegments){
			Set<String> st = t.getEntityFreqSet();
			for(String elem:st){
				if(globalEntityTopicCount.containsKey(elem)){
					globalEntityTopicCount.put(elem,globalEntityTopicCount.get(elem)+1);
				}else{
					globalEntityTopicCount.put(elem, 1);
				}
			}
		}
		for(TimeSegment t:timesegments){
			t.setTfIdf(globalEntityTopicCount);
		}
	}
	
	public void dumpTimeline(String filename,int topN) throws FileNotFoundException{
		FileOutputStream fos = new FileOutputStream(filename, false);
		PrintStream p = new PrintStream(fos);
		for(TimeSegment t:timesegments){
			p.println("\n\nTopic No. "+t.getTopicNum());
			java.sql.Timestamp startingTime = t.getStartingTime();
			java.sql.Timestamp endingTime = t.getEndingTime();
			p.println("\n"+startingTime+" - "+endingTime);
			ArrayList<EntityInfo> entityinfolist = new ArrayList<EntityInfo>(t.getEntityInfo().values());
			Collections.sort(entityinfolist);
			for(int i=entityinfolist.size()-1;i>=entityinfolist.size() - topN && i>=0;i--){
				p.print(entityinfolist.get(i).getEntity()+" - "+entityinfolist.get(i).getTfidf()+" - "+entityinfolist.get(i).getFreq()+" , ");
			}
			p.println("\n");
		}
		p.close();
		
	}
	
	
	
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

	public ArrayList<TimeSegment> getTimesegments() {
		return timesegments;
	}
	
	public void setTimesegments(ArrayList<TimeSegment> timesegments) {
		this.timesegments = timesegments;
	}

}
