package com.pitt.kill.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pitt.kill.model.entity.User;
import com.pitt.kill.model.entity.UserExample;

public interface UserMapper {

	// 根据用户名查询
	User selectByUserName(@Param("userName") String userName);

	// 根据用户名密码查询
	User selectByUserNamePsd(@Param("userName") String userName, @Param("password") String password);

	long countByExample(UserExample example);

	int deleteByExample(UserExample example);

	int deleteByPrimaryKey(Integer id);

	int insert(User record);

	int insertSelective(User record);

	List<User> selectByExample(UserExample example);

	User selectByPrimaryKey(Integer id);

	int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

	int updateByExample(@Param("record") User record, @Param("example") UserExample example);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);
}