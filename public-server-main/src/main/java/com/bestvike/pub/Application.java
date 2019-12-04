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

@SpringBootApplication(scanBasePackages = "com.bestvike.pub")
@ServletComponentScan(basePackages = {"com.bestvike.maintenance"})
@EnableScheduling
@EnableEncryptableProperties
public class Application {

	@Value("${app.redis.prefix}")
	private String redisPrefix;
	@Autowired
	private RedisTemplate redisTemplate;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public Cache cache() {
		return new Cache(redisPrefix, redisTemplate);
	}
}
