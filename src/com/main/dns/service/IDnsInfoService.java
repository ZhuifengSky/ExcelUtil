package com.main.dns.service;

import com.main.dns.bean.DnsResponseBean;
import com.main.dns.bean.DnsUpBean;

public interface IDnsInfoService {

	/**
	 * 根据IP查询Host信息
	 * @param ip
	 * @return
	 */
	public DnsResponseBean getDnsInfoByIp(String ip) throws Exception;
	
	
	/**
	 * 更新Dns信息
	 * @param dnsUpBean
	 * @return
	 */
	public DnsResponseBean upDnsInfo(DnsUpBean dnsUpBean) throws Exception ;
}
