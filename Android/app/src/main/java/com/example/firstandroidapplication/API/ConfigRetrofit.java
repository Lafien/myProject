package com.example.firstandroidapplication.API;

import com.example.firstandroidapplication.authorization.UserAuthorization;
import com.example.firstandroidapplication.authorization.UserSecurity;
import com.example.firstandroidapplication.chats.Message;
import com.example.firstandroidapplication.users.UserInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

public class ConfigRetrofit {

    private static ConfigRetrofit mInstance;
    private static final String BASE_URL = "http://172.20.10.2:8080";
    private UserApi mRetrofit;

    private ConfigRetrofit(){
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(2, TimeUnit.SECONDS)
                .connectTimeout(2, TimeUnit.SECONDS)
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserApi.class);
    }


    public static ConfigRetrofit getInstance() {
        if(mInstance == null){
            mInstance = new ConfigRetrofit();
        }
        return mInstance;
    }

    public Call<UserSecurity> authorizationUser(@Body UserAuthorization userAuthorization){
        return mRetrofit.authorization(userAuthorization);
    }

    public Call<UserInfo> myProfile(@Header("Authorization") String token){
        return mRetrofit.getMyProfile(token);
    }

    public  Call<List<UserInfo>> getContacts(@Header("Authorization") String token){
        return mRetrofit.getContacts(token);
    }

    public Call<List<UserInfo>> getChats(@Header("Authorization") String token){
        return mRetrofit.getChats(token);
    }

    public Call<List<Message>> getMessages(@Header("Authorization") String token, @Path("username") String username){
        return mRetrofit.getMessages(token, username);
    }

    public Call<Object> sendMessage(@Header("Authorization") String token, @Body Message message){
        return mRetrofit.sendMessage(token, message);
    }

    public Call<UserInfo> getContactInfo(@Header("Authorization") String token, @Path("chooseContact1") String username){
        return mRetrofit.getContactInfo(token, username);
    }

    public Call<ResponseBody> addContact(@Header("Authorization") String token, @Body UserInfo userInfo){
        return mRetrofit.addContact(token, userInfo);
    }


    public Call<ResponseBody> getImage(@Header("Authorization") String token, @Path("username")String username){
        return mRetrofit.getImage(token, username);
    }

}
