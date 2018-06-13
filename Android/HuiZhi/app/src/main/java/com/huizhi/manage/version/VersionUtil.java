package com.huizhi.manage.version;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.main.MainApplication;
import com.huizhi.manage.node.VersionNode;

public class VersionUtil {
	private VersionUpdateDialog versionUpdateDialog;
	private VersionNode versionnode;
	
	/***
	 * 检查是否更新版本
	 */
	public boolean checkVersion(Activity activity, VersionNode versionnode){
		this.versionnode = versionnode;
		if(((MainApplication)activity.getApplication()).localVersion != versionnode.getVersionCode()){
            Constants.DOWNLOAD_URL = versionnode.getDownloadUrl();
            versionUpdateDialog = new VersionUpdateDialog(activity, versionnode);
            versionUpdateDialog.showView();
            UserInfo.getInstance().setAppDownloadUpdate(infoUpdate);
			return true;
		}
		return false;
	}

	private BaseInfoUpdate infoUpdate = new BaseInfoUpdate() {
		
		@Override
		public void update(Object object) {
			// TODO Auto-generated method stub
			if(object==null)
				return;
			Integer content = (Integer)object;
			String msg = "版本更新中，已下载" + content + "%";
			Log.i("Menus", msg);
			handler.sendMessage(handler.obtainMessage(101, content));
			
		}
	};

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==101){
				Integer mesg = (Integer)msg.obj;
				versionUpdateDialog.setContent(mesg);
			}
			
		}
		
	};
}
