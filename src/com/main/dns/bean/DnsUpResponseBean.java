package com.main.dns.bean;

import java.util.List;

import com.main.dns.model.DnsInfo;

public class DnsUpResponseBean {

	private String success;
	private List<DnsUpBean> data;
	private String code;
	private String msg;
	private String domainId;
	
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public List<DnsUpBean> getData() {
		return data;
	}
	public void setData(List<DnsUpBean> data) {
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getDomainId() {
		return domainId;
	}
	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}
	
	
}
