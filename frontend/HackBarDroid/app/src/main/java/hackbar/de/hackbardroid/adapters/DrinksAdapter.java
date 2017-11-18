package hackbar.de.hackbardroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import hackbar.de.hackbardroid.MainActivity;
import hackbar.de.hackbardroid.R;
import hackbar.de.hackbardroid.model.Drink;
import hackbar.de.hackbardroid.service.INerdBarService;
import hackbar.de.hackbardroid.service.NerdBarService;
import hackbar.de.hackbardroid.tasks.ImageLoadTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrinksAdapter extends BaseAdapter {
    private final Context context;
    private final List<Drink> drinks;
    private static LayoutInflater inflater = null;

    private final String userId;

    public DrinksAdapter(Context context, List<Drink> drinks, String userId) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.drinks = drinks;
        this.userId = userId;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return drinks.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return drinks.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.drink_item, null);

        final Drink drink = drinks.get(position);

        TextView title = vi.findViewById(R.id.titleText);
        TextView price = vi.findViewById(R.id.priceText);
        final ImageView image = vi.findViewById(R.id.drinkImage);
        Button orderButton = vi.findViewById(R.id.orderButton);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderDrink(userId, drink);
            }
        });
        title.setText(drink.getDrinkName());
        price.setText(String.format(Locale.ENGLISH, "%s €", drink.getPrice()));
        new ImageLoadTask(drink.getImageURL(), image).execute();
        return vi;
    }

    private void orderDrink(String userId, final Drink drink) {
        INerdBarService service = NerdBarService.getInstance();
        Call<Void> orderCall = service.orderDrink(userId, drink.getDrinkName());
        orderCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    navigateToMainPage(drink);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void navigateToMainPage(Drink drink) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Drink.DRINK_KEY, drink.getDrinkName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
