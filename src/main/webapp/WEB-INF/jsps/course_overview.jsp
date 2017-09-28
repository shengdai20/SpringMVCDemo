<%@ page language="java" 
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我不是真正的慕课网</title>

<!-- 这里css所要放置的位置应该是在mvc-dispatcher-servlet.xml所设置的位置 -->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/main.css"
	type="text/css" />
</head>
<body>

	<div id="main">

		<div class="newcontainer" id="course_intro">
		<!-- 这里的course.title中的course也就是在controller中所加入进去的属性名称 -->
			<div class="course-title">${course.title}</div>
			<div class="course_info">
				<div class="course-embed l">
					<div id="js-course-img" class="img-wrap">
						<img width="600" height="340" alt=""
							src="<%=request.getContextPath()%>/${course.imgPath}"
							class="course_video" />
					</div>
					<div id="js-video-wrap" class="video" style="display: none">
						<div class="video_box" id="js-video"></div>
					</div>
				</div>
				<div class="course_state">
					<ul>
						<li><span>学习人数</span> <em>${course.learningNum }</em></li>
						<li class="course_hour"><span>课程时长</span> <em
							class="ft-adjust"><span>${course.duration }</span>秒</em></li>
						<li><span>课程难度</span> <em>${course.levelDesc }</em></li>
					</ul>
				</div>
			</div>
			<div class="course_list">
				<div class="outline">
					<h3 class="chapter_introduces">课程介绍</h3>
					<div class="course_shortdecription">${course.descr}</div>

					<h3 class="chapter_catalog">课程提纲</h3>
					<ul id="couList">
						<c:forEach items="${course.chapterList}" var="chapter">
							<li class="clearfix open"><a href="#">
									<div class="openicon"></div>
									<div class="outline_list l">
										<!-- <em class="outline_zt"></em> -->
										<h5 class="outline_name">${chapter.title }</h5>
										<p class="outline_descr">${chapter.descr }</p>
									</div>
							</a></li>
						</c:forEach>
					</ul>
				</div>

			</div>
		</div>

	</div>


</body>
</html>