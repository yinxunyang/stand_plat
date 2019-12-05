package com.bestvike.pub.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

 /**
   * @Author: yinxunyang
   * @Description: ElasticSearch配置
   * @Date: 2019/12/5 19:53
   * @param:
   * @return:
   */
@Configuration
public class ElasticSearchConfig {
	/**
	 * 防止netty的bug
	 * java.lang.IllegalStateException: availableProcessors is already set to [8], rejecting [8]
	 */
	@PostConstruct
	void init() {
		System.setProperty("es.set.netty.runtime.available.processors", "false");
	}
}