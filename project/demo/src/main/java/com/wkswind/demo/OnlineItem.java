package com.wkswind.demo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016-11-17.
 */

class OnlineItem implements Parcelable {
    String url;
    String background;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.background);
    }

    OnlineItem() {
    }

    protected OnlineItem(Parcel in) {
        this.url = in.readString();
        this.background = in.readString();
    }

    public static final Parcelable.Creator<OnlineItem> CREATOR = new Parcelable.Creator<OnlineItem>() {
        @Override
        public OnlineItem createFromParcel(Parcel source) {
            return new OnlineItem(source);
        }

        @Override
        public OnlineItem[] newArray(int size) {
            return new OnlineItem[size];
        }
    };

    static OnlineItem fakeItem(){
        OnlineItem item = new OnlineItem();
        item.url = BuildConfig.DOWNLOAD_URL;
//        item.url = "https://l.pic.wemepi.com/data.x/spread/app/cqxx/00001037.apk?t=1479354618";
        item.background = "http://www.wemepi.com/spread.app/app/cqxx_20160921dtqp/img/cqxx20161010/01.jpg";
        return item;
    }
}
