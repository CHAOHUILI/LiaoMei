package com.vidmt.lmei.util.think;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.ta.TAApplication;
import com.ta.util.db.TASQLiteDatabase;

public class DbUtil {
	private static TASQLiteDatabase sqLiteDatabase;

	/**
	 * 创建数据库连接资源
	 * @param TAApplication 传入getTAApplication()
	 */
	public DbUtil(TAApplication TAApplication) {
		if (sqLiteDatabase == null)
			sqLiteDatabase = TAApplication.getSQLiteDatabasePool()
					.getSQLiteDatabase();
	}
    
	/**
	 * 释放连接库资源
	 * @param TAApplication  传入getTAApplication()
	 */
	public void releaseSQLiteDatabase(TAApplication TAApplication) {
		TAApplication.getSQLiteDatabasePool().releaseSQLiteDatabase(
				sqLiteDatabase);
	}
    /**
     * 插入数据库
     * @param objData 插入的对象，需要实现接口Serializable， user u = new user() u.name='aa' 将u放入即可
     * @param c 需要插入到哪个表中  比如user.class 会自动创建user表
     */
	public void insertData(Object objData, Class c) {
		if (!sqLiteDatabase.hasTable(c)) {
			sqLiteDatabase.creatTable(c);
		}
		sqLiteDatabase.insert(objData);
	}
	/**
	 * 查询数据到List
	 * @param clazz 查询的表名，比如user.class 就是查询user表
	 * @param wherStr 查询的条件  name="'张三'"
	 * @return
	 */
	public List selectData(Class<?> clazz,String wherStr)
	{
		// TODO Auto-generated method stub
		List  list = sqLiteDatabase.query(clazz,
				false, wherStr, null, null, null, null);
		return list;
	}
	/**
	 * 更新数据
	 * @param c 更新的数据 比如user u = new user();u.name='aa';
	 * @param wherStr 更新的条件   查询的条件  id="1"  把id=1的姓名改成aa
	 */
	public void updateData(Object obj,String wherStr)
	{
		sqLiteDatabase.update(obj,wherStr);
	}
	/**
	 * 删除数据
	 * @param c 删除的表名，比如user.class 就是删除user表中的数据
	 * @param wherStr 删除的条件   查询的条件  name="'张三'"
	 */
	public void deleteData(Class c,String wherStr)
	{
		sqLiteDatabase.delete(c, wherStr);
	}
	/**
	 * 删除表
	 * @param c 删除的表名 比如user.class
	 */
	public void dropTable(Class c)
	{
		if (sqLiteDatabase.hasTable(c)) {
			sqLiteDatabase.dropTable(c);
		}
	}
	/**
	 * 创建表
	 * @param c 创建的表名 比如user.class
	 */
	public void createTable(Class c)
	{
		if (!sqLiteDatabase.hasTable(c)) {
			sqLiteDatabase.creatTable(c);
		}
	}

}
