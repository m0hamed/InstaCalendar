package fi.aalto.moble.instacalendar;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.IOError;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sshehata on 11/18/15.
 */
public class Event implements Parcelable {
    public final String name;
    public final String place;
    public final String _id;
    public final String google_id;
    public final String starts_at;
    public final String ends_at;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


    public Event(String name, String place, String google_id, String id, String starts_at, String ends_at) {
        this.name = name;
        this.place = place;
        this.google_id = google_id;
        this._id = id;
        this.starts_at = starts_at;
        this.ends_at = ends_at;
    }

    public Event(Parcel in) {
        name = in.readString();
        place = in.readString();
        this._id = in.readString();
        this.google_id = in.readString();
        this.starts_at = in.readString();
        this.ends_at = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(place);
        out.writeString(_id);
        out.writeString(google_id);
        out.writeString(starts_at);
        out.writeString(ends_at);
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

    public static Date ISOToDate(String iso) {
        try {
            String s = iso.replaceFirst("T", " ");
            return dateFormat.parse(s);
        }catch(Exception e) {
            return null;
        }
    }

    public static String dateToISO(Date date) {
         return dateFormat.format(date).replaceFirst(" ", "T");
    }

    public static String stringToISO(String date) {
        try {
            Date d = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date);
            return dateToISO(d);
        } catch(Exception e) {
            Log.w("ERROR", e);
            return null;
        }
    }

    public static long stringToMilliseconds(String date) {
        try {
            return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date).getTime();
        } catch(Exception e) {
            Log.w("ERROR", e);
            return 0;
        }
    }

    public static String ISOToString(String date) {
        try {
            Date d = ISOToDate(date);
            return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(d);
        } catch(Exception e) {
            Log.w("ERROR", e);
            return null;
        }
    }


}
