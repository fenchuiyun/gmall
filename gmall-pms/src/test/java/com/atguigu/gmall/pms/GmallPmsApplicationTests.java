package com.atguigu.gmall.pms;

import com.atguigu.gmall.pms.dao.BrandDao;
import com.atguigu.gmall.pms.entity.BrandEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
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
        new User("fenchuiyun","xjq");
    }

    @Test
    void test111(){

    }

}

@Data
@AllArgsConstructor
class User{
    String namae;
    String id;
}
