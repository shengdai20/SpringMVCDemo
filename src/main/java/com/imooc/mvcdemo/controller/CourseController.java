package com.imooc.mvcdemo.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.imooc.mvcdemo.model.Course;
import com.imooc.mvcdemo.service.CourseService;

@Controller
@RequestMapping("/courses")
public class CourseController {

	private static Logger log = LoggerFactory.getLogger(CourseController.class);
	
	private CourseService courseService;

	@Autowired
	public void setCourseService(CourseService courseService) {
		this.courseService = courseService;
	}
	
	//本方法将处理 /courses/view?courseId=123 形式的URL，这里的123可以随意指定
	//@RequestParam可以绑定传过来的参数
	@RequestMapping(value="/view", method=RequestMethod.GET)
	public String viewCourse(@RequestParam("courseId") Integer courseId, Model model) {
		log.debug("In viewCourse, courseId = {}", courseId);
		Course course = courseService.getCoursebyId(courseId);
		//这里model类型是springMVC所特有的，其加入进去的属性名称可以在返回的jsp中直接得到
		model.addAttribute(course);
		//这里返回的是某一个jsp的名字，且不用显示说明其绝对位置，因为在mvc-dispatcher-servlet.xml中已经设置好了jsp应该存放的位置
		return "course_overview";
	}
	
	//现代方法
	//本方法将处理 /courses/view2/123 形式的URL
	@RequestMapping("/view2/{courseId}")
	public String viewCourse2(@PathVariable("courseId") Integer courseId, Map<String, Object> model) {
		log.debug("In viewCourse2, courseId = {}", courseId);
		Course course = courseService.getCoursebyId(courseId);
		//这里第一个course也就是在返回的jsp中能直接使用的名称
		model.put("course",course);
		return "course_overview";
	}
	
	//传统方法
	//本方法将处理 /courses/view3?courseId=123 形式的URL
	@RequestMapping("/view3")
	public String viewCourse3(HttpServletRequest request) {
		
		Integer courseId = Integer.valueOf(request.getParameter("courseId"));
		log.debug("In viewCourse2, courseId = {}", courseId);
		Course course = courseService.getCoursebyId(courseId);
		//这里用request也可以指定jsp中所使用的对象名称
		request.setAttribute("course",course);
		
		return "course_overview";
	}
	
	@RequestMapping(value="/admin", method = RequestMethod.GET, params = "add")
	public String createCourse(){
		//如果jsp不是放在mvc-dispatcher-servlet.xml所设置的文件目录下，则应该显示说明其相对位置
		return "course_admin/edit";
	}
	
	//利用@ModelAttribute完成模型对象和jsp页面数据的绑定
	@RequestMapping(value="/save", method = RequestMethod.POST)
	public String  doSave(@ModelAttribute Course course){		
		
		log.debug("Info of Course:");
		log.debug(ReflectionToStringBuilder.toString(course));
		
		//在此进行业务操作，比如数据库持久化，这里持久化的时候应该注意jsp中的数据怎传送到当前这个url中来
		//这个数据对象在jsp中的name属性应该和Course中的属性名保持一致
		course.setCourseId(123);
		//请求重定向
		return "redirect:view2/"+course.getCourseId();
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.GET)
	public String showUploadPage(@RequestParam(value= "multi", required = false) Boolean multi){	
		if(multi != null && multi){
			return "course_admin/multifile";	
		}
		return "course_admin/file";		
	}
	
	@RequestMapping(value="/doUpload", method=RequestMethod.POST)
	//利用@RequestParam来将jsp中name属性为file的值绑定到我们controller下的参数中来
	public String doUploadFile(@RequestParam("file") MultipartFile file) throws IOException{
		
		if(!file.isEmpty()){
			log.debug("Process file: {}", file.getOriginalFilename());
			//上传到某一个指定的位置
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File("e:\\temp\\imooc\\", System.currentTimeMillis()+ file.getOriginalFilename()));
		}
		
		return "success";
	}
	
	//@ResponseBody表示我们返回的Course对象可以被我们的响应所使用，也就是会给我们的浏览器返回一个对象，然后会被默认解析成JSON格式对象
	@RequestMapping(value="/{courseId}",method=RequestMethod.GET)
	public @ResponseBody Course getCourseInJson(@PathVariable Integer courseId){
		//这里我们返回的是一个Course类
		return  courseService.getCoursebyId(courseId);
	}
	
	//这里会返回一个Course的实例对象，与上面的方法功能相同
	@RequestMapping(value="/jsontype/{courseId}",method=RequestMethod.GET)
	public  ResponseEntity<Course> getCourseInJson2(@PathVariable Integer courseId){
		Course course =   courseService.getCoursebyId(courseId);		
		return new ResponseEntity<Course>(course, HttpStatus.OK);
	}
	
	//在正确部署上面的方法后，可以访问我们的url：http://localhost:8080/course_json.jsp?courseId=123
}
