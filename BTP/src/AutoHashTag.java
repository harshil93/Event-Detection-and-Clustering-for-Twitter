import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AutoHashTag {
	private static double threshold = 0.3;

//	private class Pair {
//		public Integer tweetID;
//		public String hashtag;
//
//		Pair(Integer tweetID, String hashtag) {
//			this.tweetID = tweetID;
//			this.hashtag = hashtag;
//		}
//	}

	private static double retCosScore(int num, HashMap<String, Integer> utTweet,
			HashMap<String, Integer> tTweet) {

		double d1=0;
		double d2=0;
		for (Entry<String, Integer> t : utTweet.entrySet()) {
			d1 += t.getValue() * t.getValue();
		}
		d1=Math.sqrt(d1);
		
		for (Entry<String, Integer> t : tTweet.entrySet()) {
			d2 += t.getValue() * t.getValue();
		}
		d2=Math.sqrt(d2);
		return (((double)num) / (d1*d2));
	}

	public static HashMap<Integer, HashSet<String>> genTags() {
		ArrayList<Tweet> taggedTweets = DatabaseAPI.extractTaggedTweets();
		ArrayList<Tweet> untaggedTweets = DatabaseAPI.extractUntaggedTweets();

		ArrayList<HashMap<String, Integer>> taggedTweetMap = new ArrayList<HashMap<String, Integer>>();
		for (int i = 0; i < taggedTweets.size(); i++) {
			taggedTweetMap.add(new HashMap<String, Integer>());
			String[] tokens = taggedTweets.get(i).getTweet().split("\\s+");
			for (String t : tokens) {
				HashMap<String, Integer> temp = taggedTweetMap.get(i);
				if (temp.containsKey(t))
					temp.put(t, temp.get(t) + 1);
				else
					temp.put(t, 1);
			}
		}

		ArrayList<HashMap<String, Integer>> untaggedTweetMap = new ArrayList<HashMap<String, Integer>>();
		for (int i = 0; i < untaggedTweets.size(); i++) {
			untaggedTweetMap.add(new HashMap<String, Integer>());
			String[] tokens = untaggedTweets.get(i).getTweet().split("\\s+");
			for (String t : tokens) {
				HashMap<String, Integer> temp = untaggedTweetMap.get(i);
				if (temp.containsKey(t))
					temp.put(t, temp.get(t) + 1);
				else
					temp.put(t, 1);
			}
		}

		HashMap<Integer, HashSet<String>> newTags = new HashMap<>();
		for (int i = 0; i < untaggedTweets.size(); i++) {
			HashMap<String, Integer> utTweet = untaggedTweetMap.get(i);
			for (int j = 0; j < taggedTweets.size(); j++) {
				
				HashMap<String, Integer> tTweet = taggedTweetMap.get(j);
				int num = 0;
				for (Entry<String, Integer> t : utTweet.entrySet()) {
					
					if (tTweet.containsKey(t.getKey())==true) {
						num += t.getValue() * tTweet.get(t.getKey()).intValue();
//						System.out.println("a="+t.getValue());
//						System.out.println("b="+tTweet.get(t.getKey()).intValue());
					}
				}
				if (retCosScore(num, utTweet, tTweet) > threshold) {
					for (String h : taggedTweets.get(j).getHashtags()) {
						Integer tweetID = untaggedTweets.get(i).getTweetID();
						if(newTags.containsKey(tweetID)) {
							HashSet<String> s = newTags.get(tweetID);
							s.add(h);
							newTags.put(tweetID, s);
						}
						else {
							HashSet<String> s = new HashSet<>();
							s.add(h);
							newTags.put(tweetID, s);
						}
					}
				}

			}
		}
		return newTags;
	}

	public static void main(String[] args) {
		DatabaseAPI.establishConnection();
		HashMap<Integer, HashSet<String>> newTags = AutoHashTag.genTags();
		for (Entry<Integer,HashSet<String>> e : newTags.entrySet()) {
			System.out.print(e.getKey()+":");
			for (String s : e.getValue()) {
				System.out.print(s+",");
			}
			System.out.print("\n");
		}
	}

}
