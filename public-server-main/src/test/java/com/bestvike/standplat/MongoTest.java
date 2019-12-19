package com.bestvike.standplat;

import com.bestvike.dataCenter.entity.BvdfToEsRecordTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;

@Slf4j
public class MongoTest extends BaseTest {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	public void test1() {
		BvdfToEsRecordTime bvdfToEsRecordTime =new BvdfToEsRecordTime();
		bvdfToEsRecordTime.setId("bvdfCorp");
		bvdfToEsRecordTime.setCorpLastExcuteTime("2000-12-19 10:53:01");
		mongoTemplate.save(bvdfToEsRecordTime);
		int i = 0;
	}
	@Test
	public void test2(){
		Query query = new Query(Criteria.where("_id").is("123"));
		log.info(""+System.currentTimeMillis());
		BvdfToEsRecordTime bvdfToEsRecordTime= mongoTemplate.findOne(query, BvdfToEsRecordTime.class);
		log.info(""+System.currentTimeMillis());
		int i = 0;
	}
}
