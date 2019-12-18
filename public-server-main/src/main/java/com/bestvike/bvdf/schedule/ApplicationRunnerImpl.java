package com.bestvike.bvdf.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

/**
 * @Author: yinxunyang
 * @Description: 项目启动后执行的方法类
 * @Date: 2019/12/18 9:18
 */
@Service
@Slf4j
public class ApplicationRunnerImpl implements ApplicationRunner {
	/**
	 * 是否启用bvdfToEsSchedule
	 */
	@Value("${standplatConfig.bvdfToEsSchedule.isEnable}")
	private String isEnable;
	@Autowired
	private BvdfScheduleTask bvdfScheduleTask;

	/**
	 * @Author: yinxunyang
	 * @Description: bvdfToEs的定时任务
	 * @Date: 2019/12/18 9:22
	 * @param:
	 * @return:
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		if ("Y".equals(isEnable)) {
			log.info("启用bvdfToEsSchedule");
			bvdfScheduleTask.startCron();
		} else {
			log.info("不启用bvdfToEsSchedule");
		}

	}
}