package com.nerdbar;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@ToString
public class UserItem {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String deviceId;

    private String currentLEDColor;

    private String userId;

    private Integer maxTemp;

    private Integer minTemp;

    private Integer currentTemp;

    private String currentDrink;

    private Integer sipCount;

    private Integer drinkDuration;

    private LocalDateTime drinkStart;

    private LocalDateTime drinkEnd;

}