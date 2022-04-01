package com.appsdeveloperblog.app.ws.mobile_app_ws.ui.model.response;

import java.util.List;

public class UserRes {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;

    private List<AddressRes> addresses;

    public List<AddressRes> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressRes> addresses) {
        this.addresses = addresses;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
