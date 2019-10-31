package com.atguigu.gmall.pms;

import com.atguigu.gmall.pms.dao.BrandDao;
import com.atguigu.gmall.pms.entity.BrandEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GmallPmsApplicationTests {

    @Autowired
    private BrandDao brandDao;

    @Test
    void contextLoads() {

    }

    @Test
    void test(){
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setDescript("尚硅谷真好");
        brandEntity.setFirstLetter("S");
        brandEntity.setLogo("www.baidu.com/log.gif");
        brandEntity.setName("尚硅谷");
        brandEntity.setShowStatus(0);
        brandEntity.setSort(1);
        this.brandDao.insert(brandEntity);
    }

}
