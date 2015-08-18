package com.genie.pojo;

import java.io.Serializable;


public class LeaderboardWrapper implements Comparable<LeaderboardWrapper>, Serializable {
	
	private static final long serialVersionUID = 1627467274226136226L;
	
	private String username;
	private Long point;
	private Integer standing;
	
	public LeaderboardWrapper() {
	}
	
	public LeaderboardWrapper(String username, Long points) {
		this.username = username;
		this.point = points;
	}
	
	@Override
	public int compareTo(LeaderboardWrapper o) {
		return o.point.compareTo(this.point);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public Integer getStanding() {
		return standing;
	}

	public void setStanding(Integer standing) {
		this.standing = standing;
	}
}
