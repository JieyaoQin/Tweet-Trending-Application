package com.spring.app.trending.models;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;

import java.sql.Timestamp;
import java.util.*;

public class CustomModel {
	MongoClient mongoClient;
	MongoDatabase database;
	MongoCollection<Document> tweets;
	MongoCollection<Document> keyWord;
	MongoCollection<Document> keyPair;
	MongoClientURI uri = new MongoClientURI(
		    "<mongodb-uri>");
	
	public CustomModel() {
		mongoClient = new MongoClient(uri);
		database  = mongoClient.getDatabase("TwitterStream");
//		database  = mongoClient.getDatabase("Test");
		tweets = database.getCollection("Tweets");
		keyWord = database.getCollection("KeyWord");
		keyPair = database.getCollection("KeyPair");
	}
	
	public List<String> getKeyPair(String keyword) {
		Document query = keyPair.find(eq("keyword", keyword)).first();
		Map<String, Integer> pairs = (Map<String, Integer>) query.get("children");
//		Queue<Map.Entry<String, Integer>> heap = new PriorityQueue<Map.Entry<String, Integer>>(
//				new Comparator<Map.Entry<String, Integer>>() {
//					public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
//						return (int) (o2.getValue() - o1.getValue());
//					}
//				});
//		heap.addAll(pairs.entrySet());
//		List<String> res = new ArrayList<>();
//		int i = 0;
//		while(i < 10 && !heap.isEmpty()) {
//			res.add(heap.poll().getKey());
//			i++;
//		}
		
		List<String> res = new ArrayList<>(pairs.keySet());
		return res.size() >= 10? res.subList(0, 10): res;
	}
	
	public List<KeyWord> getKeyPairFreq(String keyword) {
		Document query = keyPair.find(eq("keyword", keyword)).first();
		if(query == null) return null;
		Map<String, Integer> pairs = (Map<String, Integer>) query.get("children");
		
		Queue<Map.Entry<String, Integer>> heap = new PriorityQueue<Map.Entry<String, Integer>>(
				new Comparator<Map.Entry<String, Integer>>() {
					public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
						return (int) (o2.getValue() - o1.getValue());
					}
				});
		heap.addAll(pairs.entrySet());
//		List<String> res = new ArrayList<>();
//		int i = 0;
//		while(i < 10 && !heap.isEmpty()) {
//			res.add(heap.poll().getKey());
//			i++;
//		}
		
//		List<String> keys = new ArrayList<>(pairs.keySet());
//		keys = keys.size() > 10? keys.subList(0, 10): keys;
//		
		List<KeyWord> res = new ArrayList<KeyWord>();
		int i = 0;
		while(i < 10 && !heap.isEmpty()) {
			Map.Entry<String, Integer> e = heap.poll();
			res.add(new KeyWord(e.getKey(), null, e.getValue()));
			i++;
			if(i >= 10) break;
		}
		
		return res;
	}
	
	
	public List<String> getURL(String word){
		Document doc = keyWord.find(eq("keyword", word)).first();
		
		// get lastest 100
		List messages = (ArrayList)doc.get("message");
		List ids = messages.size() > 100?
				messages.subList(messages.size()-100, messages.size()): messages;
		// List ids = ((ArrayList)doc.get("message"));
		Collections.reverse(ids);
		List<String> res = new ArrayList<String>();
		
		for(int i = 0; i < ids.size(); i++) {
			Document item = tweets.find(eq("_id", ids.get(i))).first();
			if(item == null) continue;
			String mes = (String) item.get("message");
			if(mes == null || res.contains(mes)) continue;
			res.add(mes);
			if(res.size() >= 10) break;
		}
		
//		System.out.println(res);
		return res;
	}
	
	public List<String> getTopTen() {
//		FindIterable<Document> docs = keyWord.find();
//		Queue<KeyWord> heap = new PriorityQueue<KeyWord>(new Comparator<KeyWord>() {
//			public int compare(KeyWord o1, KeyWord o2) {
//				return (int) (o2.getFrequency() - o1.getFrequency());
//			}
//		});
//		for(Document doc: docs) {
//			KeyWord kw = new KeyWord(
//					(String)doc.get("keyword"), 
//					(List<String>)doc.get("message"), 
//					(Integer)doc.get("frequency"));
//			kw.setId((String)doc.get("id"));
//			heap.offer(kw);
//		}
//		int i = 0;
//		List<String> res = new ArrayList<>();
//		while(i < 10 && !heap.isEmpty()) {
//			res.add(heap.poll().getKeyword());
//			i++;
//		}
		
		FindIterable<Document> docs = keyWord.find().sort( new BasicDBObject( "frequency" , -1 ) );
		int i = 0;
		List<String> res = new ArrayList<>();
		for(Document doc: docs) {
			res.add((String) doc.get("keyword"));
			i++;
		}
		return res;
	}
	
	public List<KeyWord> getTopTenFreq() {
		
//		Timestamp ts0 = new Timestamp(System.currentTimeMillis());
		FindIterable<Document> docs = keyWord.find().sort( new BasicDBObject( "frequency" , -1 ) ).limit(10);
		// FindIterable<Document> docs = keyWord.find().limit(10);
		// int i = 0;
		List<KeyWord> res = new ArrayList<>();
		for(Document doc: docs) {
			KeyWord kw = new KeyWord(
					(String)doc.get("keyword"), 
					(List<String>)doc.get("message"), 
					(Integer)doc.get("frequency"));
			kw.setId((String)doc.get("id"));
			res.add(kw);
			// i++;
			// if(i == 10) break;
		}
		
//		Timestamp ts1 = new Timestamp(System.currentTimeMillis());
//		System.out.println("Time DIFF: " + ((ts1.getTime() - ts0.getTime())));
		return res;
		
	}
	
	public List<KeyWord> getTopTenFreq_ori() {
//		Timestamp ts0 = new Timestamp(System.currentTimeMillis());
//		FindIterable<Document> docs = keyWord.find();
//		Queue<KeyWord> heap = new PriorityQueue<KeyWord>(new Comparator<KeyWord>() {
//			public int compare(KeyWord o1, KeyWord o2) {
//				return (int) (o2.getFrequency() - o1.getFrequency());
//			}
//		});
//		for(Document doc: docs) {
//			KeyWord kw = new KeyWord(
//					(String)doc.get("keyword"), 
//					(List<String>)doc.get("message"), 
//					(Integer)doc.get("frequency"));
//			kw.setId((String)doc.get("id"));
//			heap.offer(kw);
//		}
//		int i = 0;
//		List<KeyWord> res = new ArrayList<>();
//		while(i < 10 && !heap.isEmpty()) {
//			res.add(heap.poll());
//			i++;
//		}
//		Timestamp ts1 = new Timestamp(System.currentTimeMillis());
//		System.out.println("Time DIFF: " + ((ts1.getTime() - ts0.getTime())));
//		return res;
		
		Timestamp ts0 = new Timestamp(System.currentTimeMillis());
		FindIterable<Document> docs = keyWord.find().sort( new BasicDBObject( "frequency" , -1 ) );
		int i = 0;
		List<KeyWord> res = new ArrayList<>();
		for(Document doc: docs) {
			KeyWord kw = new KeyWord(
					(String)doc.get("keyword"), 
					(List<String>)doc.get("message"), 
					(Integer)doc.get("frequency"));
			kw.setId((String)doc.get("id"));
			res.add(kw);
			i++;
			if(i == 10) break;
		}
		
		Timestamp ts1 = new Timestamp(System.currentTimeMillis());
		System.out.println("Time DIFF: " + ((ts1.getTime() - ts0.getTime())));
		return res;
		
	}


//	public static void main(String[] args) {
//		CustomModel m = new CustomModel();
//		String target = "love";
////		List<String> keyPairs = m.getKeyPair(target);
////		List<String> urls = m.getURL(target);
//		
//		Timestamp ts0 = new Timestamp(System.currentTimeMillis());
//		System.out.println("Time START: " + ts0);
//		
////		List<KeyWord> keyPairs = m.getKeyPairFreq(target);
////		List<KeyWord> top10 = m.getTopTenFreq();
//		List<String> urls = m.getURL(target);
//		System.out.println(urls);
//		
//		Timestamp ts1 = new Timestamp(System.currentTimeMillis());
//		System.out.println("Time END: " + ts1);
//		System.out.println("Time DIFF: " + ((ts1.getTime() - ts0.getTime())/1000));
//	}
	
	
}
