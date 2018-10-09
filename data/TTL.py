from pymongo import MongoClient
import datetime, time

class TTL:

    def __init__(self):
        # self.client = MongoClient('localhost', 27017)
        self.client = MongoClient(<mongodb-uri>)
        self.db = self.client.TwitterStream
        # self.db = self.client.trending
        self.t_KeyWord = self.db.KeyWord
        self.t_KeyPair = self.db.KeyPair
        self.t_Tweets = self.db.Tweets

    def update(self):
        """Execute database garbage collection"""
        # time = time.time() -
        _time = datetime.datetime.utcnow() - datetime.timedelta(hours=24)
        print("The earliest valid timestamp: {}".format(_time))
        # _time = datetime.datetime.utcnow() - datetime.timedelta(minutes=12*60)
        # filter those tweets with time before _time(24 hours ago)
        cur = self.t_Tweets.find({"date": {"$lt": _time}})
        # extract message to update KeyWord and KeyPair, then delete it in Tweets
        for d in cur:
            keyword = d['keyword']
            _id = d['_id']
            self.keyword_update(keyword, _id)
            self.pair_update(keyword)
            self.t_Tweets.delete_one({"_id": _id})
            # print(_id)
            
    def keyword_update(self, keyword, _id):
        """For each kw in keyword, delete _id, freq--"""
        for kw in keyword:
            c = self.t_KeyWord.find_one({"keyword": kw})
            if c is None or not c['message'].__contains__(_id):
                continue
            c['message'].remove(_id)
            c['frequency'] -= 1
            if c['frequency'] < 0:
                c['frequency'] = 0
            mongo_id = c['_id']
		
            # delete if empty co-exist
            # if c['frequency'] <= 0:
            #     self.t_KeyWord.delete_one({"_id": mongo_id})
            #     print(mongo_id)
            # else:
            #     self.t_KeyWord.update_one({'_id': mongo_id}, {"$set": c}, upsert=False)
            self.t_KeyWord.update_one({'_id': mongo_id}, {"$set": c}, upsert=False)
        
    def pair_update(self, keyword):
        """For every pair of kws in keywords, reduce their counts by 1"""
        for i in range(len(keyword)):
            for j in range(i+1, len(keyword)):

                k_1 = keyword[i]
                k_2 = keyword[j]

                c_1 = self.t_KeyPair.find_one({"keyword": k_1})
                c_2 = self.t_KeyPair.find_one({"keyword": k_2})

                if c_1 is not None and c_2 is not None:
                    c_2_kw = c_2["keyword"]

                    if c_1['children'].__contains__(c_2_kw):
                        c_1['children'][c_2_kw] -= 1
                        if c_1['children'][c_2_kw] <= 0:
                            del c_1['children'][c_2_kw]

                            # print(c_2_kw)

                    mongo_id_1 = c_1['_id']
                    # if len(c_1['children']) == 0:
                    #     self.t_KeyPair.delete_one({"_id": mongo_id_1})
                    #     print(mongo_id_1)
                    # else:
                    #     self.t_KeyPair.update_one({'_id': mongo_id_1}, {"$set": c_1}, upsert=False)
                    self.t_KeyPair.update_one({'_id': mongo_id_1}, {"$set": c_1}, upsert=False)


                    c_1_kw = c_1["keyword"]
                    if c_2['children'].__contains__(c_1_kw):
                        c_2['children'][c_1_kw] -= 1
                        if c_2['children'][c_1_kw] <= 0:
                            del c_2['children'][c_1_kw]

                            # print(c_1_kw)

                        mongo_id_2 = c_2['_id']
                        self.t_KeyPair.update_one({'_id': mongo_id_2}, {"$set": c_2}, upsert=False)




                # why remove?? shouldn't we reduce the frequency by 1?
                # c_1['children'].remove(c_2)
                # c_2['children'].remove(c_1)


if __name__ == "__main__":
    ttl = TTL()
    while True:
        time.sleep(30)
        ttl.update()
        print("TTL Executed! Current time: {}"
              .format(datetime.datetime.utcnow()))

                    
