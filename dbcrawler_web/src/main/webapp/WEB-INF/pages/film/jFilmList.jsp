<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../baselist.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title></title>
	<script type="text/javascript" src="${ctx }/components/zTree/js/jquery-1.8.3.js"></script>
	<script>
	$(document).ready(function(){
		//显示input搜索默认值
		$("#targetText").on("focus blur",function(){
			if($(this).is(":focus")){
				if($(this).val()==$(this).attr("defaultValue")){
					$(this).val("");
				}
			} else{
				if($(this).val() == ""){
					$(this).val($(this).attr("defaultValue"));
				}
			}
		});
	});
		
		//点击添加，将页面勾选项以ajax请求的方式发送服务器，进行保存
		function toStore(){
			//1.获得所有checked td的tr，并且name != trhead
	    	var $trItem = $("input:checked").parents("tr:not([name=\"trhead\"])");
			if($trItem.size()==0){
				alert("请先勾选影片");
				return;
			}
			//拼接json
			var jsonStr = "{\"films\":[";
			//1.遍历每一行
			for(var i=0;i<$trItem.size();i++){
				//获得每个tr的子节点td
				var tds = $trItem[i].childNodes;
				//拼接对象
				jsonStr += "{";
				//console.log("childs.length",childs.length);
				//遍历每个td节点，依次拼接
				for(var j=0;j<tds.length;j++){
					//"filmId": "4bd0845507db4c64a2ef5c46d920238a",<td><input value></td>
					if(j==0){
						jsonStr += "\"filmId\":\""+tds.item(j).childNodes.item(0).value+"\",";
					//console.log(childs.item(j).innerText);
					}
					//j==1,序号，跳过
					if(j==1){
						continue;
					}
					if(j==2){
						jsonStr += "\"filmName\":\""+tds.item(j).innerText+"\",";
					}
					if(j==3){
						jsonStr += "\"filmYear\":\""+tds.item(j).innerText+"\",";
					}
					if(j==4){
						jsonStr += "\"directorName\":\""+tds.item(j).innerText+"\",";
					}
					if(j==5){
						jsonStr += "\"type\":\""+tds.item(j).innerText+"\",";
					}
					if(j==6){
						jsonStr += "\"nation\":\""+tds.item(j).innerText+"\",";
					}
					if(j==7){
						jsonStr += "\"releaseDate\":\""+tds.item(j).innerText+"\",";
					}
					if(j==8){
						jsonStr += "\"time\":\""+tds.item(j).innerText+"\",";
					}
					if(j==9){
						jsonStr += "\"average\":\""+tds.item(j).innerText+"\",";
					}
					if(j==10){
						jsonStr += "\"votes\":\""+tds.item(j).innerText+"\",";
					}
					if(j==11){
						jsonStr += "\"filmUrl\":\""+tds.item(j).childNodes.item(0).innerText+"\",";
					}
					if(j==12){
						jsonStr += "\"imdbUrl\":\""+tds.item(j).childNodes.item(0).innerText+"\"";
					}
				}
				
				//判断是否是最后一个元素
				if(i != $trItem.size()-1){
					jsonStr += "},";
				}else{
					jsonStr += "}]}";
				}
			} 
			
			//console.log(jsonStr);
			
			
			  /* var jsonData = {"films":[
			    {
				 "average": 8.7,
                 "directorName": "陈可辛",
                 "filmId": "4bd0845507db4c64a2ef5c46d920238a",
                 "filmName": "甜蜜蜜",
                 "filmUrl": "https://movie.douban.com/subject/1305164",
                 "filmYear": 1996,
                 "imdbUrl": "http://www.imdb.com/title/tt0117905",
                 "nation": "香港",
                 "releaseDate": "2015-02-13(中国大陆)1996-11-02(香港)",
                 "time": 118,
                 "type": "剧情/爱情",
                 "votes": 217768
				
			    },{
			    	"average": 8.7,
                    "directorName": "陈可辛",
                    "filmId": "4bd0845507db4c64a2ef5c46d920238a",
                    "filmName": "甜蜜蜜",
                    "filmUrl": "https://movie.douban.com/subject/1305164",
                    "filmYear": 1996,
                    "imdbUrl": "http://www.imdb.com/title/tt0117905",
                    "nation": "香港",
                    "releaseDate": "2015-02-13(中国大陆)1996-11-02(香港)",
                    "time": 118,
                    "type": "剧情/爱情",
                    "votes": 217768
			    }]};  
			console.log(JSON.stringify(jsonData)); */
			//$.post("http://localhost:8080/dbcrawler_web/filmAction_saveFilms",jsonData,"json");
			$.ajax({
    			url:"${ctx}/filmAction_saveFilms",
    			type:"POST",
    			//data: JSON.stringify(jsonData),
    			data: jsonStr,
    			//traditional:true,
    			//向服务器发送的数据类型
    			contentType: "application/json; charset=utf-8",
    			dataType:"text",
    		});
		}
		//点击搜索
		function toSearch(){
			//在表格中插入tr
			$(".tableBody").prepend("<tr id=\"holdon\"><td>正在查询，请稍后...</td></tr>");
			//获得targetfilm
	    	var filmname = document.getElementsByName("targetFilm")[0].value;
    		if(filmname == NaN || "" == filmname ){
    			alert("请输入影片名");
    		} 
    		//发送ajax请求，爬虫查询,并接收返回的film
			$.ajax({
    			url:"${ctx}/filmAction_showSearch",
    			type:"POST",
    			//发送键值对，自动装填到struts的对应字段
    			data:{targetFilm:filmname},
    			//发送到服务器的数据类型
    			contentType:"application/x-www-form-urlencoded;charset=utf-8",
    			//从服务器接收到的数据类型，注：此处如果是json，则不需要eval()将字符串转换为json对象了
    			dataType:"text",
    			//回调函数
    			success:receiveJson
    		});
	    } 
		//回调函数，处理json字符串，并在表格中进行拼接
	    function receiveJson(data){
			//起始序号
			var index = 0;
			//var i = $('#indexNum:last').text();
			//获得最后一行tr，在最后一行之后进行拼接
			var $la = $("tr:last");
			//获得序号的td，序号累加
			var $ind = $la.children("#indexNum");
			if(Number($ind.text()) != NaN){
				index = Number($ind.text());
			}
			//将json字符串转换为json对象
	    	var jsonObj = eval("("+data+")");
			//遍历json对象
	    	$.each(jsonObj,function(idx,film){
	    		//在表格的最后一行添加tr
	    		$(".tableBody").append("<tr></tr>");
	    		//拼接tr中的td
		    	var inputId = $("<td></td>");
				var chinput = $("<input type=\"checkbox\" name=\"id\"  value=\"\"/>");
				//为value属性赋值
				chinput.val(film.filmId);
				//将input插入td
				inputId.append(chinput);
		    	//依次添加text
				var id = $("<td id=\"indexNum\"></td>").text(idx+index+1);
		    	var fname = $("<td></td>").text(film.filmName);
		    	var fyear = $("<td></td>").text(film.filmYear);
		    	var dirname = $("<td></td>").text(film.directorName);
		    	var ftype = $("<td></td>").text(film.type);
		    	var fnation = $("<td></td>").text(film.nation);
		    	var reldate = $("<td></td>").text(film.releaseDate);
		    	var ftime = $("<td></td>").text(film.time);
		    	var faver = $("<td></td>").text(film.average);
		    	var fvotes = $("<td></td>").text(film.votes);
		    	//将<a>添加到<td>
		    	var faurl = $("<a></a>").attr("href",film.filmUrl);
		    	faurl.text(film.filmUrl);
		    	var furl = $("<td></td>");
		    	furl.append(faurl);
		    	//将<a>添加到<td>
		    	var faiurl = $("<a></a>").attr("href",film.imdbUrl);
		    	faiurl.text(film.imdbUrl);
		    	var iurl = $("<td></td>");
		    	iurl.append(faiurl);
		    	//将td拼接到tr中
		    	$("tr:last").append(inputId,id,fname,fyear,dirname,ftype,fnation,reldate,ftime,faver,fvotes,furl,iurl);
	    	});
	    	//删除<td>正在查询，请稍后...</td>
	    	$("#holdon").text("");
	    	$("#holdon").remove();
	    }
	     //实现删除勾选项
	     function toDelete(){
	    	 //1.获得所有checked td的tr
	    	 var $trItem = $("input:checked").parents("tr:not([name=\"trhead\"])");
	    	 //alert($trItem);
	    	 //2.删除tr
	    	 $trItem.text("");
	    	 $trItem.remove();
	    	 //3.重新循环为序号赋值
	    	 //var $trs = $(".tableBody").children("tr");
	    	 var $trs = $(".tableBody tr");
	    	 for(var i=1; i<=$trs.size();i++){
	    		 var $indexTds = $(".tableBody tr").children("#indexNum");
	    		 $indexTds[i-1].innerText = i;
	    	 }
	     }
	</script>
</head>

<body>
<form name="icform" method="post">

<div id="menubar">
<div id="middleMenubar">
  <ul>
  	<li><input id="targetText" type="text" name="targetFilm" value="请输影片全名，用空格分隔" defaultValue="请输影片全名，用空格分隔" size="60"/><input type="button" value="搜索"  onclick="toSearch()" /></li>
  </ul>
<div id="innerMenubar">
  <div id="navMenubar">
<ul>
<li>
	<div class="textbox-inner-header">
    	电影列表
    </div>
</li>
<li id="new"><a href="#" onclick="toStore();this.blur();">添加</a></li>
<li id="delete"><a href="#" onclick="toDelete();this.blur();">删除</a></li>
<li id="view"><a href="${ctx}/filmAction_showAllFilms" onclick="this.blur();">查看全部</a></li>
</ul>
  </div>
</div>
</div>
</div>
   
<div class="textbox" id="centerTextbox">
  <div class="textbox-header">
  <div class="textbox-inner-header">
  
  </div>
  </div>
  
<div>


<div class="eXtremeTable" >
<table id="ec_table" class="tableRegion" width="98%" >
	<thead>
	<tr name="trhead">
		<td class="tableHeader"><input type="checkbox" name="selid" onclick="checkAll('id',this)"></td>
		<td class="tableHeader">序号</td>
		<td class="tableHeader">片名</td>
		<td class="tableHeader">年份</td>
		<td class="tableHeader">导演</td>
		<td class="tableHeader">类型</td>
		<td class="tableHeader">国家</td>
		<td class="tableHeader">上映日期</td>
		<td class="tableHeader">片长</td>
		<td class="tableHeader">豆瓣评分</td>
		<td class="tableHeader">票数</td>
		<td class="tableHeader">豆瓣链接</td>
		<td class="tableHeader">imdb链接</td>
	</tr>
	</thead>
	<tbody class="tableBody" ><!-- <tr><td><input type="checkbox" name="id" value="4bd0845507db4c64a2ef5c46d920238a"/></td><td id="indexNum">1</td><td>甜蜜蜜</td><td>1996</td><td>陈可辛</td><td>剧情/爱情</td><td>香港</td><td>2015-02-13(中国大陆)1996-11-02(香港)</td><td>118</td><td>8.7</td><td>217775</td><td><a href="https://movie.douban.com/subject/1305164">https://movie.douban.com/subject/1305164</a></td><td><a href="http://www.imdb.com/title/tt0117905">http://www.imdb.com/title/tt0117905</a></td></tr><tr><td><input type="checkbox" name="id" value="3b58450591bd4689bdef9af2bea0dee3"/></td><td id="indexNum">2</td><td>她 Elle</td><td>2016</td><td>保罗·范霍文</td><td>剧情/惊悚</td><td>法国 / 德国 / 比利时</td><td>2016-05-21(戛纳电影节)2016-05-25(法国)</td><td>130</td><td>7.6</td><td>57960</td><td><a href="https://movie.douban.com/subject/26022182">https://movie.douban.com/subject/26022182</a></td><td><a href="http://www.imdb.com/title/tt3716530">http://www.imdb.com/title/tt3716530</a></td></tr> -->
	</tbody>
</table>
</div>
 
</div>
 
 
</form>
</body>
</html>

