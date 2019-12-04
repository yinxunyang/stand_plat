package com.bestvike.pub.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger2 configuration.
 *
 * @author Liu qingxiang
 * @since v1.0.0
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {

	@Value("${swagger2.enabled:false}")
	private boolean swaggerEnabled;

	@Bean
	public Docket swaggerSpringMvcPlugin() {
		if (swaggerEnabled) {
			return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select().apis(RequestHandlerSelectors.withMethodAnnotation(
					ApiOperation.class)).build();
		} else {
			return new Docket(DocumentationType.SWAGGER_2)
				.select().paths(PathSelectors.none()).build();
		}
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title("hmis")
			.description("hmis赤峰房产信息系统")
			.license("")
			.licenseUrl("")
			.termsOfServiceUrl("")
			.version("1.0.0")
			.contact(new Contact("", "", ""))
			.build();
	}
}