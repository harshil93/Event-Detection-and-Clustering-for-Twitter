import csv
import twitterTokenizer
from collections import Counter
import string

class TweetDoc:
	def __init__(self, tempDict):
		self.tokens = tempDict
		self.numOfUniqueWords = len(tempDict)

def extract_hash_tags(s):
	return set(part[1:].lower() for part in s.split() if part.startswith('#'))

def processCsv(filename):
	f = open(filename,'rb')
	tok = twitterTokenizer.Tokenizer(preserve_case=False)
	reader = csv.reader(f)
	tweetCol = []
	words = set()
	remove_punctuation_map = dict((ord(char), None) for char in string.punctuation)
	for row in reader:
		tokenized = [s.translate(remove_punctuation_map) for s in tok.tokenize(row[5])]
		tokenized = [s for s in tokenized if s]
		tweetCol.append(TweetDoc(Counter(tokenized)))
		for elem in tweetCol[-1].tokens:
			words.add(elem)
	print 'Tweet CSV parsing complete'
	intvocabMap = dict(enumerate(words))
	new_dict = dict((value, key) for key, value in intvocabMap.iteritems())
	fdat = open(filename+'.dat','w')
	fvoc = open(filename+'.vocab','w')
	print 'Writing Start'
	for tweetelem in tweetCol:
		fdat.write(`tweetelem.numOfUniqueWords`+" ")
		for key, value in tweetelem.tokens.iteritems():
			fdat.write(`new_dict[key]`+":"+`value`+" ")
		fdat.write("\n")
	fdat.close()
	for key,value in intvocabMap.iteritems():
		fvoc.write(value+'\n')
	fvoc.close()
	f.close()

def generateAggregatedTweetsFiles(filename):
	f = open(filename,'rb')
	tok = twitterTokenizer.Tokenizer(preserve_case=False)
	reader = csv.reader(f)
	hashtags_tweets = {}
	remove_punctuation_map = dict((ord(char), None) for char in string.punctuation)
	for row in reader:
		tokenized = [s.translate(remove_punctuation_map) for s in tok.tokenize(row[5])]
		tokenized = [s for s in tokenized if s]
		if row[3] not in hashtags_tweets:
			hashtags_tweets[row[3]] = []
		hashtags_tweets[row[3]].append(' '.join(tokenized))
	for topic in hashtags_tweets:
		f = open('./AggregatedByTopic/'+topic+'.txt','w')
		for tweet in hashtags_tweets[topic]:
			f.write(tweet)
			f.write('\n')
		f.close()

def safeFileName(filename):
	keepcharacters = ('.','_')
	return "".join(c for c in filename if c.isalnum() or c in keepcharacters).rstrip()
def generateAggregatedTweetsFilesFromHashTags(filename):
	f = open(filename,'rb')
	tok = twitterTokenizer.Tokenizer(preserve_case=False)
	reader = csv.reader(f)
	hashtags_tweets = {}
	hashtags_tweets['no$hashtag'] = []
	remove_punctuation_map = dict((ord(char), None) for char in string.punctuation)
	num = 0
	for row in reader:
		print num
		num = num + 1
		hashtags = extract_hash_tags(row[5])
		tokenized = [s.translate(remove_punctuation_map) for s in tok.tokenize(row[5])]
		tokenized = [s for s in tokenized if s]
		tokenized_tweet = ' '.join(tokenized)

		if len(hashtags) == 0:
			hashtags_tweets['no$hashtag'].append(tokenized_tweet)
		else:
			for hashtag in hashtags:
				if hashtag not in hashtags_tweets:
					hashtags_tweets[hashtag] = []
				hashtags_tweets[hashtag].append(tokenized_tweet)
		
	for topic in hashtags_tweets:
		try:
			f = open('./AggregatedByTopic/'+ safeFileName(topic)+'.txt','w')
			for tweet in hashtags_tweets[topic]:
				f.write(tweet)
				f.write('\n')
			f.close()
		except Exception, e:
			continue
		
generateAggregatedTweetsFilesFromHashTags('./Dataset/training.csv')
#generateAggregatedTweetsFiles('./Dataset/testdata.csv')
# processCsv("../../dataset/twitter_small/stopWordRemoved/training.csv.stopWordRemoved")