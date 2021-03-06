package com.taj51.efazcompany;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.taj51.efazcompany.adapters.CustomAdapter;
import com.taj51.efazcompany.adapters.MyOfferCustomAdapter;
import com.taj51.efazcompany.api_classes.Api;
import com.taj51.efazcompany.pojo.GetCompanyOfferPOJO;
import com.taj51.efazcompany.pojo.GetProfilePojo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView noOffersTxt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragments_offer, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView2);
        noOffersTxt = (TextView) view.findViewById(R.id.no_offers_txt);
        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        new myAsync().execute();
        int apiId = getArguments().getInt("id");
        Toast.makeText(getActivity(), apiId + " asdsad", Toast.LENGTH_LONG).show();

        return view;
    }


    public class myAsync extends AsyncTask<Void, Void, List<GetCompanyOfferPOJO>> {

        List<GetCompanyOfferPOJO> tempList;

        @Override
        protected List<GetCompanyOfferPOJO> doInBackground(Void... voids) {
            int apiId = getArguments().getInt("id");
            Api.getClient().getSingleCompanyOffers(apiId).enqueue(new Callback<List<GetCompanyOfferPOJO>>() {
                @Override
                public void onResponse(Call<List<GetCompanyOfferPOJO>> call, Response<List<GetCompanyOfferPOJO>> response) {

                    List<GetCompanyOfferPOJO> temp = response.body();
                    tempList = temp;
                    if (tempList.size() > 0) {
                        noOffersTxt.setVisibility(View.GONE);
                        //Log.d("getResponse", tempList.get(1).getOffer_logo() + "");
                        final ArrayList<String> productTitlesArr = new ArrayList<String>();
                        final ArrayList<String> productImagesArr = new ArrayList<String>();
                        final ArrayList<String> daysArr = new ArrayList<String>();
                        final ArrayList<String> hoursArr = new ArrayList<String>();
                        final ArrayList<String> minutesArr = new ArrayList<String>();
                        final ArrayList<Integer> productIds = new ArrayList<Integer>();
                        final ArrayList<Double> productCosts = new ArrayList<Double>();
                        final ArrayList<String> productExplanation = new ArrayList<String>();


                        for (int i = 0; i < tempList.size(); i++) {
                            productImagesArr.add(tempList.get(i).getOffer_logo());
                            productTitlesArr.add(tempList.get(i).getOffer_title());
                            String first = tempList.get(i).getOffer_display_date();
                            String second = tempList.get(i).getOffer_expired_date();
                            String third = tempList.get(i).getOffer_deliver_date();
                            productIds.add(tempList.get(i).getOffer_id());
                            productCosts.add(tempList.get(i).getOffer_cost());
                            productExplanation.add(tempList.get(i).getOffer_explaination());

                            try {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
//                            Date parsedDate = dateFormat.parse(first);
//                            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                                Date parsedDate2 = dateFormat.parse(second);
                                Timestamp timestamp2 = new java.sql.Timestamp(parsedDate2.getTime());
                                Date parsedDate3 = dateFormat.parse(third);
                                Timestamp timestamp3 = new java.sql.Timestamp(parsedDate3.getTime());

                                java.sql.Timestamp display = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                                long milliseconds1 = display.getTime();
                                long milliseconds2 = timestamp2.getTime();
                                long diff = milliseconds2 - milliseconds1;
                                long diffDays = diff / (24 * 60 * 60 * 1000);
                                long rem = diff % (24 * 60 * 60 * 1000);
                                long diffHours = rem / (60 * 60 * 1000);
                                long rem2 = rem % (60 * 60 * 1000);
                                long diffMinutes = rem2 / (60 * 1000);
                                daysArr.add(String.valueOf(diffDays));
                                hoursArr.add(String.valueOf(diffHours));
                                minutesArr.add(String.valueOf(diffMinutes));
                            } catch (Exception e) { //this generic but you can control another types of exception
                                // look the origin of excption
                            }

                        }

                        final int companyId = getArguments().getInt("id");
                        Api.getClient().getProfile(companyId).enqueue(new Callback<GetProfilePojo>() {
                            @Override
                            public void onResponse(Call<GetProfilePojo> call, Response<GetProfilePojo> response) {
                                String companyLogo = response.body().getCompany_logo_image();
                                String companyName = response.body().getCompany_name();
                                MyOfferCustomAdapter adapter = new MyOfferCustomAdapter(getActivity(), companyId ,companyLogo, companyName, productTitlesArr,
                                        productImagesArr, daysArr, hoursArr, minutesArr, productIds, productCosts, productExplanation);
                                recyclerView.setAdapter(adapter);

                            }

                            @Override
                            public void onFailure(Call<GetProfilePojo> call, Throwable t) {

                            }
                        });
                    }else{
                        noOffersTxt.setVisibility(View.VISIBLE);
                    }


                }

                @Override
                public void onFailure(Call<List<GetCompanyOfferPOJO>> call, Throwable t) {
                    Log.d("getResponse", "Error  " + t.getMessage());
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(List<GetCompanyOfferPOJO> getCompanyOfferPOJOS) {
            super.onPostExecute(getCompanyOfferPOJOS);
//            if (tempList == null) {
//                //Toast.makeText(getActivity(), "EMPTY", Toast.LENGTH_LONG).show();
//
//            } else {
//                //Toast.makeText(getActivity(), tempList.get(17).getOffer_logo() + "", Toast.LENGTH_LONG).show();
//
//            }
        }
    }
}
