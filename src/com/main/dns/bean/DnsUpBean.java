package com.main.dns.bean;

import java.util.List;

public class DnsUpBean {

	private String id;
	private String zone;
	private String host;
	private String type;
	private String view;
	private String record;
	private Integer ttl;
	private Integer status;
	
	private String owner;
	private String sysop;
	private List<RecordBean> records;
	
	
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
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getSysop() {
		return sysop;
	}
	public void setSysop(String sysop) {
		this.sysop = sysop;
	}
	public List<RecordBean> getRecords() {
		return records;
	}
	public void setRecords(List<RecordBean> records) {
		this.records = records;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	
	
	

}
