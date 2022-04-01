package com.appsdeveloperblog.app.ws.mobile_app_ws.io.repositories;

import com.appsdeveloperblog.app.ws.mobile_app_ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.mobile_app_ws.io.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity,Long> {
    List<AddressEntity> findAllByUserDetails(UserEntity userEntity);
    AddressEntity findByAddressId(String addressId);
}
