package com.wkswind.demo;

import android.content.Context;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;

public class FileDownloadListenerAdapter extends FileDownloadListener {
	private Context context;
	public FileDownloadListenerAdapter(Context context){
		this.context = context.getApplicationContext();
	}

	@Override
	protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
		
	}

	@Override
	protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
		
	}

	@Override
	protected void completed(BaseDownloadTask task) {
//		ActionService.statistic(context, Constants.CONTINUE_FINISH, task);
	}

	@Override
	protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
		
	}

	@Override
	protected void error(BaseDownloadTask task, Throwable e) {
		
	}

	@Override
	protected void warn(BaseDownloadTask task) {
		
	}
	
	@Override
	protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
		super.connected(task, etag, isContinue, soFarBytes, totalBytes);
//		if(soFarBytes == 0){
//			ActionService.statistic(context, Constants.BEGIN_PAUSE, task);
//		}
	}

}
