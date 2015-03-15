import json
import sys
from csv import writer
from itertools import izip

# TODO for RTs choose text from retweeted_status, rather than the tweet itself
def isRT(tweet):
    if tweet.split(' ')[0] == 'RT':
        return True
    else:
        return False

def removeNewLine(tweet):
    return tweet.replace('\n',' ').replace('\r',' ')

def mainFn(inFile, outFile):
    with open(inFile) as in_file, \
         open(outFile, 'w') as out_file:
        # print >> out_file, 'tweet_id, tweet_time, tweet_author, tweet_author_id,    tweet_language, tweet_geo, tweet_text'
        csv = writer(out_file)
        tweet_count = 0

        for line in in_file:
            if line in ['\n','\r\n']:
                continue
            tweet_count += 1
            tweet = json.loads(line)

            if isRT(tweet['text']) or tweet['lang'] != 'en':
                # print tweet['id']
                continue
            # Pull out various data from the tweets

            tweetText = removeNewLine(tweet['text'])
            row = (
                0,
                0,
                tweet['created_at'],            # tweet_time
                'NO_QUERY',
                tweet['user']['screen_name'],   # tweet_author
                tweetText                   # tweet_text
                # tweet['user']['id_str'],        # tweet_authod_id
            )
            values = [(value.encode('utf8') if hasattr(value, 'encode') else value) for value in row]
            csv.writerow(values)

    # print the name of the file and number of tweets imported
    print "File Imported:", str(inFile)
    print "# Tweets Imported:", tweet_count
    print "File Exported:", str(outFile)

# with open(sys.argv[1]) as in_file, open(sys.argv[2]) as out_file:
mainFn(sys.argv[1],sys.argv[2])
    # for line1, line2 in izip(in_file, out_file):
    #     mainFn(line1.replace('\n',''),line2.replace('\n',''))