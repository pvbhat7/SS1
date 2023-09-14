package com.example.ss1.api;


import android.os.TestLooperManager;

import com.example.ss1.modal.ContactViewedModal;
import com.example.ss1.modal.Customer;
import com.example.ss1.modal.Level_1_cardModal;
import com.example.ss1.modal.Level_2_Modal;
import com.example.ss1.modal.MembershipModal;
import com.example.ss1.modal.MyMembershipModal;
import com.example.ss1.modal.NotificationModal;
import com.example.ss1.modal.OrderModal;
import com.example.ss1.modal.SingleResponse;

import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @GET("orders/getCountLeft.php")
    Call<SingleResponse> getCountLeft(@Query("cpid") String cpid);

    // GET LEVEL 1 DATA
    @GET("level1_view/all.php")
    Call<List<Level_1_cardModal>> getAllCustomerProfiles(@Query("cpid") String cpid);

    // GET LEVEL 2 DATA
    @GET("level2_view/byCustomerProfileId.php")
    Call<List<Level_2_Modal>> getLevel2DataByCPID(@Query("cpid") String cpid);

    // VIEW CONTACT ( ADD / VIEW )
    @GET("level1_view/viewcontact/viewContactData.php")
    Call<SingleResponse> viewContactData(@Query("cpid") String cpid , @Query("vcpid") String vcpid);
    @GET("level1_view/viewcontact/getProfilesByTag.php")
    Call<List<Level_1_cardModal>> getProfilesByTag(@Query("cpid") String cpid , @Query("tag") String tag);

    @GET("level1_view/viewcontact/getByCpid.php")
    Call<List<ContactViewedModal>> getContactViewedProfiles(@Query("cpid") String cpid);


    // SHORTLIST ( ADD / REMOVE / VIEW )
    @GET("level1_view/shortlist/all.php")
    Call<List<Level_1_cardModal>> getShortListedProfiles(@Query("cpid") String cpid);

    @GET("level1_view/shortlist/add.php")
    Call<List<Level_1_cardModal>> addToShortListedProfiles(@Query("cpid") String cpid,@Query("vcpid") String vcpid);

    @GET("level1_view/shortlist/remove.php")
    Call<List<Level_1_cardModal>> removeFromShortListedProfiles(@Query("cpid") String cpid,@Query("vcpid") String vcpid);

    // NOT INTERESTED ( ADD / REMOVE / VIEW )
    @GET("level1_view/notinterested/all.php")
    Call<List<Level_1_cardModal>> getNotInterestedProfiles(@Query("cpid") String cpid);

    @GET("level1_view/notinterested/add.php")
    Call<List<Level_1_cardModal>> addToNotInterestedProfiles(@Query("cpid") String cpid,@Query("vcpid") String vcpid);

    @GET("level1_view/notinterested/remove.php")
    Call<List<Level_1_cardModal>> removeFromNotInterestedProfiles(@Query("cpid") String cpid,@Query("vcpid") String vcpid);

    // INTEREST PROFILES ( ADD / REMOVE / VIEW )
    @GET("level1_view/interestedProfiles/allSent.php")
    Call<List<Level_1_cardModal>> getSentInterestedProfiles(@Query("cpid") String cpid);

    @GET("level1_view/interestedProfiles/allReceived.php")
    Call<List<Level_1_cardModal>> getReceivedInterestedProfiles(@Query("cpid") String cpid);

    @GET("level1_view/interestedProfiles/add.php")
    Call<List<Level_1_cardModal>> addToInterestedProfiles(@Query("cpid") String cpid,@Query("vcpid") String vcpid);

    @GET("level1_view/interestedProfiles/remove.php")
    Call<List<Level_1_cardModal>> removeFromInterestedProfiles(@Query("cpid") String cpid,@Query("vcpid") String vcpid);

    // INTEREST PROFILES ( ADD / REMOVE / VIEW )
    @GET("level1_view/likedProfiles/add.php")
    Call<List<Level_1_cardModal>> addToLikedProfiles(@Query("cpid") String cpid,@Query("vcpid") String vcpid);

    // NOTIFICATION
    @POST("notification/add.php")
    Call<ResponseBody> addNotification(@Body NotificationModal modal);

    // NOTIFICATION
    @POST("orders/getOrderByCpid.php")
    Call<List<OrderModal>> getActiveOrderByCpid(@Query("cpid") String cpid);


    @GET("notification/getByCpid.php")
    Call<List<NotificationModal>> getUserNotifications(@Query("cpid") String cpid);

    @GET("notification/updateById.php")
    Call<ResponseBody> updateViewedNotificationState(@Query("id") String id);


    // CUSTOMER
    @GET("customer/byMobile.php")
    Call<List<Customer>> getCustomerByMobile(@Query("mobile") String mobile);

    @POST("customer/app_submit_register.php")
    Call<SingleResponse> registerNewCustomer(@Body Customer customer);

    @GET("customer/isMobileExists.php")
    Call<SingleResponse> isMobileExists(@Query("mobile") String mobile);

    @GET("customer/isMobileExistsAll.php")
    Call<SingleResponse> isMobileExistsAll(@Query("mobile") String mobile);

    // memberships
    @GET("orders/getMyMemberships.php")
    Call<List<MyMembershipModal>> getMyMemberships(@Query("cpid") String cpid);

    @GET("membership/all.php")
    Call<List<MembershipModal>> getAllMembershipPlans();



}
