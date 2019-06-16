package cin.ufpe.br.sdbuttomapp.data;

import cin.ufpe.br.sdbuttomapp.model.Occurrence;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static IOccurrencesApi service;
    private static ApiManager apiManager;

    private ApiManager(String baseUri) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUri)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IOccurrencesApi.class);
    }

    public static ApiManager getInstance(String baseUri) {
        if (apiManager == null) {
            apiManager = new ApiManager(baseUri);
        }
        return apiManager;
    }

    public void createOccurrence(Occurrence occurrence, Callback<Occurrence> callback) {
        Call<Occurrence> userCall = service.createOccurrence(occurrence);
        userCall.enqueue(callback);
    }
}