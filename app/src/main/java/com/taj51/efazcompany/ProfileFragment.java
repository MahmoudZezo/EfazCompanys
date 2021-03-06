package com.taj51.efazcompany;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.taj51.efazcompany.api_classes.Api;
import com.taj51.efazcompany.pojo.GetProfilePojo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {


    private ImageView profileLogo;
    private TextView profileCompanyName;
    private TextView profileCompanyEmail;
    private TextView profileCompanyService;
    private TextView profileCompanyWebsite;
    private ImageButton btn;
    private CardView cardView;

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String encodedImage) {
        byte[] decodedByte = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        profileLogo = (ImageView) view.findViewById(R.id.profile_logo_container);
        profileCompanyName = (TextView) view.findViewById(R.id.profile_company_name);
        profileCompanyEmail = (TextView) view.findViewById(R.id.profile_company_mail);
        profileCompanyService = (TextView) view.findViewById(R.id.profile_company_service);
        profileCompanyWebsite = (TextView) view.findViewById(R.id.profile_company_website);
        btn = (ImageButton) view.findViewById(R.id.play_youtube);
        cardView = (CardView)view.findViewById(R.id.displayVideo);


        new myAsync().execute();


        return view;
    }

    public class myAsync extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            final int id = getArguments().getInt("id");
            final String email = getArguments().getString("email");
            Log.d("TestAPI", id + " + " + email);
            Api.getClient().getProfile(id).enqueue(new Callback<GetProfilePojo>() {
                @Override
                public void onResponse(Call<GetProfilePojo> call, Response<GetProfilePojo> response) {
                    Log.d("TestAPI", "hskdjgaskjd   " + id);
                    GetProfilePojo pojo = response.body();
                    Log.d("TestAPI", pojo.getCompany_logo_image());
                    profileLogo.setImageBitmap(decodeBase64(pojo.getCompany_logo_image()));
                    profileCompanyName.setText(pojo.getCompany_name());
                    profileCompanyEmail.setText(email);
                    profileCompanyService.setText(pojo.getCompany_service_desc());
                    profileCompanyWebsite.setText(pojo.getCompany_website_url());
                    final String youtube = pojo.getCompany_link_youtube().trim();

                        if (youtube.equals("") || youtube.equals(null)){
                            cardView.setVisibility(View.GONE);
                        }else {
                        cardView.setVisibility(View.VISIBLE);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(youtube)));
                            }
                        });

                    }
                    //Toast.makeText(getActivity(), pojo.getCompany_name(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<GetProfilePojo> call, Throwable t) {
                    Log.d("TestAPI", t.getMessage() + t.getLocalizedMessage());

                }
            });
            return null;
        }
    }
}
