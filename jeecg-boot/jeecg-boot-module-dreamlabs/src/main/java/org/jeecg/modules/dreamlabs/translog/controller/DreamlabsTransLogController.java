package org.jeecg.modules.dreamlabs.translog.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.dreamlabs.translog.entity.DreamlabsTransLog;
import org.jeecg.modules.dreamlabs.translog.service.IDreamlabsTransLogService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 交易日志表
 * @Author: jeecg-boot
 * @Date:   2020-07-05
 * @Version: V1.0
 */
@Api(tags="交易日志表")
@RestController
@RequestMapping("/translog/dreamlabsTransLog")
@Slf4j
public class DreamlabsTransLogController extends JeecgController<DreamlabsTransLog, IDreamlabsTransLogService> {
	@Autowired
	private IDreamlabsTransLogService dreamlabsTransLogService;
	
	/**
	 * 分页列表查询
	 *
	 * @param dreamlabsTransLog
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "交易日志表-分页列表查询")
	@ApiOperation(value="交易日志表-分页列表查询", notes="交易日志表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DreamlabsTransLog dreamlabsTransLog,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DreamlabsTransLog> queryWrapper = QueryGenerator.initQueryWrapper(dreamlabsTransLog, req.getParameterMap());
		Page<DreamlabsTransLog> page = new Page<DreamlabsTransLog>(pageNo, pageSize);
		IPage<DreamlabsTransLog> pageList = dreamlabsTransLogService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param dreamlabsTransLog
	 * @return
	 */
	@AutoLog(value = "交易日志表-添加")
	@ApiOperation(value="交易日志表-添加", notes="交易日志表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DreamlabsTransLog dreamlabsTransLog) {
		dreamlabsTransLogService.save(dreamlabsTransLog);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param dreamlabsTransLog
	 * @return
	 */
	@AutoLog(value = "交易日志表-编辑")
	@ApiOperation(value="交易日志表-编辑", notes="交易日志表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DreamlabsTransLog dreamlabsTransLog) {
		dreamlabsTransLogService.updateById(dreamlabsTransLog);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "交易日志表-通过id删除")
	@ApiOperation(value="交易日志表-通过id删除", notes="交易日志表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		dreamlabsTransLogService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "交易日志表-批量删除")
	@ApiOperation(value="交易日志表-批量删除", notes="交易日志表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.dreamlabsTransLogService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "交易日志表-通过id查询")
	@ApiOperation(value="交易日志表-通过id查询", notes="交易日志表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DreamlabsTransLog dreamlabsTransLog = dreamlabsTransLogService.getById(id);
		if(dreamlabsTransLog==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(dreamlabsTransLog);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param dreamlabsTransLog
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DreamlabsTransLog dreamlabsTransLog) {
        return super.exportXls(request, dreamlabsTransLog, DreamlabsTransLog.class, "交易日志表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, DreamlabsTransLog.class);
    }

}
