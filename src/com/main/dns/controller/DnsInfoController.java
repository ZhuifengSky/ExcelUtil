package com.main.dns.controller;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;























import com.main.dns.bean.DnsResponseBean;
import com.main.dns.bean.DnsUpBean;
import com.main.dns.bean.DnsUpResponseBean;
import com.main.dns.bean.RecordBean;
import com.main.dns.model.DnsInfo;
import com.main.dns.service.IDnsInfoService;
import com.main.dns.util.ExcelUtils;
import com.main.dns.util.ImportExcelXSSFUtil;
import com.main.dns.util.JsonUtil;
import com.main.dns.util.NullJudgeUtil;


@Controller
@RequestMapping("/dnsInfo")
public class DnsInfoController {
	
	@Autowired
	private IDnsInfoService dnsInfoService;
	
	
	/**
	 * 根据Ip获取hostInfo
	 * @param request
	 * @param response
	 * @param ip
	 * @return
	 */
	@RequestMapping(value ="/getDnsInfoByIp", produces ="application/json;charset=UTF-8", method=RequestMethod.GET)
	@ResponseBody
    public String getDnsInfoByIp(HttpServletRequest request,HttpServletResponse response,String ip) {
		try {
			DnsResponseBean responseBean = dnsInfoService.getDnsInfoByIp(ip);
			return JsonUtil.toJson(responseBean, "yyyy-MM-dd HH:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
    }
	
	

    /**
     * 导出Excel
     * @param model
     * @param request
     * @param response
     * @param ip
     * @return
     */
    @RequestMapping(value = "exportExcel")
    @ResponseBody
    public ModelAndView exportExcel(ModelMap model,HttpServletRequest request,HttpServletResponse response,String ip) {
		try {
			DnsResponseBean responseBean = dnsInfoService.getDnsInfoByIp(ip);
	        String[] cellHead = {"序号","id","host","zone","type","view","record（源IP地址）","ttl","status","view_description","目标Ip地址"};
	        ExcelUtils excelUtils = new ExcelUtils();
	        excelUtils.export(model, request, response,responseBean.getData(), cellHead);	        
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
    }
    
    
    @RequestMapping(value = "/UpDnsInfo",produces ="application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
	public String readExcel(
			MultipartFile file, Model model,String remark) throws Exception {
    	DnsResponseBean responseBean = new DnsResponseBean();
			if (!file.isEmpty()) {
				String suffix = null;
				int index = file.getOriginalFilename().lastIndexOf(".");
				if (index != -1 && index != file.getOriginalFilename().length() - 1) {
					suffix = file.getOriginalFilename().substring(index + 1);					
					if (suffix == null || "xls|xlsx".indexOf(suffix) == -1) {
						responseBean.setMsg("请上传Excel文件");
						responseBean.setCode("1001"); // 1001为 文件格式不正确
					}else{						
						InputStream is = file.getInputStream();				
						List<DnsInfo> data = ImportExcelXSSFUtil.getExcelStringList(is);
						if (data!=null && data.size()>0) {
							for (DnsInfo dnsInfo : data) {
								if (checkDnsUpBean(dnsInfo)) {
									DnsUpBean dnsUpBean = generalUpBean(dnsInfo, remark);									
									DnsResponseBean bean = dnsInfoService.upDnsInfo(dnsUpBean);
									if (bean!=null) {
										if (bean.getSuccess().equals("true") && bean.getCode().equals("000")) {
											dnsInfo.setDealStatus("成功");
										}else{
											dnsInfo.setDealStatus("失败"+bean.getMsg());
										}	
									}else{
										dnsInfo.setDealStatus("失败");
									}
									
									if (dnsInfo.getDealStatus().equals("失败")) {
										Thread.sleep(2000);
										DnsResponseBean bean2 = dnsInfoService.upDnsInfo(dnsUpBean);
										if (bean2!=null) {
											if (bean2.getSuccess().equals("true") && bean2.getCode().equals("000")) {
												dnsInfo.setDealStatus("成功");
											}else{
												dnsInfo.setDealStatus("失败"+bean.getMsg());
											}	
										}else{
											dnsInfo.setDealStatus("失败");
										}
									}
									
								}else{
									dnsInfo.setDealStatus("未处理-信息不完整");
								}
							}
							responseBean.setData(data);
							responseBean.setMsg("读取成功!");
							responseBean.setCode("1000"); // 1000上传成功	
						}else{
							responseBean.setMsg("未找到可更新数据!");
							responseBean.setCode("1012"); // 1000上传成功	
						}						
															
					}
					return JsonUtil.toJson(responseBean, "yyyy-MM-dd HH:mm:ss");
				}				
			}else{
				responseBean.setMsg("文件为空!");
				responseBean.setCode("1002"); // 1000上传成功	
				return JsonUtil.toJson(responseBean, "yyyy-MM-dd HH:mm:ss");
			} 
		return null;

	}
    
    
    private DnsUpBean generalUpBean(DnsInfo dnsInfo,String remark) throws IllegalAccessException, InvocationTargetException{
    	DnsUpBean dnsUpBean = new DnsUpBean();
    	BeanUtils.copyProperties(dnsUpBean, dnsInfo);
    	List<RecordBean> records = new ArrayList<RecordBean>();
    	RecordBean record = new RecordBean();
    	record.setId(dnsInfo.getId().trim());
    	record.setView(dnsInfo.getView().trim());
    	record.setType(dnsInfo.getType().trim());
    	record.setTtl(dnsInfo.getTtl().toString().trim());
    	record.setRecord(dnsInfo.getTargetIp().trim());
    	record.setStatus(dnsInfo.getStatus().toString().trim());
    	record.setRemark(remark);
    	records.add(record);
    	dnsUpBean.setRecord(dnsInfo.getTargetIp().trim());
    	dnsUpBean.setOwner("xx");
    	dnsUpBean.setSysop("xx");
    	dnsUpBean.setRecords(records);
		return dnsUpBean;
    }
    
    private boolean checkDnsUpBean(DnsInfo dnsUpBean){
		if (NullJudgeUtil.isNotNull(dnsUpBean.getZone()) && NullJudgeUtil.isNotNull(dnsUpBean.getHost()) 
				&& NullJudgeUtil.isNotNull(dnsUpBean.getId()) && NullJudgeUtil.isNotNull(dnsUpBean.getTargetIp())
				&& NullJudgeUtil.isNotNull(dnsUpBean.getView()) && NullJudgeUtil.isNotNull(dnsUpBean.getType())
				) {
			return true;
		}
		
		return false;
		
	}
}
