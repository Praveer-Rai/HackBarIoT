package hackbar.de.hackbardroid.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Drink {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("drinkName")
    @Expose
    private String drinkName;

    @SerializedName("minTemp")
    @Expose
    private int minTemp;

    @SerializedName("maxTemp")
    @Expose
    private int maxTemp;

    public Drink(long id, String drinkName, int minTemp, int maxTemp) {
        this.id = id;
        this.drinkName = drinkName;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    @Override
    public String toString() {
        return drinkName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }
}
