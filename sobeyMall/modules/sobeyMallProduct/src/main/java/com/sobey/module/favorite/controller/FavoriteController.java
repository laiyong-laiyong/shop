package com.sobey.module.favorite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.favorite.model.Favorite;
import com.sobey.module.favorite.service.FavoriteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("${api.v1}/favorite")
@Api(value = "商品收藏", description = "商品收藏")
public class FavoriteController {

	@Autowired
	private FavoriteService fs;

	@ApiOperation(value = "删除")
	@DeleteMapping("/{userCode}/{productId}")
	public void remove(@PathVariable("userCode") String userCode,@PathVariable("productId") String productId) {
		Favorite et = new Favorite();
		et.setUserCode(userCode);
		et.setProductId(productId);
		this.fs.remove(et);
	}

	@ApiOperation(value = "分页查询")
	@GetMapping
	public Page<Favorite> page(@RequestParam int page, @RequestParam int size, Favorite entity) {
		Page<Favorite> page1 = new Page<Favorite>(page, size);
		fs.page(page1, entity);
		return page1;
	}

	@ApiOperation(value = "查询")
	@PassToken
	@GetMapping("/list")
	public List<Favorite> list(Favorite entity) {

		List<Favorite> list = fs.list(entity);
		return list;
	}
	
	@ApiOperation(value = "新增")
	@PostMapping
	public void save(@RequestBody(required=true) Favorite entity) {
		
		fs.save(entity);
	}

}
