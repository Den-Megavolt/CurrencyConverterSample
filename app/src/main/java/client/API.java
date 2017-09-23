package client;

import java.util.List;

import model.RateResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by dkotov on 19-Sep-17.
 */

public class API {

    public static final String BASE_PATH = "http://api.fixer.io/latest";

    public interface DataService {

        @GET
        Call<RateResponse> getRates(@Url String path);
    }
}
