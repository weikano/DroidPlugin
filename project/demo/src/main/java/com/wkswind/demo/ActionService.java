package com.wkswind.demo;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.content.Context;
import android.text.TextUtils;

import com.morgoo.helper.Log;
import com.weme.group.utils.DidHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ActionService extends IntentService {
    private static final String TAG = ActionService.class.getSimpleName();
    public static final String ACTION_DOWNLOAD_FINISH = "N";
    public static final String ACTION_GAME_ACTIVATE = "M";

    private static final String GAME_ID = "22";
    private HttpUrl url;
    private OkHttpClient client;
    public ActionService() {
        super("ActionService");
        url = new HttpUrl.Builder().scheme("http").host("sd.wemepi.com").addPathSegment(BuildConfig.VERSION_NAME).addPathSegment("dispatch.php").build();
        client = new OkHttpClient.Builder().build();
    }
    private static final String KEY_ACTION_ID = ActionService.class.getName() +".KEY_ACTION_ID";
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String actionId = intent.getStringExtra(KEY_ACTION_ID);
            if(!TextUtils.isEmpty(actionId)){
                Map<String, Object> params = new Params(this).addParams("v_cmd", "103").addParams("v_class", "100").addParams("main_game_id", GAME_ID).addParams("game_id", GAME_ID).addParams("action_id", actionId).build();
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    bodyBuilder.add(entry.getKey(), String.valueOf(entry.getValue()));
                }

//                FormBody body = new FormBody.Builder().add("v_cmd","103").add("v_class","100").add("main_game_id",GAME_ID).add("channel_id",BuildConfig.CHANNEL_NAME)
//                        .add("game_id",GAME_ID).add("action_id",actionId).add("did", DidHelper.getInstance(this).getDid())
//                        .add("equipment_id", CommHelper.getEquipmentId()).add("os_version", CommHelper.getOsVersion())
//                        .add("app_version", BuildConfig.VERSION_NAME).add("type","2").build();
                Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
                try {
                    Response response = client.newCall(request).execute();
                    Log.i(TAG, String.valueOf(params)+"," + String.valueOf(response.body().string()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void action(Context context, String actionId){
        Intent intent = new Intent(context, ActionService.class);
        intent.setPackage(context.getPackageName());
        intent.putExtra(KEY_ACTION_ID, actionId);
        context.startService(intent);
    }

    private static class Params {
        private Map<String,Object> params;
        Params(Context context){
            params = new HashMap<>();
            params.put("channel_id",BuildConfig.CHANNEL_NAME);
            params.put("did", DidHelper.getInstance(context).getDid());
            params.put("equipment_id", CommHelper.getEquipmentId());
            params.put("os_version", CommHelper.getOsVersion());
            params.put("app_version", BuildConfig.VERSION_NAME);
            params.put("type",2);
        }
        Params addParams(String key, Object value){
            if(!TextUtils.isEmpty(key) && value != null){
                params.put(key,value);
            }
            return this;
        }

        Map<String,Object> build(){
            return params;
        }
    }
}
