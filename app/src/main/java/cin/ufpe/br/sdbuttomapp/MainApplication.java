package cin.ufpe.br.sdbuttomapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

public class MainApplication extends Application {

    public static ApiManager apiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        apiManager = ApiManager.getInstance(getString(R.string.base_uri));
    }
}
