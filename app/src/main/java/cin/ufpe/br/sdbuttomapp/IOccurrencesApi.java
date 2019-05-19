package cin.ufpe.br.sdbuttomapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IOccurrencesApi {

    @POST("/occurrences")
    Call<Occurrence> createOccurrence(@Body Occurrence occurrence);

}
