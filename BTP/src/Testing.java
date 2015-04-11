import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.json.simple.*;

// TODO - use location dictionary as well for location extractions http://www.geonames.org/about.html
// TODO - see if extracting Nouns etc is necessary in addition Entities
public class Testing {
	
	// Change this a/c to your requirements
	public final static String _nerInFile = "./TweetByTopic_Test/tweetsForNER.out";
	public final static int _numOfTopic = 3;
	public final static String _tweetsFile= "./TweetByTopic_Test/tweets";
	public final static String _topicFolder = "./TweetByTopic_Test/";
	
	public final static String _LLDAidfilePrefix = "LLDA.id.";
	public final static String _LLDAwordfilePrefix = "LLDA.word.";
	public final static String _LLDAentityfilePrefix = "LLDA.entity.";
	static Map<Integer, Tweet> tweets = new HashMap<Integer, Tweet>();
	static ArrayList< ArrayList<Integer> > topics = new ArrayList<ArrayList<Integer>>();
	public static void main(String[] args) throws IOException{
		
		readFiles(_nerInFile,_tweetsFile,_topicFolder,_numOfTopic);
		createFileForLabelledLDA(_LLDAidfilePrefix,_LLDAwordfilePrefix, _LLDAentityfilePrefix);
		
		
	}
	
	private static void createFileForLabelledLDA(String lldaidfileprefix,String lldawordfileprefix,String lldaentityfileprefix) throws IOException {
		
		for(int topicNum = 0; topicNum< topics.size();topicNum++){
			
			HashMap<String,ArrayList<Integer>> entityWiseTweets = new HashMap<String, ArrayList<Integer>>();
			ArrayList<Integer> topic  = topics.get(topicNum);
			File file = new File(_topicFolder+lldaidfileprefix+topicNum);
            BufferedWriter idWriter = new BufferedWriter(new FileWriter(file));
            
            
            File file1 = new File(_topicFolder+lldaentityfileprefix+topicNum);
            BufferedWriter entityIntWriter = new BufferedWriter(new FileWriter(file1));
            
            File file2 = new File(_topicFolder+lldawordfileprefix+topicNum);
            BufferedWriter wordWriter = new BufferedWriter(new FileWriter(file2));
            
			for (int i = 0; i < topic.size(); i++) {
				Tweet tw = tweets.get(topic.get(i));
				ArrayList<String> entities = tw.getEntities();
				
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
			int entityNum = 0;
			
			for (Map.Entry<String, ArrayList<Integer>> entry : entityWiseTweets.entrySet()) {
			    String key = entry.getKey();
			    ArrayList<Integer> value = entry.getValue();
			    
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
			    
			    entityNum++;
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
				if(tweetArr.length>1){
					String tweet="";
					for(int i = 1;i<tweetArr.length;i++){
						tweet += tweetArr[i]+" ";
					}
					
					
					Integer tweetId = Integer.parseInt(tweetArr[0]);
					
					Tweet tweetObj = new Tweet();
					tweetObj.setTweetID(tweetId);
					tweetObj.setTweet(tweet);
					
					tweets.put(tweetId, tweetObj);
				}
			}
			
			// read tweetsfile
			String id;
			while((id = nerReader.readLine()) !=null){
				
				Integer tweetId = Integer.parseInt(id);
				String tweet = nerReader.readLine();
				String tags = nerReader.readLine();
				String pos = nerReader.readLine();
				String events = nerReader.readLine();
				Tweet tw = tweets.get(tweetId);
				
				ArrayList<String> arr1 = NER.extractEntities(getJSONArray(tweet), getJSONArray(tags));
				ArrayList<String> arr2 = NER.extractLocations(getJSONArray(tweet), getJSONArray(tags));
				arr1.addAll(arr2);
				
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
			
		} catch (Exception e) {
			e.printStackTrace();
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
