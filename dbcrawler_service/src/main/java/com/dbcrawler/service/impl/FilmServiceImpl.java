package com.dbcrawler.service.impl;

import java.util.Collection;
import java.util.List;

import com.dbcrawler.dao.BaseDao;
import com.dbcrawler.film.Film;
import com.dbcrawler.service.FilmService;
import com.dbcrawler.utils.Page;

public class FilmServiceImpl implements FilmService {
	private BaseDao baseDao;
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	@Override
	public void save(Object... params) {
		String sql = "insert into film_info (fid,fname,furl,fyear,dirname,ftype,nation,reldate,ftime,imdburl,faverage,votes) ";
		sql += " values (?,?,?,?,?,?,?,?,?,?,?,?) ";
		baseDao.save(sql, params);
	}

	@Override
	public int[] saveAll( Collection<Film> entitys) {
		String sql = "insert into film_info (fid,fname,furl,fyear,dirname,ftype,nation,reldate,ftime,imdburl,faverage,votes) ";
		sql += " values ( :filmId , :filmName, :filmUrl, :filmYear, :directorName, :type, :nation, :releaseDate, :time, :imdbUrl, :average, :votes) ";
		return baseDao.saveAll(sql, entitys);
	}

	@Override
	public void deleteById( String id) {
		String sql = "delete from film_info where fid = ? ";
		baseDao.delete(sql, id);
	}

	@Override
	public void deleteAllById(String[] ids) {
		int count = ids.length;
		StringBuilder sb = new StringBuilder("delete from film_info where fid in ");
		
		sb.append("(");
		for (int i = 0; i < ids.length; i++) {
			if (i==ids.length-1) {
				sb.append("?");
			}
			sb.append("?,");
		}
		sb.append(")");
		baseDao.deleteAll(sb.toString(), ids);
	}

	@Override
	public List<Film> findAll() {
		String sql = "select * from film_info where 1=1 ";
		return (List<Film>)baseDao.find(sql,null);
	}

	@Override
	public Film getById(String id) {
		return baseDao.getById(id);
	}

	@Override
	public Page<Film> findPage( Page<Film> page) {
		String countSql = "select count(fid) from film_info ";
		String sql = "select * from film_info limit ?,? ";
		return baseDao.findPage(countSql, sql, page);
	}
	
	
	
}
