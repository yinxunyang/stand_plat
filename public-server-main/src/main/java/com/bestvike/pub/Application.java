package com.bestvike.pub;

import com.bestvike.commons.redis.Cache;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.bestvike")
@ServletComponentScan(basePackages = {"com.bestvike"})
@EnableScheduling
@EnableEncryptableProperties
public class Application {

	@Value("${app.redis.prefix}")
	private String redisPrefix;
	@Autowired
	private RedisTemplate redisTemplate;

	public static void main(String[] args) {
		/**
		 * Springboot整合Elasticsearch 在项目启动前设置一下的属性，防止报错
		 * 初始化client时还会抛出异常
		 * java.lang.IllegalStateException: availableProcessors is already set to [8], rejecting [8]
		 */
		System.setProperty("es.set.netty.runtime.available.processors", "false");
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public Cache cache() {
		return new Cache(redisPrefix, redisTemplate);
	}
}
