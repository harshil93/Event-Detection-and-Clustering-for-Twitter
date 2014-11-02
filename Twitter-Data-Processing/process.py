import csv
import twitterTokenizer
from collections import Counter
import string

class TweetDoc:
	def __init__(self, tempDict):
		self.tokens = tempDict
		self.numOfUniqueWords = len(tempDict)

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

processCsv("training.csv")