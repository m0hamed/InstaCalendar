package fi.aalto.moble.instacalendar;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by sshehata on 11/18/15.
 */
public class Event implements Parcelable {
    public final String name;
    public final String place;
    public final String _id;
    public final String google_id;


    public Event(String name, String place, String google_id, String id) {
        this.name = name;
        this.place = place;
        this.google_id = google_id;
        this._id = id;
    }

    public Event(Parcel in) {
        name = in.readString();
        place = in.readString();
        this._id = in.readString();
        this.google_id = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(place);
        out.writeString(_id);
        out.writeString(google_id);
    }

    public static final Parcelable.Creator<Event> CREATOR
            = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String toString() {
        return name + " - " + (place == null? "-" : place);
    }



}
