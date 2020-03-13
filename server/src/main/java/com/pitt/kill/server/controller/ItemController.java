package com.pitt.kill.server.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pitt.kill.model.entity.ItemKill;
import com.pitt.kill.server.service.IItemService;

/**
 * 待秒杀商品列表
 * 
 * @author pitt
 *
 */
@Controller
public class ItemController {

	private static final Logger log = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private IItemService iItemService;

	// 获取待秒杀商品列表
	@GetMapping(value = { "/", "/index" })
	public String list(ModelMap modeMap) {
		try {
			List<ItemKill> list = iItemService.getKillItems();
			modeMap.put("list", list);
			log.info("获取待秒杀商品列表-数据:{}", list);
		} catch (Exception e) {
			log.error("获取待秒杀商品列表-发生异常：", e.fillInStackTrace());
			return "redirect:/error";
		}
		return "list";
	}

	// 获取商品详情
	@GetMapping("/item/detail/{id}")
	public String info(@PathVariable("id") Integer id, ModelMap modelMap) {
		if (id == null || id <= 0) {
			return "redirect:/error";
		}
		try {
			ItemKill detail = iItemService.getKillDetail(id);
			modelMap.put("detail", detail);
		} catch (Exception e) {
			log.error("获取商品详情-发生异常：", e.fillInStackTrace());
			return "redirect:/error";
		}
		return "info";
	}
}
