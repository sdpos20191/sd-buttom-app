package cin.ufpe.br.sdbuttomapp.data;

import android.app.Application;

import cin.ufpe.br.sdbuttomapp.R;
import cin.ufpe.br.sdbuttomapp.data.ApiManager;

public class MainApplication extends Application {

    public static ApiManager apiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        apiManager = ApiManager.getInstance(getString(R.string.base_uri));
    }
}
