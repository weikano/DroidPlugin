package com.wkswind.demo;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.morgoo.droidplugin.pm.PluginManager;
import com.morgoo.helper.compat.PackageManagerCompat;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InstallActivity extends AppCompatActivity {
    private OnlineItem item;

    private TextView label;
    private ImageView background;
    private ProgressBar progressBar;
    private Subscriber<Integer> subscriber;
    private PackageManager pm;
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_install);
        pm = getPackageManager();
        background = (ImageView) findViewById(R.id.background);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        label = (TextView) findViewById(R.id.info);
        subscriber = new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
//                createShortCut(path);
            }

            @Override
            public void onNext(Integer integer) {
                switch (integer) {
                    case PackageManagerCompat.INSTALL_SUCCEEDED:
                        label.setText(R.string.install_success);
                        Utils.createShortCut(InstallActivity.this, path);
                        launch(path);
                        finish();
                        break;
                    case PackageManagerCompat.INSTALL_FAILED_INTERNAL_ERROR:
                        label.setText(R.string.install_failed);
                        break;
                    case -100001:
                        label.setText(R.string.install_failed_permission);
                        break;
                    default:
                        return;
                }
//                if(integer == PackageManagerCompat.INSTALL_SUCCEEDED) {
//                    launch(path);
//                    Utils.createShortCut(InstallActivity.this, path);
//                }
            }
        };
        Bundle args = getIntent().getExtras();
        if(args != null && args.containsKey(OnlineItem.class.getName())) {
            item = args.getParcelable(OnlineItem.class.getName());
            if(item != null) {
                path = Utils.getDownloadPath(this, item.url);
                downloadOnlineItem();
            }else {
                finish();
            }
        }else{
            finish();
        }
    }


    private void downloadOnlineItem() {
        int status = FileDownloader.getImpl().getStatus(item.url, path);
        if(status == FileDownloadStatus.completed) {
            installPlugin(path);
        }else {
            label.setText(R.string.download);
            FileDownloader.getImpl().create(item.url).setPath(path).setListener(new FileDownloadListenerAdapter(this){

                @Override
                protected void completed(BaseDownloadTask task) {
                    super.completed(task);
                    installPlugin(task.getTargetFilePath());
                }

                @Override
                protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    super.progress(task, soFarBytes, totalBytes);
                    progressBar.setProgress((int) (1f * soFarBytes / totalBytes * 100));
                }
            }).start();
        }
    }

    private void installPlugin(String path) {
        final PackageInfo info = pm.getPackageArchiveInfo(path, 0);
        if(PluginManager.getInstance().isConnected()) {
            try {
                if(PluginManager.getInstance().isPluginPackage(info.packageName)) {
                    launch(path);
                    finish();
                }else{
                    label.setText(R.string.install);
                    Utils.doInstallRx(path).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void launch(String path) {
        Utils.launch(this, path);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileDownloader.getImpl().pauseAll();
        subscriber.unsubscribe();
    }
}
