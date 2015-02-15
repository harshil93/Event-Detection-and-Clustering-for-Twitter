import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HashTagLabeller {
	
	private class TopicWord implements Comparable<TopicWord>{
		String word;
		Double val;
		
		@Override
		public int compareTo(TopicWord w) {
			if(this.val < w.val) return -1;
			else if(this.val > w.val) return 1;
			else return 0;
		}
	}
	
	private class Topic{
		Map<String,Double> words;
		ArrayList<TopicWord> list;
	}
	
	private List<Topic> topics;
	
	private static final Pattern TAG_PATTERN = Pattern.compile("(?:^|\\s|[\\p{Punct}&&[^/]])(#[\\p{L}0-9-_]+)");
	
	public HashTagLabeller(String topicsFile) throws NumberFormatException, IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(topicsFile));
		String line;
		Topic curTopic = null;
		
		while((line = br.readLine().trim()) != null){
			
			if(line.startsWith("Topic")){
				if(curTopic!= null) topics.add(curTopic);
				curTopic = new Topic();
				line = line.split(":")[1];
			}
			
			String[] tokens = line.split("\\s+");
			assert(curTopic == null);
			curTopic.words.put(tokens[0], Double.parseDouble(tokens[1]));
			curTopic.list.add(new TopicWord(){{
				word = tokens[0];
				val = Double.parseDouble(tokens[1]);
			}});
		}
		topics.add(curTopic);
		
		for(Topic t : topics){
			Collections.sort(t.list);
		}
		br.close();
	}
	
	
	public List<String> label(String tweet){
		List<String> retVal = new ArrayList<String>();
		Matcher m = TAG_PATTERN.matcher(tweet);
		if(m.find()){
			retVal.add(m.group(0));
		}else{
			String[] tokens = tweet.split("\\s+");
			Double maxScore = 0.0;
			Topic maxTopic = null;
			for(Topic t : topics){
				Double score = 0.0;
				for(String token : tokens){
					score += t.words.get(token);
				}
				if(score > maxScore){
					maxScore = score;
					maxTopic = t;
				}
			}
			
			if(maxTopic != null){
				retVal.add(maxTopic.list.get(0).word);
				retVal.add(maxTopic.list.get(1).word);
			}else{
				retVal.add("$nohashtag$");
			}
		}
		
		return retVal;
	}
}
