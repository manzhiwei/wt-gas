package com.welltech.service.statistics;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.welltech.dao.indexData.IndexDataDao;
import com.welltech.dao.statistics.WaterStatisticDao;

@Service
public class WaterStatisticService {

	@Autowired
	private WaterStatisticDao waterStatisticDao;
	
	@Autowired
	private IndexDataDao indexDataDao;
	
	public Map<String, Object> getWaterStatistic(List<Integer> companyIds, List<Integer> stationIds, String param,
			Date startTime, Date endTime, String typeCode){

		Map<String, Object> result = new LinkedHashMap<>();
		if(param==null||!param.matches("^[p][1-9]?\\d$")){
			param = "p1";
		}

		List<String> levels = indexDataDao.findLevelNameAsc(typeCode);
		for( String level: levels ){
			result.put(level, 0);
		}

		List<Map<String, Object>> queryResult = waterStatisticDao.findWaterStatistic(companyIds, stationIds, param, startTime, endTime, typeCode);


		List<Map<String, Object>> waterSampleResult=new ArrayList<>();
		//进行水样判断处理(相邻数据不同则算为一条水样)
		double neighbourValue=-88888888;
		String neighbourMcu="88888888";
		if( queryResult != null){
			for(Map<String, Object> map: queryResult){
				try {
					String stringValue=map.get("val").toString();
					String mcu=map.get("mcu").toString();

					double currentValue=Double.valueOf(stringValue);
					if(neighbourValue!=currentValue&&
							(neighbourMcu=="88888888"||
							neighbourMcu.equals(mcu)
							)	){
						int currentCount=(Integer) result.get((String)map.get("level"));
						result.put((String)map.get("level"), ++currentCount);
					}
					neighbourValue=currentValue;
					neighbourMcu=mcu;
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}
	
}
