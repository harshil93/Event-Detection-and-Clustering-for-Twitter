import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class TimeSegment {
	
	java.sql.Timestamp startingTime;
	java.sql.Timestamp endingTime;
	Integer topicNum;
	public Integer getTopicNum() {
		return topicNum;
	}

	public void setTopicNum(Integer topicNum) {
		this.topicNum = topicNum;
	}

	ArrayList<Tweet> tweets;
	Map<String,EntityInfo> entityInfo = new HashMap<String, EntityInfo>();
	Map<String,Integer> entityWiseFreq = new HashMap<String, Integer>();
	Map<String,ArrayList<Integer>> entityWiseTweets = new HashMap<String, ArrayList<Integer>>();
	public java.sql.Timestamp getStartingTime() {
		return startingTime;
	}
	
	public void setStartingTime(java.sql.Timestamp startingTime) {
		this.startingTime = startingTime;
	}
	public java.sql.Timestamp getEndingTime() {
		return endingTime;
	}
	
	public void setEndingTime(java.sql.Timestamp endingTime) {
		this.endingTime = endingTime;
	}
	public ArrayList<Tweet> getTweets() {
		return tweets;
	}
	
	public void setTweets(ArrayList<Tweet> tweets) {
		this.tweets = tweets;
		calcEntityFreqMap();
	}
	
	public Map<String, EntityInfo> getEntityInfo() {
		return entityInfo;
	}
	
	public void setEntityInfo(Map<String, EntityInfo> entityInfo) {
		this.entityInfo = entityInfo;
	}
	
	public Set<String> getEntityFreqSet(){
		return entityWiseFreq.keySet();
	}
	
	private void calcEntityFreqMap() {
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
		
		for(Map.Entry<String, ArrayList<Integer>> et:entityWiseTweets.entrySet()){
			entityWiseFreq.put(et.getKey(), et.getValue().size());
		}
		
	}
	
	public void setTfIdf(Map<String,Integer> entityPresentTopicCount){
		for(Map.Entry<String, Integer> et:entityWiseFreq.entrySet()){
			EntityInfo ei = new EntityInfo(et.getKey());
			ei.setFreq(et.getValue());
			ei.setTfidf(et.getValue()/(1.0 * entityPresentTopicCount.get(et.getKey())));
			entityInfo.put(ei.getEntity(), ei);
		}
	}
	
}


class EntityInfo implements Comparable<EntityInfo>{
	String entity;
	
	public EntityInfo(String e) {
		// TODO Auto-generated constructor stub
		entity = e;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	Integer freq;
	Double tfidf;

	
	public Integer getFreq() {
		return freq;
	}
	public void setFreq(Integer freq) {
		this.freq = freq;
	}
	public Double getTfidf() {
		return tfidf;
	}
	public void setTfidf(Double tfidf) {
		this.tfidf = tfidf;
	}
	@Override
	public int compareTo(EntityInfo o) {
		return tfidf.compareTo(o.getTfidf());
	}
	
	
}
