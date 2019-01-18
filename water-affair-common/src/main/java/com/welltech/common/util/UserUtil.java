package com.welltech.common.util;

import com.welltech.security.entity.WtUser;

public class UserUtil {

    public static WtUser getUser(){
        Object obj = SpringWebUtils.getSession().getAttribute("user");
        if(obj instanceof WtUser){
            return (WtUser) obj;
        }
        return null;
    }

    public static Boolean isAdmin(){

        WtUser wtUser = getUser();
        if (wtUser != null && wtUser.getUsername().equals("admin")){
            return true;
        }
        return false;
    }

    public static Integer getCompanyIdByUser(){
        if(getUser() !=null){
            return getUser().getCompanyId();
        }
        return -1;
    }
}
