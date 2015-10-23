package com.fisher.dictionary.Utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by Fisher on 10/22/2015.
 */
public class ContextHelper {


	public static boolean fnCheckService(Context context, Class ServiceClass){
		ActivityManager manager = ( ActivityManager ) context.getSystemService( Context.ACTIVITY_SERVICE );
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (ServiceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

}
