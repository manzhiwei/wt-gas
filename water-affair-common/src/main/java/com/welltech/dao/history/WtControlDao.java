package com.welltech.dao.history;

import com.welltech.dto.WtControlDto;

import java.util.List;
import java.util.Map;

public interface WtControlDao {

    /**
     * 分页查询分析仪历史数据
     * @param map
     * @return
     */
    List<WtControlDto> findPageWtControl(Map<String, Object> map);
}
