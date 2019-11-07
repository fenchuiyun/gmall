package com.atguigu.gmall.search.feign;

import com.atguigu.gmall.pms.api.GmallPmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Created by 12441 on 2019/11/5
 */
@FeignClient("pms-service")
public interface GmallPmsClient extends GmallPmsApi {
}
