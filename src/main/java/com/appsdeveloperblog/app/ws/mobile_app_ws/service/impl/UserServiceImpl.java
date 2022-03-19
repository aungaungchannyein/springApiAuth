package com.appsdeveloperblog.app.ws.mobile_app_ws.service.impl;

import com.appsdeveloperblog.app.ws.mobile_app_ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.mobile_app_ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.mobile_app_ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.mobile_app_ws.service.UserService;
import com.appsdeveloperblog.app.ws.mobile_app_ws.shared.Utils;
import com.appsdeveloperblog.app.ws.mobile_app_ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.mobile_app_ws.ui.model.response.ErrorMessage;
import com.appsdeveloperblog.app.ws.mobile_app_ws.ui.model.response.ErrorMessages;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto user) {

        if(userRepository.findByEmail(user.getEmail()) != null) throw new IllegalStateException("Record already exit!");

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user,userEntity);

        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setUserId(utils.generateUserId(30));
        
        UserEntity savedUserDetails=userRepository.save(userEntity);

        UserDto returnValue = new UserDto();

        BeanUtils.copyProperties(savedUserDetails,returnValue);
        

        return returnValue;
    }

    @Override
    public UserDto getUser(String email)  {

        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email);

        UserDto returnValue = new UserDto();

        BeanUtils.copyProperties(userEntity,returnValue);

        return returnValue;


    }

    @Override
    public UserDto getUserById(String userId){

        UserDto returnValue = new UserDto();
        UserEntity userEntity= userRepository.findByUserId(userId);
        if(userEntity == null) throw new UsernameNotFoundException("User with Id: "+ userId +" not found");

        BeanUtils.copyProperties(userEntity,returnValue);

        return returnValue;


    }

    @Override
    public UserDto updateUser(String userId, UserDto user) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity= userRepository.findByUserId(userId);
        if(userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());

        UserEntity updatedUserDetails =userRepository.save(userEntity);

        BeanUtils.copyProperties(updatedUserDetails,returnValue);

        return returnValue;

    }

    @Override
    public void deleteUser(String userId){

        UserEntity userEntity= userRepository.findByUserId(userId);
        if(userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userRepository.delete(userEntity);


    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null) throw new UsernameNotFoundException(email);


        return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(),new ArrayList<>());
    }
}
