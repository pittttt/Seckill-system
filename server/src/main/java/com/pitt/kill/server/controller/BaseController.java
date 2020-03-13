package com.pitt.kill.server.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pitt.kill.api.enums.StatusCode;
import com.pitt.kill.api.response.BaseResponse;

@Controller
public class BaseController {
	private static final Logger log = LoggerFactory.getLogger(BaseController.class);

	@GetMapping("/hello")
	public String hello(String name, ModelMap modelMap) {
		if (StringUtils.isBlank(name)) {
			name = "这是hello~";
		}
		modelMap.put("name", name);
		return "hello";
	}

	@ResponseBody
	@GetMapping("/response")
	public BaseResponse<String> response(String name) {
		BaseResponse<String> baseResponse = new BaseResponse<String>(StatusCode.Success);
		if (StringUtils.isBlank(name)) {
			name = "这是hello~";
		}
		baseResponse.setData(name);
		return baseResponse;
	}

	@GetMapping("/error")
	public String error() {
		return "error";
	}
}
