package hackbar.de.hackbardroid.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Drink {

    public static final String DRINK_KEY = "drink";

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("drinkName")
    @Expose
    private String drinkName;

    @SerializedName("minTemp")
    @Expose
    private Integer minTemp;

    @SerializedName("maxTemp")
    @Expose
    private Integer maxTemp;

    @SerializedName("imageURL")
    @Expose
    private String imageURL;

    @SerializedName("price")
    @Expose
    private String price;

    public Drink(long id, String drinkName, int minTemp, int maxTemp, String imageURL, String price) {
        this.id = id;
        this.drinkName = drinkName;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.imageURL = imageURL;
        this.price = price;
    }

    @Override
    public String toString() {
        return drinkName;
    }

    public static String getDrinkKey() {
        return DRINK_KEY;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public Integer getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Integer minTemp) {
        this.minTemp = minTemp;
    }

    public Integer getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Integer maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
