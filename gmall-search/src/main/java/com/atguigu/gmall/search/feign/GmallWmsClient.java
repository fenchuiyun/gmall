package com.atguigu.gmall.search.feign;

import com.atguigu.gmall.wms.api.GmallWmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Created by 12441 on 2019/11/5
 */
@FeignClient("wms-service")
public interface GmallWmsClient extends GmallWmsApi {
}
