package com.welltech.service;

import com.welltech.common.util.MD5Util;
import com.welltech.common.util.UserUtil;
import com.welltech.dao.UiElementDao;
import com.welltech.dao.WtCompanyDao;
import com.welltech.dao.WtDataRawDao;
import com.welltech.dao.history.WtProtocolDayDao;
import com.welltech.dto.PointDto;
import com.welltech.entity.WtDataRaw;
import com.welltech.entity.WtProtocolDay;
import com.welltech.security.dao.LoginUserDao;
import com.welltech.security.entity.WtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AndroidUserService {
    @Autowired
    private LoginUserDao loginUserDao;

    @Autowired
    private WtCompanyDao wtCompanyDao;

    @Autowired
    private UiElementDao uiElementDao;

    @Autowired
    private WtDataRawDao wtDataRawDao;

    @Autowired
    private WtProtocolDayDao wtProtocolDayDao;

    public WtUser getUser(String name){
        return loginUserDao.findByUserName(name);
    }

    public boolean isUser(String name,String password){

        WtUser user = getUser(name);

        System.out.println(MD5Util.encode(password));
        System.out.println(user.getPassword());
        if(MD5Util.encode(password).equals(user.getPassword())){
            return true;
        }else{
            return false;
        }
    }

    public List<PointDto> findPointListByUserName(String username) {
        List<PointDto> result  = new ArrayList<>();
        WtUser user = getUser(username);
        if(wtCompanyDao.findCompanyById(user.getCompanyId()).getId() !=
                wtCompanyDao.findCompanyById(user.getCompanyId()).getParentId()){
            result = uiElementDao.findAllPointOnCompany(user.getCompanyId());
        }else {
            result = uiElementDao.findAllPointOnParentId(user.getCompanyId());
        }
        return result;
    }

    public WtDataRaw findLastData(String gatewaySerial) {
        return wtDataRawDao.findLatestWtDataRaw(gatewaySerial);
    }

    public WtProtocolDay findLatestWtProtocolDay(String gatewaySerial){
        return wtProtocolDayDao.findLatestWtProtocolDay(gatewaySerial);
    }
}
