package hackbar.de.hackbardroid.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("maxTemp")
    @Expose
    private Integer maxTemp;

    @SerializedName("minTemp")
    @Expose
    private Integer minTemp;

//    @SerializedName("currentTemp")
//    @Expose
//    private Integer currentTemp;

    @SerializedName("currentDrink")
    @Expose
    private String currentDrink;

    @SerializedName("sipCount")
    @Expose
    private Integer sipCount;

//    @SerializedName("drinkDuration")
//    @Expose
//    private Integer drinkDuration;
//
//    @SerializedName("drinkStart")
//    @Expose
//    //private String drinkStart;
//
//    @SerializedName("drinkEnd")
//    @Expose
//    private String drinkEnd;

    @SerializedName("findMyDrink")
    @Expose
    private Boolean findMyDrink;

    @SerializedName("needAssistance")
    @Expose
    private Boolean needAssistance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Integer maxTemp) {
        this.maxTemp = maxTemp;
    }

    public Integer getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Integer minTemp) {
        this.minTemp = minTemp;
    }

    public Integer getSipCount() {
        return sipCount;
    }

    public void setSipCount(Integer sipCount) {
        this.sipCount = sipCount;
    }

    public Boolean getFindMyDrink() {
        return findMyDrink;
    }

    public void setFindMyDrink(Boolean findMyDrink) {
        this.findMyDrink = findMyDrink;
    }

    public Boolean getNeedAssistance() {
        return needAssistance;
    }

    public void setNeedAssistance(Boolean needAssistance) {
        this.needAssistance = needAssistance;
    }

    public String getCurrentDrink() {
        return currentDrink;
    }

    public void setCurrentDrink(String currentDrink) {
        this.currentDrink = currentDrink;
    }
}