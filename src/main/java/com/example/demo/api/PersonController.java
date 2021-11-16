package com.example.demo.api;

import com.example.demo.Dao.Person;
import com.example.demo.Dao.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PersonController {
    @Autowired
    private PersonRepo repo;

    /*Sample GET*/
    /*http://localhost:8080/feature?email=wololo@mail.com&featureName=sendpicture*/
    @GetMapping("/feature")
    public Map<String,String> getUserAccess(@RequestParam Map<String,String> requestParams){
        Map<String,String> userAccessResponse = new HashMap<>();

        String urlEmail=requestParams.get("email");
        String urlFeatureName=requestParams.get("featureName");

        Person searchP=repo.findByEmail(urlEmail);
        if(ObjectUtils.isEmpty(searchP)){
            //email not found
            return userAccessResponse;
        }

        int permMask=searchP.setPermMask(urlFeatureName);
        if(permMask==-1){
            //invalid urlFeatureName
            return userAccessResponse;
        }

        userAccessResponse.put("canAccess", searchP.getPermIsAllowed(permMask).toString());
        return userAccessResponse;
    }

    /*http://localhost:8080/feature*/
    @PostMapping("/feature")
    public HttpStatus setUserAccess(@RequestParam Map<String,String> requestParams) {
        Map<String, String> response = new HashMap<>();

        String urlFeatureName = requestParams.get("featureName");
        String urlEmail = requestParams.get("email");
        String urlEnable = requestParams.get("enable");
        Boolean urlBoolEnable=Boolean.valueOf(urlEnable);

        Person searchP=repo.findByEmail(urlEmail);
        if(ObjectUtils.isEmpty(searchP)){
            return HttpStatus.NOT_MODIFIED;
        }
        int permMask=searchP.setPermMask(urlFeatureName);
        if(permMask==-1){
            return HttpStatus.NOT_MODIFIED;
        }

        if((searchP.getPermIsAllowed(permMask) && urlBoolEnable) || (!searchP.getPermIsAllowed(permMask) && !urlBoolEnable)){
            /*already enabled, to enable || already disabled, to disable*/
            return HttpStatus.NOT_MODIFIED;
        }else if(searchP.getPermIsAllowed(permMask) && urlBoolEnable){
            /*already disabled, to enable*/
            searchP.setPermissionTypeMask(searchP.getPermissionTypeMask()+permMask);
        }else if(searchP.getPermIsAllowed(permMask) && !urlBoolEnable){
            /*already enabled, to disable*/
            searchP.setPermissionTypeMask(searchP.getPermissionTypeMask()-permMask);
        }

        try {
            repo.save(searchP);
        } catch(Exception e){
            System.out.println(e);
            return HttpStatus.NOT_MODIFIED;
        }

        return HttpStatus.OK;
    }
}