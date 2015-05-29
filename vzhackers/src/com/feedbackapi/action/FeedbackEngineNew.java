package com.feedbackapi.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class FeedbackEngineNew {

	public String findFeedBack(String feedBackstring) {
		String feedbackResult = null;
		if (feedBackstring != null) {
			feedBackstring = feedBackstring.toLowerCase();
			String[] sentenceArray = splitSentenceArray(feedBackstring);
			ArrayList<String[]> commaArrayList = splitCommaArray(sentenceArray);
			feedbackResult = getFeedBackstring(commaArrayList);
		} else {
			feedbackResult = "No String";
		}
		return feedbackResult;
	}

	private String getFeedBackstring(ArrayList<String[]> commaArrayList) {
		int finalresult = 0;
		for (String[] commaArrayListEle : commaArrayList) {
			String[] strCommaArray = commaArrayListEle;
			int result = 0;
			for (int i = 0; i < strCommaArray.length; i++) {
				String str = strCommaArray[i];
				if (strCommaArray.length > 1) {
					result = splitGrammartoNode(str);
				} else {
					result += splitGrammartoNode(str);
				}
			}
			if (result == 0)
				finalresult += 0;
			else if (result > 0)
				finalresult++;
			else
				finalresult--;
		}
		if (finalresult == 0)
			return "Neutral";
		else if (finalresult > 0)
			return "Positive";
		else
			return "Negative";
	}

	private int splitGrammartoNode(String str) {
		String[] butSplit = str.split("but");
		int result = 0;
		for (int i = 0; i < butSplit.length; i++) {
			if (butSplit.length > 1) {
				result = getResult(butSplit[i]);
			} else {
				result += getResult(butSplit[i]);
			}
		}
		if (result == 0)
			return 0;
		else if (result > 0)
			return 1;
		else
			return -1;
	}

	private int getResult(String string) {
		String[] spcaeSplitStr = string.split(" ");
		String counts = checkInDictionaryWords(spcaeSplitStr);
		int pcnt = Integer.parseInt(counts.split("_")[0]);
		int ncnt = Integer.parseInt(counts.split("_")[1]);
		int zcnt = Integer.parseInt(counts.split("_")[2]);
		int fcnt = 0;
		if (pcnt == ncnt) {
			fcnt = 0;
		} else if (pcnt > ncnt) {
			fcnt = 1;
		} else {
			fcnt = -1;
		}
		return fcnt;
	}

	private String checkInDictionaryWords(String[] wordsArray) {
		String[] words = null;
		int pcount = 0;
		int ncount = 0;
		int zcount = 0;
		CacheManager cacheManager = Dictionary.cm;
		Cache cache = cacheManager.getCache("cache1");
		List list = cache.getKeys();
		int listSize = list.size();
		String[] dbWords = new String[listSize];
		int parrcnt = 0;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String pwords = (String) iterator.next();
			Element ele = (Element) cache.get(pwords);
			if (ele.getObjectValue() != null && !ele.getObjectValue().equals("")) {
				dbWords[parrcnt] = pwords + "_" + ele.getObjectValue();
				parrcnt++;
			}
		}
		words = dbWords;
		for (int i = 0; i < wordsArray.length; i++) {
			int nottemp = 0;
			int notindex = 0;
			int negativeflag = 0;
			int negativeindex = 0;
			int positiveflag = 0;
			int positiveindex = 0;
			String firstWord = wordsArray[i].toLowerCase();
			for (int j = 0; j < words.length; j++) {
				if (words[j] != null) {
					String secondWord = words[j].split("_")[0].toLowerCase();
					String type = words[j].split("_")[1];
					if ("N".equalsIgnoreCase(type)) {
						if (firstWord.equalsIgnoreCase(secondWord)) {
							ncount++;
							negativeflag = 1;
						}
						if ("not".equalsIgnoreCase(firstWord)) {
							negativeflag = 1;
							ncount++;
							nottemp = 1;
							notindex = i;
						}
					} else if ("P".equalsIgnoreCase(type)) {
						if (firstWord.equalsIgnoreCase(secondWord)) {
							if (nottemp == 1 && notindex + 1 == i) {
								pcount += 0;
							} else {
								positiveflag = 1;
								positiveindex = i;
								pcount++;
							}
						}
					} else {
						if (firstWord.equalsIgnoreCase(secondWord)) {
							zcount++;
						}
					}
				}
			}
		}
		return pcount + "_" + ncount + "_" + zcount;
	}

	private ArrayList<String[]> splitCommaArray(String[] sentenceArray) {
		ArrayList<String[]> commaArrayList = new ArrayList<String[]>();
		for (int i = 0; i < sentenceArray.length; i++) {
			if (!"".equalsIgnoreCase(sentenceArray[i].trim())) {
				String[] commaArray = sentenceArray[i].split(",");
				commaArrayList.add(commaArray);
			}
		}
		return commaArrayList;
	}

	private String[] splitSentenceArray(String feedBackstring) {
		String[] sentenceArray = feedBackstring.split("\\.");
		return sentenceArray;
	}
}