/**
 * 
 */
package com.welltech.controller.history;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.welltech.framework.aop.pagination.bean.MyPage;
import com.welltech.service.impl.LoginServiceImpl;

/**
 * Created by Zhujia at 2017年8月14日 下午5:45:32
 */
@Controller
public class LoginHistoryController {

	@Autowired
	LoginServiceImpl loginServiceImpl;
	
	@RequestMapping(value = { "loginHistory" })
	public String loginData(Model model, MyPage myPage, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date beginTime,
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
		if (myPage == null) {
			myPage = new MyPage();
		}
		model.addAttribute("datas", loginServiceImpl.listLoginHistory(myPage , beginTime, endTime));
		model.addAttribute("myPage", myPage);
		model.addAttribute("beginTime", beginTime);
		model.addAttribute("endTime", endTime);
		return "history/loginHistory";
	}

}
