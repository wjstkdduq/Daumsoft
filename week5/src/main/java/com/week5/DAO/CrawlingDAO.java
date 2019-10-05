package com.week5.DAO;

import java.util.List;

import com.week5.VO.CrawlingVO;

public interface CrawlingDAO {

	void insert(List<CrawlingVO> dataList) throws Exception;

	void update(List<CrawlingVO> dataList) throws Exception;

	
	
}
