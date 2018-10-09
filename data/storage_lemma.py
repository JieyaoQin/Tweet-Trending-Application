from pymongo import MongoClient
import datetime
import json
from tweepy.streaming import StreamListener
from tweepy import OAuthHandler
from tweepy import Stream
from nltk.corpus import stopwords
from nltk.stem import PorterStemmer
from nltk.stem.wordnet import WordNetLemmatizer
from nltk.corpus import wordnet as wn
import numpy as np
import gensim
import string
import re
from urllib3.exceptions import ProtocolError


client = MongoClient(<mongodb-uri>)
db = client.TwitterStream

# create some collections
t_KeyWord = db.KeyWord
t_KeyPair = db.KeyPair
t_Tweets = db.Tweets

stop_words = stopwords.words('english')
filename = 'GoogleNews-vectors-negative300.bin.gz'
predictor = gensim.models.KeyedVectors.load_word2vec_format(filename, binary=True)

# keys/tokens for tweepy
consumer_key = <>
consumer_secret = <>
access_token = <>
access_token_secret = <>

# Data Structure:
#t_KeyWord{
#   _id:default,
#   keyword:keyword,
#   message:[],出现过的message_id
#   frequency:int
# }

#t_Tweets{
#   _id:default,
#   message: message,# url of the tweet
#   keyword: [],
#   date: datetime
# }

#t_KeyPair{
#   keyword:keyword,
#   children:{
#               key:value # the associated keyword and the frequency of appearance
#            }
#
# }
#

# store Tweets
def store(keyword,message):# list and url
    Mes = {
        "message": message, # url of the tweet
        "keyword": keyword,
        "date": datetime.datetime.utcnow()# use the current time
    }

    t_Tweets.insert_one(Mes).inserted_id # generates a default _id
    mes_id = t_Tweets.find_one({"message":message})['_id']

    for i in range(len(keyword)):
        update_KeyWord(keyword[i],mes_id)
        for j in range(len(keyword)):
            if i != j:
                update_Keypair(keyword[i],keyword[j])


# update the keyword table
def update_KeyWord(keyword,mes_id):

    data = {"keyword":keyword}

    #if current word is not in the table, post a new one
    if not t_KeyWord.find_one(data):
        post = {
            "keyword": keyword,
            "message": [mes_id],
            "frequency": 1
        }

        t_KeyWord.insert_one(post).inserted_id

    #if already in the table, update tweet_id_list and frequency
    else:
        cur = t_KeyWord.find_one(data)
        cur['message'].append(mes_id)
        cur['frequency'] =  cur['frequency']+1
        mongo_id = cur['_id']

        t_KeyWord.update_one({'_id': mongo_id}, {"$set": cur}, upsert=False)


def update_Keypair(keyword_1,keyword_2):

    data = {"keyword":keyword_1}

    if not t_KeyPair.find_one(data):
        post = {
            "keyword": keyword_1,
            "children":{
                        keyword_2:1
                    }
        }

        t_KeyPair.insert_one(post).inserted_id

    else:

        cur = t_KeyPair.find_one(data)
        mongo_id = cur['_id']

        if cur['children'].__contains__(keyword_2):
            num = cur['children'][keyword_2]+1
            cur['children'][keyword_2] = num

        else:
            cur['children'][keyword_2] = 1

        t_KeyPair.update_one({'_id': mongo_id}, {"$set": cur}, upsert=False)

# def wordnet_filter(word):
#     lemma_set = wn.lemmas(word)
#     if len(lemma_set) == 0:
#         return True
#     lemma_count = [lemma.count() for lemma in lemma_set]
#     max_index = np.argmax(lemma_count)
#     if lemma_set[max_index].synset().pos() in ['a', 'r', 'v', 's']:
#         return False
#
#     return True

def wordnet_filter(word):
    wl = WordNetLemmatizer()
    lem_set = [wl.lemmatize(word, 'a'), wl.lemmatize(word, 'r'), wl.lemmatize(word, 'v'), wl.lemmatize(word, 's')]
    for lem in lem_set:
        if lem != word:
            return False

    lemma_set = wn.lemmas(word)
    if len(lemma_set) == 0:
        return True######
    lemma_count = [lemma.count() for lemma in lemma_set]
    max_index = np.argmax(lemma_count)
    if lemma_set[max_index].synset().pos() in ['a', 'r', 'v', 's']:
        return False

    return True


def prep(text):
    text = re.sub("[^A-Za-z]", " ", text)
    text = "".join(" " if x in string.punctuation else x for x in text.lower())
    safety_belt = ["fucks", "porno", "anal", "chick", "chicks", "girls", "woman", "women", "boy",
                   "boys", "man", "men", "videos", "video", "pic", "pics", "pictures", "free", "tube",
                   "dick", "suck", "sucking", "ass", "naked", "girl", "fucking", "nude", "sexy", "porn",
                   "pornography", "fuck", "sex", "sexes", "fucked", "pussy", "slut", "xxx", "bitch",
                   "motherfucker", "fucker", "sucker"]
    tokens = text.split()
    ps = PorterStemmer()
    filtered_token_0 = [t for t in tokens if not t in stop_words and t in predictor.vocab]
    filtered_token_1 = [t for t in filtered_token_0 if len(t) > 3 and t != 'http' and t != 'https']
    filtered_token_2 = [w for w in filtered_token_1 if w not in safety_belt]
    # filtered_token_3 = list(set([ps.stem(w) for w in filtered_token_2]))
    filtered_token_4 = [w for w in filtered_token_2 if wordnet_filter(w)]
    return filtered_token_4

class StdOutListener(StreamListener):
    def on_data(self, data):
        # Load the Tweet into the variable "t"
        t = json.loads(data)
        if 'entities' in t and t['lang'] == 'en' and len(t['text']) > 0:
            text = t['text']
            if 'media' in t['entities']:
                expanded_url = t['entities']['media'][0]['expanded_url']
                print(expanded_url)
                keywords = prep(text)
                if len(keywords) > 1:
                    store(keywords, expanded_url)
                    print(keywords)# only store it if len(keywords) > 1

        return True

    def on_error(self, status):
        print(status)

if __name__ == '__main__':
    l = StdOutListener()
    auth = OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)

    stream = Stream(auth, l)
    while True:
        try:
            stream.sample()
        except ProtocolError:
            continue
