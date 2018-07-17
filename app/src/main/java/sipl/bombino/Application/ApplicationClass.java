package sipl.bombino.Application;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class ApplicationClass extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Fabric.with(this, new Crashlytics());
    }
}
