<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!-- 使用JSTL标签需要引入的头文件 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- 使用fmt 对时间进行格式化的时候需要引入的头文件 -->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="data_list">
		<div class="data_list_title">
		<img src="${pageContext.request.contextPath}/images/list_icon.png"/>
		博客列表</div>
		<div class="diary_datas">
			<ul>
				<c:forEach var="diary" items="${diaryList }">
					<li>『<fmt:formatDate value="${diary.releaseDate }" type="date" pattern="yyyy-MM-dd"/>』<span>&nbsp;<a href="diary?action=show&diaryId=${diary.diaryId }">${diary.title }</a></span></li>
				</c:forEach>                                                          <!-- 给后台传入两个参数，一个是action，一个是diaryId，
                                                                               action是确定点击链接，diaryId是点击链接之后传递给后台的日志Id -->
				<!-- formatDate是将日期转换为字符串 -->
			</ul>
		</div>
		<div class="pagination pagination-centered">
			<ul>
				${pageCode }
			</ul>
		</div>
</div>
