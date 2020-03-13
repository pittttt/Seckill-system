package com.pitt.kill.server.service;

import java.util.List;

import com.pitt.kill.model.entity.ItemKill;

public interface IItemService {

	List<ItemKill> getKillItems() throws Exception;

	ItemKill getKillDetail(Integer id) throws Exception;
}
