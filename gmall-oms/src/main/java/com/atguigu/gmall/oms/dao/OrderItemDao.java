package com.atguigu.gmall.oms.dao;

import com.atguigu.gmall.oms.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author xujiqiang
 * @email fenchuiyun@gmail.com
 * @date 2019-10-29 16:36:55
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
