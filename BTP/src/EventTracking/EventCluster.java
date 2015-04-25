package EventTracking;

import java.util.Map;

public class EventCluster {
	
	protected	Map<String,Double> entityFreq;
	
	public boolean isEntityPresent(String entity){
		return entityFreq.containsKey(entity);
	}
	
	public Double getEntityFreq(String entity){
		if(!isEntityPresent(entity)){
			return 0.0;
		}
		return entityFreq.get(entity);
	}
	
	public void putEntity(String word,Double score){
		entityFreq.put(word, score);
	}
	
		
	
}
