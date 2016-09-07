package com.plugin.utils.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by  ZXL on 2016/7/11.
 */
public class LoginUserBean implements Parcelable {
    private int id;
    private String mobile_no;
    private String nickname;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.mobile_no);
        dest.writeString(this.nickname);
    }

    protected LoginUserBean(Parcel in) {
        this.id = in.readInt();
        this.mobile_no = in.readString();
        this.nickname = in.readString();
    }

    public static final Creator<LoginUserBean> CREATOR = new Creator<LoginUserBean>() {
        @Override
        public LoginUserBean createFromParcel(Parcel source) {
            return new LoginUserBean(source);
        }

        @Override
        public LoginUserBean[] newArray(int size) {
            return new LoginUserBean[size];
        }
    };


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "LoginUserBean{" +
                "id=" + id +
                ", mobile_no='" + mobile_no + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
