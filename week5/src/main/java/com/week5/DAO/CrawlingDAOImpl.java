package com.week5.DAO;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.week5.VO.CrawlingVO;

@Repository
public class CrawlingDAOImpl implements CrawlingDAO {

	@Inject
	private SqlSession sql;

	public void insert(List<CrawlingVO> dataList) throws Exception {
		for (CrawlingVO vo : dataList) {
			sql.insert("crawling.insert", vo);
		}
	}
	
	public void update(List<CrawlingVO> dataList) throws Exception {
		for (CrawlingVO vo : dataList) {
			sql.update("crawling.update", vo);
		}
	}
	
}
