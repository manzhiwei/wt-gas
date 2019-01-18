package com.welltech.dao.history;

import com.welltech.dto.WtControlDto;
import com.welltech.dto.WtControlLimitDto;
import com.welltech.entity.WtControlLimit;

import java.util.List;
import java.util.Map;

public interface WtControlLimitDao {
    List<WtControlLimitDto> findPageWtControlLimit(Map<String, Object> map);
}
