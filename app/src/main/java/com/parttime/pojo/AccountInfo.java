package com.parttime.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cjz on 2015/7/30.
 */
public class AccountInfo implements Parcelable{
    public long id;
    public int type;
    public String hire_type;
    public int rank;
    public String name;
    public int status;
    public String avatar;
    public int point;
    public String introduction;

    public AccountInfo(Parcel in){
        id = in.readLong();
        type = in.readInt();
        hire_type = in.readString();
        rank = in.readInt();
        name = in.readString();
        status = in.readInt();
        avatar = in.readString();
        point = in.readInt();
        introduction = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(type);
        dest.writeString(hire_type);
        dest.writeInt(rank);
        dest.writeString(name);
        dest.writeInt(status);
        dest.writeString(avatar);
        dest.writeInt(point);
        dest.writeString(introduction);
    }

    public static final Creator<AccountInfo> CREATOR = new Creator<AccountInfo>() {
        @Override
        public AccountInfo createFromParcel(Parcel source) {
            return new AccountInfo(source);
        }

        @Override
        public AccountInfo[] newArray(int size) {
            return new AccountInfo[size];
        }
    };

}
