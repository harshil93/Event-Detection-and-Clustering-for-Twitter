import csv
import twitterTokenizer
from collections import Counter
import string
import operator

threshold = 10000

def countFreq(filename):
	f = open(filename,'rb')
	tok = twitterTokenizer.Tokenizer(preserve_case=False)
	reader = csv.reader(f)
	words = {}
	remove_punctuation_map = dict((ord(char), None) for char in string.punctuation)
	for row in reader:
		tokenized = [s.translate(remove_punctuation_map) for s in tok.tokenize(row[5])]
		tokenized = [s for s in tokenized if s]
		
		for w in tokenized:
			if w in words:
				words[w] += 1
			else:
				words[w] = 1;
	sortedFreq = sorted(words.items(), key=operator.itemgetter(1), reverse=True)
	f.close()
	# print len(sortedFreq)
	# print sortedFreq

	topWords = set()

	for term,freq in sortedFreq:
		if freq>threshold:
			topWords.add(term)
	return topWords


# countFreq("../../dataset/twitter_small/training.1600000.processed.noemoticon.csv")
# countFreq("sample.csv")