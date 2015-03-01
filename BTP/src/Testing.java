import java.io.IOException;
import java.util.*;


public class Testing {
	public static void main(String[] args){
		List<String> tweets = new ArrayList<String>();
		tweets.add("I am going to #IITG tomorrow/today zup :) http://imgur.com");
		tweets.add("I am going to IITG day after tomorrow");
		
		System.out.println(TweetCleaner.clean(tweets.get(0)));
		
	}
}
