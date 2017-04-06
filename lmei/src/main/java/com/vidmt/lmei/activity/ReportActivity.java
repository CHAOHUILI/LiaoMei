package com.vidmt.lmei.activity;

import java.util.ArrayList;
import java.util.List;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.menu;
import com.vidmt.lmei.adapter.AdapterReport;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.entity.Report;
import com.vidmt.lmei.util.think.JsonUtil;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReportActivity extends BaseActivity {
	@TAInjectView(id = R.id.headerthemeleft)
	RelativeLayout headerthemeleft;
	@TAInjectView(id = R.id.user)
	ImageView user;
	@TAInjectView(id = R.id.headerright)
	LinearLayout headerright;
	@TAInjectView(id = R.id.typelog)
	ImageView typelog;
	@TAInjectView(id = R.id.headercontent)
	TextView headercontent;
	@TAInjectView(id = R.id.headercontentv)
	View headercontentv;
	@TAInjectView(id = R.id.headercontent1)
	TextView headercontent1;
	@TAInjectView(id = R.id.headercontentv1)
	View headercontentv1;
	@TAInjectView(id = R.id.headconrel1)
	RelativeLayout headconrel1;
	@TAInjectView(id = R.id.Ok)
	TextView okreport;
	@TAInjectView(id = R.id.reportlist)
	ListView reportlist;
	List<Report> reportdata;
	AdapterReport adapterReport;
	Report reportselect;
	int otherid=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		themes();
		InitView();
	}
  @Override
  public void InitView() {
	// TODO Auto-generated method stub
	super.InitView();
	headerthemeleft.setVisibility(View.VISIBLE);
	headerright.setVisibility(View.VISIBLE);
	headconrel1.setVisibility(View.GONE);
	headercontentv.setVisibility(View.GONE);
	headercontent.setText("举报");
	Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
	user.setBackgroundDrawable(drawable);
	reportdata=new ArrayList<Report>();
	Report report=new Report();
	report.setReportcontent("色情");
	report.setIsselected(2);
	Report report1=new Report();
	report1.setReportcontent("谣言");
	report1.setIsselected(2);
	Report report2=new Report();
	report2.setReportcontent("恶意营销");
	report2.setIsselected(2);
	Report report3=new Report();
	report3.setReportcontent("诱导分享");
	report3.setIsselected(2);
	Report report4=new Report();
	report4.setReportcontent("政治敏感");
	report4.setIsselected(2);
	reportdata.add(report);
	reportdata.add(report1);
	reportdata.add(report2);
	reportdata.add(report3);
	reportdata.add(report4);
	otherid=getIntent().getIntExtra("otherid", 0);
	adapterReport=new AdapterReport(context, reportdata, imageLoader, options);
	adapterReport.setSeclection(-1);
	reportlist.setAdapter(adapterReport);
	reportlist.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			reportselect=reportdata.get(position);
			adapterReport.setSeclection(position);
			adapterReport.notifyDataSetChanged();
		}
	});
   }
  @Override
	protected void onAfterSetContentView() {
		// TODO Auto-generated method stub
		super.onAfterSetContentView();
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
			
				case R.id.headerthemeleft:
					finish();					
					break;
				case R.id.Ok:
					if(reportselect==null){
						ToastShow("请选择举报内容");
					}else{
						Report();
					}
					break;
				default:
					break;
				}
			}
		};
		headerthemeleft.setOnClickListener(onClickListener);
		okreport.setOnClickListener(onClickListener);
	}

   public void Report(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String json = Person_Service.complaint(b_person.getId(), otherid, reportselect.getReportcontent());
				Message msg = mUIHandler.obtainMessage(1);
				msg.obj = JsonUtil.JsonToObj(json, String.class);
				msg.sendToTarget();
			}
		}).start();
	}
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (msg.obj != null) {
					String mes = (String) msg.obj;
				    if("success".equals(mes)){
						ToastShow("举报成功");
					}else {
						ToastShow("操作失败，请重试");
					}
				}
				break;
			}
		}
	};
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
