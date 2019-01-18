package com.welltech.dao.history;


import com.welltech.dto.WtControlAirDto;

import java.util.List;
import java.util.Map;

public interface WtControlAirDao {

    /**
     * 分页查询分析仪历史数据
     * @param map
     * @return
     */
    List<WtControlAirDto> findPageWtControlAir(Map<String, Object> map);
}
