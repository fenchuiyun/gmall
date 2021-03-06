package com.atguigu.gmall.pms.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by 12441 on 2019/10/30
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean("商品平台")
    public Docket userApis(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("商品平台")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.regex("/pms.*"))//pms下的所有路径
                .build()
                .apiInfo(apiInfo())
                .enable(true);

    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("谷粒商城-商品平台接口文档")
                .description("提供商品的平台文档")
                .termsOfServiceUrl("http://www.fenchuiyun.com/")
                .version("1.0")
                .build();
    }

}
