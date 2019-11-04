package com.atguigu.gmall.pms;

import com.atguigu.gmall.pms.dao.BrandDao;
import com.atguigu.gmall.pms.entity.BrandEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
class GmallPmsApplicationTests {

    private static HashMap<Integer,String> map = new HashMap<Integer,String>(2,0.75f);

    @Autowired
    private BrandDao brandDao;

    @Test
    void contextLoads() {

    }

    @Test
    void hashMapInfiniteLoop(){
        map.put(5, "C");

        new Thread("Thread1") {
            public void run() {
                map.put(7, "B");
                System.out.println(map);
            };
        }.start();
        new Thread("Thread2") {
            public void run() {
                map.put(3, "A");
                        System.out.println(map);
            };
        }.start();
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
