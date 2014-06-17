package com.ych.demo.dao;

import org.springframework.jdbc.core.JdbcTemplate;

public class AbstrJdbcTemplate {

	protected JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
