import java.util.*;


public class Testing {
	public static void main(String[] args){
		List<String> tweets = new ArrayList<String>();
		tweets.add("I am going to IITG tomorrow");
		tweets.add("I am going to IITG day after tomorrow");
		System.out.println(TemporalExtractor.extract(tweets.get(0), "2015-02-13"));
		System.out.println(TemporalExtractor.extract(tweets.get(1), "2015-02-13"));
	}
}
