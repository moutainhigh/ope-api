package com.xianjinxia.service.remote;

import com.xianjinxia.conf.MyRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseRemoteService {

	@Autowired
	protected MyRestTemplate myRestTemplate;

	protected String buildUrl(String relativeUrl){
		StringBuilder stringBuilder = new StringBuilder(getServiceName());
		return stringBuilder.append(relativeUrl).toString();
	}

	protected abstract String getServiceName();
}
