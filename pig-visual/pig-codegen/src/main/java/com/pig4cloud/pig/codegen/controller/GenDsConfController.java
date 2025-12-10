package com.pig4cloud.pig.codegen.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.smallbun.screw.boot.config.Screw;
import cn.smallbun.screw.boot.properties.ScrewProperties;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.codegen.entity.GenDatasourceConf;
import com.pig4cloud.pig.codegen.service.GenDatasourceConfService;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.security.annotation.Inner;
import com.pig4cloud.pig.common.xss.core.XssCleanIgnore;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.List;

/**
 * 数据源管理控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/dsconf")
public class GenDsConfController {

	private final GenDatasourceConfService datasourceConfService;

	private final Screw screw;

	/**
	 * 分页查询数据源配置
	 */
	@GetMapping("/page")
	public Result<Page<GenDatasourceConf>> getDsConfPage(Page<GenDatasourceConf> page, GenDatasourceConf datasourceConf) {
		return Result.ok(datasourceConfService.page(page,
				Wrappers.<GenDatasourceConf>lambdaQuery().like(CharSequenceUtil.isNotBlank(datasourceConf.getDsName()), GenDatasourceConf::getDsName,
							datasourceConf.getDsName())));
	}

	/**
	 * 查询全部数据源列表
	 */
	@GetMapping("/list")
	@Inner(value = false)
	public Result<List<GenDatasourceConf>> listDsConfs() {
		return Result.ok(datasourceConfService.list());
	}

	/**
	 * 根据ID查询数据源表
	 * @param id 数据源ID
	 */
	@GetMapping("/{id}")
	public Result<GenDatasourceConf> getDsConfById(@PathVariable("id") Long id) {
		return Result.ok(datasourceConfService.getById(id));
	}

	/**
	 * 新增数据源表
	 */
	@PostMapping
	@XssCleanIgnore
	public Result<Boolean> saveDsConf(@RequestBody GenDatasourceConf datasourceConf) {
		return Result.ok(datasourceConfService.saveDsByEnc(datasourceConf));
	}

	/**
	 * 修改数据源表
	 */
	@PutMapping
	@XssCleanIgnore
	public Result<Boolean> updateDsConf(@RequestBody GenDatasourceConf conf) {
		return Result.ok(datasourceConfService.updateDsByEnc(conf));
	}

	/**
	 * 通过id数组删除数据源表
	 */
	@DeleteMapping
	public Result<Boolean> removeDsConfByIds(@RequestBody Long[] ids) {
		return Result.ok(datasourceConfService.removeByDsId(ids));
	}

	/**
	 * 生成指定数据源的数据库文档并输出到响应流
	 * @param dsName 数据源名称
	 * @param response HTTP响应对象
	 */
	@SneakyThrows
	@GetMapping("/doc")
	public void generatorDoc(String dsName, HttpServletResponse response) {
		// 设置指定的数据源
		DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
		DynamicDataSourceContextHolder.push(dsName);
		DataSource dataSource = dynamicRoutingDataSource.determineDataSource();

		// 设置指定的目标表
		ScrewProperties screwProperties = SpringContextHolder.getBean(ScrewProperties.class);

		// 生成
		byte[] data = screw.documentGeneration(dsName, dataSource, screwProperties).toByteArray();
		response.reset();
		response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(data.length));
		response.setContentType("application/octet-stream");
		IoUtil.write(response.getOutputStream(), Boolean.FALSE, data);
	}

}
