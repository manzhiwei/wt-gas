package com.welltech.dto;

/**
 * id测点
 * @author wangxin
 *
 */
public class PointDto {
	private Integer id;

	private String point;

	private Integer companyId;

	//mcuId
	private String gatewaySerial;

	public String getGatewaySerial() {
		return gatewaySerial;
	}

	public void setGatewaySerial(String gatewaySerial) {
		this.gatewaySerial = gatewaySerial;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}
	
}
