package com.huizhi.manage.request.teacher;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.huizhi.manage.base.ThreadPoolDo;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.http.HttpConnect;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.HomeOperateNode;
import com.huizhi.manage.node.ResultNode;
import com.huizhi.manage.node.TeacherTrainingNode;
import com.huizhi.manage.request.topic.TopicRequest;
import com.huizhi.manage.util.JSONUtil;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class TeacherRequest {

	public void getTeacherData(String teacherId, Handler handler){
		List<BasicNameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("Method", URLData.METHORD_TEACHER_TRAINING));
		params.add(new BasicNameValuePair("TeacherId", teacherId));
		ThreadPoolDo.getInstance().executeThread(new TeacherInfoThread(params, handler));
	}

	private class TeacherInfoThread extends Thread {
		private List<BasicNameValuePair> params;
		private Handler handler;

		public TeacherInfoThread(List<BasicNameValuePair> params, Handler handler) {
			this.params = params;
			this.handler = handler;
		}

		@Override
		public void run() {
			super.run();
			try {
				String result = HttpConnect.getHttpConnect(URLData.getUrlTeacherTrainingService(), params);
				Log.i("HuiZhi", "The result:" + result);
				if(TextUtils.isEmpty(result))
					return;
				ResultNode resultNode = JSONUtil.parseResult(result);
				Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
				if(resultNode == null)
					return;
				if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
					String data = resultNode.getReturnObj();
					if (TextUtils.isEmpty(data))
						return;
					Gson gson = new Gson();//TeacherTrainingNode
					TeacherTrainingNode node = gson.fromJson(data, TeacherTrainingNode.class);
					if(handler!=null)
						handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, node));
				}
			}catch (Exception e){
				e.printStackTrace();
			}

		}
	}

	public void getProgressData(String teacherId, Handler handler){
		List<BasicNameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("Method", URLData.METHORD_TEACHER_PROGRESS));
		params.add(new BasicNameValuePair("TeacherId", teacherId));
		ThreadPoolDo.getInstance().executeThread(new TeacherpProgressThread(params, handler));
	}

	private class TeacherpProgressThread extends Thread {
		private List<BasicNameValuePair> params;
		private Handler handler;

		public TeacherpProgressThread(List<BasicNameValuePair> params, Handler handler) {
			this.params = params;
			this.handler = handler;
		}

		@Override
		public void run() {
			super.run();
			try {
				String result = HttpConnect.getHttpConnect(URLData.getUrlTeacherTrainingService(), params);
				Log.i("HuiZhi", "The result:" + result);
				if(TextUtils.isEmpty(result))
					return;
				ResultNode resultNode = JSONUtil.parseResult(result);
				Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
				if(resultNode == null)
					return;
				if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
					String data = resultNode.getReturnObj();
					if (TextUtils.isEmpty(data))
						return;
					Gson gson = new Gson();//TeacherTrainingNode
					TeacherTrainingNode.ObjProgress node = gson.fromJson(data, TeacherTrainingNode.ObjProgress.class);
					if(handler!=null)
						handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_FIVE, node));
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
