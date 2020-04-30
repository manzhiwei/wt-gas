package com.welltech.controller;

import com.welltech.common.util.UserUtil;
import com.welltech.dto.PointDto;
import com.welltech.dto.WtDataDto;
import com.welltech.dto.WtDataVo;
import com.welltech.entity.WtDataRaw;
import com.welltech.entity.WtProtocolDay;
import com.welltech.security.entity.WtUser;
import com.welltech.service.AndroidUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AndroidDataController {


    @Autowired
    private AndroidUserService androidUserService;


    @RequestMapping(value = "/android/homeData",method = RequestMethod.GET )
    @ResponseBody
    public List<WtDataDto> getPointDto(String username){

        List<PointDto> pointDtos =null;

        pointDtos = androidUserService.findPointListByUserName(username);
        List<WtDataDto> wtDataRaws = new ArrayList<>();
        for (int i = 0; i < pointDtos.size(); i++) {
            WtDataRaw wtDataRaw = androidUserService.findLastData(pointDtos.get(i).getGatewaySerial());
            WtDataDto wtDataDto = new WtDataDto();
            wtDataDto.setPointName(pointDtos.get(i).getPoint());
            wtDataDto.setTime(wtDataRaw.getTime());
            wtDataDto.setP6(wtDataRaw.getP6());
            WtProtocolDay wtProtocolDay = androidUserService.findLatestWtProtocolDay(pointDtos.get(i).getGatewaySerial());
            wtDataDto.setP1( BigDecimal.valueOf(wtProtocolDay.getP11Avg() * wtProtocolDay.getP14Cou()/1000000).setScale(3,BigDecimal.ROUND_HALF_UP));
            wtDataRaws.add(wtDataDto);
        }

        return wtDataRaws;
    }

    @RequestMapping(value = "/android/alarmData",method = RequestMethod.GET)
    @ResponseBody
    public  List<WtDataDto> getAlarmData(String username){
        List<PointDto> pointDtos =null;
        pointDtos = androidUserService.findPointListByUserName(username);
        List<WtDataDto> wtDataRaws = new ArrayList<>();
        for (int i = 0; i < pointDtos.size(); i++) {
            WtDataRaw wtDataRaw = androidUserService.findLastData(pointDtos.get(i).getGatewaySerial());
            WtDataDto wtDataDto = new WtDataDto();
            wtDataDto.setPointName(pointDtos.get(i).getPoint());
            wtDataDto.setTime(wtDataRaw.getTime());
            wtDataDto.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(wtDataRaw.getTime()));
            wtDataDto.setP6(wtDataRaw.getP6());
            wtDataRaws.add(wtDataDto);
        }
        return wtDataRaws;
    }

    @RequestMapping(value = "/android/pointData",method = RequestMethod.GET )
    @ResponseBody
    public WtDataVo getPointDtos(String userName, int start, int count){

        WtDataVo wtDataVo = new WtDataVo();
        List<PointDto> pointDtos =null;
        pointDtos = androidUserService.findPointListByUserName(userName);
        List<WtDataDto> wtDataRaws = new ArrayList<>();
        for (int i = 0; i < pointDtos.size(); i++) {
            WtDataRaw wtDataRaw = androidUserService.findLastData(pointDtos.get(i).getGatewaySerial());
            WtDataDto wtDataDto = new WtDataDto();
            wtDataDto.setPointName(pointDtos.get(i).getPoint());
            wtDataDto.setCompanyName(userName);
            wtDataDto.setTime(wtDataRaw.getTime());
            wtDataDto.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(wtDataRaw.getTime()));
            wtDataDto.setP6(wtDataRaw.getP6());
            WtProtocolDay wtProtocolDay = androidUserService.findLatestWtProtocolDay(pointDtos.get(i).getGatewaySerial());
            wtDataDto.setP1( BigDecimal.valueOf(wtProtocolDay.getP11Avg() * wtProtocolDay.getP14Cou()/1000000).setScale(3,BigDecimal.ROUND_HALF_UP));
            wtDataRaws.add(wtDataDto);
        }
        //return start 0 count 10 total 10 lists  分页数据

        wtDataVo.setStart(start);
        wtDataVo.setCount(count);
        wtDataVo.setTotal(pointDtos.size());

        wtDataVo.setSubjects(wtDataRaws);
        return wtDataVo;
    }

}
