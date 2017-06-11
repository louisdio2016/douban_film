package com.dbcrawler.dao;

import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import com.dbcrawler.film.Film;
import com.dbcrawler.utils.Page;


/**
 * @Description:
 * @Author:		传智播客 java学院	传智.宋江
 * @Company:	http://java.itcast.cn
 * @CreateDate:	2014年10月31日
 */
public interface BaseDao {

	//查询所有，带条件查询
	public List<Film> find(String sql);
	
	public List<Film> find(String sql,Object...params);
	//获取一条记录
	public Film getById(String id);
	//分页查询，将数据封装到一个page分页工具类对象
	public Page<Film> findPage(String countSql,String sql, Page<Film> page);
	
	//新增和修改保存
	public void save(String sql,Object... params);
	//批量新增和修改保存
	public int[] saveAll(String sql,Collection<Film> entitys);
	
	//单条删除，按id
	public void delete(String sql,Object... params);
	//批量删除
	public void deleteAll(String sql,Object... params);

}