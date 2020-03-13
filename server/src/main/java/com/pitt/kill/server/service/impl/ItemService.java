package com.pitt.kill.server.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pitt.kill.model.entity.ItemKill;
import com.pitt.kill.model.mapper.ItemKillMapper;
import com.pitt.kill.server.controller.ItemController;
import com.pitt.kill.server.service.IItemService;

@Service
public class ItemService implements IItemService {
	private static final Logger log = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemKillMapper itemMapper;

	// 获取待秒杀的商品列表
	@Override
	public List<ItemKill> getKillItems() throws Exception {
		return itemMapper.selectAll();
	}

	// 获取秒杀商品详情
	@Override
	public ItemKill getKillDetail(Integer id) throws Exception {
		ItemKill item = itemMapper.selectById(id);
		if (item == null) {
			throw new Exception("获取秒杀商品详情不存在");
		}
		return item;
	}
}
