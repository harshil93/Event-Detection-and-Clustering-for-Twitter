import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.json.simple.*;

// TODO - use location dictionary as well for location extractions http://www.geonames.org/about.html
// TODO - see if extracting Nouns etc is necessary in addition Entities
public class Testing {
	public final static String inFile = "/home/shobhit/in";
	public final static String outFile_geoLoc = "/home/shobhit/out_geoLoc";
	public final static String outFile_entities = "/home/shobhit/out_entities";
	
	public static void main(String[] args){
		
//		readTweet(System.in);
		process();
		
//		List<String> tweets = new ArrayList<String>();
//		tweets.add("I am going to #IITG tomorrow/today zup :) http://imgur.com");
//		tweets.add("I am going to IITG day after tomorrow");
//		
//		System.out.println(TweetCleaner.clean(tweets.get(0)));
		
		
//		DatabaseAPI.establishConnection();
//		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
//		tweets = DatabaseAPI.extractUntaggedTweets();
//		
//		// for each tweet remove URL, tokenize the text and replace slangs
//		for (Tweet tw : tweets) {
//			String tweet = TweetCleaner.removeUrl(tw.getTweet());
//			List<String> newToks = Twokenize.tokenizeRawTweetText(tweet);
//			TweetCleaner.replaceSlangs(newToks);
//			tweet = String.join(" ", newToks);
//			tw.setTweet(tweet);
//		}
//		
//		// before removing stopwords, we should first run twitter_nlp to extract
//		// location mentions and other named entities. 
//		
//		
//		// remove stopwords from tweets
//		for (Tweet tw : tweets) {
//			String tweet = TweetCleaner.removeUrl(tw.getTweet());
//			List<String> newToks = Twokenize.tokenizeRawTweetText(tweet);
//			newToks = TweetCleaner.removeStopWords(newToks);
//			tweet = String.join(" ", newToks);
//			tw.setTweet(tweet);
//		}
//		ArrayList<ArrayList<Tweet>> m = Timeline.partition(tweets,1);
		
	}
	
	private static JSONArray getJSONArray(String s){
		Object Obj=JSONValue.parse(s);
		JSONArray arr=(JSONArray)Obj;
		return arr;
	}
	
	// read tweets+tag lists from file, write entities+geoLocs to file
	public static void process() {
		BufferedReader r = null;
		BufferedWriter w1 = null;	
		BufferedWriter w2 = null;
		String tweet;
		ArrayList<String> geoLoc = new ArrayList<String>();
		ArrayList<String> entities = new ArrayList<String>();
		try {
			r = new BufferedReader(new FileReader(inFile));
			w1 = new BufferedWriter(new FileWriter(outFile_geoLoc));
			w2 = new BufferedWriter(new FileWriter(outFile_entities));
			while ((tweet = r.readLine()) != null) {
				String tags = r.readLine();
				String pos = r.readLine();
				String events = r.readLine();
				
				geoLoc = NER.extractEntities(getJSONArray(tweet), getJSONArray(tags));
				entities = NER.extractLocations(getJSONArray(tweet), getJSONArray(tags));
				
				for (String e : geoLoc) {
					w1.write(e.toLowerCase()+";");
				}
				w1.newLine();
				
				for (String e : entities) {
					w2.write(e.toLowerCase()+";");
				}
				w2.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error in File I/O");
		} finally {
			try {
				r.close();
				w1.close();
				w2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static void readTweet(InputStream in){
		Scanner sc = new Scanner(in);
		String tweet = sc.nextLine();
		String tags = sc.nextLine();
		String pos = sc.nextLine();
		String events = sc.nextLine();
		
		System.out.println(NER.extractEntities(getJSONArray(tweet), getJSONArray(tags)));
		System.out.println(NER.extractLocations(getJSONArray(tweet), getJSONArray(tags)));
		sc.close();
	}
	
	
	
	
}
