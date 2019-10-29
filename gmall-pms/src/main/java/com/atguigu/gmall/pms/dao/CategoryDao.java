package com.atguigu.gmall.pms.dao;

import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author xujiqiang
 * @email fenchuiyun@gmail.com
 * @date 2019-10-29 00:56:19
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
