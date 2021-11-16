package com.example.demo.Dao;

import org.springframework.http.HttpStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int permissionTypeMask;
    private String email;

    public Person(){

    }

    public Person(int permissionTypeMask, String email) {
        this.permissionTypeMask = permissionTypeMask;
        this.email = email;
    }

    public void setPermissionTypeMask(int permissionTypeMask) {
        this.permissionTypeMask = permissionTypeMask;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public int getPermissionTypeMask() {
        return permissionTypeMask;
    }

    public String getEmail() {
        return email;
    }

    public int setPermMask(String feature){
        int permMask=0;
        switch(feature){
            /*should put this in a table if have more time*/
            case "attachImage":
                permMask = 1;
                break;
            case "attachCat":
                permMask = 2;
                break;
            case "attachDog":
                permMask = 4;
                break;
            case "attachKoala":
                permMask = 8;
                break;
            default:
                return -1;
        }
        return permMask;
    }

    public Boolean getPermIsAllowed(int userPerm){
        if((permissionTypeMask&userPerm)>0){
            return true;
        }else{
            return false;
        }
    }
}
