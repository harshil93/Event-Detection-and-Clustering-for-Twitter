package EventTracking;

import java.util.*;

public class EventClusterNode extends EventCluster {
	private LinkedList<Edge> adjList;
	
	public void addEdge(EventClusterNode dest,Double cost){
		Edge e = new Edge(this, dest, cost);
		adjList.add(e);
	}
	
	public void addEdge(EventClusterNode dest){
		Edge e = new Edge(this, dest, computeEdgeWeight(dest));
		adjList.add(e);
	}
	
	public List<Edge> getEdges(){
		return new LinkedList<Edge> (adjList);
	}
	
	public Double computeEdgeWeight(EventCluster dest){		
		Double score = 0.0;
		for(Map.Entry<String, Double> entry : entityFreq.entrySet()){
			if(dest.isEntityPresent(entry.getKey())){
				score += entry.getValue()*dest.getEntityFreq(entry.getKey());
			}
		}
		return score;
	}
}
