package top.jiangc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("handleController")
@RequestMapping("/handle")
public class HandleController {

	private Logger logger = LoggerFactory.getLogger(HandleController.class);
	
	@RequestMapping("/test")
	public String test(){
		logger.info("qweqwr");
		System.out.println("qwewe");
		return "214";
	}
}
