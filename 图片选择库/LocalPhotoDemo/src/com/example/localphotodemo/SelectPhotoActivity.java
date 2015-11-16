package com.example.localphotodemo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localphotodemo.PhotoFolderFragment.OnPageLodingClickListener;
import com.example.localphotodemo.PhotoFragment.OnPhotoSelectClickListener;
import com.example.localphotodemo.bean.PhotoInfo;
import com.example.localphotodemo.bean.PhotoSerializable;
import com.example.localphotodemo.util.CheckImageLoaderConfiguration;

/**    
 * @title SelectPhotoActivity.java
 * @package com.centaline.mhc.activity.friendGroupActivity
 * @author guilin   
 * @date 2013-8-6 上午10:42:15  
 */
public class SelectPhotoActivity extends FragmentActivity implements OnPageLodingClickListener
				,OnPhotoSelectClickListener{

	private PhotoFolderFragment photoFolderFragment;
	
	private Button btnback,btnright;
	
	private TextView title;
	
	private List<PhotoInfo> hasList;
	
	private FragmentManager manager;
	private int backInt = 0;
	/**
	 * 已选择图片数量
	 */
	private int count;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectphoto);
		
		getWindowManager().getDefaultDisplay().getMetrics(MyApplication.getDisplayMetrics());   
		
		count = getIntent().getIntExtra("count", 0);
		
		manager = getSupportFragmentManager();
		
		hasList = new ArrayList<PhotoInfo>();
		
		btnback = (Button)findViewById(R.id.btnback);
		btnright = (Button)findViewById(R.id.btnright);
		title = (TextView)findViewById(R.id.title);
		btnback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(backInt==0){
					finish();
				}else if(backInt==1){
					backInt--;
					hasList.clear();
					title.setText("请选择相册");
					FragmentTransaction transaction = manager.beginTransaction();
					transaction.show(photoFolderFragment).commit();
					manager.popBackStack(0, 0);
				}
			}
		});
		btnright.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(hasList.size()>0){
					for(PhotoInfo mPhotoInfo:hasList){
						Log.e("haitian", "mPhotoInfo.toString="+mPhotoInfo.toString());
					}
				}else{
					Toast.makeText(SelectPhotoActivity.this, "至少选择一张图片", Toast.LENGTH_SHORT).show();
				}
			}
		});
		title.setText("请选择相册");
		photoFolderFragment = new PhotoFolderFragment();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.body,photoFolderFragment);  
		transaction.addToBackStack(null);
		// Commit the transaction  
		transaction.commit(); 
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		CheckImageLoaderConfiguration.checkImageLoaderConfiguration(this);
	}

	@Override
	public void onPageLodingClickListener(List<PhotoInfo> list) {
		// TODO Auto-generated method stub
		title.setText("已选择0张");
		FragmentTransaction transaction = manager.beginTransaction();
		PhotoFragment photoFragment = new PhotoFragment();
		Bundle args = new Bundle();
		PhotoSerializable photoSerializable = new PhotoSerializable();
		for (PhotoInfo photoInfoBean : list) {
			photoInfoBean.setChoose(false);
		}
		photoSerializable.setList(list);
		args.putInt("count", count);
		args.putSerializable("list", photoSerializable);
		photoFragment.setArguments(args);
		transaction = manager.beginTransaction();
		transaction.hide(photoFolderFragment).commit();
		transaction = manager.beginTransaction();
		transaction.add(R.id.body,photoFragment);  
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.addToBackStack(null);
		// Commit the transaction  
		transaction.commit(); 
		backInt++;
	}

	@Override
	public void onPhotoSelectClickListener(List<PhotoInfo> list) {
		// TODO Auto-generated method stub
		hasList.clear();
		for (PhotoInfo photoInfoBean : list) {
			if(photoInfoBean.isChoose()){
				hasList.add(photoInfoBean);
			}
		}
		title.setText("已选择"+hasList.size()+"张");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK&&backInt==0){
			finish();
		}else if(keyCode == KeyEvent.KEYCODE_BACK&&backInt==1){
			backInt--;
			hasList.clear();
			title.setText("请选择相册");
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.show(photoFolderFragment).commit();
			manager.popBackStack(0, 0);
		}
		return false;
	}
}
