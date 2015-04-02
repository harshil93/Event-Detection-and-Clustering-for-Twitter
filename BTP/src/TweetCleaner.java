import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



// TODO - Implement Smily detection and remove it.
// TODO - What to do with punctuations?
public  class TweetCleaner {
	private static Map<String, String> slangDict;
	private static Set<String> stopWords;
	private static String inTweetsFile = "/home/shobhit/btp/dataset/twitter_small/original/in.txt";
	private static String outTweetsFile = "/home/shobhit/btp/dataset/twitter_small/original/out.txt";
	static Pattern p;
	static{
		// Filenames are important
		String dictFile = "slangDict.txt";
		String stopWordsFile = "stopWords.txt";
		
		String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        p = Pattern.compile(urlPattern,Pattern.CASE_INSENSITIVE);
		
		// Preparing the dictionary
		try {
			slangDict = new HashMap<String, String>();
			BufferedReader br = new BufferedReader(new FileReader(dictFile));
			String line1;
			String line2 = null;
			while((line1 = br.readLine()) != null ){
				if(	(line2 = br.readLine()) != null )
					slangDict.put(line1.trim(), line2.trim());
			}
			br.close();
			
			// Preparing the stop words collection.
			stopWords = new HashSet<String>();
			br = new BufferedReader(new FileReader(stopWordsFile));
			while((line1 = br.readLine()) != null){
				stopWords.add(line1.trim());
			}
			br.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in reading slang or stop words file");
			System.exit(1);
		}
		
	}
	
	public static void replaceSlangs(List<String> tokens){
		String val;
		for (int i = 0; i < tokens.size(); i++){
			if((val = slangDict.get(tokens.get(i))) != null){
				tokens.set(i, val);
			}
		}
	}
	public static  List<String> removeStopWords(List<String> tokens){
		List<String> retTokens = new ArrayList<String>();
		for (int i = 0; i < tokens.size(); i++) {
			if(!stopWords.contains(tokens.get(i))){
				retTokens.add(tokens.get(i));
			}
		}
		return retTokens;
	}
	
	public static void clean(Tweet tw){
		String tweet = removeUrl(tw.getTweet());
		List<String> newToks = Twokenize.tokenizeRawTweetText(tweet);
		replaceSlangs(newToks);
		newToks = removeStopWords(newToks);
		tweet = String.join(" ", newToks);
		tw.setTweet(tweet);
	}
	
	public static String clean(String tw){
		String tweet = removeUrl(tw).toLowerCase();
		List<String> newToks = Twokenize.tokenizeRawTweetText(tweet);
		replaceSlangs(newToks);
		newToks = removeStopWords(newToks);
		tweet = String.join(" ", newToks);
		return tweet;
	}
	
	public static String removeUrl(String text)
    {
//        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
//        Pattern p = Pattern.compile(urlPattern,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        int i = 0;
        while (m.find()) {
            text = text.replaceAll(Pattern.quote(m.group(i)),"").trim();
            i++;
        }
        return text;
    }
	
	public static void readTweetsFromFile(){
		
		BufferedReader r = null;
		BufferedWriter w = null;	 
		String line;
		try {
			r = new BufferedReader(new FileReader(inTweetsFile));
			w = new BufferedWriter(new FileWriter(outTweetsFile));
			while ((line = r.readLine()) != null) {
//				String[] tokens = line.split(",");
//				tokens[5] = clean(tokens[5]);
				line = clean(line);
				w.write(line);
				w.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error in File I/O");
		} finally {
			try {
				r.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				w.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		readTweetsFromFile();
	}
}
