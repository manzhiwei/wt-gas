package com.welltech.dao.history;

import com.welltech.dto.WtGasVariablesDto;

import java.util.List;
import java.util.Map;

public interface WtGasVariablesDao {

    List<WtGasVariablesDto> listWtGasVariables(Map<String, Object> map);

}
