package com.nerdbar;

import org.springframework.data.repository.CrudRepository;

public interface DrinkItemRepository extends CrudRepository<DrinkItem, Long> {

    DrinkItem findByDrinkName(String drinkName);

}