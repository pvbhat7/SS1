package com.sdgvvk.v1.api;


import com.sdgvvk.v1.livedata.YourDataModel;
import com.sdgvvk.v1.modal.ContactViewedModal;
import com.sdgvvk.v1.modal.Customer;
import com.sdgvvk.v1.modal.CustomerActivityModal;
import com.sdgvvk.v1.modal.FcmNotificationModal;
import com.sdgvvk.v1.modal.FcmTokenModal;
import com.sdgvvk.v1.modal.FilterModal;
import com.sdgvvk.v1.modal.FollowUpModal;
import com.sdgvvk.v1.modal.Level_1_cardModal;
import com.sdgvvk.v1.modal.Level_2_Modal;
import com.sdgvvk.v1.modal.MembershipModal;
import com.sdgvvk.v1.modal.MyMembershipModal;
import com.sdgvvk.v1.modal.NotificationModal;
import com.sdgvvk.v1.modal.OrderModal;
import com.sdgvvk.v1.modal.SingleResponse;
import com.sdgvvk.v1.modal.Stat;
import com.sdgvvk.v1.modal.TransactionModal;

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

    @GET("level1_view/byadminfilter.php")
    Call<List<Level_1_cardModal>> getAllCustomerProfilesByFilter(@Query("filter") String filter);

    /*@POST("level1_view/allFilteredLevel1.php")
    Call<List<Level_1_cardModal>> getFilteredLevel1Profiles(@Body FilterModal modal);*/

    @POST("level1_view/search.php")
    Call<List<Level_1_cardModal>> searchLevel1Profiles(@Body FilterModal modal);

    @POST("level1_view/searchByCpid.php")
    Call<List<Level_1_cardModal>> searchLevel1ProfileByCpid(@Body FilterModal modal);

    @POST("level2_view/allFilteredLevel2.php")
    Call<List<Customer>> getFilteredLevel2Profiles(@Body FilterModal modal);

    // GET LEVEL 2 DATA
    @GET("level2_view/byCustomerProfileId.php")
    Call<List<Level_2_Modal>> getLevel2DataByCPID(@Query("cpid") String cpid);

    // VIEW CONTACT ( ADD / VIEW )
    @GET("level1_view/viewcontact/viewContactData.php")
    Call<SingleResponse> viewContactData(@Query("cpid") String cpid , @Query("vcpid") String vcpid);
    @GET("level1_view/viewcontact/getProfilesByTag.php")
    Call<List<Level_1_cardModal>> getProfilesByTag(@Query("cpid") String cpid , @Query("tag") String tag);


    @GET("customer/getActivityData.php")
    Call<List<CustomerActivityModal>> getActivityData(@Query("cpid") String cpid );

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

    @GET("notification/adminByCpid.php")
    Call<List<NotificationModal>> adminByCpid(@Query("cpid") String cpid);



    @GET("notification/getDistinctUsers.php")
    Call<List<Level_1_cardModal>> getDistinctUserNotifications();

    @GET("notification/updateById.php")
    Call<ResponseBody> updateViewedNotificationState(@Query("id") String id);



    // CUSTOMER
    @GET("customer/byMobile.php")
    Call<List<Customer>> getCustomerByMobile(@Query("mobile") String mobile);

    @GET("customer/byCpid.php")
    Call<List<Customer>> getCustomerByCpid(@Query("cpid") String mobile);

    @GET("customer/byName.php")
    Call<List<Customer>> getCustomerByName(@Query("name") String mobile);

    @POST("customer/app_submit_register.php")
    Call<SingleResponse> registerNewCustomer(@Body Customer customer);

    @POST("customer/app_submit_register_new.php")
    Call<List<Customer>> registerAndGetCustomer(@Body Customer customer);

    @POST("customer/updateProfile.php")
    Call<List<Customer>> updateProfile(@Body Customer customer);

    @GET("customer/isMobileExists.php")
    Call<SingleResponse> isMobileExists(@Query("mobile") String mobile);

    @GET("customer/isMobileExistsAll.php")
    Call<SingleResponse> isMobileExistsAll(@Query("mobile") String mobile);

    @GET("getAdminPhone.php")
    Call<SingleResponse> getAdminPhone();

    // memberships
    @GET("orders/getMyMemberships.php")
    Call<List<MyMembershipModal>> getMyMemberships(@Query("cpid") String cpid);

    @POST("orders/assignMembership.php")
    Call<SingleResponse> assignMembership(@Body OrderModal orderModal);

    @GET("membership/all.php")
    Call<List<MembershipModal>> getAllMembershipPlans();

    @GET("getAdminCode.php")
    Call<SingleResponse> getAdminCode();

    @GET("isLive.php")
    Call<SingleResponse> isLive();

    @GET("admin/getStats.php")
    Call<List<Stat>> getStats();

    @GET("admin/getAdminNotice.php")
    Call<SingleResponse> getAdminNotice(@Query("noticeType") String noticeType,@Query("cpid") String cpid);


    @GET("customer/disableProfile.php")
    Call<SingleResponse> disableProfile(@Query("vcpid") String vcpid);

    @GET("customer/deleteProfile.php")
    Call<SingleResponse> deleteProfile(@Query("cpid") String cpid);

    @GET("customer/checkAccountStatus.php")
    Call<SingleResponse> checkAccountStatus(@Query("mobile") String mobile);

    @GET("transaction/all.php")
    Call<List<TransactionModal>> getAllTransactions();

    @GET("educationList_v1.php")
    Call<List<SingleResponse>> getAllEducationList(@Query("gender") String gender);

    @GET("citylist_v1.php")
    Call<List<SingleResponse>> getAllCityList(@Query("gender") String gender);

    @GET("castelist_v1.php")
    Call<List<SingleResponse>> getAllCasteList(@Query("gender") String gender);

    @GET("occupationList_v1.php")
    Call<List<SingleResponse>> getAllOccupationList(@Query("gender") String gender);

    @GET("lastnames_v1.php")
    Call<List<SingleResponse>> getAllLastnamesList(@Query("gender") String gender);

    @GET("admin/saveFollowUp.php")
    Call<ResponseBody> saveFollowUp(@Query("cpid") String cpid , @Query("followupdate") String followupdate ,@Query("comment") String comment);

    
    @GET("admin/getFollowupByCpid.php")
    Call<List<FollowUpModal>> getFollowUpByCpid(@Query("cpid") String cpid);

    @GET("admin/getAllFollowUps.php")
    Call<List<FollowUpModal>> getAllFollowUps();

    @GET("admin/updateFollowup.php")
    Call<ResponseBody> updateFollowup(@Query("id") String id);

    @POST("fcmToken/create.php")
    Call<String> createFcmTokenToServer(@Body FcmTokenModal modal);
    @POST("fcmNotifications/add.php")
    Call<String> createFcmNotificationToServer(@Body FcmNotificationModal modal);

    @GET("admin/addLeads.php")
    Call<ResponseBody> addLeads(@Query("cpid") String cpid , @Query("vcpid") String vcpid , @Query("type") String type);

    @GET("users.php")
    Call<List<YourDataModel>> getAllUsers();


    @GET("updateUserStatus.php")
    Call<ResponseBody> updateUserStatus(@Query("isApproved") String isApproved,@Query("id") String id);
}
