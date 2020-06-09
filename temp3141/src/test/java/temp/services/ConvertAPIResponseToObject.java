package com.sprint.iice_tests.lib.dao.vo;

import com.sprint.iice_tests.lib.dao.vo.API_InterfacesForResponseData.HistoricChargesUsage;
import com.sprint.iice_tests.lib.dao.vo.API_ResponseObjects.CustomerServiceID_vo;
import com.sprint.iice_tests.lib.dao.vo.API_ResponseObjects.HistoricChargesUsage_vo;

public class ConvertAPIResponseToObject {
	private CustomerServiceID_vo customerServiceID_vo;
	private HistoricChargesUsage_vo historicChargesUsage_vo;

	public CustomerServiceID_vo getCustomerServiceId() {
		return customerServiceID_vo;
	}

	public HistoricChargesUsage getHistoricChargesUsage() {
		return historicChargesUsage_vo;
	}

	public ConvertAPIResponseToObject(CustomerServiceID_vo customerServiceID_vo) {
		this.customerServiceID_vo = customerServiceID_vo;
	}

	public ConvertAPIResponseToObject(HistoricChargesUsage_vo historicChargesUsage_vo) {
		this.historicChargesUsage_vo = historicChargesUsage_vo;
	}
}
