package com.example.localphotodemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.example.localphotodemo.adapter.PhotoAdapter;
import com.example.localphotodemo.bean.PhotoInfo;
import com.example.localphotodemo.bean.PhotoSerializable;
import com.example.localphotodemo.util.UniversalImageLoadTool;

/**    
 */
public class PhotoFragment extends Fragment {

	public interface OnPhotoSelectClickListener {  
		public void onPhotoSelectClickListener(List<PhotoInfo> list);  

	}
	
	private OnPhotoSelectClickListener onPhotoSelectClickListener;
	
	private GridView gridView;
	private PhotoAdapter photoAdapter;
	
	private List<PhotoInfo> list;
	
	private int hasSelect = 1;
	
	private int count;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		if(onPhotoSelectClickListener==null){
			onPhotoSelectClickListener = (OnPhotoSelectClickListener)activity;
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_photoselect, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		gridView = (GridView)getView().findViewById(R.id.gridview);
		
		Bundle args = getArguments();
		
		PhotoSerializable photoSerializable = (PhotoSerializable) args.getSerializable("list");
		list = new ArrayList<PhotoInfo>(); 
		list.addAll(photoSerializable.getList());
		hasSelect +=count;
		
		photoAdapter = new PhotoAdapter(getActivity(), list,gridView);
		gridView.setAdapter(photoAdapter);
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(list.get(position).isChoose()&&hasSelect>1){
					list.get(position).setChoose(false);
					hasSelect--;
				}else if(hasSelect<10){
					list.get(position).setChoose(true);
					hasSelect++;
				}else{
					Toast.makeText(getActivity(), "最多选择9张图片！", Toast.LENGTH_SHORT).show();
				}
				photoAdapter.refreshView(position);
				onPhotoSelectClickListener.onPhotoSelectClickListener(list);
			}
		});
		
		gridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState==0){
					UniversalImageLoadTool.resume();
				}else{
					UniversalImageLoadTool.pause();
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
	}
}
