package com.atguigu.gmall.pms.service.impl;

import com.alibaba.nacos.client.utils.StringUtils;
import com.atguigu.gmall.pms.vo.SpuInfoVO;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.SpuInfoDescDao;
import com.atguigu.gmall.pms.entity.SpuInfoDescEntity;
import com.atguigu.gmall.pms.service.SpuInfoDescService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoDescService")
public class SpuInfoDescServiceImpl extends ServiceImpl<SpuInfoDescDao, SpuInfoDescEntity> implements SpuInfoDescService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuInfoDescEntity> page = this.page(
                new Query<SpuInfoDescEntity>().getPage(params),
                new QueryWrapper<SpuInfoDescEntity>()
        );

        return new PageVo(page);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSpuDesc(SpuInfoVO spuInfoVO, Long spuId) {
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        //注意:spu_info_desc表的主键是spu_id，需要在实体类中配置不是自增主键
        spuInfoDescEntity.setSpuId(spuId);
        //把商品的描述，保存到spu详情中，图片地址以逗号进行分割spuImagesgetSupImages
        spuInfoDescEntity.setDecript(StringUtils.join(spuInfoVO.getSpuImages(),","));
        this.save(spuInfoDescEntity);
    }

}