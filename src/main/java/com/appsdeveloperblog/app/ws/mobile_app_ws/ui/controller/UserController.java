package com.appsdeveloperblog.app.ws.mobile_app_ws.ui.controller;


import com.appsdeveloperblog.app.ws.mobile_app_ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.mobile_app_ws.service.UserService;
import com.appsdeveloperblog.app.ws.mobile_app_ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.mobile_app_ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.mobile_app_ws.ui.model.response.ErrorMessages;
import com.appsdeveloperblog.app.ws.mobile_app_ws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.mobile_app_ws.ui.model.response.RequestOperationStatus;
import com.appsdeveloperblog.app.ws.mobile_app_ws.ui.model.response.UserRes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path="/{id}")
    public UserRes getUser(@PathVariable String id){
        UserRes returnValue =new UserRes();

        UserDto userDto = userService.getUserById(id);

        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue ;
    }

    @PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE })
    public UserRes createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

        if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        UserRes returnValue= new UserRes();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);

        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }



    @PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE })
    public UserRes updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
        UserRes returnValue = new UserRes();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updateUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updateUser, returnValue);

        return returnValue;
    }

    @DeleteMapping(path = "/{id}",produces = { MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE })
    public OperationStatusModel deleteUser(@PathVariable String id){

        OperationStatusModel returnValue = new OperationStatusModel();

        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;

    }

}
