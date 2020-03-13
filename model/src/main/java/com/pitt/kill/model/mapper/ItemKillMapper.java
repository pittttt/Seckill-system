package com.pitt.kill.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pitt.kill.model.entity.ItemKill;
import com.pitt.kill.model.entity.ItemKillExample;

public interface ItemKillMapper {

	// 查询待秒杀的商品列表
	List<ItemKill> selectAll();

	// 获取秒杀商品详情
	ItemKill selectById(Integer id);

	// 扣库存
	int updateKillItem(Integer id);

	long countByExample(ItemKillExample example);

	int deleteByExample(ItemKillExample example);

	int deleteByPrimaryKey(Integer id);

	int insert(ItemKill record);

	int insertSelective(ItemKill record);

	List<ItemKill> selectByExample(ItemKillExample example);

	ItemKill selectByPrimaryKey(Integer id);

	int updateByExampleSelective(@Param("record") ItemKill record, @Param("example") ItemKillExample example);

	int updateByExample(@Param("record") ItemKill record, @Param("example") ItemKillExample example);

	int updateByPrimaryKeySelective(ItemKill record);

	int updateByPrimaryKey(ItemKill record);
}