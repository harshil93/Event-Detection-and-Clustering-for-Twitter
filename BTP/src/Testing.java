import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.json.simple.*;

// TODO - use location dictionary as well for location extractions http://www.geonames.org/about.html
// TODO - see if extracting Nouns etc is necessary in addition Entities

public class Testing {
	// Change this a/c to your requirements
	public final static String _nerInFile = "./TweetByTopics_50/tweetsForNER.out";
	public final static int _numOfTopic = 50;
	public final static String _tweetsFile= "./TweetByTopics_50/tweets";
	public final static String _topicFolder = "./TweetByTopics_50/";
	
	public final static String _LLDAidfilePrefix = "LLDA.id.";
	public final static String _LLDAwordfilePrefix = "LLDA.word.";
	public final static String _LLDAentityfilePrefix = "LLDA.entity.";
	static Map<Integer, Tweet> tweets = new HashMap<Integer, Tweet>();
	static ArrayList< ArrayList<Integer> > topics = new ArrayList<ArrayList<Integer>>();
	public static void main(String[] args) throws IOException{
		
		readFiles(_nerInFile,_tweetsFile,_topicFolder,_numOfTopic);
		printTimeline();
		//createFileForLabelledLDA(_LLDAidfilePrefix,_LLDAwordfilePrefix, _LLDAentityfilePrefix);
		
	}
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map )
	{
	      Map<K,V> result = new LinkedHashMap<>();
	     Stream <Entry<K,V>> st = map.entrySet().stream();
	
	     st.sorted(Comparator.comparing(e -> e.getValue()))
	          .forEach(e ->result.put(e.getKey(),e.getValue()));
	     return result;
	}
	
	public static void printTimeline() throws FileNotFoundException{
		ArrayList< ArrayList<Tweet>> topicWiseTweets = getTopicWiseTweets(topics);
		FileOutputStream fos = new FileOutputStream("timeline", false);
		PrintStream p = new PrintStream(fos);		
		for(int topicNum = 0; topicNum< topics.size();topicNum++){
			
			ArrayList< ArrayList<Tweet>> paritionedTweets = Timeline.partition(topicWiseTweets.get(topicNum),15);
			p.println("\n\nTopic No. "+topicNum);
			for (ArrayList<Tweet> arrayList : paritionedTweets) {
				if(arrayList.size()>1){
					java.sql.Timestamp startingTime = arrayList.get(0).getTimestamp();
					java.sql.Timestamp endingTime = arrayList.get(arrayList.size() - 1).getTimestamp();
					p.println("\n"+startingTime+" - "+endingTime);
					Map<String,ArrayList<Integer>> entitySet = getTopEntitySetForTweetSet(arrayList,10);
					for (Map.Entry<String, ArrayList<Integer>> entry : entitySet.entrySet()) {
						p.print(entry.getKey()+" - "+entry.getValue().size()+" , ");
					}					
				}
			}
		}
		p.close();
	}
	
	private static Map<String, ArrayList<Integer>> getTopEntitySetForTweetSet(
			ArrayList<Tweet> tweets,int topNum) {
		HashMap<String,ArrayList<Integer>> entityWiseTweets = new HashMap<String, ArrayList<Integer>>();
		for (int i = 0; i < tweets.size(); i++) {
			
			Tweet tw = tweets.get(i);
			ArrayList<String> entities = tw.getEntities();
			
			if(entities == null){
				continue;
			}
						
			for (int j = 0; j < entities.size(); j++) {
				String currentEntity = entities.get(j);
				
				if(entityWiseTweets.containsKey(currentEntity)){
					entityWiseTweets.get(currentEntity).add(tw.getTweetID());
				}else{
					entityWiseTweets.put(currentEntity,new ArrayList<Integer>());
					entityWiseTweets.get(currentEntity).add(tw.getTweetID());
				}
			}
			
		}
		
		// Sorting the entities by the number of tweets in which they are present
		Map<String, Integer> entitiesCount = new HashMap<String, Integer>();
		for (Map.Entry<String, ArrayList<Integer>> entry : entityWiseTweets.entrySet()) {
			entitiesCount.put(entry.getKey(), entry.getValue().size());
		}
		
		Map<String, Integer>  sortEntitiesCount = sortByValue(entitiesCount);
		
		int entityNum = 0;
		int size = sortEntitiesCount.size();
		Map<String, ArrayList<Integer>>  retVal = new HashMap<String, ArrayList<Integer>>();
		for (Map.Entry<String, Integer> dentry: sortEntitiesCount.entrySet()) {
			entityNum++;
			if(entityNum>size-topNum){
				ArrayList<Integer> temp = entityWiseTweets.get(dentry.getKey());
				retVal.put(dentry.getKey(), temp);
			}
		}
		return retVal;
	}
	private static ArrayList<ArrayList<Tweet>> getTopicWiseTweets(
			ArrayList<ArrayList<Integer>> topics2) {
		ArrayList<ArrayList<Tweet>> topicWiseTweets = new ArrayList<ArrayList<Tweet>>();
		
		for(int topicNum = 0; topicNum< topics.size();topicNum++){
			ArrayList<Tweet> curTopicTweets = new ArrayList<Tweet>();
			for(Integer i: topics.get(topicNum)){
				Tweet tw = tweets.get(i);
				if(tw!=null)
					curTopicTweets.add(tweets.get(i));
			}
			topicWiseTweets.add(curTopicTweets);
		}
		return topicWiseTweets;
	}
	private static void createFileForLabelledLDA(String lldaidfileprefix,String lldawordfileprefix,String lldaentityfileprefix) throws IOException {
		
		for(int topicNum = 0; topicNum< topics.size();topicNum++){
			System.out.println("createFileForLabelledLDA"+" "+topicNum);
			HashMap<String,ArrayList<Integer>> entityWiseTweets = new HashMap<String, ArrayList<Integer>>();
			
			ArrayList<Integer> topic  = topics.get(topicNum);
			File file = new File(_topicFolder+lldaidfileprefix+topicNum);
            BufferedWriter idWriter = new BufferedWriter(new FileWriter(file));
            
            
            File file1 = new File(_topicFolder+lldaentityfileprefix+topicNum);
            BufferedWriter entityIntWriter = new BufferedWriter(new FileWriter(file1));
            
            File file2 = new File(_topicFolder+lldawordfileprefix+topicNum);
            BufferedWriter wordWriter = new BufferedWriter(new FileWriter(file2));
            Set<Integer> tweetsDone = new HashSet<Integer>();
            
			for (int i = 0; i < topic.size(); i++) {
				
				Tweet tw = tweets.get(topic.get(i));
				ArrayList<String> entities = tw.getEntities();
				
				if(entities == null){
					continue;
				}
				
				if(entities.size()>0){
					tweetsDone.add(tw.getTweetID());
				}
				
				for (int j = 0; j < entities.size(); j++) {
					String currentEntity = entities.get(j);
					
					if(entityWiseTweets.containsKey(currentEntity)){
						entityWiseTweets.get(currentEntity).add(tw.getTweetID());
					}else{
						entityWiseTweets.put(currentEntity,new ArrayList<Integer>());
						entityWiseTweets.get(currentEntity).add(tw.getTweetID());
					}
				}
			}
			
			// Sorting the entities by the number of tweets in which they are present
			Map<String, Integer> entitiesCount = new HashMap<String, Integer>();
			for (Map.Entry<String, ArrayList<Integer>> entry : entityWiseTweets.entrySet()) {
				entitiesCount.put(entry.getKey(), entry.getValue().size());
			}
			
			Map<String, Integer>  sortEntitiesCount = sortByValue(entitiesCount);
						
			int entityNum = 0;
			
			for (Map.Entry<String, Integer> dentry: sortEntitiesCount.entrySet()) {
			    String key = dentry.getKey();
			    ArrayList<Integer> value = entityWiseTweets.get(dentry.getKey());
			    
			    try {
			    	
			    	entityIntWriter.write(entityNum+" "+key);
		            idWriter.write("["+entityNum+"] ");
		            wordWriter.write("["+entityNum+"] ");
		            
		            for (int i = 0; i < value.size(); i++) {
		            	idWriter.write(value.get(i)+" ");
		            	wordWriter.write(tweets.get(value.get(i)).getTweet()+" ");
					}
		           
		        } catch ( IOException e ) {
		            e.printStackTrace();
		        }
			    idWriter.write("\n");
			    wordWriter.write("\n");
			    entityIntWriter.write("\n");
			    entityNum++;
			}
			
			// Writing to id file
			idWriter.write("[ ");
			wordWriter.write("[ ");
			for(int i=0;i<entityNum;i++){
				idWriter.write(i+" ");
				wordWriter.write(i+" ");
			}
			idWriter.write("] ");
			wordWriter.write("] ");
			for (int i = 0; i < topic.size(); i++) {
				if(!tweetsDone.contains(topic.get(i) )){
					idWriter.write(topic.get(i)+" ");
					wordWriter.write(tweets.get(topic.get(i)).getTweet()+" ");
				}
			}
			
			
			
			
			idWriter.close();
			entityIntWriter.close();
			wordWriter.close();
		}
	}
	private static JSONArray getJSONArray(String s){
		Object Obj=JSONValue.parse(s);
		JSONArray arr=(JSONArray)Obj;
		return arr;
	}
	public static void readFiles(String nerInFile, String tweetsfile, String topicFolder, int numoftopic) throws FileNotFoundException {
		BufferedReader nerReader = new BufferedReader(new FileReader(nerInFile));
		BufferedReader tweetsReader = new BufferedReader(new FileReader(tweetsfile));
		String line;
		try {
			// read tweetsfile
			while((line = tweetsReader.readLine()) !=null){
				String[] tweetArr = line.split("\t");
				if(tweetArr.length>2){
					String tweet="";
					for(int i = 1;i<tweetArr.length;i++){
						tweet += tweetArr[i]+" ";
					}
					
					
					Integer tweetId = Integer.parseInt(tweetArr[0]);
					java.sql.Timestamp datetime = java.sql.Timestamp.valueOf(tweetArr[1]);
					Tweet tweetObj = new Tweet();
					tweetObj.setTweetID(tweetId);
					tweetObj.setTweet(tweet);
					if(datetime != null)
						tweetObj.setTimestamp(datetime);
					else
						tweetObj.setTimestamp(new Timestamp(0));
					
					
					tweets.put(tweetId, tweetObj);
				}
			}
			System.out.println("Tweets file read");
			// read tweetsfile
			String id;
			while((id = nerReader.readLine()) !=null){
				
				Integer tweetId = Integer.parseInt(id);
				if(tweetId%1000 == 0) System.out.println(id);
				String tweet = nerReader.readLine();
				String tags = nerReader.readLine();
				String pos = nerReader.readLine();
				String events = nerReader.readLine();
				Tweet tw = tweets.get(tweetId);
				if(tw == null) continue;
				ArrayList<String> arr1 = NER.extractEntities(getJSONArray(tweet), getJSONArray(tags));
				///ArrayList<String> arr2 = NER.extractLocations(getJSONArray(tweet), getJSONArray(tags));
				//arr1.addAll(arr2);
				
				//tw.setLocations(arr2);			
				tw.setEntities(arr1);	
			}
			for(int topicNum = 0; topicNum< numoftopic;topicNum++){
				topics.add(new ArrayList<Integer>());
			}
			
			for(int topicNum = 0; topicNum< numoftopic;topicNum++){
				BufferedReader topicReader = new BufferedReader(new FileReader(topicFolder+topicNum));
				while((id = topicReader.readLine())!=null){
					topics.get(topicNum).add(Integer.parseInt(id));
				}
			}
			
			
			
			nerReader.close();
			tweetsReader.close();
			System.out.println("ner read completed");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
}
