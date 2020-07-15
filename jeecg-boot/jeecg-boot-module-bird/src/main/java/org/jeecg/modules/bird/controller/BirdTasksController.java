package org.jeecg.modules.bird.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.bird.entity.BirdTasks;
import org.jeecg.modules.bird.entity.BirdUser;
import org.jeecg.modules.bird.service.IBirdTasksService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.bird.service.IBirdUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 笨鸟任务
 * @Author: jeecg-boot
 * @Date: 2020-01-02
 * @Version: V1.0
 */
@RestController
@RequestMapping("/bird/birdTasks")
@Slf4j
public class BirdTasksController extends JeecgController<BirdTasks, IBirdTasksService> {
    @Autowired
    private IBirdTasksService birdTasksService;
    @Autowired
    private IBirdUserService birdUserService;

    /**
     * 分页列表查询
     *
     * @param birdTasks
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BirdTasks birdTasks,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BirdTasks> queryWrapper = QueryGenerator.initQueryWrapper(birdTasks, req.getParameterMap());
        Page<BirdTasks> page = new Page<BirdTasks>(pageNo, pageSize);
        IPage<BirdTasks> pageList = birdTasksService.page(page, queryWrapper);
        List<BirdTasks> records = pageList.getRecords();
        for (BirdTasks record : records) {
            BirdUser user = birdUserService.getById(record.getUserId());
            if (user != null)
                record.setUserId(user.getName());
        }
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param birdTasks
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BirdTasks birdTasks) {
        birdTasksService.save(birdTasks);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param birdTasks
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BirdTasks birdTasks) {
    	log.info("Edit Task.");
    	birdTasks.setUserId(null); // 编辑时不更新User ID
        birdTasksService.updateById(birdTasks);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        birdTasksService.removeById(id);
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
        this.birdTasksService.removeByIds(Arrays.asList(ids.split(",")));
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
        BirdTasks birdTasks = birdTasksService.getById(id);
        if (birdTasks == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(birdTasks);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param birdTasks
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BirdTasks birdTasks) {
        return super.exportXls(request, birdTasks, BirdTasks.class, "笨鸟任务");
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
        return super.importExcel(request, response, BirdTasks.class);
    }

}
