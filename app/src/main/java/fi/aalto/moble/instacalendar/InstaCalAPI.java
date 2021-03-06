package fi.aalto.moble.instacalendar;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.DELETE;


/**
 * Created by mohamed on 11/14/15.
 */
public interface InstaCalAPI {
    @POST("users/login")
    Call<LoginToken> login(@Body User user);

    @POST("users")
    Call<Void> register(@Body User user);

    @GET("calendars")
    Call<List<Calendar>> calendars(@Query("auth_token") String token);

    @GET("calendars/{cal_id}/events")
    Call<List<Event>> events(@Path("cal_id") String userId, @Query("auth_token") String token);

    @POST("calendars/{cal_id}/events")
    Call<Void> createEvent(@Path("cal_id") String userId, @Query("auth_token") String token, @Body CreateEvent.AppEvent event);

    @POST("calendars/{cal_id}/events/{id}")
    Call<Void> updateEvent(@Path("cal_id") String calId, @Path("id") String id, @Query("auth_token") String token, @Body CreateEvent.AppEvent event);

    @DELETE("calendars/{cal_id}/events/{id}")
    Call<Void> deleteEvent(@Path("cal_id") String calID, @Path("id") String id, @Query("auth_token") String token);

    @POST("calendars/{cal_id}/events/search")
    Call<List<Event>> search(@Path("cal_id") String calId, @Query("auth_token") String token, @Body ListEventsActivity.Query query);

}
