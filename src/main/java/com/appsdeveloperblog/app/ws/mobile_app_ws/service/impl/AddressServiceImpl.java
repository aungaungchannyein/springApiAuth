package com.appsdeveloperblog.app.ws.mobile_app_ws.service.impl;

import com.appsdeveloperblog.app.ws.mobile_app_ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.mobile_app_ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.mobile_app_ws.io.repositories.AddressRepository;
import com.appsdeveloperblog.app.ws.mobile_app_ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.mobile_app_ws.service.AddressService;
import com.appsdeveloperblog.app.ws.mobile_app_ws.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<AddressDto> getAddresses(String userId) {

        List<AddressDto> returnValue = new ArrayList<>();


        UserEntity userEntity=userRepository.findByUserId(userId);
        if(userEntity == null) return returnValue;
//        List<AddressEntity> addresses=userEntity.getAddresses();
//
            ModelMapper modelMapper = new ModelMapper();
//        Type listType = new TypeToken<List<AddressDto>>() {}.getType();
//        returnValue = modelMapper.map(addresses, listType);


        Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
        for(AddressEntity addressEntity:addresses)
        {
            returnValue.add( modelMapper.map(addressEntity, AddressDto.class) );
        }

        return returnValue;
    }

    @Override
    public AddressDto getAddress(String addressId) {
        AddressDto returnValue = null;

        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);


        if(addressEntity!=null)
        {
            returnValue = new ModelMapper().map(addressEntity, AddressDto.class);
        }

        return returnValue;
    }

}
