package com.huizhi.manage.util;

import android.util.Log;

public class TLog {
	public static final String LOG_TAG = "HuiZhi";
	public static boolean DEBUG = true;

	public TLog() {
	}

	public static final void analytics(String log) {
		if (DEBUG)
			Log.d(LOG_TAG, log);
	}

	public static final void error(String log) {
		if (DEBUG)
			Log.e(LOG_TAG, "" + log);
	}

	public static final void log(String log) {
		if (DEBUG)
			Log.i(LOG_TAG, log);
	}

	public static final void log(String tag, String log) {
		if (DEBUG)
			Log.i(tag, log);
	}

	public static final void logv(String log) {
		if (DEBUG)
			Log.v(LOG_TAG, log);
	}

	public static final void warn(String log) {
		if (DEBUG)
			Log.w(LOG_TAG, log);
	}

	public static final void logLong(String log){
		if (log.length() > 4000) {
			for (int i = 0; i < log.length(); i += 4000) {
				//当前截取的长度<总长度则继续截取最大的长度来打印
				if (i + 4000 < log.length()) {
					log(log.substring(i, i + 4000));
				} else {
					//当前截取的长度已经超过了总长度，则打印出剩下的全部信息
					log(log.substring(i, log.length()));
				}
			}
		} else {
			//直接打印
			log(log);
		}
	}

}
