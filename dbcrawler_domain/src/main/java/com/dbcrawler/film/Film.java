package com.dbcrawler.film;

import java.io.Serializable;

public class Film implements Serializable{
	public String filmId;
	public String filmName;
	public String filmUrl;
	public Integer filmYear;
	public String directorName;
	public String type;
	public String nation;
	public String releaseDate;
	public Integer time;
	public String imdbUrl;
	public Double average;
	public Integer votes;
	
	public String getFilmId() {
		return filmId;
	}
	public void setFilmId(String filmId) {
		this.filmId = filmId;
	}
	public String getFilmName() {
		return filmName;
	}
	public void setFilmName(String filmName) {
		this.filmName = filmName;
	}
	public String getFilmUrl() {
		return filmUrl;
	}
	public void setFilmUrl(String filmUrl) {
		this.filmUrl = filmUrl;
	}
	public Integer getFilmYear() {
		return filmYear;
	}
	public void setFilmYear(Integer filmYear) {
		this.filmYear = filmYear;
	}
	public String getDirectorName() {
		return directorName;
	}
	public void setDirectorName(String directorName) {
		this.directorName = directorName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public String getImdbUrl() {
		return imdbUrl;
	}
	public void setImdbUrl(String imdbUrl) {
		this.imdbUrl = imdbUrl;
	}
	public Double getAverage() {
		return average;
	}
	public void setAverage(Double average) {
		this.average = average;
	}
	public Integer getVotes() {
		return votes;
	}
	public void setVotes(Integer votes) {
		this.votes = votes;
	}
	@Override
	public String toString() {
		return "Film [filmId=" + filmId + ", filmName=" + filmName + ", filmUrl=" + filmUrl + ", filmYear=" + filmYear
				+ ", directorName=" + directorName + ", type=" + type + ", nation=" + nation + ", releaseDate="
				+ releaseDate + ", time=" + time + ", imdbUrl=" + imdbUrl + ", average=" + average + ", votes=" + votes
				+ "]";
	}
	
	
}
