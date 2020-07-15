package org.jeecg.modules.dreamlabs.org.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrgParam;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.org.vo.DreamlabsOrgPage;
import org.jeecg.modules.dreamlabs.org.service.IDreamlabsOrgService;
import org.jeecg.modules.dreamlabs.org.service.IDreamlabsOrgParamService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 金融机构表
 * @Author: jeecg-boot
 * @Date:   2020-07-04
 * @Version: V1.0
 */
@Api(tags="金融机构表")
@RestController
@RequestMapping("/org/dreamlabsOrg")
@Slf4j
public class DreamlabsOrgController {
	@Autowired
	private IDreamlabsOrgService dreamlabsOrgService;
	@Autowired
	private IDreamlabsOrgParamService dreamlabsOrgParamService;
	
	/**
	 * 分页列表查询
	 *
	 * @param dreamlabsOrg
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "金融机构表-分页列表查询")
	@ApiOperation(value="金融机构表-分页列表查询", notes="金融机构表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DreamlabsOrg dreamlabsOrg,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DreamlabsOrg> queryWrapper = QueryGenerator.initQueryWrapper(dreamlabsOrg, req.getParameterMap());
		Page<DreamlabsOrg> page = new Page<DreamlabsOrg>(pageNo, pageSize);
		IPage<DreamlabsOrg> pageList = dreamlabsOrgService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param dreamlabsOrgPage
	 * @return
	 */
	@AutoLog(value = "金融机构表-添加")
	@ApiOperation(value="金融机构表-添加", notes="金融机构表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DreamlabsOrgPage dreamlabsOrgPage) {
		DreamlabsOrg dreamlabsOrg = new DreamlabsOrg();
		BeanUtils.copyProperties(dreamlabsOrgPage, dreamlabsOrg);
		dreamlabsOrgService.saveMain(dreamlabsOrg, dreamlabsOrgPage.getDreamlabsOrgParamList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param dreamlabsOrgPage
	 * @return
	 */
	@AutoLog(value = "金融机构表-编辑")
	@ApiOperation(value="金融机构表-编辑", notes="金融机构表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DreamlabsOrgPage dreamlabsOrgPage) {
		DreamlabsOrg dreamlabsOrg = new DreamlabsOrg();
		BeanUtils.copyProperties(dreamlabsOrgPage, dreamlabsOrg);
		DreamlabsOrg dreamlabsOrgEntity = dreamlabsOrgService.getById(dreamlabsOrg.getId());
		if(dreamlabsOrgEntity==null) {
			return Result.error("未找到对应数据");
		}
		dreamlabsOrgService.updateMain(dreamlabsOrg, dreamlabsOrgPage.getDreamlabsOrgParamList());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "金融机构表-通过id删除")
	@ApiOperation(value="金融机构表-通过id删除", notes="金融机构表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		dreamlabsOrgService.delMain(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "金融机构表-批量删除")
	@ApiOperation(value="金融机构表-批量删除", notes="金融机构表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.dreamlabsOrgService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "金融机构表-通过id查询")
	@ApiOperation(value="金融机构表-通过id查询", notes="金融机构表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DreamlabsOrg dreamlabsOrg = dreamlabsOrgService.getById(id);
		if(dreamlabsOrg==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(dreamlabsOrg);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "金融机构参数表通过主表ID查询")
	@ApiOperation(value="金融机构参数表主表ID查询", notes="金融机构参数表-通主表ID查询")
	@GetMapping(value = "/queryDreamlabsOrgParamByMainId")
	public Result<?> queryDreamlabsOrgParamListByMainId(@RequestParam(name="id",required=true) String id) {
		List<DreamlabsOrgParam> dreamlabsOrgParamList = dreamlabsOrgParamService.selectByMainId(id);
		return Result.ok(dreamlabsOrgParamList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param dreamlabsOrg
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DreamlabsOrg dreamlabsOrg) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<DreamlabsOrg> queryWrapper = QueryGenerator.initQueryWrapper(dreamlabsOrg, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<DreamlabsOrg> queryList = dreamlabsOrgService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<DreamlabsOrg> dreamlabsOrgList = new ArrayList<DreamlabsOrg>();
      if(oConvertUtils.isEmpty(selections)) {
          dreamlabsOrgList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          dreamlabsOrgList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<DreamlabsOrgPage> pageList = new ArrayList<DreamlabsOrgPage>();
      for (DreamlabsOrg main : dreamlabsOrgList) {
          DreamlabsOrgPage vo = new DreamlabsOrgPage();
          BeanUtils.copyProperties(main, vo);
          List<DreamlabsOrgParam> dreamlabsOrgParamList = dreamlabsOrgParamService.selectByMainId(main.getId());
          vo.setDreamlabsOrgParamList(dreamlabsOrgParamList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "金融机构表列表");
      mv.addObject(NormalExcelConstants.CLASS, DreamlabsOrgPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("金融机构表数据", "导出人:"+sysUser.getRealname(), "金融机构表"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<DreamlabsOrgPage> list = ExcelImportUtil.importExcel(file.getInputStream(), DreamlabsOrgPage.class, params);
              for (DreamlabsOrgPage page : list) {
                  DreamlabsOrg po = new DreamlabsOrg();
                  BeanUtils.copyProperties(page, po);
                  dreamlabsOrgService.saveMain(po, page.getDreamlabsOrgParamList());
              }
              return Result.ok("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
    }

}
