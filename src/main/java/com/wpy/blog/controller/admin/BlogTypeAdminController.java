package com.wpy.blog.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.wpy.blog.entity.BlogType;
import com.wpy.blog.entity.PageBean;
import com.wpy.blog.service.BlogTypeService;
import com.wpy.blog.util.ResponseUtil;


@Controller
@RequestMapping("/admin/blogType")
public class BlogTypeAdminController {

	@Resource
	private BlogTypeService blogTypeService;
	
	
	@RequestMapping("/blogTypeManage")
	public  String blogTypeManage(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		
		
	    return "admin/blogTypeManage";
	}
	
	@RequestMapping("/list")
	public  String list(HttpServletRequest request,HttpServletResponse response,Model model,@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String pageSize) throws Exception{
		PageBean pageBean = new PageBean(Integer.valueOf(page),Integer.valueOf(pageSize));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		List<BlogType> blogTypeList=blogTypeService.getAll(map);
		Integer total=blogTypeService.getTotalCount();
		Map<String,Object> result = new HashMap<>();
		result.put("rows",blogTypeList);
		result.put("total", total);
		String json = JSON.toJSONString(result);
		ResponseUtil.write(response, json);
	    return null;
	}
	
	@RequestMapping("/deleteBlogType")
	public void deleteBlogType(HttpServletRequest request,HttpServletResponse response,String ids){
		String[] idsArray = ids.split(",");
		for(int i=0;i<idsArray.length;i++){
			blogTypeService.delete(Integer.valueOf(idsArray[i]));
		}
		
	}
	
	@RequestMapping("/addBlogType")
	public void addBlogType(HttpServletRequest request,HttpServletResponse response,BlogType blogType) throws Exception{
		
		Map<String, Object> map= new HashMap<>();
		try {
			if("".equals(blogType.getId()) ||blogType.getId()== null){
				blogTypeService.add(blogType);
				map.put("success", true);
			}
			else{
				blogTypeService.update(blogType);
				map.put("success", true);
			}
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		String result = JSON.toJSONString(map);
		ResponseUtil.write(response,result);
	}
	
}
