package com.dbcrawler.service;

import java.util.Collection;
import java.util.List;

import com.dbcrawler.film.Film;
import com.dbcrawler.utils.Page;

public interface FilmService {
	public void save(Object... params);
	
	public int[] saveAll(Collection<Film> entitys);
	
	public void deleteById(String id);
	
	public void deleteAllById(String[] ids);
	
	public List<Film> findAll();
	
	public Film getById(String id);
	
	public Page<Film> findPage( Page<Film> page);
	
}
