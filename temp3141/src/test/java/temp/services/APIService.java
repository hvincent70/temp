package com.sprint.iice_tests.lib.dal.services;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.sprint.iice_tests.definitions.API.API_Exp1.IL_Reusable;
import com.sprint.iice_tests.definitions.API.API_Exp2.CL_Reusable;
import com.sprint.iice_tests.lib.dao.vo.MapJsonFileToObject;

public class APIService {
	
	IL_Reusable il = IL_Reusable.getInstance();
	CL_Reusable cl = CL_Reusable.getInstance();
	
	public static ConcurrentMap<Long, IL_Reusable> ilApiMap;
	public static ConcurrentMap<Long, CL_Reusable> clApiMap;
	public static ConcurrentMap<Long, MapJsonFileToObject> dataMap;
	
	public APIService() {
		APIService.dataMap = new ConcurrentHashMap<Long, MapJsonFileToObject>();
	}
	
	public static ConcurrentMap<Long, IL_Reusable> getILMap(){
		return ilApiMap;
	}
	
	public static ConcurrentMap<Long, CL_Reusable> getCLMap(){
		return clApiMap;
	}

	public static ConcurrentMap<Long, MapJsonFileToObject> getDataMap(){
		return dataMap;
	}
}
