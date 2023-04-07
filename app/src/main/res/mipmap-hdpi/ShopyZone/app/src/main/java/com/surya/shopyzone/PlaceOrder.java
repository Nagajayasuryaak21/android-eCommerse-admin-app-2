package com.surya.shopyzone;

import android.os.Parcel;
import android.os.Parcelable;

public class PlaceOrder implements Parcelable {

    private String courseName;
    public PlaceOrder(){

    }
    public PlaceOrder(String courseName){
        this.courseName=courseName;
    }
    protected PlaceOrder(Parcel in) {
        courseName = in.readString();
    }

    public static final Creator<PlaceOrder> CREATOR = new Creator<PlaceOrder>() {
        @Override
        public PlaceOrder createFromParcel(Parcel in) {
            return new PlaceOrder(in);
        }

        @Override
        public PlaceOrder[] newArray(int size) {
            return new PlaceOrder[size];
        }
    };

    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseName);
    }
}
