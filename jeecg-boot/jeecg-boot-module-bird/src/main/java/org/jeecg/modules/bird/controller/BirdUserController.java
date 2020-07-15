package org.jeecg.modules.bird.controller;

import java.net.URLDecoder;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.AESUtil;
import org.jeecg.modules.bird.entity.BirdUser;
import org.jeecg.modules.bird.service.DoStartEndTasksService;
import org.jeecg.modules.bird.service.IBirdUserLogService;
import org.jeecg.modules.bird.service.IBirdUserService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.bird.service.DoSignedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 笨鸟用户
 * @Author: jeecg-boot
 * @Date: 2019-12-25
 * @Version: V1.0
 */
@RestController
@RequestMapping("/bird/birdUser")
@Slf4j
public class BirdUserController extends JeecgController<BirdUser, IBirdUserService> {
    @Autowired
    private IBirdUserService birdUserService;
    @Autowired
	private DoSignedService doSignedService;
    @Autowired
    private IBirdUserLogService birdUserLogService;
    /**
     * 分页列表查询
     *
     * @param birdUser
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BirdUser birdUser,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BirdUser> queryWrapper = QueryGenerator.initQueryWrapper(birdUser, req.getParameterMap());
        Page<BirdUser> page = new Page<BirdUser>(pageNo, pageSize);
        IPage<BirdUser> pageList = birdUserService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 通过phone，获取用户信息
     *
     * @param phone
     * @return
     */
    @GetMapping(value = "/getByPhone")
    public Result<?> queryPageList(@RequestParam(name = "phone") String phone) {
        QueryWrapper<BirdUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        BirdUser one = birdUserService.getOne(queryWrapper);
        return Result.ok(one);
    }

    /**
     * 添加
     *
     * @param birdUser
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BirdUser birdUser) throws Exception {
        log.info("token code from：{}", birdUser.getTokenStr());
        String token = URLDecoder.decode(birdUser.getTokenStr(), "UTF-8");
        token = URLDecoder.decode(token, "UTF-8");
        token = AESUtil.decrypt(token);
        birdUser.setTokenStr(token);
        log.info("token code：{}", token);
        birdUserService.save(birdUser);
//        BirdUserLog entity = new BirdUserLog();
//        entity.setLogMsg();
//        birdUserLogService.save(entity);
        return Result.ok("添加成功！");
    }

 
    /**
     * 编辑
     *
     * @param birdUser
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BirdUser birdUser) throws Exception {

        BirdUser birdUserO = birdUserService.getById(birdUser.getId());

        if (StringUtils.isNotBlank(birdUser.getTokenStr()) && !birdUser.getTokenStr().equals(birdUserO.getTokenStr())) {
            log.info("token code from：{}", birdUser.getTokenStr());
            String token = URLDecoder.decode(birdUser.getTokenStr(), "UTF-8");
            token = URLDecoder.decode(token, "UTF-8");
            token = AESUtil.decrypt(token);
            birdUser.setTokenStr(token);
            log.info("token code：{}", token);
        } else {
            log.info("should not decode token");
        }
        birdUserService.updateById(birdUser);
        return Result.ok("编辑成功!");
    }


	@PostMapping(value = "/signin")
	public Result<?> signin(@RequestBody BirdUser birdUser) throws Exception {
		this.edit(birdUser);
		BirdUser birdUserO = birdUserService.getById(birdUser.getId());
        String execute = doSignedService.signin(birdUserO);
        return Result.ok(execute);
	}

    @PostMapping(value = "/signout")
    public Result<?> signout(@RequestBody BirdUser birdUser) throws Exception {
        this.edit(birdUser);
        BirdUser birdUserO = birdUserService.getById(birdUser.getId());
        String execute = doSignedService.signout(birdUserO);
        return Result.ok(execute);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        birdUserService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.birdUserService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BirdUser birdUser = birdUserService.getById(id);
        if (birdUser == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(birdUser);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param birdUser
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BirdUser birdUser) {
        return super.exportXls(request, birdUser, BirdUser.class, "笨鸟用户");
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
        return super.importExcel(request, response, BirdUser.class);
    }

}
