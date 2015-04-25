import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.json.simple.JSONArray;

public class NER {
	static Set<String> geolist;
	
	static{
		 String geolistFilename = "geolist.txt";
		 geolist = new TreeSet<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(geolistFilename));
			String line;
			while((line = br.readLine()) != null){
				geolist.add(line.toLowerCase());
			}
			
			System.out.println("geolist file read done");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> extractLocations(JSONArray tweet,JSONArray tags){		
			HashSet<String> locs = new HashSet<String>();
			
			for (int i = 0; i < tweet.size(); i++) {
				String temp = tags.get(i).toString().trim();
				if( temp.equals("B-geo-loc")){
					String location=(String) tweet.get(i) ;
					int j = i+1;
					while(j<tweet.size()){
						if(tags.get(j).toString().equals("I-geo-loc")){
							location+=" "+ (String) tweet.get(j);
							j++;
						}else{
							break;
						}
					}
					locs.add(location.toLowerCase());
					continue;
				}
				
				if(geolist.contains(tweet.get(i).toString().toLowerCase())){
					locs.add(tweet.get(i).toString().toLowerCase());
				}
			}
						
			return new ArrayList<String>(locs);
		}
	
	public static ArrayList<String> extractEntities(JSONArray tweet,JSONArray tags){
		ArrayList<String> entities= new ArrayList<String>();
		for (int i = 0; i < tweet.size(); i++) {
			String temp = tags.get(i).toString().trim();
			if( temp.startsWith("B-") && !temp.equals("B-geo-loc")){
				String entity=(String) tweet.get(i) ;
				int j = i+1;
				while(j<tweet.size()){
					if(tags.get(j).toString().startsWith("I-")){
						entity+=" "+ (String) tweet.get(j);
						j++;
					}else{
						break;
					}
				}
				entities.add(entity);
			}
		}
		return entities;
	}
	
}






