package com.example.homepc.walkinggroupapp.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * The ProxyBuilder class will handle the apiKey and token being injected as a header to all calls
 * This is a Retrofit interface.
 */
public interface WGServerProxy {
    @GET("getApiKey")
    Call<String> getApiKey(@Query("groupName") String groupName, @Query("sfuUserId") String sfuId);

    @POST("/users/signup")
    Call<User> createNewUser(@Body User user);

    @POST("/login")
    Call<Void> login(@Body User userWithEmailAndPassword);

    @GET("/users")
    Call<List<User>> getUsers();

    @GET("/users/{id}")
    Call<User> getUserById(@Path("id") Long userId);

    @GET("/users/byEmail")
    Call<User> getUserByEmail(@Query("email") String email);

    @POST("users/{id}")
    Call<User> editUserById(@Path("id") Long userId, @Body User user);

    @GET("/users/{id}/monitorsUsers")
    Call<List<User>> getMonitorUser(@Path("id") Long userId);

    @GET ("/users/{id}/monitoredByUsers")
    Call<List<User>> getMonitoredByUser(@Path("id") Long userId);

    @POST("/users/{id}")
    Call<User> editUser(@Path("id") Long userId, @Body User editedUser);

    @POST("/users/{id}/monitorsUsers")
    Call<List<User>> addMonitorUser(@Path("id") Long userId, @Body User otherUserId);

    @POST("/users/{id}/monitoredByUsers")
    Call<List<User>> addMonitoredByUser(@Path("id") Long userId, @Body User otherUserId);

    @DELETE("/users/{idA}/monitorsUsers/{idB}")
    Call<Void> deleteMonitor(@Path("idA") Long currUserId, @Path("idB") Long otherUserId);

    @GET("/groups")
    Call<List<Group>> getGroups();

    @POST("/groups")
    Call<Group> createNewGroup(@Body Group group);

    @GET("/groups/{id}")
    Call<Group> getGroupById(@Path("id") Long groupId);

    @POST("/groups/{id}")
    Call<Group> updateGroupDetails(@Path("id") Long groupId, @Body Group group);

    @DELETE("/groups/{id}")
    Call<Void> deleteGroup(@Path("id") Long groupId);

    @GET("/groups/{id}/memberUsers")
    Call<List<User>> getGroupMembers(@Path("id") Long groupId);

    @POST("/groups/{id}/memberUsers")
    Call<List<User>> addMemberToGroup(@Path("id") Long groupId, @Body User user);

    @DELETE("/groups/{groupId}/memberUsers/{userId}")
    Call<Void> removeFromGroup(@Path("groupId") long groupId, @Path("userId") Long userId);

    @GET("/users/{id}/lastGpsLocation")
    Call<LastGpsLocation> getLastGpsLocation(@Path("id") Long id);

    @POST("/users/{id}/lastGpsLocation")
    Call<LastGpsLocation> setLastGpsLocation(@Path("id") Long id, @Body LastGpsLocation lastGpsLocation);

    @GET("/messages")
    Call<List<Message>> getMessages();

    @GET("/messages")
    Call<List<Message>> getEmergencyMessages(@Query("is-emergency") String condition);

    @GET("/messages")
    Call<List<Message>> getGroupMessages(@Query("togroup") String group);

    @GET("/messages")
    Call<List<Message>> getEmergencyGroupMessages(@Query("is-emergency") String condition);

    @GET("/messages")
    Call<List<Message>> getUserMessages(@Query("foruser") String id);

    @GET("/messages")
    Call<List<Message>> getMessagesStatus(@Query("foruser") String id, @Query("status") String status);

    @POST("/messages/togroup/{groupId}")
    Call<Message> sendMessageToGroup(@Path("groupId") Long groupId, @Body Message message);

    @POST("messages/toparentsof/{userId}")
    Call<Message> sendMessageToParents(@Path("userId") Long groupId, @Body Message message);

    @POST("/messages/{messageId}/readby/{userId}")
    Call<User> setMessageAsRead(@Path("messageId") Long messageId, @Path("userId") Long userId, @Body boolean read);

    @DELETE("/messages/{id}")
    Call<Void> deleteMessage(@Path("id") Long id);

    @GET("/permissions")
    Call<List<Permission>> getAllPermissions();

    @GET("/permissions")
    Call<List<Permission>> getUserPermissions(@Query("userId") String id, @Query("statusForUser") String status);

    @POST("/permissions/{id}")
    Call<Permission> approveDenyPermission(@Path("id") Long id, @Body String string);
}
