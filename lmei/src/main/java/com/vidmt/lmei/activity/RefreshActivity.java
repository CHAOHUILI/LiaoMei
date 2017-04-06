package com.vidmt.lmei.activity;

import java.util.ArrayList;
import java.util.List;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.adapter.AdapterString;
import com.vidmt.lmei.widget.PullToRefreshView;
import com.vidmt.lmei.widget.PullToRefreshView.OnFooterRefreshListener;
import com.vidmt.lmei.widget.PullToRefreshView.OnHeaderRefreshListener;

import android.os.Bundle;
import android.widget.ListView;

public class RefreshActivity extends BaseActivity implements OnHeaderRefreshListener,OnFooterRefreshListener {
	@TAInjectView(id = R.id.pull)
	PullToRefreshView pull;
	@TAInjectView(id = R.id.pull_list)
	ListView pull_list;
	private AdapterString adapter;
	private List<String> datelist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refresh);
		pull.setOnHeaderRefreshListener(this);
		pull.setOnFooterRefreshListener(this);
		datelist = new ArrayList<String>();
		datelist = date();
		adapter = new AdapterString(RefreshActivity.this, datelist);
		pull_list.setAdapter(adapter);
	}
	/**
	 * 数据
	 * @Title date
	 * @Description TODO(这里用一句话描述这个方法的作用)
	 * @param @return    参数
	 * @return List<String>    返回类型
	 */
	public List<String> date()
	{
		List<String> list = new ArrayList<String>();
		list.add("李炜民");
		list.add("李炜民");
		list.add("李炜民");
		list.add("李炜民");
		list.add("李炜民");
		list.add("李炜民");
		list.add("李炜民");
		list.add("李炜民");
		return list;
	}


	/**
	 * 上啦刷新
	 */
	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		datelist.addAll(date());
		adapter.notifyDataSetChanged();
		pull.onFooterRefreshComplete();
	}
	/**
	 * 下拉刷新
	 */
	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		pull.onHeaderRefreshComplete();
	}

}
