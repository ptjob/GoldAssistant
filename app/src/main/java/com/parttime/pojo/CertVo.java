package com.parttime.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cjz on 2015/8/2.
 */
public class CertVo implements Parcelable{

    public int accountType;
    public int certStatus;
    public String name;
    public String idNum;
    public String idFront;
    public String idBack;

    public String regId;
    public String regIdPic;

    public CertVo() {
    }

    public CertVo(Parcel in){
        accountType = in.readInt();
        certStatus = in.readInt();
        name = in.readString();
        idNum = in.readString();
        idFront = in.readString();
        idBack = in.readString();

        regId = in.readString();
        regIdPic = in.readString();
    }

    public CertVo(int accountType, int certStatus, String name, String idNum, String idFront, String idBack, String regId, String regIdPic) {
        this.accountType = accountType;
        this.certStatus = certStatus;
        this.name = name;
        this.idNum = idNum;
        this.idFront = idFront;
        this.idBack = idBack;
        this.regId = regId;
        this.regIdPic = regIdPic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(accountType);
        dest.writeInt(certStatus);
        dest.writeString(name);
        dest.writeString(idNum);
        dest.writeString(idFront);
        dest.writeString(idBack);

        dest.writeString(regId);
        dest.writeString(regIdPic);
    }

    public static final Creator<CertVo> CREATOR = new Creator<CertVo>() {
        @Override
        public CertVo createFromParcel(Parcel source) {
            return new CertVo(source);
        }

        @Override
        public CertVo[] newArray(int size) {
            return new CertVo[size];
        }
    };
}
