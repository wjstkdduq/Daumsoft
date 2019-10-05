package com.week5.VO;

public class CrawlingVO {
	
	private String newsID;
	private int siteNM;
	private String category;
	private String newsTitle;
	private String newsContent;
	private String newsURL;
	private String regDate;
	
	public CrawlingVO() {
		// TODO Auto-generated constructor stub
	}

	public CrawlingVO(String newsID, int siteNM, String category, String newsTitle, String newsContent, String newsURL,
			String regDate) {
		super();
		this.newsID = newsID;
		this.siteNM = siteNM;
		this.category = category;
		this.newsTitle = newsTitle;
		this.newsContent = newsContent;
		this.newsURL = newsURL;
		this.regDate = regDate;
	}

	public String getNewsID() {
		return newsID;
	}

	public void setNewsID(String newsID) {
		this.newsID = newsID;
	}

	public int getSiteNM() {
		return siteNM;
	}

	public void setSiteNM(int siteNM) {
		this.siteNM = siteNM;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getNewsContent() {
		return newsContent;
	}

	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}

	public String getNewsURL() {
		return newsURL;
	}

	public void setNewsURL(String newsURL) {
		this.newsURL = newsURL;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
	
}
