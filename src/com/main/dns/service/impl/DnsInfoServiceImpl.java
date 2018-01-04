package com.main.dns.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;

import net.minidev.json.JSONArray;

import org.springframework.stereotype.Service;

import com.main.dns.bean.DnsResponseBean;
import com.main.dns.bean.DnsUpBean;
import com.main.dns.service.IDnsInfoService;
import com.main.dns.util.HttpClientUtil;
import com.main.dns.util.JsonUtil;
import com.main.dns.util.NullJudgeUtil;
import com.main.dns.util.SendRequestUtil;
import com.main.dns.util.Utility;

@Service(value="dnsInfoService")
public class DnsInfoServiceImpl implements IDnsInfoService{

	@Override
	public DnsResponseBean getDnsInfoByIp(String ip) throws Exception {
		String url = Utility.DNS_INFO_URL+"appId="+Utility.appId
				+"&appSecret="+Utility.appSecret+"&ip="+ip+"&returnType=brief";
		String result = HttpClientUtil.httpGet(url , "utf-8");
		if (result!=null) {
			DnsResponseBean responseBean = (DnsResponseBean) JsonUtil.fromObj(result, DnsResponseBean.class,"yyyy-MM-dd HH:mm:ss");
		    return responseBean;
		}
		return null;
	}

	@Override
	public DnsResponseBean upDnsInfo(DnsUpBean dnsUpBean) throws Exception {
		String records=JSONArray.toJSONString(dnsUpBean.getRecords()).toString();
		String noUserurl = Utility.DNS_UP_URL;
		String param="appId="+Utility.appId
				+"&appSecret="+Utility.appSecret+"&zone="+dnsUpBean.getZone()
				+ "&host="+dnsUpBean.getHost()+"&records="+records;
		/*String url = Utility.DNS_UP_URL;
		String param="appId="+Utility.appId
				+"&appSecret="+Utility.appSecret+"&zone="+dnsUpBean.getZone()
				+ "&host="+dnsUpBean.getHost()+"&owner="+dnsUpBean.getOwner()
				+ "&sysop="+dnsUpBean.getSysop()+"&records="+records;
		String result = SendRequestUtil.sendGet(url, param);*/
		//String result = SendRequestUtil.sendGet(noUserurl,param);
		HashMap<String, String> parasMap = new HashMap<>();
		parasMap.put("appId", Utility.appId);
		parasMap.put("appSecret", Utility.appSecret);
		parasMap.put("zone", dnsUpBean.getZone());
		parasMap.put("host", dnsUpBean.getHost());
		String flag = Utility.IS_UP_OWER_SYSOP;
		if (flag.equals("yes")) {
			parasMap.put("owner", dnsUpBean.getOwner());
			parasMap.put("sysop", dnsUpBean.getSysop());
		}		
		parasMap.put("records",records);
		String result = HttpClientUtil.httpPost(noUserurl, parasMap, "utf-8");
		/*String url = URLEncoder.encode(noUserurl, "utf-8");
		String result = HttpClientUtil.httpGet(url , "utf-8");*/
		if (result!=null) {
			DnsResponseBean responseBean = (DnsResponseBean) JsonUtil.fromObj(result, DnsResponseBean.class,"yyyy-MM-dd HH:mm:ss");
		    return responseBean;
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(Utility.DNS_INFO_URL);
	}
}
