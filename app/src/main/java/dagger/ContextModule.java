package dagger;

import android.content.Context;
import javax.inject.Singleton;

@Module
public class ContextModule {

    private Context context;
    public ContextModule(Context context){
        this.context = context;
    }

    @Provides
    @Singleton
    public Context context(){
        return context;
    }
}
