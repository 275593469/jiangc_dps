package top.jiangc.common;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("commonController")
public class CommonController {
	
	@RequestMapping("/{view}")
	public ModelAndView forwardView(@PathVariable("view") String view){
		if(view.indexOf("#") > 0){
			view = view.replace("#", "/");
		}
		return new ModelAndView(view);
		//index/index.html
	}
	
	@RequestMapping("/view/**")
	public ModelAndView forwardViews(final HttpServletRequest request){
		String url = request.getRequestURI();
		String view = "view";
		
		view = url.substring(url.indexOf(view)+view.length());
		
		return new ModelAndView(view);
		
	}
	
	//跳转到登录页面
	@RequestMapping("/")
	public ModelAndView forwardLogin(final HttpServletRequest request,final HttpServletResponse response) throws IOException{
		response.sendRedirect(request.getContextPath()+"/view/user/login");
		return null;
		
	}
	
	
	
}
