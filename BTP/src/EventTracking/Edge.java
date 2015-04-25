package EventTracking;

public class Edge {
	private EventClusterNode src,dest;
	private Double cost;
	
	public EventClusterNode getSrc() {
		return src;
	}

	public void setSrc(EventClusterNode src) {
		this.src = src;
		
	}

	public EventClusterNode getDest() {
		return dest;
	}

	public void setDest(EventClusterNode dest) {
		this.dest = dest;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Edge(EventClusterNode src, EventClusterNode dest,Double cost){
		this.src = src;
		this.dest = dest;
		this.cost = cost;
	}
	
	
}
