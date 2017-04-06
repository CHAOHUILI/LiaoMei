package com.vidmt.lmei.util.think;
import java.util.Date;

import com.ta.TAApplication;
import com.ta.util.config.TAIConfig;

public class PreferencesUtil {
	
	public static  void set(Object o,TAApplication application)
	{
		TAIConfig config = application.getConfig(TAApplication.PROPERTIESCONFIG);
		
	/*	TestDataEntity testDataEntity = new TestDataEntity();
		testDataEntity.setName("Think Android ADD");
		testDataEntity.setB(true);
		Character c1 = new Character('c');
		testDataEntity.setC(c1);
		testDataEntity.setD(10);
		testDataEntity.setDate(new Date());
		testDataEntity.setF(2f);
		testDataEntity.setI(123);*/
		// ��������
		config.setConfig(o);
	}
	public static <T extends Object> T get(TAApplication application,Class<T> clazz)
	{
		// ��ȡ����
		//TestDataEntity dataEntity = config.getConfig(TestDataEntity.class);
		return application.getConfig(TAApplication.PROPERTIESCONFIG).getConfig(clazz);
	}
	
}
