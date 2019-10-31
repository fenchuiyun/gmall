package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;


import java.util.List;

/**
 * Created by 12441 on 2019/10/31
 */
public class ProductAttrValueVO extends ProductAttrValueEntity {
    public void setValueSelected(List<Object> valueSelected){
        //如何接受的集合为空，则不设置
        if (CollectionUtils.isEmpty(valueSelected)){
            return;
        }
        this.setAttrValue(StringUtils.join(valueSelected,","));
    }
}
