package cin.ufpe.br.sdbuttomapp.data;

import cin.ufpe.br.sdbuttomapp.model.Occurrence;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IOccurrencesApi {

    @POST("/occurrences")
    Call<Occurrence> createOccurrence(@Body Occurrence occurrence);

}
