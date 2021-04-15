package com.sobey.util.common.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.util.common.uuid.UUIDUtils;

public class FileUtil {

	private static Logger log = LoggerFactory.getLogger(FileUtil.class);

	public static String readFileToString(String name) {
		String path = FileUtil.class.getResource(name).getFile();
		File file = new File(path);
		String data = null;
		try {
			data = FileUtils.readFileToString(file, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException("读取文件异常" + name + e);
		}
		return data;

	}

	/**
	 * 暂时不用
	 * 
	 * @param name
	 * @return
	 */
	public static String readFileToString2(String name) {
		ClassPathResource cpr = new ClassPathResource(name);
		String data = null;
		try {
			File file2 = cpr.getFile();
			data = FileUtils.readFileToString(file2, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException("读取文件异常" + name + e);
		}
		System.out.println(data);
		return data;

	}

	

	/**
	 * C获取所有的上传文件
	 * 
	 * @param files
	 * @param destPath
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static List<String> storeFile(MultipartFile[] files, String destPath) {

		if (files == null || files.length <= 0) {
			return null;
		}

		List<String> list = new ArrayList<String>();
		for (MultipartFile file : files) {

			String fileName = file.getOriginalFilename();
			// C防止文件重名
			String dir = UUIDUtils.simpleUuid();
			String path = destPath + "/" + dir + "/" + fileName;
			list.add(path);

			File dest = new File(path);
			if (!dest.getParentFile().exists()) {
				dest.getParentFile().mkdirs();
			}

			try {
				file.transferTo(dest);
			} catch (IllegalStateException | IOException e) {
				log.error("文件上传报错", e);
				throw new AppException(ExceptionType.SYS_FILE_UPLOAD, e);
			}

		}

		return list;

	}

	public static void forceDelete(String path) {

		if (StringUtils.isEmpty(path)) {
			return;
		}

		File file = new File(path);
		try {
			FileUtils.forceDelete(file);
		} catch (IOException e) {
			log.error("删除文件失败");
		}

	}

	/**
	 * C通用文件下载
	 * 
	 * @param address 绝对路径,包含文件名
	 * @param res
	 */
	public static void download(String address, HttpServletResponse res) {

		BufferedInputStream bis = null;
		try {
			// 发送给客户端的数据
			OutputStream os = res.getOutputStream();
			byte[] buff = new byte[1024];
			// 读取filename
			bis = new BufferedInputStream(new FileInputStream(new File(address)));

			int count = bis.read(buff);
			while (count != -1) {
				os.write(buff, 0, count);
				os.flush();
				count = bis.read(buff);
			}
		} catch (Exception e) {
			throw new AppException(ExceptionType.SYS_FILE_DOWNLOAD, e);
		} finally {
			try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getClassPath() {
		String path = null;
		try {
			/**
			 * 在linux中获取的路径为：file:/usr/local/sobeyMallPay/sobeyMallPay-1.0.jar!/BOOT-INF/classes!/
			 * 
			 */
			path = ResourceUtils.getURL("classpath:").getPath();
			path = "D:/excel/test/pay/sobeyMallPay-1.0.jar!/BOOT-INF/classes!/";
			log.info("path = " +path);
//			if (StringUtils.isNotEmpty(path)) {
//					String[] list = path.split(":");
//					if (list != null) {
//						//取最后一个
//						path = list[1]+list[2];
//					}
//			}
			
		} catch (FileNotFoundException e1) {
			throw new AppException("获取classpath失败");
		}
		
		return path;
	}
	

}
