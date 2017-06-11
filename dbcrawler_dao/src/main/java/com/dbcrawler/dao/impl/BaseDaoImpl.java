package com.dbcrawler.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.dbcrawler.dao.BaseDao;
import com.dbcrawler.utils.Page;


/**
 * @Description:
 * @Author:		传智播客 java学院	传智.宋江
 * @Company:	http://java.itcast.cn
 * @CreateDate:	2014年10月31日
 */
public class BaseDaoImpl extends JdbcDaoSupport  {
	

	//带条件查询
	public <T> List<T> find(String sql, Class<T> entityClass, Object... params) {
		if (params!=null) {
			return (List<T>)this.getJdbcTemplate().queryForList(sql, entityClass, params);
		}
		return this.getJdbcTemplate().queryForList(sql, entityClass);
	}
	
	//获取一条，根据主键id
	public <T> T get(String sql, Class<T> entityClass,Serializable id) {
		return (T) this.getJdbcTemplate().queryForObject(sql, entityClass, id);
	}

	//分页查询，查询两次，一次查询总数，一次查询分页记录
	public <T> Page<T> findPage(String countSql,String sql, Page<T> page, Class<T> entityClass, Object... params){
		List<T> queryList = null;
		if (params!=null) {
			queryList =  (List<T>)this.getJdbcTemplate().queryForList(sql, entityClass, params);
		}
		queryList = this.getJdbcTemplate().queryForList(sql, entityClass);
		//获得总记录数
		int count = queryList.size();
		page.setTotalRecord(count);
		
		//设置分页
		queryList = (List<T>)this.getJdbcTemplate().queryForList(sql, entityClass, params,((page.getPageNo()-1)*page.getPageSize()),page.getPageSize());
		page.setResults(queryList);
		
		return page;
	}
	
	//单个新增
	public <T> void save(String sql,Object... params) {
		this.getJdbcTemplate().update(sql, params);
	}
	
	//用于批量操作
//	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(super.getDataSource());
	
	//批量新增
	public <T> int[] saveAll(String sql,Collection<T> entitys){
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(super.getDataSource());
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(entitys.toArray());
		int[] batchUpdate = namedParameterJdbcTemplate.batchUpdate(sql, batch);
		return batchUpdate;
	}

	//按主键id删除
	public <T> void delete(String sql,Object... params) {
		this.getJdbcTemplate().update(sql, params);
	}

	//批量删除
	public <T> void deleteAll(String sql,Object... params) {
		this.getJdbcTemplate().update(sql, params);
	}


}

