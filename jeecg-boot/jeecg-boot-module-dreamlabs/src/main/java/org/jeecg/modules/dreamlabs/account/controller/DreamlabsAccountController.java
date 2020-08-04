package org.jeecg.modules.dreamlabs.account.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccountParam;
import org.jeecg.modules.dreamlabs.account.service.IDreamlabsAccountParamService;
import org.jeecg.modules.dreamlabs.account.service.IDreamlabsAccountService;
import org.jeecg.modules.dreamlabs.account.vo.DreamlabsAccountPage;
import org.jeecg.modules.dreamlabs.sprider.Spider;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.github.logger.controller.annotation.Logging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 账户表
 * @Author: jeecg-boot
 * @Date:   2020-07-03
 * @Version: V1.0
 */
@Api(tags="账户表")
@RestController
@RequestMapping("/account/dreamlabsAccount")
@Slf4j
public class DreamlabsAccountController {
	@Autowired
	private IDreamlabsAccountService dreamlabsAccountService;
	@Autowired
	private IDreamlabsAccountParamService dreamlabsAccountParamService;
	
	/**
	 * 分页列表查询
	 *
	 * @param dreamlabsAccount
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "账户表-分页列表查询")
	@ApiOperation(value="账户表-分页列表查询", notes="账户表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DreamlabsAccount dreamlabsAccount,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DreamlabsAccount> queryWrapper = QueryGenerator.initQueryWrapper(dreamlabsAccount, req.getParameterMap());
		Page<DreamlabsAccount> page = new Page<DreamlabsAccount>(pageNo, pageSize);
		IPage<DreamlabsAccount> pageList = dreamlabsAccountService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param dreamlabsAccountPage
	 * @return
	 */
	@AutoLog(value = "账户表-添加")
	@ApiOperation(value="账户表-添加", notes="账户表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DreamlabsAccountPage dreamlabsAccountPage) {
		DreamlabsAccount dreamlabsAccount = new DreamlabsAccount();
		BeanUtils.copyProperties(dreamlabsAccountPage, dreamlabsAccount);
		dreamlabsAccountService.saveMain(dreamlabsAccount, dreamlabsAccountPage.getDreamlabsAccountParamList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param dreamlabsAccountPage
	 * @return
	 */
	@AutoLog(value = "账户表-编辑")
	@ApiOperation(value="账户表-编辑", notes="账户表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DreamlabsAccountPage dreamlabsAccountPage) {
		DreamlabsAccount dreamlabsAccount = new DreamlabsAccount();
		BeanUtils.copyProperties(dreamlabsAccountPage, dreamlabsAccount);
		DreamlabsAccount dreamlabsAccountEntity = dreamlabsAccountService.getById(dreamlabsAccount.getId());
		if(dreamlabsAccountEntity==null) {
			return Result.error("未找到对应数据");
		}
		dreamlabsAccountService.updateMain(dreamlabsAccount, dreamlabsAccountPage.getDreamlabsAccountParamList());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "账户表-通过id删除")
	@ApiOperation(value="账户表-通过id删除", notes="账户表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		dreamlabsAccountService.delMain(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "账户表-批量删除")
	@ApiOperation(value="账户表-批量删除", notes="账户表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.dreamlabsAccountService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "账户表-通过id查询")
	@ApiOperation(value="账户表-通过id查询", notes="账户表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DreamlabsAccount dreamlabsAccount = dreamlabsAccountService.getById(id);
		if(dreamlabsAccount==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(dreamlabsAccount);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "账户参数表通过主表ID查询")
	@ApiOperation(value="账户参数表主表ID查询", notes="账户参数表-通主表ID查询")
	@GetMapping(value = "/queryDreamlabsAccountParamByMainId")
	public Result<?> queryDreamlabsAccountParamListByMainId(@RequestParam(name="id",required=true) String id) {
		List<DreamlabsAccountParam> dreamlabsAccountParamList = dreamlabsAccountParamService.selectByMainId(id);
		return Result.ok(dreamlabsAccountParamList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param dreamlabsAccount
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DreamlabsAccount dreamlabsAccount) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<DreamlabsAccount> queryWrapper = QueryGenerator.initQueryWrapper(dreamlabsAccount, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<DreamlabsAccount> queryList = dreamlabsAccountService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<DreamlabsAccount> dreamlabsAccountList = new ArrayList<DreamlabsAccount>();
      if(oConvertUtils.isEmpty(selections)) {
          dreamlabsAccountList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          dreamlabsAccountList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<DreamlabsAccountPage> pageList = new ArrayList<DreamlabsAccountPage>();
      for (DreamlabsAccount main : dreamlabsAccountList) {
          DreamlabsAccountPage vo = new DreamlabsAccountPage();
          BeanUtils.copyProperties(main, vo);
          List<DreamlabsAccountParam> dreamlabsAccountParamList = dreamlabsAccountParamService.selectByMainId(main.getId());
          vo.setDreamlabsAccountParamList(dreamlabsAccountParamList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "账户表列表");
      mv.addObject(NormalExcelConstants.CLASS, DreamlabsAccountPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("账户表数据", "导出人:"+sysUser.getRealname(), "账户表"));
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
              List<DreamlabsAccountPage> list = ExcelImportUtil.importExcel(file.getInputStream(), DreamlabsAccountPage.class, params);
              for (DreamlabsAccountPage page : list) {
                  DreamlabsAccount po = new DreamlabsAccount();
                  BeanUtils.copyProperties(page, po);
                  dreamlabsAccountService.saveMain(po, page.getDreamlabsAccountParamList());
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
    
    
    /**
	 * 同步持仓信息
	 *
	 * @param id
	 * @return
     * @throws Exception 
	 */
	@AutoLog(value = "同步持仓信息")
	@ApiOperation(value="根据账户编号同步持仓信息", notes="根据账户编号同步持仓信息")
	@Logging
	@GetMapping(value = "/hoding")
	public Result<?> holding(@RequestParam(name="id",required=true) String id) throws Exception {
		//1278680504878501890
		log.info("Start to sync holdings with account id:{}", id);
		DreamlabsAccount dreamlabsAccount = dreamlabsAccountService.getById(id);
		if (dreamlabsAccount == null) {
			log.error("Please check account id");
			return Result.error("Please check account id");
		}
		String alias = dreamlabsAccount.getAccountAlias();
		Spider spider = (Spider) SpringContextUtils.getBean(alias+"HoldingSpider");
		Map<String,Object> result = spider.process(id);
		return Result.ok(result);
	}
	
	
	/**
	 * 同步交易信息
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "同步交易信息")
	@ApiOperation(value="根据账户编号同步交易信息", notes="根据账户编号同步交易信息")
	@Logging
	@GetMapping(value = "/transactions")
	public Result<?> transactions(@RequestParam(name="id",required=true) String id)  throws Exception {
		log.info("Start to sync transactions with account id:{}", id);
		DreamlabsAccount dreamlabsAccount = dreamlabsAccountService.getById(id);
		String alias = dreamlabsAccount.getAccountAlias();
		Spider spider = (Spider) SpringContextUtils.getBean(alias+"TransactionSpider");
		Map<String,Object> result = spider.process(id);
		return Result.ok(result);
	}

}
