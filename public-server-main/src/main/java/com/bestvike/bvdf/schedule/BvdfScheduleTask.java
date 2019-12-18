package com.bestvike.bvdf.schedule;

import com.bestvike.bvdf.service.BvdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledFuture;

/**
 * @Author: yinxunyang
 * @Description: bvdfToEs的定时任务配置
 * @Date: 2019/12/18 9:24
 */
@Slf4j
@Service
public class BvdfScheduleTask {
	/**
	 * 定时任务的间隔时间
	 */
	@Value("${standplatConfig.bvdfToEsSchedule.cornTime}")
	private String cornTime;
	@Autowired
	private BvdfService bvdfService;
	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;
	private ScheduledFuture future;

	/**
	 * @Author: yinxunyang
	 * @Description: 启用bvdfToEsSchedule
	 * @Date: 2019/12/18 9:43
	 * @param:
	 * @return:
	 */
	public void startCron() {
		future = threadPoolTaskScheduler.schedule(new Runnable() {
			@Override
			public void run() {
				bvdfService.bvdfHouseToEs();
			}
		}, new CronTrigger(cornTime));
	}
}
