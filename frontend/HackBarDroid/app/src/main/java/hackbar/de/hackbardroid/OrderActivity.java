package hackbar.de.hackbardroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import hackbar.de.hackbardroid.model.Drink;
import hackbar.de.hackbardroid.service.INerdBarService;
import hackbar.de.hackbardroid.service.NerdBarService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    private INerdBarService service;

    private ListView drinksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        drinksList = (ListView)findViewById(R.id.drinksList);

        service = NerdBarService.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadDrinks();
    }

    private void updateList(List<Drink> drinks) {
        ArrayAdapter<Drink> drinksAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, drinks);
        drinksList.setAdapter(drinksAdapter);
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
}
