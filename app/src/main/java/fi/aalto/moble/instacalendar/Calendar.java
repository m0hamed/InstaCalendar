package fi.aalto.moble.instacalendar;

/**
 * Created by mohamed on 11/14/15.
 */
public class Calendar {
    public final String name;
    public final String _id;

    public Calendar(String name, String id) {
        this.name = name;
        this._id = id;
    }

    public String toString() {
        return "Calendar<id="+_id+", name=" + name+">";
    }
}
