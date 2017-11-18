package com.nerdbar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping({"/nerdbar"})
public class NerdBarController {

    @Autowired
    private UserItemRepository userItemRepository;

    @Autowired
    private DrinkItemRepository drinkItemRepository;

    @PostMapping("/temperature")
    public ResponseEntity temperature(
            @RequestParam("userId") final String userId,
            @RequestParam("temp") Integer temp
    ) {
        UserItem user = userItemRepository.findByUserId(userId);

        if(user != null){
            user.setCurrentTemp(temp);
            userItemRepository.save(user);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/currentTemperature")
    public ResponseEntity getCurrentTemperature(
            @RequestParam("userId") final String userId
    ) {
        UserItem user = userItemRepository.findByUserId(userId);

        Integer temp = null;

        if(user != null) {
            temp = user.getCurrentTemp();
        }
        return new ResponseEntity<>(temp, HttpStatus.OK);
    }


    @GetMapping("/currentLEDColor")
    public ResponseEntity getCurrentLEDColor(
            @RequestParam("userId") final String userId
    ) {
        UserItem user = userItemRepository.findByUserId(userId);

        String currentLEDColor = null;

        if(user != null) {
            currentLEDColor = user.getCurrentLEDColor();
        }
        return new ResponseEntity<>(currentLEDColor, HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity register(
            @RequestParam("userId") final String userId,
            @RequestParam("deviceId") String deviceId
    ) {
        UserItem user = userItemRepository.findByUserId(userId);

        if(user != null){
            user.setDeviceId(deviceId);
        } else {
            user = new UserItem();
            user.setDeviceId(deviceId);
            user.setUserId(userId);
        }

        userItemRepository.save(user);

        UserItem oldUser = userItemRepository.findByDeviceId(deviceId);

        if(oldUser != null) {
            oldUser.setDeviceId(null);
            userItemRepository.save(oldUser);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity register(
            @RequestParam("userId") final String userId
    ) {
        UserItem user = userItemRepository.findByUserId(userId);
        if(user != null){
            user.setDeviceId(null);
            userItemRepository.save(user);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/orderDrink")
    public ResponseEntity orderDrink(
            @RequestParam("userId") final String userId,
            @RequestParam("drinkName") String drinkName
    ) {
        UserItem user = userItemRepository.findByUserId(userId);

        DrinkItem drinkItem = drinkItemRepository.findByDrinkName(drinkName);

        if(user != null){
            user.setCurrentDrink(drinkName);
            user.setMinTemp(drinkItem.getMinTemp());
            user.setMaxTemp(drinkItem.getMaxTemp());
            user.setDrinkStart(LocalDateTime.now());
            userItemRepository.save(user);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/addDrink")
    public ResponseEntity addDrink(
            @RequestParam("drinkName") String drinkName,
            @RequestParam("minTemp") Integer minTemp,
            @RequestParam("maxTemp") Integer maxTemp
    ) {

        DrinkItem drinkItem = new DrinkItem();
        drinkItem.setDrinkName(drinkName);
        drinkItem.setMinTemp(minTemp);
        drinkItem.setMaxTemp(maxTemp);
        drinkItemRepository.save(drinkItem);

        return new ResponseEntity<>(drinkItem, HttpStatus.OK);
    }

    @GetMapping("/getDrinks")
    public ResponseEntity getDrinks(
    ) {
        return new ResponseEntity<>(drinkItemRepository.findAll(), HttpStatus.OK);
    }


    @PostMapping("/deleteDrink")
    public ResponseEntity deleteDrink(
            @RequestParam("drinkName") String drinkName
    ) {
        DrinkItem drinkItem = drinkItemRepository.findByDrinkName(drinkName);

        if(drinkItem != null) {
            drinkItemRepository.delete(drinkItem);
        }

        return new ResponseEntity<>(drinkItem, HttpStatus.OK);
    }

    @PostMapping("/deleteUser")
    public ResponseEntity deleteUser(
            @RequestParam("userId") final String userId
    ) {
        UserItem user = userItemRepository.findByUserId(userId);

        if(user != null) {
            userItemRepository.delete(user);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
