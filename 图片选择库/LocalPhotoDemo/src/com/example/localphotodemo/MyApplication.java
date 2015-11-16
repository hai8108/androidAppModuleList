package com.example.localphotodemo;


import android.app.Application;
import android.util.DisplayMetrics;

/**    
 * @author GuiLin
 */
public class MyApplication extends Application {

	private static DisplayMetrics dm = new DisplayMetrics();   
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public static DisplayMetrics getDisplayMetrics(){
		return dm;
	}
}
