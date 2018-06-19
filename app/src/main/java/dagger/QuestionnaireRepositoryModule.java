package dagger;

import javax.inject.Singleton;
import org.kinecosystem.kinit.repository.DataStoreProvider;
import org.kinecosystem.kinit.repository.QuestionnaireRepository;

@Module(includes = DataStoreProviderModule.class)
public class QuestionnaireRepositoryModule {

    @Provides
    @Singleton
    public QuestionnaireRepository questionnaireRepository(DataStoreProvider dataStoreProvider) {
        return new QuestionnaireRepository(dataStoreProvider, null);
    }
}
