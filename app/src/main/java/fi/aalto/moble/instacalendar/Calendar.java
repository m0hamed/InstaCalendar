package fi.aalto.moble.instacalendar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mohamed on 11/14/15.
 */
public class Calendar implements Parcelable{
    public final String name;
    public final String _id;

    public Calendar(String name, String id) {
        this.name = name;
        this._id = id;
    }

    public Calendar(Parcel in) {
        this.name = in.readString();
        this._id = in.readString();
    }

    public String toString() {
        return "Calendar<id="+_id+", name=" + name+">";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(_id);
    }

    public static final Parcelable.Creator<Calendar> CREATOR
            = new Parcelable.Creator<Calendar>() {
        public Calendar createFromParcel(Parcel in) {
            return new Calendar(in);
        }

        public Calendar[] newArray(int size) {
            return new Calendar[size];
        }
    };

}
