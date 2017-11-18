package hackbar.de.hackbardroid.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("maxTemp")
    @Expose
    private int maxTemp;

    @SerializedName("minTemp")
    @Expose
    private int minTemp;

    @SerializedName("currentTemp")
    @Expose
    private int currentTemp;

    @SerializedName("currentDrink")
    @Expose
    private int currentDrink;

    @SerializedName("sipCount")
    @Expose
    private int sipCount;

    @SerializedName("drinkDuration")
    @Expose
    private int drinkDuration;

    @SerializedName("drinkStart")
    @Expose
    private int drinkStart;

    @SerializedName("drinkEnd")
    @Expose
    private int drinkEnd;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(int currentTemp) {
        this.currentTemp = currentTemp;
    }

    public int getCurrentDrink() {
        return currentDrink;
    }

    public void setCurrentDrink(int currentDrink) {
        this.currentDrink = currentDrink;
    }

    public int getSipCount() {
        return sipCount;
    }

    public void setSipCount(int sipCount) {
        this.sipCount = sipCount;
    }

    public int getDrinkDuration() {
        return drinkDuration;
    }

    public void setDrinkDuration(int drinkDuration) {
        this.drinkDuration = drinkDuration;
    }

    public int getDrinkStart() {
        return drinkStart;
    }

    public void setDrinkStart(int drinkStart) {
        this.drinkStart = drinkStart;
    }

    public int getDrinkEnd() {
        return drinkEnd;
    }

    public void setDrinkEnd(int drinkEnd) {
        this.drinkEnd = drinkEnd;
    }
}