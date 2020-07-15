package org.jeecg.modules.bird.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.bird.entity.BirdUserLog;
import org.jeecg.modules.bird.service.IBirdUserLogService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 笨鸟操作日志
 * @Author: jeecg-boot
 * @Date:   2019-12-25
 * @Version: V1.0
 */
@RestController
@RequestMapping("/bird/birdUserLog")
@Slf4j
public class BirdUserLogController extends JeecgController<BirdUserLog, IBirdUserLogService> {
	@Autowired
	private IBirdUserLogService birdUserLogService;

	/**
	 * 分页列表查询
	 *
	 * @param birdUserLog
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BirdUserLog birdUserLog,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BirdUserLog> queryWrapper = QueryGenerator.initQueryWrapper(birdUserLog, req.getParameterMap());
		Page<BirdUserLog> page = new Page<BirdUserLog>(pageNo, pageSize);
		IPage<BirdUserLog> pageList = birdUserLogService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param birdUserLog
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BirdUserLog birdUserLog) {
		birdUserLogService.save(birdUserLog);
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param birdUserLog
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BirdUserLog birdUserLog) {
		birdUserLogService.updateById(birdUserLog);
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
		birdUserLogService.removeById(id);
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
		this.birdUserLogService.removeByIds(Arrays.asList(ids.split(",")));
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
		BirdUserLog birdUserLog = birdUserLogService.getById(id);
		if(birdUserLog==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(birdUserLog);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param birdUserLog
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BirdUserLog birdUserLog) {
        return super.exportXls(request, birdUserLog, BirdUserLog.class, "笨鸟操作日志");
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
        return super.importExcel(request, response, BirdUserLog.class);
    }

}
