package com.catorv.test.gallop.cfg;

import com.catorv.gallop.cfg.Configuration;

import java.util.Date;

/**
 * 配置文件模块测试用例
 * Created by cator on 6/21/16.
 */
@Configuration("test.config")
public class Model1 {

	int intValue;
	float floatValue;
	double doubleValue;
	long longValue;
	String stringValue;
	boolean booleanValue;
	Date dateValue;

}
