package com.nerdbar.repo;

import com.nerdbar.model.UserItem;
import org.springframework.data.repository.CrudRepository;

public interface UserItemRepository extends CrudRepository<UserItem, Long> {

    UserItem findByUserId(String userId);

    UserItem findByDeviceId(String deviceId);

}