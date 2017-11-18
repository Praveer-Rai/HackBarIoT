package hackbar.de.hackbardroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import hackbar.de.hackbardroid.adapters.DrinksAdapter;
import hackbar.de.hackbardroid.model.Drink;
import hackbar.de.hackbardroid.service.INerdBarService;
import hackbar.de.hackbardroid.service.NerdBarService;
import hackbar.de.hackbardroid.settings.UserSettings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    private INerdBarService service;

    private UserSettings settings;

    private ListView drinksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        drinksList = (ListView)findViewById(R.id.drinksList);

        settings = new UserSettings(getApplicationContext());
        service = NerdBarService.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadDrinks();
    }

    private void loadDrinks() {
        Call<List<Drink>> drinksCall = service.getDrinks();
        drinksCall.enqueue(new Callback<List<Drink>>() {
            @Override
            public void onResponse(Call<List<Drink>> call, Response<List<Drink>> response) {
                if (response.isSuccessful()) {
                    List<Drink> drinks = response.body();
                    updateList(drinks);
                }
            }

            @Override
            public void onFailure(Call<List<Drink>> call, Throwable t) {

            }
        });
    }

    private void updateList(List<Drink> drinks) {
        DrinksAdapter adapter = new DrinksAdapter(this, drinks, settings.getUserId());
        drinksList.setAdapter(adapter);
    }
}
