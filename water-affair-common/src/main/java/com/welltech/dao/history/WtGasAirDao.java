package com.welltech.dao.history;

import com.welltech.dto.WtGasAirDto;

import java.util.List;
import java.util.Map;

public interface WtGasAirDao {

    List<WtGasAirDto> listWtGasAir(Map<String, Object> map);
}
