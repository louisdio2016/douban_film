package com.dbcrawler.action;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.dbcrawler.dao.BaseDao;
import com.dbcrawler.film.Film;
import com.dbcrawler.service.FilmService;
import com.dbcrawler.service.impl.GetFilmsService;
import com.dbcrawler.utils.DownloadUtil;
import com.dbcrawler.utils.Page;
import com.opensymphony.xwork2.ActionContext;



public class FilmAction extends BaseAction{
	private FilmService filmService;
	public void setFilmService(FilmService filmService) {
		this.filmService = filmService;
	}
	
	private List<Film> films = new ArrayList<Film>();
	public List<Film> getFilms() {
		return films;
	}
	public void setFilms(List<Film> films) {
		this.films = films;
	}

	private String targetFilm;
	public String getTargetFilm() {
		return targetFilm;
	}
	public void setTargetFilm(String targetFilm) {
		this.targetFilm = targetFilm;
	}

	public String showPage(){
		return "showPage";
	}
	
	public String showSearch() throws IOException{
//		System.out.println(this.targetFilm);
//		System.out.println("拼接json");
//		UUID uuid1 = UUID.randomUUID();
//		List<Film> list = new ArrayList<Film>();
//		Film film1 = new Film();
//		film1.setFilmId(uuid1.toString().replace("-", ""));
//		film1.setFilmName("低俗小说 Pulp Fiction");
//		film1.setFilmYear(1994);
//		film1.setDirectorName("昆汀·塔伦蒂诺");
//		film1.setType("剧情/喜剧");
//		film1.setNation("美国");
//		film1.setReleaseDate("1994-05-12(戛纳电影节)1994-10-14(美国)");
//		list.add(film1);
//		
//		Film film2 = new Film();
//		film2.setFilmId(uuid1.toString().replace("-", ""));
//		film2.setFilmName("逃出绝命镇 Get Out");
//		film2.setFilmYear(2017);
//		film2.setDirectorName("乔丹·皮尔");
//		film2.setType("悬疑/恐怖");
//		film2.setNation("美国");
//		film2.setReleaseDate("2017-01-23(圣丹斯电影节)2017-02-24(美国)");
//		list.add(film2);
		String[] filmNames = targetFilm.split(" ");
		List<String> list = Arrays.asList(filmNames);
		//GetFilmsService用于向豆瓣发送请求，获得document，解析页面信息，封装到films中
		Runnable getFilmsService = new GetFilmsService(list);
		Thread t1 = new Thread(getFilmsService);
		Thread t2 = new Thread(getFilmsService);
		Thread t3 = new Thread(getFilmsService);
		
		t1.start();
		t2.start();
		t3.start();
		
		try {
			t1.join();
			t2.join();
			t3.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException("线程join异常");
		}
		GetFilmsService gfs = (GetFilmsService)getFilmsService;
		//将电影转为json数组
		String jsonData = JSON.toJSONString(gfs.getFilms());
		//System.out.println(jsonData);
		//发送json数据
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(jsonData);
		
		return NONE;
	}
	public String saveFilms(){
		filmService.saveAll(films);
		return NONE;
	}
	private Page page = new Page();
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public String showAllFilms(){
		Page<Film> filmPage = filmService.findPage(page);
		filmPage.setUrl("filmAction_showAllFilms");
		ActionContext.getContext().getValueStack().push(page);
		return "showAll";
	}
	
	public String download() throws Exception{
		//通用变量:row从0开始，列从0开始
		int rowNo = 0, cellNo=0;
		Row nRow = null;
		Cell nCell = null;
		//1.读取工作薄
		String path = ServletActionContext.getServletContext().getRealPath("/xlsxsample/film/sample.xlsx");
		InputStream inputStream = new FileInputStream(path);
		Workbook workbook = new XSSFWorkbook(inputStream);
		//2.读取工作表
		Sheet sheet = workbook.getSheetAt(0);
		//3.读取列宽
		
		//获得行对象
		nRow = sheet.getRow(rowNo++);
		
		//设置单元格
		nCell = nRow.getCell(cellNo);
		nCell.setCellValue("豆瓣电影列表");
		
		//小表特
		rowNo++;
		//填充数据
		nRow = sheet.getRow(rowNo);
		CellStyle idStype = nRow.getCell(cellNo++).getCellStyle();
		CellStyle fnameStype = nRow.getCell(cellNo++).getCellStyle();
		CellStyle dburlStype = nRow.getCell(cellNo++).getCellStyle();
		CellStyle yearStype = nRow.getCell(cellNo++).getCellStyle();
		CellStyle dirStype = nRow.getCell(cellNo++).getCellStyle();
		CellStyle typeStype = nRow.getCell(cellNo++).getCellStyle();
		CellStyle nationStype = nRow.getCell(cellNo++).getCellStyle();
		CellStyle relStype = nRow.getCell(cellNo++).getCellStyle();
		CellStyle timeStype = nRow.getCell(cellNo++).getCellStyle();
		CellStyle imdbStype = nRow.getCell(cellNo++).getCellStyle();
		CellStyle averStype = nRow.getCell(cellNo++).getCellStyle();
		CellStyle voteStype = nRow.getCell(cellNo++).getCellStyle();
		
		//获得所有film
		List<Film> filmList = filmService.findAll();
		for (Film film : filmList) {
			nRow = sheet.createRow(rowNo++);
			nRow.setHeightInPoints(24);
			//从0开始填充数据
			cellNo = 0;
			
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(film.getFilmId());
			nCell.setCellStyle(idStype);
			
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(film.getFilmName());
			nCell.setCellStyle(fnameStype);
			
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(film.getFilmUrl());
			nCell.setCellStyle(dburlStype);
			
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(film.getFilmYear());
			nCell.setCellStyle(yearStype);
			
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(film.getDirectorName());
			nCell.setCellStyle(dirStype);
			
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(film.getType());
			nCell.setCellStyle(typeStype);
			
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(film.getNation());
			nCell.setCellStyle(nationStype);
			
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(film.getReleaseDate());
			nCell.setCellStyle(relStype);
			
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(film.getTime());
			nCell.setCellStyle(timeStype);
			
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(film.getImdbUrl());
			nCell.setCellStyle(imdbStype);
			
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(film.getAverage());
			nCell.setCellStyle(averStype);
			
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(film.getVotes());
			nCell.setCellStyle(voteStype);
			
		}
		
		//输出
		DownloadUtil downUtil = new DownloadUtil();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();//内存中的缓存区
		workbook.write(outputStream);//将excel表格中的内容输出到缓存
		HttpServletResponse response = ServletActionContext.getResponse();
		//获得request，提供给解决下载文件名乱码的方法;这里不能使用BaseAction的request,因为是Map类型
		HttpServletRequest request2 = ServletActionContext.getRequest();
		
		downUtil.download(outputStream, response, "豆瓣电影列表.xlsx",request2);
		outputStream.close();//刷新缓存
		
		return NONE;
	}
	@Test
	public void testJson(){
		UUID uuid1 = UUID.randomUUID();
		List<Film> list = new ArrayList<Film>();
		Film film1 = new Film();
		film1.setFilmId(uuid1.toString().replace("-", ""));
		film1.setFilmName("低俗小说 Pulp Fiction");
		film1.setFilmYear(1994);
		film1.setDirectorName("昆汀·塔伦蒂诺");
		film1.setType("剧情/喜剧");
		film1.setNation("美国");
		film1.setReleaseDate("1994-05-12(戛纳电影节)1994-10-14(美国)");
		list.add(film1);
		
		Film film2 = new Film();
		film2.setFilmId(uuid1.toString().replace("-", ""));
		film2.setFilmName("逃出绝命镇 Get Out");
		film2.setFilmYear(2017);
		film2.setDirectorName("乔丹·皮尔");
		film2.setType("悬疑/恐怖");
		film2.setNation("美国");
		film2.setReleaseDate("2017-01-23(圣丹斯电影节)2017-02-24(美国)");
		list.add(film2);
		
		String jsonData = JSON.toJSONString(list);
		System.out.println(jsonData);
	}
	
	@Test
	public void testService(){
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		BaseDao dao = (BaseDao)applicationContext.getBean("baseDao");
		String sql = "insert into film_info (fid,fname,furl,fyear,dirname,ftype,nation,reldate,ftime,imdburl,faverage,votes) ";
		//sql.concat(" values (?,?,?,?,?,?,?,?,?,?,?,?) ");
		sql += " values (?,?,?,?,?,?,?,?,?,?,?,?) ";
		dao.save(sql, "aa3d2c51a29e479c8558bdd156fe871f","教父 The Godfather","https://movie.douban.com/subject/1291841",
				1972,"弗朗西斯·福特·科波拉","剧情/犯罪","美国","1972-03-15(纽约首映)1972-03-24(美国)",175,
				"http://www.imdb.com/title/tt0068646",9.2,327262);
		
	}
}
