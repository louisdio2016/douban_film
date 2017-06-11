<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../baselist.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title></title>
	<script type="text/javascript" src="${ctx }/components/zTree/js/jquery-1.8.3.js"></script>
	<script>
	     function isOnlyChecked(){
	    	 var checkBoxArray = document.getElementsByName('id');
				var count=0;
				for(var index=0; index<checkBoxArray.length; index++) {
					if (checkBoxArray[index].checked) {
						count++;
					}	
				}
			//jquery
			//var count = $("[input name='id']:checked").size();
			if(count==1)
				return true;
			else
				return false;
	     }
	     function toView(){
	    	 if(isOnlyChecked()){
	    		 formSubmit('deptAction_toview','_self');
	    	 }else{
	    		 alert("请先选择一项并且只能选择一项，再进行操作！");
	    	 }
	     }
	     //实现更新
	     function toUpdate(){
	    	 if(isOnlyChecked()){
	    		 formSubmit('deptAction_toupdate','_self');
	    	 }else{
	    		 alert("请先选择一项并且只能选择一项，再进行操作！");
	    	 }
	     }
	</script>
</head>

<body>
<form name="icform" method="post">

<div id="menubar">
<div id="middleMenubar">
<div id="innerMenubar">
  <div id="navMenubar">
<ul>
<!-- <li id="view"><a href="#" onclick="javascript:toView()">查看</a></li>
<li id="new"><a href="#" onclick="formSubmit('deptAction_tocreate','_self');this.blur();">新增</a></li>
<li id="update"><a href="#" onclick="javascript:toUpdate()">修改</a></li>
<li id="delete"><a href="#" onclick="formSubmit('deptAction_delete','_self');this.blur();">删除</a></li> -->
<li id="back"><a href="${ctx}/filmAction_showPage">返回</a></li>
<li id="save"><a href="#" onclick="formSubmit('filmAction_download','_self');this.blur();">下载</a></li>
</ul>
  </div>
</div>
</div>
</div>
   
<div class="textbox" id="centerTextbox">
  <div class="textbox-header">
  <div class="textbox-inner-header">
  <div class="textbox-title">
    全部影片
  </div> 
  </div>
  </div>
  
<div>


<div class="eXtremeTable" >
<table id="ec_table" class="tableRegion" width="98%" >
	<thead>
	<tr>
		<!-- <td class="tableHeader"><input type="checkbox" name="selid" onclick="checkAll('id',this)"></td> -->
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
	<tbody class="tableBody" >
    ${links }
	
	<c:forEach items="${results}" var="film"  varStatus="st">
		<tr class="odd" onmouseover="this.className='highlight'" onmouseout="this.className='odd'" >
			<%-- <td><input type="checkbox" name="id" value="${dept.id }"/></td> --%>
			<td>${st.count }</td>
			<td>${film.filmName }</td>
			<td>${film.filmYear }</td>
			<td>${film.directorName }</td>
			<td>${film.type }</td>
			<td>${film.nation }</td>
			<td>${film.releaseDate }</td>
			<td>${film.time }</td>
			<td>${film.average }</td>
			<td>${film.votes }</td>
			<td><a href="${film.filmUrl}">${film.filmUrl }</a></td>
			<td><a href="${film.imdbUrl }">${film.imdbUrl }</a></td>
		</tr>
   </c:forEach>
	</tbody>
</table>
</div>
 
</div>
 
 
</form>
</body>
</html>

