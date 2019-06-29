package com.wl.wlp2ploansystem.infrastructures.common.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.any;

/**
 * Swagger2 文档配置
 *
 */
@Configuration
public class Swagger2Configuration {

        @Bean
        public Docket createRestApi() {
            ParameterBuilder aParameterBuilder = new ParameterBuilder();
            aParameterBuilder
                    .parameterType("header") //参数类型支持header, cookie, body, query etc
                    .name("Authorization") //参数名
                    .defaultValue("******************") //默认值
                    .description("用户token")
                    .name("X-Requested-With") //参数名
                    .defaultValue("XMLHttpRequest") //默认值
                    .description("访问方式")
                    .modelRef(new ModelRef("string"))//指定参数值的类型
                    .required(false).build(); //非必需，这里是全局配置，然而在登陆的时候是不用验证的
            List<Parameter> aParameters = new ArrayList<Parameter>();
            aParameters.add(aParameterBuilder.build());

            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .select()
                    .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                    .paths(any())
                    .build()
                    .globalOperationParameters(aParameters);
        }

        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title("api文档")
                    .description("api文档")
                    .termsOfServiceUrl("")
                    .version("1.0")
                    .build();
        }
    }
