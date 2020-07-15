package org.jeecg.modules.bird.controller;

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
import org.jeecg.modules.bird.entity.BirdParam;
import org.jeecg.modules.bird.service.IBirdParamService;

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

 /**
 * @Description: 笨鸟参数
 * @Author: jeecg-boot
 * @Date:   2019-12-25
 * @Version: V1.0
 */
@RestController
@RequestMapping("/bird/birdParam")
@Slf4j
public class BirdParamController extends JeecgController<BirdParam, IBirdParamService> {
	@Autowired
	private IBirdParamService birdParamService;

	/**
	 * 分页列表查询
	 *
	 * @param birdParam
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BirdParam birdParam,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BirdParam> queryWrapper = QueryGenerator.initQueryWrapper(birdParam, req.getParameterMap());
		Page<BirdParam> page = new Page<BirdParam>(pageNo, pageSize);
		IPage<BirdParam> pageList = birdParamService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param birdParam
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BirdParam birdParam) {
		birdParamService.save(birdParam);
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param birdParam
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BirdParam birdParam) {
		birdParamService.updateById(birdParam);
		return Result.ok("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		birdParamService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.birdParamService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BirdParam birdParam = birdParamService.getById(id);
		if(birdParam==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(birdParam);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param birdParam
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BirdParam birdParam) {
        return super.exportXls(request, birdParam, BirdParam.class, "笨鸟参数");
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
        return super.importExcel(request, response, BirdParam.class);
    }

}
