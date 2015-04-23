package com.nari.rdt.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface ICommonDao {
	public void save2DBByBatch(List list,String executeSql) throws DataAccessException;
	public List<Map> extractFromDB(String extractSQL);
	public List<Map> extractFromDB(String extractSQL,List<Map> list);
}
