package com.feedbackapi.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class Dictionary {
	static CacheManager cm = null;

	public void getWords() {
		try {
			Connection connection = DBConnect.getConnection();
			PreparedStatement ps = connection
					.prepareStatement("select * from feedback_entries");
			ResultSet rs = ps.executeQuery();
			Cache cache = getCacheManager("cache1");
			while (rs.next()) {
				String name = rs.getString("name");
				String type = rs.getString("type");
				if (name != null && type != null) {
					if (cache != null) {
						cache.put(new Element(name, type));
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Cache getCacheManager(String cacheName) {
		if (cm == null) {
			cm = CacheManager.getInstance();
		}
		Cache cache = null;
		if (cm.getCache(cacheName) == null) {
			cm.addCache(cacheName);
			cache = cm.getCache(cacheName);
		} else {
			cache = cm.getCache(cacheName);
		}
		return cache;
	}
}
