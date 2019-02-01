package com.welltech.dao.history;

import com.welltech.dto.WtControlAirDto;
import com.welltech.dto.WtProtocolDayDto;

import java.util.List;
import java.util.Map;

public interface  WtProtocolDayDao {

    /**
     * 分页查询分析仪历史数据日数据
     * @param map
     * @return
     */
    List<WtProtocolDayDto> findPageWtProtocolDay(Map<String, Object> map);
    /**
    * @Author  Man Zhiwei
    * @Comment 不进行分页拦截
    * @Param
    * @Date        2019-01-31 10:51
    */
    List<WtProtocolDayDto> listWtProtocolDay(Map<String, Object> map);

    /**
     * 分页查询分析仪历史数据时数据
     * @param map
     * @return
     */
    List<WtProtocolDayDto> findPageWtProtocolHour(Map<String, Object> map);

    /**
    * @Author  Man Zhiwei
    * @Comment 不进行分页拦截
    * @Param
    * @Date        2019-01-31 10:52
    */
    List<WtProtocolDayDto> listWtProtocolHour(Map<String, Object> map);
    /**
     * 分页查询分析仪历史数据分数据
     * @param map
     * @return
     */
    List<WtProtocolDayDto> findPageWtProtocolMinuter(Map<String, Object> map);

    /**
    * @Author  Man Zhiwei
    * @Comment 不进行分页拦截
    * @Param
    * @Date        2019-01-31 10:52
    */
    List<WtProtocolDayDto> listWtProtocolMinuter(Map<String, Object> map);
}
