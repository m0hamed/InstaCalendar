package fi.aalto.moble.instacalendar;

import java.util.Date;

/**
 * Created by mohamed on 11/15/15.
 */
public class Event {

    public final String _id;
    public final String calendar_id;
    public final String place;
    public final Date starts_at;
    public final Date ends_at;

    public Event(String _id, String calendar_id, String place, Date starts_at, Date ends_at) {
        this._id = _id;
        this.calendar_id = calendar_id;
        this.place = place;
        this.starts_at = starts_at;
        this.ends_at = ends_at;
    }
}
