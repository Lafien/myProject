package com.example.firstandroidapplication;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.firstandroidapplication.model.UserAuthentification;
import com.example.firstandroidapplication.model.UserSecurity;
import com.example.firstandroidapplication.service.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentUserAuthorization extends Fragment {

    public static String token = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.authorization_content, container, false);

        Button authorization = view.findViewById(R.id.send);

        final EditText editText = view.findViewById(R.id.login);
        final EditText editText1 = view.findViewById(R.id.password);
        final TextView textView = view.findViewById(R.id.textView2);


        authorization.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                UserAuthentification userAuthentification = new UserAuthentification();


                String login = editText.getText().toString();


                String password = editText1.getText().toString();

                userAuthentification.setUsername(login);
                userAuthentification.setPassword(password);


                NetworkService.getInstance()
                        .authorizationUser()
                        .authorization(userAuthentification)
                        .enqueue(new Callback<UserSecurity>() {
                            @Override
                            public void onResponse(Call<UserSecurity> call, Response<UserSecurity> response) {

                                if(response.isSuccessful()) {
                                    UserSecurity post = response.body();

                                    //textView.setText("Авторизация успешна");
                                    token = "Bearer_" + post.getToken();

                                    FragmentTransaction fTrans;

                                    FragmentMainPage fragmentMainPage = new FragmentMainPage();

                                    fTrans = getFragmentManager().beginTransaction();
                                    fTrans.replace(R.id.main, fragmentMainPage);
                                    fTrans.addToBackStack(null);
                                    fTrans.commit();

                                }
                                else {
                                    textView.setText("Неверный логин или пароль");
                                }
                            }

                            @Override
                            public void onFailure(Call<UserSecurity> call, Throwable t) {
                                textView.setText("Error occurred while getting request!");
                                t.printStackTrace();
                            }
                        });



            }



        });




        return  view;
    }



}
