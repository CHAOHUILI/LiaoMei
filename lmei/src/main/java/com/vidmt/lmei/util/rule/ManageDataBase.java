package com.vidmt.lmei.util.rule;
import java.util.ArrayList;
import java.util.List;

import com.ta.util.TALogger;
import com.vidmt.lmei.util.think.DbUtil;

import android.util.Log;


public class ManageDataBase  {
	/**
	 * 插入多条数据
	* @Title InsertGoods
	* @Description TODO(这里用一句话描述这个方法的作用)
	* @param @param db
	* @param @param list    参数
	* @return void    返回类型
	 */
	public static <T> void AddList(DbUtil db,List<T> list,Class<T> cla)
	{
		db.createTable(cla);
		try {
			for (T t : list) {
				db.insertData(t, cla);
			}
			TALogger.e(cla+"", "插入db成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			TALogger.e(cla+"", "插入db失败"+e.getLocalizedMessage());
		}
		
		
	}
	/**
	 * 删除多条数据
	* @Title DeleteAll
	* @Description TODO(这里用一句话描述这个方法的作用)
	* @param @param db
	* @param @param list
	* @param @param cla    参数
	* @return void    返回类型
	 */
	public static <T> void  DeleteAll(DbUtil db,List<T> list,Class cla,String whr)
	{
		db.createTable(cla);
		try {

			for (int i = 0; i < list.size(); i++) {
				if(whr!=null)
					db.deleteData(cla, whr);
				else
					db.deleteData(cla, null);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 查询广告
	 * @param <T>
	* @Title AdverAll
	* @Description TODO(这里用一句话描述这个方法的作用)
	* @param @param db
	* @param @param adverts
	* @param @return    参数
	* @return List<Advert>    返回类型
	 */
	public static <T> ArrayList SelectList(DbUtil db,Class<T> cstr ,String whr)
	{
		db.createTable(cstr);
		Object obj = null;
		ArrayList list = new ArrayList();
		try {
			if(whr!=null)
				list = (ArrayList)db.selectData(cstr,whr);
			else
				list = (ArrayList)db.selectData(cstr,null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return  list;
	}
	
	/**
	 * 插入
	* @Title Insert
	* @Description TODO(这里用一句话描述这个方法的作用)
	* @param @param db
	* @param @param clas    参数
	* @return void    返回类型
	 */
	public static <T> void Insert(DbUtil db,Class entity,Object obj)
	{
		db.createTable(entity);
		try {
			db.insertData(obj, entity);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			TALogger.e(entity+"", "插入失败");
		}
		
	}
	/**
	 * 删除
	* @Title Delete
	* @Description TODO(这里用一句话描述这个方法的作用)
	* @param @param db
	* @param @param enetiy
	* @param @param str    参数
	* @return void    返回类型
	 */
	public static void Delete(DbUtil db,Class enetiy,String str)
	{
		try {
			db.deleteData(enetiy, str);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			TALogger.e(enetiy+"", "删除失败");
		}
		
	}
}
