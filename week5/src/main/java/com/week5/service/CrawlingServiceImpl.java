package com.week5.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.week5.DAO.CrawlingDAO;
import com.week5.VO.CrawlingVO;

@Service
public class CrawlingServiceImpl implements CrawlingService {

	@Inject
	CrawlingDAO dao;

	Boolean loopEscape = false;
	
	public int getParameter(int page) {
		return page;
	}

	public void insightCrawling(String categoryURL, String categoryNM) {

      String insightURL = "https://www.insight.co.kr/" + categoryURL;

      List<CrawlingVO> dataList = new ArrayList<CrawlingVO>();
      CrawlingVO vo = null;

      for (int i = 1; i < 100; i++) {
         // 기사 목록 화면
         Document doc;

         try {
            doc = Jsoup.connect(insightURL + "page=" + getParameter(i)).timeout(0).get();


            // 카테고리
            Elements categoryElements = doc.select(
                  ".nav-wrap .nav-ul li a[href=\"https://www.insight.co.kr/section/" + categoryNM + "\"]");

            // 포털사이트 이름
            Elements webElements = doc.select(".section-gnb a[title=\"인사이트\"]");

            // 기사 목록
            Elements elements = doc.select(".section-list li");

            String newsURL;

            for (Element element : elements) {

               // 기사의 상세 내용 URL
               newsURL = ""; // 기사 상세 URL 초기화
               newsURL = element.select("a").attr("href");

               Document detailDoc = Jsoup.connect(newsURL).timeout(0).get();
               Elements detailElements = detailDoc.select(".news-container");

               // 기사URL에서 기사코드만 가져오기 위해 스플릿
               String[] newsURLArray = newsURL.split("/");
               String newsID = newsURLArray[4];

               // 기사 등록일만 구하기 위해 substring으로 앞에 기자 이름 제거
               String tempDate = element.select(".section-list-article-byline").text();
               int tempDateLength = tempDate.length();
               String regDate = tempDate.substring(tempDateLength - 19, tempDateLength);

               // 현재날짜
               String currentTime;
               // 일주일 전 날짜
               String weekAgo;

               // 7일전 구하기
               Calendar calendar = Calendar.getInstance();
               SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
               Date date = new Date();
               calendar.setTime(date);
               currentTime = format.format(calendar.getTime());

               calendar.add(Calendar.DATE, -3);
               weekAgo = format.format(calendar.getTime());

               int compare = weekAgo.compareTo(regDate);
               
               // 7일전에 해당하는 기사만 가져옴
               if (compare > 0) {
                  System.out.println("끝");
                  loopEscape = true;
                  break;
               }

               // 포털사이트 이름
               String siteNM = webElements.text();

               if (siteNM.equals("인사이트")) {
                  siteNM = "1";
               }

               // 카테고리
               String category = categoryElements.text();

               if (category.equals("엔터테인먼트")) {
                  category = "11";
               } else if (category.equals("스포츠")) {
                  category = "12";
               }

               for (Element detailElement : detailElements) {
                  // 기사 제목
                  String newsTitle = detailElement.select(".news-header").text();
                  // 기자 이름
                  String reporterName = detailElement.select(".news-byline > .news-byline-writer").text();
                  // 기자 메일
                  String reporterMail = detailElement.select(".news-byline > .news-byline-mail").text();
                  // 기사 내용
                  String newsContent = detailElement.select(".news-article-memo").text();
                  
                  System.out.println("newsID : " + newsID);
                  System.out.println("siteNM : " + siteNM);
                  System.out.println("category : " + category);
                  System.out.println("newsTitle : " + newsTitle);
                  System.out.println("newsContent : " + newsContent);
                  System.out.println("newsURL : " + newsURL);
                  System.out.println("regDate : " + regDate);
                  System.out.println("================================================");
                  
                  vo = new CrawlingVO(newsID, Integer.parseInt(siteNM), category, newsTitle, newsContent, newsURL, regDate);
                  
                  dataList.add(vo);
               }
            }
            dao.insert(dataList);
            dao.update(dataList);
            
            
            if(loopEscape) {
            	break;
            }
            
            
         } catch (IOException e) {
           e.printStackTrace();
           } catch (SQLException e) {
              System.out.println("데이터베이스 관련 예외 발생!");
              e.printStackTrace();
           } catch (ClassNotFoundException e) {
              e.printStackTrace();
           } catch (Exception e) {
              e.printStackTrace();
           }
      }
	}
	
	public void huffingtonPostCrawling(String categoryURL) {
		String huffingtonURL = "https://www.huffingtonpost.kr/" + categoryURL;

	      List<CrawlingVO> dataList = new ArrayList<CrawlingVO>();
	      CrawlingVO vo = null;

	      for (int i = 1; i < 100; i++) {
	         // 기사 목록 화면
	         Document doc;

	         try {
	            doc = Jsoup.connect(huffingtonURL + getParameter(i)).timeout(0).get();


	            // 카테고리
	            Elements categoryElements = doc.select(".master-container .section-name");
	            String category = categoryElements.text();
	            System.out.println("category : " + category);
	            
	            // 포털사이트 이름
	            Elements webElements = doc.select(".footer__bottom .footer__bottom__nav .footer__bottom__nav__item");
	            String siteNM = webElements.text().substring(6, 25);
	            System.out.println("siteNM : " + siteNM);

	            // 기사 목록
	            Elements elements = doc.select("div[class='col col--body-center bnp__card-list yr-col-body-center'] .card__headline");
	            System.out.println("elements : " + elements.text());
	            
	            String newsURL;

	            for (Element element : elements) {

	               // 기사의 상세 내용 URL
	               newsURL = ""; // 기사 상세 URL 초기화
	               newsURL = "https://www.huffingtonpost.kr" + element.select("a").attr("href");;
	               System.out.println("newsURL : " + newsURL);
	               
	               Document detailDoc = Jsoup.connect(newsURL).userAgent("Chrome").timeout(0).get();
	               Elements detailElements = detailDoc.select(".page__content");
	               
	               System.out.println("detail : " + detailElements.text());
					// 고유 idx를 가져오기 위함
	               String[] newsURLArray = newsURL.split("/");
	               String tempNewsID = newsURLArray[4];
	               String[] newsURLArray2 = tempNewsID.split("\\?");
	               String newsID = newsURLArray2[0];
	               System.out.println("newsID : " + newsID);

	               
	               
	               String regDate = null;
	               
	               

	               if (siteNM.equals("HuffingtonPostKorea")) {
	                  siteNM = "2";
	               }

	               if (category.equals("사회")) {
	                  category = "21";
	               } else if (category.equals("국회")) {
	                  category = "22";
	               }

	               for (Element detailElement : detailElements) {
	            	   
	            	   
	            	   regDate = detailElement.select("span[class=\"timestamp__date timestamp__date--published\"]").text();
	            	   System.out.println("regDate : " + regDate);
		               regDate = regDate.substring(0, 21);
		              
	                   SimpleDateFormat original_format = new SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분");
	                   SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	                   Date original_date = original_format.parse(regDate);
	                   regDate = new_format.format(original_date);
		               
		               
		               
		               // 현재날짜
		               String currentTime;
		               // 일주일 전 날짜
		               String weekAgo;

		               // 7일전 구하기
		               Calendar calendar = Calendar.getInstance();
		               SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		               Date date = new Date();
		               calendar.setTime(date);
		               currentTime = format.format(calendar.getTime());

		               calendar.add(Calendar.DATE, -7);
		               weekAgo = format.format(calendar.getTime());

		               int compare = weekAgo.compareTo(regDate);
		               
	            	   
	            	   
	                  // 기사 제목
	                  String newsTitle = detailElement.select(".headline__title").text();
	                  // 기사 내용
	                  String newsContent = detailElement.select("div[class='content-list-component text']").text();
	                  
	                  // 7일전에 해당하는 기사만 가져옴
	                  if (compare > 0) {
	                	  System.out.println("끝");
	                	  loopEscape = true;
	                	  break;
	                  }
	           
	                  System.out.println("newsID : " + newsID);
	                  System.out.println("siteNM : " + siteNM);
	                  System.out.println("category : " + category);
	                  System.out.println("newsTitle : " + newsTitle);
	                  System.out.println("newsContent : " + newsContent);
	                  System.out.println("newsURL : " + newsURL);
	                  System.out.println("regDate : " + regDate);
	                  System.out.println("================================================");
	                  
	                  vo = new CrawlingVO(newsID, Integer.parseInt(siteNM), category, newsTitle, newsContent, newsURL, regDate);
	                  
	                  dataList.add(vo);
	                  
	               }
	               
	               dao.insert(dataList);
	               dao.update(dataList);
	               if(loopEscape) {
		            	break;
		            }
	            }
	            
	            
	            if(loopEscape) {
	            	break;
	            }
	            
	            
	         } catch (IOException e) {
	           e.printStackTrace();
	           } catch (SQLException e) {
	              System.out.println("데이터베이스 관련 예외 발생!");
	              e.printStackTrace();
	           } catch (ClassNotFoundException e) {
	              e.printStackTrace();
	           } catch (Exception e) {
	              e.printStackTrace();
	           }
	         
	         
	      }
		}
	
	
}
            
                  
                  
                  
                  // 사이트 이름, 카테고리, 기사 URL, 기사 제목, 기자 이름, 기자메일, 기사 내용은 기사의 상세 내용에서 가져옴
//	               if(!newsContent.equals("")) {
//	                  System.out.println("기사 내용 : " + newsContent);
//	               }
//               }
//            }
        
               
//               String regDate = null;
//               // 등록일
//               String tempDate = detailElement.select(".timestamp span[class=\"timestamp__date timestamp__date--published\"]").text();
//               if (!tempDate.equals("")) {
//                  String subTempDate = tempDate.substring(0, 21);
//                  SimpleDateFormat original_format = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
//                  SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                  Date original_date;
//                  
//                  try {
//                     
//                     original_date = original_format.parse(subTempDate);
//                     regDate = new_format.format(original_date);
//                     System.out.println("등록일 : " + regDate);
//                     
//                  } catch (ParseException e) {
//                     e.printStackTrace();
//                  }
//
//               }
//               
//
//               // 현재날짜
////               String currentTime;
//               // 일주일 전 날짜
//               String weekAgo;
//
//               // 7일전 구하기
//               Calendar calendar = Calendar.getInstance();
//               SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//               Date date = new Date();
//               calendar.setTime(date);
////               currentTime = format.format(calendar.getTime());
//
//               calendar.add(Calendar.DATE, -7);
//               weekAgo = format.format(calendar.getTime());
//               
//               
//               System.out.println("weekAgo : "  + weekAgo + "      regDate : " + regDate);
//               int compare = weekAgo.compareTo(regDate);
//               
//               
//               
//               System.out.println("=========================================");
//               System.out.println("사이트 : " + siteNM + " 카테고리 : " + category + " 제목 : " + newsTitle + " 내용 : " + newsContent + " URL : " + newsURL + " 등록일 : " + regDate + " ID : " + newsID +"\n");
//               vo = new CrawlingVO(Integer.parseInt(siteNM), Integer.parseInt(category), newsTitle,
//                     newsContent, newsURL, regDate, newsID);
//
//               dataList.add(vo);
//               
//               // 7일전에 해당하는 기사만 가져옴
//               if (compare > 0) {
//                  System.out.println("끝");
//                  loopEscape = true;
//                  break;
//               }
//            }
//         }
//         System.out.println("넣는다");
//         dao.insert(dataList);
//         dao.update(dataList);
//         
//         if(loopEscape) {
//            break;
//         }
//
//      } catch (IOException e) {
//         e.printStackTrace();
//      } catch (SQLException e) {
//         System.out.println("데이터베이스 관련 예외 발생!");
//         e.printStackTrace();
//      } catch (ClassNotFoundException e) {
//         e.printStackTrace();
//      } catch (Exception e) {
//         e.printStackTrace();
//      }
//   }
           
