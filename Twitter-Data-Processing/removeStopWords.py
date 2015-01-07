import csv
import twitterTokenizer
from collections import Counter
import string
import operator
from frequency import countFreq

def process(filename):
	stopWords = countFreq(filename)

	f = open(filename,'rb')
	o = open(filename+".stopWordRemoved",'wb')
	tok = twitterTokenizer.Tokenizer(preserve_case=False)
	reader = csv.reader(f)
	writer = csv.writer(o,delimiter=',')
	words = {}
	remove_punctuation_map = dict((ord(char), None) for char in string.punctuation)
	dataset=[]

	for row in reader:
		# print row
		tokenized = [s.translate(remove_punctuation_map) for s in tok.tokenize(row[5])]
		tokenized = [s for s in tokenized if s]
		processedTweet = []
		for w in tokenized:
			if w not in stopWords:
				processedTweet.append(w)
		
		tweet = ''
		for w in processedTweet:
			tweet+=w+' '
		row[5]=tweet
		# print row
		dataset.append(row)
	
	writer.writerows(dataset)
	f.close()
	o.close()


process("../../dataset/twitter_small/training.1600000.processed.noemoticon.csv")