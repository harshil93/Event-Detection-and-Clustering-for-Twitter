import java.io.InputStream;
import java.util.*;

import org.json.simple.*;


public class Testing {
	public static void main(String[] args){
		
		readTweet(System.in);
		
//		List<String> tweets = new ArrayList<String>();
//		tweets.add("I am going to #IITG tomorrow/today zup :) http://imgur.com");
//		tweets.add("I am going to IITG day after tomorrow");
//		
//		System.out.println(TweetCleaner.clean(tweets.get(0)));
		
	}
	
	private static JSONArray getJSONArray(String s){
		Object Obj=JSONValue.parse(s);
		JSONArray arr=(JSONArray)Obj;
		return arr;
	}
	
	public static void readTweet(InputStream in){
		Scanner sc = new Scanner(in);
		String tweet = sc.nextLine();
		String tags = sc.nextLine();
		String chunks = sc.nextLine();
		String events = sc.nextLine();
		
		System.out.println(NER.extractEntities(getJSONArray(tweet), getJSONArray(tags)));
		System.out.println(NER.extractLocations(getJSONArray(tweet), getJSONArray(tags)));
		sc.close();
	}
	
	
	
	
}
