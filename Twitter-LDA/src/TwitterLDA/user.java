package TwitterLDA;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Common.FileUtil;

//import edu.mit.jwi.item.Word;

public class user {

	protected String userID;
	
	protected int tweetCnt;
	
	ArrayList<tweet> tweets = new ArrayList<tweet>();
	
	
	public user(String Dir, String id, HashMap<String, Integer> wordMap, 
			ArrayList<String> uniWordMap) {
		
		this.userID = id;
		ArrayList<String> datalines = new ArrayList<String>();
		FileUtil.readLines(Dir, datalines);		
		
		this.tweetCnt = datalines.size();
		
		for(int lineNo = 0; lineNo < datalines.size(); lineNo++) {
			String line = datalines.get(lineNo);
			String[] tweetSplit = line.split("\t");
			if(tweetSplit.length==2) {
				tweet tw = new tweet(tweetSplit[0], tweetSplit[1], wordMap, uniWordMap);
			tweets.add(tw);
			}
		}
		
		datalines.clear();
	}
}
