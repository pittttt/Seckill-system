package com.pitt.kill.server.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pitt.kill.api.enums.StatusCode;
import com.pitt.kill.api.response.BaseResponse;
import com.pitt.kill.model.dto.KillSuccessUserInfo;
import com.pitt.kill.model.mapper.ItemKillSuccessMapper;
import com.pitt.kill.server.dto.KillDto;
import com.pitt.kill.server.service.IKillService;

@Controller
public class KillController {
	private static final Logger log = LoggerFactory.getLogger(KillController.class);

	@Autowired
	private IKillService killService;

	@Autowired
	private ItemKillSuccessMapper itemKillSuccessMapper;

	/**
	 * 商品秒杀业务逻辑
	 * 
	 * GET方式无请求体，所以使用@RequestBody接收数据时，前端不能使用GET方式提交数据，而是用POST方式进行提交
	 * 
	 * @param dto
	 * @param result
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/execute", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public BaseResponse execute(@RequestBody @Validated KillDto dto, BindingResult result, HttpSession session) {
		// 进行数据校验
		if (result.hasErrors() || dto.getKillId() <= 0) {
			return new BaseResponse(StatusCode.InvalidParams);
		}
		Object uId = session.getAttribute("uid");
		if (uId == null) {
			return new BaseResponse(StatusCode.UserNotLogin);
		}
		Integer userId = (Integer) uId;
		BaseResponse response = new BaseResponse(StatusCode.Success);
		try {
			Boolean res = killService.killItem(dto.getKillId(), userId);
			if (!res) {
				return new BaseResponse(StatusCode.Fail.getCode(), "商品已抢购完毕了哦!");
			}

		} catch (Exception e) {
			response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
		}
		return response;
	}

	/**
	 * 商品秒杀业务逻辑-用于压力测试
	 * 
	 * @param dto
	 * @param result
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/execute/lock", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public BaseResponse executeLock(@RequestBody @Validated KillDto dto, BindingResult result) {
		// 进行数据校验
		if (result.hasErrors() || dto.getKillId() <= 0) {
			return new BaseResponse(StatusCode.InvalidParams);
		}
		BaseResponse response = new BaseResponse(StatusCode.Success);
		try {
			// 不加分布式锁
			// Boolean res = killService.killItem(dto.getKillId(),
			// dto.getUserId());
			// if (!res) {
			// return new BaseResponse(StatusCode.Fail.getCode(),
			// "不加分布式锁-商品已抢购完毕了哦!");
			// }

			// 基于Redis的分布式锁进行控制
			Boolean res = killService.killItemV3(dto.getKillId(), dto.getUserId());
			if (!res) {
				return new BaseResponse(StatusCode.Fail.getCode(), "基于Redis的分布式锁进行控制-商品已抢购完毕了哦!");
			}

			// 基于Redisson的分布式锁进行控制
			// Boolean res = killService.killItemV4(dto.getKillId(),
			// dto.getUserId());
			// if (!res) {
			// return new BaseResponse(StatusCode.Fail.getCode(),
			// "基于Redisson的分布式锁进行控制-商品已抢购完毕了哦!");
			// }

			// 基于ZooKeeper的分布式锁进行控制
			// Boolean res = killService.killItemV5(dto.getKillId(),
			// dto.getUserId());
			// if (!res) {
			// return new BaseResponse(StatusCode.Fail.getCode(),
			// "基于ZooKeeper的分布式锁进行控制-商品已抢购完毕了哦!");
			// }

		} catch (Exception e) {
			response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
		}
		return response;
	}

	/**
	 * 查看订单详情
	 */
	@RequestMapping(value = "/record/detail/{orderNo}", method = RequestMethod.GET)
	public String killRecordDetail(@PathVariable String orderNo, ModelMap modelMap) {
		if (StringUtils.isBlank(orderNo)) {
			return "error";
		}
		KillSuccessUserInfo info = itemKillSuccessMapper.selectByCode(orderNo);
		if (info == null) {
			return "error";
		}
		modelMap.put("info", info);
		return "killRecord";
	}

	// 抢购成功跳转页面
	@GetMapping("/execute/success")
	public String executeSuccess() {
		return "executeSuccess";
	}

	// 抢购失败跳转页面
	@GetMapping("/execute/fail")
	public String executeFail() {
		return "executeFail";
	}
}
