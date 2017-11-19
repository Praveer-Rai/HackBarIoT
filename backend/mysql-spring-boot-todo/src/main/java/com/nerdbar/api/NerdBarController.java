package com.nerdbar.api;

import com.nerdbar.model.DrinkItem;
import com.nerdbar.repo.DrinkItemRepository;
import com.nerdbar.model.UserItem;
import com.nerdbar.repo.UserItemRepository;
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
            @RequestParam("deviceId") final String deviceId,
            @RequestParam("temp") Integer temp
    ) {
        UserItem user = userItemRepository.findByDeviceId(deviceId);

        if(user != null){
            user.setCurrentTemp(temp);
            userItemRepository.save(user);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/currentTemperature")
    public ResponseEntity getCurrentTemperature(
            @RequestParam("userId") final String deviceId
    ) {
        UserItem user = userItemRepository.findByDeviceId(deviceId);

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
            @RequestParam(value="deviceId", required=false) String deviceId
    ) {
        UserItem user = userItemRepository.findByUserId(userId);

        if(deviceId!=null && !deviceId.isEmpty()) {
            UserItem oldUser = userItemRepository.findByDeviceId(deviceId);
            if(oldUser != null) {
                oldUser.setDeviceId(null);
                userItemRepository.save(oldUser);
            }
        }

        if(user != null){
            user.setDeviceId(deviceId);
        } else {
            user = new UserItem();
            user.setDeviceId(deviceId);
            user.setUserId(userId);
            user.setSipCount(0);
            user.setDrinkCount(0);
        }

        userItemRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/getUser")
    public ResponseEntity getUser(
            @RequestParam("userId") final String userId
    ) {
        UserItem user = userItemRepository.findByUserId(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(
            @RequestParam("userId") final String userId
    ) {
        UserItem user = userItemRepository.findByUserId(userId);
        if(user != null){
            user.setDeviceId(null);
            user.setCurrentDrink(null);
            user.setNeedAssistance(false);
            user.setFindMyDrink(false);
            user.setMinTemp(null);
            user.setMaxTemp(null);
            user.setDrinkCount(0);
            user.setSipCount(0);
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
            user.setDrinkCount(user.getDrinkCount()+1);
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

    @PostMapping("/findMyDrink")
    public ResponseEntity findMyDrink(
            @RequestParam("userId") final String userId
    ) {

        UserItem user = userItemRepository.findByUserId(userId);

        if(user != null){
            if(user.isFindMyDrink()){
                user.setFindMyDrink(false);
            } else {
                user.setFindMyDrink(true);
            }
            userItemRepository.save(user);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/needAssistance")
    public ResponseEntity needAssistance(
            @RequestParam("userId") final String userId
    ) {

        UserItem user = userItemRepository.findByUserId(userId);

        if(user != null){
            user.setNeedAssistance(true);
            userItemRepository.save(user);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/incrementSipCount")
    public ResponseEntity incrementSipCount(
            @RequestParam("deviceId") final String deviceId
    ) {
        UserItem user = userItemRepository.findByDeviceId(deviceId);

        if(user != null){
            user.setSipCount(user.getSipCount() + 1);
            userItemRepository.save(user);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/resetSipCount")
    public ResponseEntity resetSipCount(
            @RequestParam("userId") final String userId
    ) {
        UserItem user = userItemRepository.findByUserId(userId);

        if(user != null){
            user.setSipCount(0);
            userItemRepository.save(user);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/getDrinks")
    public ResponseEntity getDrinks(
    ) {
        return new ResponseEntity<>(drinkItemRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/getDrinkByDeviceId")
    public ResponseEntity getDrinksByDeviceId(
            @RequestParam("deviceId") String deviceId
    ) {
        UserItem user = userItemRepository.findByDeviceId(deviceId);
        return new ResponseEntity<>(drinkItemRepository.findByDrinkName(user.getCurrentDrink()), HttpStatus.OK);
    }



    @GetMapping("/getDrinkInfo")
    public ResponseEntity getDrinkIdealTemp(
            @RequestParam("deviceId") String deviceId
    ) {
        String drinkInfo = null;
        UserItem user = userItemRepository.findByDeviceId(deviceId);
        if(user != null) {
            DrinkItem drinkItem = drinkItemRepository.findByDrinkName(user.getCurrentDrink());
            if(drinkItem != null) {
                String fmd;
                if(user.isFindMyDrink()) {
                    fmd = "1";
                } else {
                    fmd = "0";
                }

                String una;
                if(user.isNeedAssistance()) {
                    una = "1";
                } else {
                    una = "0";
                }

                drinkInfo = drinkItem.getMinTemp() + ":" + drinkItem.getMaxTemp() + ":" + fmd + ":" + una;

                user.setNeedAssistance(false);
                userItemRepository.save(user);
            }
        }

        return new ResponseEntity<>(drinkInfo, HttpStatus.OK);
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
