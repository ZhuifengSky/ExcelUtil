package com.main.dns.model;

public class DnsInfo {

	private String id;
	private String host;
	private String zone;
	private String type;
	private String view;
	private String record;
	private Integer ttl;
	private Integer status;
	private String view_description;
	private String targetIp;
	
	private String dealStatus;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTargetIp() {
		return targetIp;
	}
	public void setTargetIp(String targetIp) {
		this.targetIp = targetIp;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}
	public String getRecord() {
		return record;
	}
	public void setRecord(String record) {
		this.record = record;
	}
	public Integer getTtl() {
		return ttl;
	}
	public void setTtl(Integer ttl) {
		this.ttl = ttl;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getView_description() {
		return view_description;
	}
	public void setView_description(String view_description) {
		this.view_description = view_description;
	}
	public String getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}
	
	

}
