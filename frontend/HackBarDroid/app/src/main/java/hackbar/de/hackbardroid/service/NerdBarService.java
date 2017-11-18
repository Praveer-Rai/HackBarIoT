package hackbar.de.hackbardroid.service;

import retrofit2.Retrofit;

public class NerdBarService {

    private static INerdBarService service;

    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://hackatumdemoapp.azurewebsites.net/nerdbar/")
                .build();

        service = retrofit.create(INerdBarService.class);
    }

    private NerdBarService() { }

    public static INerdBarService getInstance() {
        return service;
    }
}
