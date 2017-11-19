package com.nerdbar.repo;

import com.nerdbar.model.DrinkItem;
import org.springframework.data.repository.CrudRepository;

public interface DrinkItemRepository extends CrudRepository<DrinkItem, Long> {

    DrinkItem findByDrinkName(String drinkName);

}