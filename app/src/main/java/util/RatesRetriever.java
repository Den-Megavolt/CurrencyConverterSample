package util;

import android.net.Uri;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import client.API;
import client.ApiClient;
import model.MessageEvent;
import model.RateResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dkotov on 19-Sep-17.
 */

public class RatesRetriever {

    private static final String TAG = "ElectroluxTest :" + RatesRetriever.class.getSimpleName();
    private static HashMap<String, Double> rates;
    private static MessageEvent messageEvent;

    public static void retrieveRates(String currencyCode) {

        Uri uri = Uri.parse(API.BASE_PATH).buildUpon()
                .appendQueryParameter("base", currencyCode).build();
        API.DataService dataService = ApiClient.getClient().create(API.DataService.class);
        Call<RateResponse> getRates = dataService.getRates(uri.toString());
        Log.d(TAG, "Requesting rates" + getRates.request());

        getRates.enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(Call<RateResponse> call, Response<RateResponse> response) {
                RateResponse rateResponse = response.body();
                if (rateResponse.getRates() != null) {
                    rates = rateResponse.getRates();
                    messageEvent = new MessageEvent(0, rates);
                    EventBus.getDefault().postSticky(messageEvent);
                    Log.d(TAG, "Request successful, event posted");
                }
            }
            @Override
            public void onFailure(Call<RateResponse> call, Throwable t) {
                messageEvent = new MessageEvent(1, rates);
                EventBus.getDefault().postSticky(messageEvent);
                Log.d(TAG, "Failed to request rates " + t.getMessage() +
                " " + ". Failure event posted.");
            }
        });

    }
}
