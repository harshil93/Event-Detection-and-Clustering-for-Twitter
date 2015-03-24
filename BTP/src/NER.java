import java.io.*;
import java.util.ArrayList;

import org.json.simple.JSONArray;

public class NER {

	public static ArrayList<String> extractLocations(JSONArray tweet,JSONArray tags){
			ArrayList<String> locs= new ArrayList<String>();
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
					locs.add(location);
				}
			}
			return locs;
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






