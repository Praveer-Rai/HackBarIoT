package com.nerdbar.model;

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
public class DrinkItem {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String drinkName;

    private Integer maxTemp;

    private Integer minTemp;

    private String imageURL;

    private String price;

}