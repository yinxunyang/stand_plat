package com.bestvike.pub.service;

import com.bestvike.pub.data.table.PPublish;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

@Service
public interface PPublishService {

	PageInfo<PPublish> fetchPublishes(PPublish pPublish);

	PPublish insert(PPublish pPublish, String userIdAndUserName);

	PPublish modify(PPublish pPublish);

	int remove(String ids);
}
