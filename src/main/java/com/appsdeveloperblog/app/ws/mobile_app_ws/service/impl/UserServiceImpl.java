package com.appsdeveloperblog.app.ws.mobile_app_ws.service.impl;

import com.appsdeveloperblog.app.ws.mobile_app_ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.mobile_app_ws.io.entity.PasswordResetTokenEntity;
import com.appsdeveloperblog.app.ws.mobile_app_ws.io.entity.RoleEntity;
import com.appsdeveloperblog.app.ws.mobile_app_ws.io.repositories.RoleRepository;
import com.appsdeveloperblog.app.ws.mobile_app_ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.mobile_app_ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.mobile_app_ws.io.repositories.PasswordResetTokenRepository;
import com.appsdeveloperblog.app.ws.mobile_app_ws.security.UserPrincipal;
import com.appsdeveloperblog.app.ws.mobile_app_ws.service.AddressService;
import com.appsdeveloperblog.app.ws.mobile_app_ws.service.EmailSenderService;
import com.appsdeveloperblog.app.ws.mobile_app_ws.service.UserService;
import com.appsdeveloperblog.app.ws.mobile_app_ws.shared.Utils;
import com.appsdeveloperblog.app.ws.mobile_app_ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.mobile_app_ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.mobile_app_ws.ui.model.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AddressService addressService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public UserDto createUser(UserDto user) {

        if(userRepository.findByEmail(user.getEmail()) != null) throw new IllegalStateException("Record already exit!");

        for(int i=0;i<user.getAddresses().size();i++)
        {
            AddressDto address = user.getAddresses().get(i);
            address.setUserDetails(user);
            address.setAddressId(utils.generateAddressId(30));
            user.getAddresses().set(i, address);
        }
//        BeanUtils.copyProperties(user,userEntity);
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);

        String publicUserId =utils.generateUserId(30);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setUserId(publicUserId);
        userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
        userEntity.setEmailVerificationStatus(false);



        Collection<RoleEntity> roleEntities = new HashSet<>();
        for(String role: user.getRoles()){
            RoleEntity roleEntity = roleRepository.findByName(role);
            if(roleEntity != null){
                roleEntities.add(roleEntity);
            }

        }
        userEntity.setRoles(roleEntities);
        
        UserEntity savedUserDetails=userRepository.save(userEntity);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("aungchan391@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8080/verification-service/index.html?token="+userEntity.getEmailVerificationToken());

        emailSenderService.sendEmail(mailMessage);

//        UserDto returnValue = new UserDto();
//
//        BeanUtils.copyProperties(savedUserDetails,returnValue);
        UserDto returnValue = modelMapper.map(savedUserDetails, UserDto.class);

        

        return returnValue;
    }

    @Override
    public UserDto getUser(String email)  {

        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email);

//        List<AddressDto> addressDto = addressService.getAddresses(userEntity.getUserId());

        UserDto returnValue = new UserDto();

       BeanUtils.copyProperties(userEntity,returnValue);
//        returnValue.setAddresses(addressDto);

        return returnValue;


    }

    @Override
    public UserDto getUserById(String userId){

        UserDto returnValue = new UserDto();
        UserEntity userEntity= userRepository.findByUserId(userId);
        if(userEntity == null) throw new UsernameNotFoundException("User with Id: "+ userId +" not found");
        List<AddressDto> addressDto = addressService.getAddresses(userEntity.getUserId());
        BeanUtils.copyProperties(userEntity,returnValue);
        returnValue.setAddresses(addressDto);

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

    public List<UserDto> getUsers(int page, int limit){
        List<UserDto> returnValue = new ArrayList<>();

        if(page>0) page=page-1;

        Pageable pageableRequest = PageRequest.of(page,limit);

        Page<UserEntity> usersPage =userRepository.findAll(pageableRequest);

        List<UserEntity> users =usersPage.getContent();

        BeanUtils.copyProperties(users,returnValue);

        for (UserEntity userEntity : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            returnValue.add(userDto);
        }

        return returnValue;

    }

    @Override
    public boolean requestPasswordReset(String email) {

        boolean returnValue = false;

        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            return returnValue;
        }

        String token = new Utils().generatePasswordResetToken(userEntity.getUserId());

        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUserDetails(userEntity);
        passwordResetTokenRepository.save(passwordResetTokenEntity);

//        returnValue = new AmazonSES().sendPasswordResetRequest(
//                userEntity.getFirstName(),
//                userEntity.getEmail(),
//                token);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userEntity.getEmail());
        mailMessage.setSubject("A request to reset your password ");
        mailMessage.setFrom("aungchan391@gmail.com");
        mailMessage.setText( "Hi,"+userEntity.getFirstName()+"! "
                + "Someone has requested to reset your password with our project. If it were not you, please ignore it."
                + " otherwise please open the link below in your browser window to set a new password:"
                +"http://localhost:8080/verification-service/passwordreset.html?token="+passwordResetTokenEntity.getToken());

        emailSenderService.sendEmail(mailMessage);

        returnValue=true;

        return returnValue;
    }

    @Override
    public boolean resetPassword(String token, String password) {
        boolean returnValue = false;

        if( Utils.hasTokenExpired(token) )
        {
            return returnValue;
        }

        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);

        if (passwordResetTokenEntity == null) {
            return returnValue;
        }

        // Prepare new password
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        // Update User password in database
        UserEntity userEntity = passwordResetTokenEntity.getUserDetails();
        userEntity.setEncryptedPassword(encodedPassword);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        // Verify if password was saved successfully
        if (savedUserEntity != null && savedUserEntity.getEncryptedPassword().equalsIgnoreCase(encodedPassword)) {
            returnValue = true;
        }

        // Remove Password Reset token from database
        passwordResetTokenRepository.delete(passwordResetTokenEntity);

        return returnValue;
    }

    @Override
    public boolean verifyEmailToken(String token) {
        boolean returnValue = false;

        // Find user by token
        UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);

        if (userEntity != null) {
            boolean hastokenExpired = Utils.hasTokenExpired(token);
            if (!hastokenExpired) {
                userEntity.setEmailVerificationToken(null);
                userEntity.setEmailVerificationStatus(Boolean.TRUE);
                userRepository.save(userEntity);
                returnValue = true;
            }
        }

        return returnValue;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null) throw new UsernameNotFoundException(email);

        return new UserPrincipal(userEntity);


//        return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(),new ArrayList<>());
    }
}
