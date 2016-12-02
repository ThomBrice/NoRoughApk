package com.example.isen.noroughapk.Class;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by isen on 01/12/2016.
 */

public class Player implements Parcelable {

    String firstname;
    String surname;
    Float handicap;
    String golfDeReference;
    Integer positionGolf;

    public Player() {
        this.firstname="";
        this.surname="";
        this.handicap= (float) -1.0;
        this.golfDeReference="";
        this.positionGolf = -1;
    }

    public Player(Parcel in) {
        firstname = in.readString();
        surname = in.readString();
        golfDeReference = in.readString();
        handicap = in.readFloat();
        positionGolf = in.readInt();
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Float getHandicap() {
        return handicap;
    }

    public void setHandicap(Float handicap) {
        handicap = handicap;
    }

    public String getGolfDeReference() {
        return golfDeReference;
    }

    public void setGolfDeReference(String golfDeReference) {
        this.golfDeReference = golfDeReference;
    }

    public Integer getPositionGolf() {
        return positionGolf;
    }

    public void setPositionGolf(Integer positionGolf) {
        this.positionGolf = positionGolf;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(firstname);
        parcel.writeString(surname);
        parcel.writeString(golfDeReference);
        parcel.writeFloat(handicap);
        parcel.writeInt(positionGolf);
    }
}
