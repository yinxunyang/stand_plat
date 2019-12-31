package com.bestvike.standplat;

import com.bestvike.commons.enums.MatchTypeEnum;
import com.bestvike.commons.enums.RecordTimeEnum;
import com.bestvike.dataCenter.entity.BvdfToEsRecordTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Slf4j
public class MongoTest extends BaseTest {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	public void test1() {
		BvdfToEsRecordTime bvdfToEsRecordTime =new BvdfToEsRecordTime();
		bvdfToEsRecordTime.setId(RecordTimeEnum.BVDF_CORP_ID.getCode());
		bvdfToEsRecordTime.setLastExcuteTime("2000-12-19 10:53:01");
		bvdfToEsRecordTime.setMatchType(MatchTypeEnum.DEVELOP.getCode());
		bvdfToEsRecordTime.setDescribe(MatchTypeEnum.DEVELOP.getDesc());
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
	@Test
	public void test3() {
		BvdfToEsRecordTime bvdfToEsRecordTime =new BvdfToEsRecordTime();
		bvdfToEsRecordTime.setId(RecordTimeEnum.BVDF_REGION_ID.getCode());
		bvdfToEsRecordTime.setLastExcuteTime("2000-12-19 10:53:01");
		bvdfToEsRecordTime.setMatchType(MatchTypeEnum.REGION.getCode());
		bvdfToEsRecordTime.setDescribe(MatchTypeEnum.REGION.getDesc());
		mongoTemplate.save(bvdfToEsRecordTime);
		int i = 0;
	}
	@Test
	public void test5() {
		BvdfToEsRecordTime bvdfToEsRecordTime =new BvdfToEsRecordTime();
		bvdfToEsRecordTime.setId(RecordTimeEnum.BVDF_BLD_ID.getCode());
		bvdfToEsRecordTime.setLastExcuteTime("2000-12-19 10:53:01");
		bvdfToEsRecordTime.setMatchType(MatchTypeEnum.BLD.getCode());
		bvdfToEsRecordTime.setDescribe(MatchTypeEnum.BLD.getDesc());
		mongoTemplate.save(bvdfToEsRecordTime);
		int i = 0;
	}
	@Test
	public void test4() {
		mongoTemplate.dropCollection(BvdfToEsRecordTime.class);
		int i = 0;
	}
	@Test
	public void test6() {
		BvdfToEsRecordTime bvdfToEsRecordTime =new BvdfToEsRecordTime();
		bvdfToEsRecordTime.setId(RecordTimeEnum.BVDF_HOUSE_ID.getCode());
		bvdfToEsRecordTime.setLastExcuteTime("2018-11-06 10:53:01");
		bvdfToEsRecordTime.setMatchType(MatchTypeEnum.BLD.getCode());
		bvdfToEsRecordTime.setDescribe(MatchTypeEnum.BLD.getDesc());
		mongoTemplate.save(bvdfToEsRecordTime);
		int i = 0;
	}
}
