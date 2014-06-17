package com.ych.demo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ych.demo.model.LeaveModel;

/**
 * 请假实体持久化
 * @author ych
 *
 */
public class LeaveDao extends AbstrJdbcTemplate{

	public void saveLeave(LeaveModel leave){
		String insertSql = "insert into oa_leave(qjbh,qjr,qjsj) values(?,?,?)";
		List<Object> args = new ArrayList<Object>();
		args.add(leave.getQjbh());
		args.add(leave.getQjr());
		args.add(leave.getQjsj());
		jdbcTemplate.update(insertSql, args.toArray());
	}
	
	public Map<String,Object> selectLeaveById(String id){
		String selectSql = "select * from oa_leave where qjbh = ?";
		return this.jdbcTemplate.queryForList(selectSql,id).get(0);
	}
}
