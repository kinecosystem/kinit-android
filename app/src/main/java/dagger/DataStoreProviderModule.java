package dagger;

import javax.inject.Singleton;
import org.kinecosystem.kinit.repository.DataStoreProvider;
import org.kinecosystem.kinit.repository.QuestionnaireRepository;

@Module
public class DataStoreProviderModule {

    private DataStoreProvider dataStoreProvider;

    public DataStoreProviderModule(DataStoreProvider dataStoreProvider) {
        this.dataStoreProvider = dataStoreProvider;

    }

    @Provides
    @Singleton
    public DataStoreProvider dataStoreProvider() {
        return dataStoreProvider;
    }
}
