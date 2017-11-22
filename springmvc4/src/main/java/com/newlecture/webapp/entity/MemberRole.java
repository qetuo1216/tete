package com.newlecture.webapp.entity;

public class MemberRole extends Member {
	
	private String memberId;
	private String roleId;
	private int defaultRole;
	
	
	public MemberRole() {
		super();
	}
	
	
	public MemberRole(String memberId, String roleId, int defaultRole) {
		super();
		this.memberId = memberId;
		this.roleId = roleId;
		this.defaultRole = defaultRole;
	}


	public String getMemberId() {
		return memberId;
	}


	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}


	public String getRoleId() {
		return roleId;
	}


	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}


	public int getDefaultRole() {
		return defaultRole;
	}


	public void setDefaultRole(int defaultRole) {
		this.defaultRole = defaultRole;
	}
	
	
	

}
