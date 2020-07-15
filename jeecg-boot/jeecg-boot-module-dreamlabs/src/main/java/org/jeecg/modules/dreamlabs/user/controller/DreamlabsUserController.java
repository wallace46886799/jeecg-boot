package org.jeecg.modules.dreamlabs.user.controller;

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
import org.jeecg.modules.dreamlabs.user.entity.DreamlabsUserParam;
import org.jeecg.modules.dreamlabs.user.entity.DreamlabsUser;
import org.jeecg.modules.dreamlabs.user.vo.DreamlabsUserPage;
import org.jeecg.modules.dreamlabs.user.service.IDreamlabsUserService;
import org.jeecg.modules.dreamlabs.user.service.IDreamlabsUserParamService;
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
 * @Description: 用户表
 * @Author: jeecg-boot
 * @Date:   2020-07-04
 * @Version: V1.0
 */
@Api(tags="用户表")
@RestController
@RequestMapping("/user/dreamlabsUser")
@Slf4j
public class DreamlabsUserController {
	@Autowired
	private IDreamlabsUserService dreamlabsUserService;
	@Autowired
	private IDreamlabsUserParamService dreamlabsUserParamService;
	
	/**
	 * 分页列表查询
	 *
	 * @param dreamlabsUser
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "用户表-分页列表查询")
	@ApiOperation(value="用户表-分页列表查询", notes="用户表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DreamlabsUser dreamlabsUser,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DreamlabsUser> queryWrapper = QueryGenerator.initQueryWrapper(dreamlabsUser, req.getParameterMap());
		Page<DreamlabsUser> page = new Page<DreamlabsUser>(pageNo, pageSize);
		IPage<DreamlabsUser> pageList = dreamlabsUserService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param dreamlabsUserPage
	 * @return
	 */
	@AutoLog(value = "用户表-添加")
	@ApiOperation(value="用户表-添加", notes="用户表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DreamlabsUserPage dreamlabsUserPage) {
		DreamlabsUser dreamlabsUser = new DreamlabsUser();
		BeanUtils.copyProperties(dreamlabsUserPage, dreamlabsUser);
		dreamlabsUserService.saveMain(dreamlabsUser, dreamlabsUserPage.getDreamlabsUserParamList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param dreamlabsUserPage
	 * @return
	 */
	@AutoLog(value = "用户表-编辑")
	@ApiOperation(value="用户表-编辑", notes="用户表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DreamlabsUserPage dreamlabsUserPage) {
		DreamlabsUser dreamlabsUser = new DreamlabsUser();
		BeanUtils.copyProperties(dreamlabsUserPage, dreamlabsUser);
		DreamlabsUser dreamlabsUserEntity = dreamlabsUserService.getById(dreamlabsUser.getId());
		if(dreamlabsUserEntity==null) {
			return Result.error("未找到对应数据");
		}
		dreamlabsUserService.updateMain(dreamlabsUser, dreamlabsUserPage.getDreamlabsUserParamList());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户表-通过id删除")
	@ApiOperation(value="用户表-通过id删除", notes="用户表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		dreamlabsUserService.delMain(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "用户表-批量删除")
	@ApiOperation(value="用户表-批量删除", notes="用户表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.dreamlabsUserService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户表-通过id查询")
	@ApiOperation(value="用户表-通过id查询", notes="用户表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DreamlabsUser dreamlabsUser = dreamlabsUserService.getById(id);
		if(dreamlabsUser==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(dreamlabsUser);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户参数表通过主表ID查询")
	@ApiOperation(value="用户参数表主表ID查询", notes="用户参数表-通主表ID查询")
	@GetMapping(value = "/queryDreamlabsUserParamByMainId")
	public Result<?> queryDreamlabsUserParamListByMainId(@RequestParam(name="id",required=true) String id) {
		List<DreamlabsUserParam> dreamlabsUserParamList = dreamlabsUserParamService.selectByMainId(id);
		return Result.ok(dreamlabsUserParamList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param dreamlabsUser
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DreamlabsUser dreamlabsUser) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<DreamlabsUser> queryWrapper = QueryGenerator.initQueryWrapper(dreamlabsUser, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<DreamlabsUser> queryList = dreamlabsUserService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<DreamlabsUser> dreamlabsUserList = new ArrayList<DreamlabsUser>();
      if(oConvertUtils.isEmpty(selections)) {
          dreamlabsUserList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          dreamlabsUserList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<DreamlabsUserPage> pageList = new ArrayList<DreamlabsUserPage>();
      for (DreamlabsUser main : dreamlabsUserList) {
          DreamlabsUserPage vo = new DreamlabsUserPage();
          BeanUtils.copyProperties(main, vo);
          List<DreamlabsUserParam> dreamlabsUserParamList = dreamlabsUserParamService.selectByMainId(main.getId());
          vo.setDreamlabsUserParamList(dreamlabsUserParamList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "用户表列表");
      mv.addObject(NormalExcelConstants.CLASS, DreamlabsUserPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户表数据", "导出人:"+sysUser.getRealname(), "用户表"));
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
              List<DreamlabsUserPage> list = ExcelImportUtil.importExcel(file.getInputStream(), DreamlabsUserPage.class, params);
              for (DreamlabsUserPage page : list) {
                  DreamlabsUser po = new DreamlabsUser();
                  BeanUtils.copyProperties(page, po);
                  dreamlabsUserService.saveMain(po, page.getDreamlabsUserParamList());
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
