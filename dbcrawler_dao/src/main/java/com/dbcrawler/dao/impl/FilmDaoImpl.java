package com.dbcrawler.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.dbcrawler.dao.BaseDao;
import com.dbcrawler.film.Film;
import com.dbcrawler.utils.Page;

public class FilmDaoImpl extends JdbcDaoSupport implements BaseDao{
	//单个新增
	public void save(String sql,Object... params) {
		this.getJdbcTemplate().update(sql, params);
	}
	
	//用于批量操作
//		private NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(super.getDataSource());
	
	//批量新增
	public int[] saveAll(String sql,Collection<Film> entitys){
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(super.getDataSource());
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(entitys.toArray());
		int[] batchUpdate = namedParameterJdbcTemplate.batchUpdate(sql, batch);
		return batchUpdate;
	}

	//按主键id删除
	public void delete(String sql,Object... params) {
		this.getJdbcTemplate().update(sql, params);
	}

	//批量删除
	public void deleteAll(String sql, Object...params) {
		this.getJdbcTemplate().update(sql,params);
	}
	
	//查询
	public List<Film> find(String sql){
		ArrayList<Film> list = new ArrayList<Film>();
		this.getJdbcTemplate().query(sql, new RowCallbackHandler(){

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Film film = new Film();
				film.setFilmId(rs.getString("fid"));
				film.setFilmName(rs.getString("fname"));
				film.setFilmUrl(rs.getString("furl"));
				film.setFilmYear(rs.getInt("fyear"));
				film.setDirectorName(rs.getString("dirname"));
				film.setType(rs.getString("ftype"));
				film.setNation(rs.getString("nation"));
				film.setReleaseDate(rs.getString("reldate"));
				film.setTime(rs.getInt("ftime"));
				film.setImdbUrl(rs.getString("imdburl"));
				film.setAverage(rs.getDouble("faverage"));
				film.setVotes(rs.getInt("votes"));
				list.add(film);
			}
		});
		return list;
	}
	public List<Film> find(String sql,Object...params){
		ArrayList<Film> list = new ArrayList<Film>();
		this.getJdbcTemplate().query(sql, new RowCallbackHandler(){

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Film film = new Film();
				film.setFilmId(rs.getString("fid"));
				film.setFilmName(rs.getString("fname"));
				film.setFilmUrl(rs.getString("furl"));
				film.setFilmYear(rs.getInt("fyear"));
				film.setDirectorName(rs.getString("dirname"));
				film.setType(rs.getString("ftype"));
				film.setNation(rs.getString("nation"));
				film.setReleaseDate(rs.getString("reldate"));
				film.setTime(rs.getInt("ftime"));
				film.setImdbUrl(rs.getString("imdburl"));
				film.setAverage(rs.getDouble("faverage"));
				film.setVotes(rs.getInt("votes"));
				list.add(film);
			}
		},params);
		return list;
	}
	
	
	public Film getById(String id){
		String sql = "select * from film_info where fid = ?";
		Film result = this.getJdbcTemplate().query(sql, new Object[]{id}, new RowMapper<Film>(){

			@Override
			public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
				Film film = new Film();
				film.setFilmId(rs.getString("fid"));
				film.setFilmName(rs.getString("fname"));
				film.setFilmUrl(rs.getString("furl"));
				film.setFilmYear(rs.getInt("fyear"));
				film.setDirectorName(rs.getString("dirname"));
				film.setType(rs.getString("ftype"));
				film.setNation(rs.getString("nation"));
				film.setReleaseDate(rs.getString("reldate"));
				film.setTime(rs.getInt("ftime"));
				film.setImdbUrl(rs.getString("imdburl"));
				film.setAverage(rs.getDouble("faverage"));
				film.setVotes(rs.getInt("votes"));
				
				return film;
			}
		}).get(0);
		return result;
	}
	
	
	public Page<Film> findPage(String countSql,String sql, Page<Film> page){
		
		
		int count = this.getJdbcTemplate().queryForObject(countSql,  new RowMapper<Integer>(){

			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt(1);
			}
		});
		//System.out.println(count);
		//设置总行数
		page.setTotalRecord(count);
		List<Film> list = find(sql, (page.getPageNo()-1)*page.getPageSize(),page.getPageSize());
		
		page.setResults(list);
		return page;
	}
	
	
	

}
