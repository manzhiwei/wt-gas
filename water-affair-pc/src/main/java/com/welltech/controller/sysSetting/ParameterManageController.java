/**
 * 
 */
package com.welltech.controller.sysSetting;

import java.util.List;
import java.util.Map;

import com.welltech.common.util.OperateCodeEnum;
import com.welltech.service.history.OperateHistoryServiceImpl;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.welltech.common.util.ReturnEntity;
import com.welltech.entity.WtParam;
import com.welltech.service.sysSetting.PageParamManageService;

/**
 * 页面参数配置
 * Created by Zhujia at 2017年7月28日 下午4:38:45
 */
@Controller
@RequestMapping("/syssetting/ppm")
public class ParameterManageController {
	
	@Autowired
	private PageParamManageService pageParamManageService;

	@Autowired
	OperateHistoryServiceImpl operateHistoryServiceImpl;
	
	/**
	 * 区域管理
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "page" })
    public String paramManage(Map<String,Object> map) {
		List<WtParam> param = pageParamManageService.findAllParams();
		map.put("params",param); 
        return "sysSetting/paramManage";
    }
	
	/**
	 * 修改是否显示
	 * @param map
	 * @param request
	 * @param wtParam
	 * @return
	 */
	@RequestMapping(value = { "changeStatus" })
	@ResponseBody
    public ReturnEntity<WtParam> addArea(Map<String,Object> map, HttpServletRequest request, WtParam wtParam) {
		ReturnEntity<WtParam> re = new ReturnEntity<>();
		com.welltech.security.entity.WtUser user = (com.welltech.security.entity.WtUser) request.getSession().getAttribute("user");
		wtParam = pageParamManageService.changeStatus(wtParam);
		operateHistoryServiceImpl.addOperate(OperateCodeEnum.OPERATE_14, user.getId(),"p"+wtParam.getId());
		re.setReturnData(wtParam);
		return re;
    }

	/**
	 * 根据id获取该条参数
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "getParamInfo" })
	@ResponseBody
    public ReturnEntity<WtParam> getParamInfo(String id) {
		ReturnEntity<WtParam> re = new ReturnEntity<>();
		WtParam param = pageParamManageService.getWtParamById(id);
		re.setReturnData(param);
		return re;
    }

	/**
	 * 保存更新
	 * @param wtParam
	 * @return
	 */
	@RequestMapping(value = { "saveWtParam" })
	@ResponseBody
    public ReturnEntity<WtParam> saveWtParam(WtParam wtParam, HttpServletRequest request) {
		ReturnEntity<WtParam> re = new ReturnEntity<>();
		com.welltech.security.entity.WtUser user = (com.welltech.security.entity.WtUser) request.getSession().getAttribute("user");
		pageParamManageService.saveWtParam(wtParam);
		operateHistoryServiceImpl.addOperate(OperateCodeEnum.OPERATE_14, user.getId(),"p"+wtParam.getId());
		re.setReturnData(wtParam);
		return re;
    }


}
