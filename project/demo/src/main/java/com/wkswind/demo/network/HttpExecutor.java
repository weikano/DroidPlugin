package com.wkswind.demo.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.wkswind.demo.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-11-19.
 */

public final class HttpExecutor {
    private static final String URL = "http://www.baidu.com/" + BuildConfig.VERSION_NAME;

    private static final int IO_TIMEOUT_SENCONDS = 5;
    private static final int CONNECT_TIMEOUT_SENCONDS = 5;

    public static Observable<HttpResponse> postHttpResponse(final Context context, final int cmd, final HashMap<String,Object> params) {
        return postString(context,cmd, params).observeOn(Schedulers.io()).map(new Func1<String, HttpResponse>() {
            @Override
            public HttpResponse call(String s) {
                try {
                    JSONObject json = new JSONObject(s);
                    return new HttpResponse(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    private static Observable<String> postString(final Context context, final int cmd, final HashMap<String, Object> params) {
        params.put("v_cmd", cmd);
        params.put("v_class", cmd / 100 * 100);
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    OkHttpClient client = defaultHttpClient();
                    Request request = makeRequestMethodPost(params);
                    Response response = client.newCall(request).execute();
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(response.body().string());
                        subscriber.onCompleted();
                    }
                } catch (IOException e) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(e);
                    }
                }

            }
        });
    }

    private static Request makeRequestMethodPost(HashMap<String, Object> params) {
        return new Request.Builder().method("POST", makeRequestBody(params)).url(URL).cacheControl(new CacheControl.Builder().noCache().build()).build();
    }

    private static OkHttpClient defaultHttpClient() {
        return new OkHttpClient.Builder().connectTimeout(CONNECT_TIMEOUT_SENCONDS, TimeUnit.SECONDS)
                .writeTimeout(IO_TIMEOUT_SENCONDS, TimeUnit.SECONDS).readTimeout(IO_TIMEOUT_SENCONDS, TimeUnit.SECONDS)
                .build();
    }

    private static RequestBody makeRequestBody(HashMap<String, Object> params) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() != null) {
                builder.addEncoded(entry.getKey(), entry.getValue().toString());
            }
        }
        return builder.build();
    }
}
