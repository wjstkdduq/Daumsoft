package com.week5.service;

public interface CrawlingService {

	void insightCrawling(String categoryURL, String categoryNM) throws Exception;

	void huffingtonPostCrawling(String categoryURL) throws Exception;

}
