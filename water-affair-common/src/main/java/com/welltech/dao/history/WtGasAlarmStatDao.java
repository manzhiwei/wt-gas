package com.welltech.dao.history;

import com.welltech.dto.WtGasAlarmStatDto;

import java.util.List;
import java.util.Map;

public interface WtGasAlarmStatDao {

    List<WtGasAlarmStatDto> findPageWtGasAlarmStat(Map<String, Object> map);
}