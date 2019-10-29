package com.atguigu.gmall.ums.dao;

import com.atguigu.gmall.ums.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author xujiqiang
 * @email fenchuiyun@gmail.com
 * @date 2019-10-29 17:31:27
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
