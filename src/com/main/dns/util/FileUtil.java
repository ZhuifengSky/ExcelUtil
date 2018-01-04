package com.main.dns.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

/**
 * 文件工具类 
 * @author maodl
 */
public class FileUtil {

	/** 文件转字节数组
	 * @param filePath 文件路径
	 * @return byte[]
	 * @throws IOException */
	public static byte[] getByte(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
			byte[] b = new byte[16 * 1024 * 1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
	
	/** 字节数组转文件
	 * @param bfile 文件字节数组
	 * @param filePath 文件路径 */
	public static void getFile(byte[] bfile, String filePath) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			String path = getPath(filePath);
			File file = new File(path);
			if (!file.exists()) { //判断文件目录是否存在
				file.mkdirs();
			}
			file = new File(filePath);
			if (file.exists()) { //如果有相同的文件名字就把它删除。没有的话就新建。
				file.delete();
            }
			
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	/** 输入流转文件(内存流)
	 * @param in 输入流
	 * @param filePath 文件路径 */
	public static void getFile(InputStream in, String filePath) throws IOException {
		String path = getPath(filePath);
		File file = new File(path);
		if (!file.exists()) { //判断文件目录是否存在
			file.mkdirs();
		}
		file = new File(filePath);
		if (file.exists()) { //如果有相同的文件名字就把它删除。没有的话就新建。
			file.delete();
        }
		BufferedInputStream is = new BufferedInputStream(in);
		BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] b = new byte[16 * 1024 * 1024];
        int len = 0;
        while((len = is.read(b)) != -1){
        	os.write(b, 0, len);
        }
        is.close();
        os.close();
        in.close();
	}
	
	/**
	 * 字符串输出到文件
	 * @param filePath
	 * @param str
	 * @throws IOException */
    public static void toFile(String filePath, String str) throws IOException {
    	File file = new File(filePath);
		FileOutputStream fos = new FileOutputStream(file, false); // 覆盖false,追加true
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(str);
		bw.close();
		osw.close();
    }
	
	/** 删除文件或文件夹
	 * @param path 文件夹路径 */
	public static void deleteFile(String path) {
		File file = new File(path);
		if (file != null) {
			if (file.isDirectory()) {
				File[] delFile = file.listFiles();
				if (delFile != null) {
					for (int i = 0; i < delFile.length; i++) {
						if (delFile[i].isDirectory()) {
							deleteFile(delFile[i].getAbsolutePath()); //递归调用del方法并取得子目录路径
						}
						delFile[i].delete();
					}
				}
			}
			boolean flag = file.delete();
			if (flag) {
				System.out.println("删除成功 " + file.getAbsolutePath());
			} else {
				System.out.println("删除失败 " + file.getAbsolutePath());
			}
		}
	}
	
	
	/** 根据路径获取文件名
	 * @param fileUrl 文件路径
	 * @return String 文件名 */
	public static String getFileName(String filePath){
		int index = filePath.lastIndexOf("/");
		if(index==-1){
			index = filePath.lastIndexOf("\\");
		}
    	String fileName = filePath.substring(index+1, filePath.length());
    	return fileName;
	}
	
	/** 根据路径获取文件目录
	 * @param fileUrl 文件路径
	 * @return String 文件名 */
	public static String getPath(String filePath){
		int index = filePath.lastIndexOf("/");
		if(index==-1){
			index = filePath.lastIndexOf("\\");
		}
    	String path = filePath.substring(0, index + 1);
    	return path;
	}

	/** 获得音乐总长度 单位s
	 * @param @param url
	 * @param @return
	 * @return int
	 * @throws */
	public static int getVoiceLength(String url) {
		File file = new File(url);
		long total = 0;
		try {
			AudioFileFormat aff = AudioSystem.getAudioFileFormat(file);
			Map<String, Object> props = aff.properties();
			if (props.containsKey("duration")) {
				total = (long) Math.round((((Long) props.get("duration")).longValue()) / 1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (int) (total / 1000);
	}

    /** 获得AMR音频文件的时长
     * @param url
     * @return int
     * @throws IOException */
    public static int getAmrVoiceLength(String url) throws IOException {
        File source = new File(url);
        long duration = -1;
        int[] packedSize = { 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0, 0, 0 };
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(source, "rw");
            long length = source.length();// 文件的长度
            int pos = 6;// 设置初始位置
            int frameCount = 0;// 初始帧数
            int packedPos = -1;

            byte[] datas = new byte[1];// 初始数据值
            while (pos <= length) {
                randomAccessFile.seek(pos);
                if (randomAccessFile.read(datas, 0, 1) != 1) {
                    duration = length > 0 ? ((length - 6) / 650) : 0;
                    break;
                }
                packedPos = (datas[0] >> 3) & 0x0F;
                pos += packedSize[packedPos] + 1;
                frameCount++;
            }

            duration += frameCount * 20;// 帧数*20
        } finally {
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
        }
        if (duration < 1000) {
            return 1;
        } else {
            return (int) (duration / 1000);
        }

    }

    public static void main(String[] args) {
    	deleteFile("c:/mnt/data/serverftp/videozip/101");
	}
}
