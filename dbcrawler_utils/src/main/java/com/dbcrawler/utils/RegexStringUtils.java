package com.dbcrawler.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexStringUtils {
	public static String regexString(String targetStr, String patternStr){
		Pattern pattern = Pattern.compile(patternStr);
		//定义一个Matcher用作匹配
		Matcher matcher = pattern.matcher(targetStr);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return "notmatch";
	}
}
