package com.sobey.module.media.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.media.mapper.MediaMapper;
import com.sobey.module.media.model.Media;
import com.sobey.util.common.file.FileUtil;
import com.sobey.util.common.uuid.UUIDUtils;

/**
 * 
 * 
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class MediaService extends ServiceImpl<MediaMapper, Media> {

	@Autowired
	private MediaMapper mdMp;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	public Page<Media> page(Page<Media> page, Media ct) {
		List<Media> cts = mdMp.page(page, ct);
		page.setRecords(cts);
		return page;
	}

	public String insert(List<String> list, String productId, String mediaId, String type) {

		if (CollectionUtils.isEmpty(list)) {
			return null;
		}

		String uuid = null;
		for (String address : list) {
			Media md = new Media();
			uuid = UUIDUtils.simpleUuid();
			md.setUuid(uuid);
			String name = StringUtils.substringAfterLast(address, "/");
			md.setName(name);
			md.setProductId(productId);
			md.setMediaId(mediaId);
			md.setAddress(address);
			md.setType(type);
			this.insert(md);
		}
		
		return uuid;

	}

	/**
	 * C保存上传的实体文件到磁盘和基本信息到数据库
	 * 
	 * @param files
	 * @param destPath
	 * @param productId
	 * @param mediaId
	 */
	public void saveBatch(MultipartFile[] files, String destPath, String productId, String mediaId,
			String type) {

		List<String> paths = FileUtil.storeFile(files, destPath);
		this.insert(paths, productId, mediaId, type);

	}
	
	/**
	 * 为了同步把上传的素材的uuid返回给前台,所以这里每次只能上传一张图片
	 * 
	 * @param files
	 * @param destPath
	 * @param productId
	 * @param mediaId
	 * @param type
	 * @return
	 */
	public String saveSingle(MultipartFile[] files, String destPath, String productId, String mediaId,
			String type) {
		
		if (files == null || files.length !=1) {
			throw new AppException(ExceptionType.SYS_FILE_TOO_MANY);
		}
		List<String> paths = FileUtil.storeFile(files, destPath);
		String uuid = this.insert(paths, productId, mediaId, type);
		return uuid;
	}

	public void deleteBatch(List<Media> list) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		for (Media cs : list) {
			String uuid = cs.getUuid();
			this.deleteById(uuid);
		}
	}

	public List<Media> list(Media media) {
		if (media == null) {
			return null;
		}

		List<Media> list = this.mdMp.list(media);
		return list;
	}

	public void remove(Media media) {
		if (media == null) {
			return;
		}
		List<Media> list = this.list(media);
		if (!CollectionUtils.isEmpty(list)) {
			for (Media media2 : list) {
				if (media2 != null) {
					String address = media2.getAddress();
					this.mdMp.remove(media2);
					FileUtil.forceDelete(address);
				}
			}
		}

	}

}
