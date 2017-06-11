package com.dbcrawler.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dbcrawler.film.Film;
import com.dbcrawler.utils.RegexStringUtils;
import com.dbcrawler.utils.UtilFuns;

public class GetFilmsService implements Runnable{
	private List<String> filmsName;
	//存储url
	private List<String> filmsUrl;
	public List<String> getFilmsUrl() {
		return filmsUrl;
	}
	public void setFilmsUrl(List<String> filmsUrl) {
		this.filmsUrl = filmsUrl;
	}
	//存储所有film
	private List<Film> films = new ArrayList<Film>();
	public List<Film> getFilms() {
		return films;
	}
	public void setFilms(List<Film> films) {
		this.films = films;
	}
	//1.获得电影名称集合,存入filmsName
	//2.通过filmsName获得所有url,存入filmsUrl
	//3.开启多线程，查询每一个url
	public GetFilmsService(){
	}
	
	public GetFilmsService(List<String> queryNames){
		this.filmsName = queryNames;
		this.filmsUrl = getFilmUrl(queryNames);
	}
	/**
	 * 根据url获得film
	 * @param url
	 * @return
	 */
	public Film getSingleFilm(String url){
		if (url == null || "".equals(url)) {
			return null;
		}
		Film film = new Film();
		Connection connect = Jsoup.connect(url);
		Document document;
		try {
			document = connect.get();
//			System.out.println(document.toString());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("获取document异常");
		}
		
		//filmId
		UUID uuid = UUID.randomUUID();
		film.setFilmId(uuid.toString().replace("-", ""));
		//filmUrl
		String newUrl = url.substring(0, url.length()-1);
		film.setFilmUrl(newUrl);
		//获得filmName
		Elements filmNameEle = document.getElementsByAttributeValue("property", "v:itemreviewed");
		String filmName = filmNameEle.text();
		film.setFilmName(filmName);
//		System.out.println(filmName);
		//releaseDate
		Elements rdEle = document.getElementsByAttributeValue("property", "v:initialReleaseDate");
		StringBuilder sb2 = new StringBuilder();
		for (Element ele : rdEle) {
			sb2.append(ele.text());
		}
		String releaseDate = sb2.toString();
		film.setReleaseDate(releaseDate);
//		System.out.println(releaseDate);
		//获得year;如果elements<class="year">为空，就从releaseDate中取year
		Elements yearEle = document.getElementsByAttributeValue("class", "year");
		String year = yearEle.text();
//		if (UtilFuns.isEmpty(year)) {
//			Elements elements = document.getElementsByTag("h1");
//			System.out.println(elements.text());
			//Element spanele = elements.
			//year = spanele.text();
//			year = releaseDate.substring(0, 4);
//		}
//		System.out.println(year);
		film.setFilmYear(Integer.valueOf(year.substring(1, year.length()-1)));
//		String regexString = RegexStringUtils.regexString(year, "\\((\\d+)\\)");
//		System.out.println(regexString);
//		film.setFilmYear(Integer.valueOf(year));
//		System.out.println(year.substring(1, year.length()-1));
		//获得director
		Elements directorEle = document.getElementsByAttributeValue("rel", "v:directedBy");
		String director = directorEle.text();
		film.setDirectorName(director);
//		System.out.println(director);
		//获得type
		Elements typeEle = document.getElementsByAttributeValue("property", "v:genre");
		StringBuilder sb1 = new StringBuilder();
		//如果有多种类型，进行拼接
		for (int i = 0; i < typeEle.size(); i++) {
			if (typeEle.size()==1 || i==typeEle.size()-1) {
				sb1.append(typeEle.get(i).text());
				break;
			}
			sb1.append(typeEle.get(i).text()).append("/");
		}
		String type = sb1.toString();
		film.setType(type);
//		System.out.println(type);
		//获得nation		制片国家/地区:</span> 美国<br/>
		Element infoEle = document.getElementById("info");
		//info内的所有内容
		String infoContent = infoEle.html();
//		System.out.println(infoContent);
		String regex = "制片国家/地区:</span>(.*[\u4e00-\u9fa5]+)\\n";		//[\u4e00-\u9fa5]+
		String nation = RegexStringUtils.regexString(infoContent, regex).trim();
		film.setNation(nation);
//		System.out.println(nation);
		//time
		Elements timeEle = document.getElementsByAttributeValue("property", "v:runtime");
		String timeContent = timeEle.text();
		regex = "(\\d+).*";
		Integer time = Integer.valueOf(RegexStringUtils.regexString(timeContent, regex).trim());
		film.setTime(time);
//		System.out.println(time);
		//从infoContent中找imdb		IMDb链接:</span> <a href="
		regex = "IMDb链接:</span> \\n<a href=\"(.*)\" target";
		String imdb = RegexStringUtils.regexString(infoContent, regex);
		film.setImdbUrl(imdb);
//		System.out.println(imdb);
		//average
		Elements averEle = document.getElementsByAttributeValue("property", "v:average");
		//average判断没有分数的情况
		if (averEle.text() == null || "".equals(averEle.text())) {
			film.setAverage(0.0);
		}else {
			Double average = Double.valueOf(averEle.text().trim());
			film.setAverage(average);
		}
//		System.out.println(average);
		//votes：判断没有投票的情况
		Elements votesEle = document.getElementsByAttributeValue("property", "v:votes");
		if (votesEle.text() == null || "".equals(votesEle.text())) {
			film.setVotes(0);
		}else {
			int votes = Integer.valueOf(votesEle.text().trim());
			film.setVotes(votes);
		}
//		System.out.println(votes);
//		this.films.add(film);
		return film;
	}
	/**
	 * 根据影片名称集合获得url集合
	 * @param queryStr
	 * @return
	 */
	public List<String> getFilmUrl(List<String> queryNames){
		List<String> urlList = new ArrayList<String>();
		if (queryNames==null) {
			throw new RuntimeException("影片名称集合不能为null");
		}
		
		for (String qn : queryNames) {
			if ("".equals(qn)) {
				continue;
			}
			String queryUrl = "https://movie.douban.com/subject_search?search_text="+qn;
			
			String filmUrl = null;
			Connection connect = Jsoup.connect(queryUrl);
			Document document;
			try {
				document = connect.get();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("获取document异常");
			}
			Elements itemsEle = document.getElementsByAttributeValue("class", "nbg");
			//去第一个href="https://movie.douban.com/subject/"
			for (Element element : itemsEle) {
				String result = RegexStringUtils.regexString(element.attr("href"), "(https://movie\\.douban\\.com/subject/\\d+/)");
				if ("notmatch".equals(result)) {
					continue;
				}
				filmUrl = element.attr("href");
				break;
			}
			urlList.add(filmUrl);
		}
		return urlList;
	}
	
	@Override
	public void run() {
		while (!filmsUrl.isEmpty()) {
			String currentUrl = null;
			synchronized (this) {
				if (!this.filmsUrl.isEmpty()) {
					currentUrl = this.filmsUrl.remove(0);					
				}
			}
			this.films.add(getSingleFilm(currentUrl));
//			getSingleFilm(currentUrl);
		}
	}
	
}
