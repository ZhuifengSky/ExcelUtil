package com.main.dns.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.main.dns.model.DnsInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Notes : 导出excel的工具类
 * Created by ruihin on 16/3/3.集成分成
 */
@SuppressWarnings("deprecation")
public class ExcelUtils extends AbstractExcelView {
    /**
     * set excel
     */
    public void export(Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,List<DnsInfo> data,String[] cellHead) throws Exception {
        //1.创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        //2.在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("DnsInfoList");
        //3.在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow(0);

        //4.创建单元格，并设置值表头 设置表头居中
        //4.1.设置字体
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short)10); //字体高度
        font.setFontName("黑体"); //字体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //宽度

        //4.2.设置单元格类型样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        style.setFont(font);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);//带边框
        style.setWrapText(true);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);//行底色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        for(int i=0; i<cellHead.length; i++){
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(cellHead[i]);
            cell.setCellStyle(style);
        }

        //5.写入实体数据 实际应用中这些数据从数据库得到，
        for (int i = 0; i < data.size(); i++) {
            row = sheet.createRow(i + 1);
             DnsInfo dnsInfo = data.get(i);
            //5.1.创建单元格，并设置值
            row.createCell(0).setCellValue(i+1);
            row.createCell(1).setCellValue(dnsInfo.getId());
            row.createCell(2).setCellValue(dnsInfo.getHost());
            row.createCell(3).setCellValue(dnsInfo.getZone());
            row.createCell(4).setCellValue(dnsInfo.getType());
            row.createCell(5).setCellValue(dnsInfo.getView());
            row.createCell(6).setCellValue(dnsInfo.getRecord());
            row.createCell(7).setCellValue(dnsInfo.getTtl());
            row.createCell(8).setCellValue(dnsInfo.getStatus());
            row.createCell(9).setCellValue(dnsInfo.getView_description());
            row.createCell(10).setCellValue("");
        }

        //6.将文件存到指定位置
        buildExcelDocument(model,wb,request,response);
    }

    /**
     * export excel
     */
    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=\""+ getFileName("DnsInfo")+"\"");

        //6.将文件存到指定位置
        try {
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private HSSFSheet getRows(String[] cellHead, HSSFWorkbook wb,String fileName) {
        HSSFSheet sheet = wb.createSheet(fileName);
        //3.在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow(0);

        //4.创建单元格，并设置值表头 设置表头居中
        //4.1.设置字体
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short)10); //字体高度
        font.setFontName("黑体"); //字体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //宽度

        //4.2.设置单元格类型样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        style.setFont(font);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);//带边框
        style.setWrapText(true);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);//行底色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        for(int i=0; i<cellHead.length; i++){
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(cellHead[i]);
            cell.setCellStyle(style);
        }
        return sheet;
    }

    /**
     * 输出到客户端
     */
    public void write(HttpServletResponse response,HSSFWorkbook wb,String fileName) throws IOException {
        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename="+ Encodes.urlEncode(getFileName(fileName)));
        //6.将文件存到指定位置
        try {
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 根据本地时间获得文件名称，精确到毫秒。
     * @return .xls文件名
     */
    private String getFileName(String fileName){
        SimpleDateFormat datetime = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        Date time = new Date();
        return fileName +"-"+datetime.format(time)+".xls";
    }
    
    
    
    
  

}