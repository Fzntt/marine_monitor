package io.marine.modules.generator.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.marine.modules.generator.entity.WarnConfigEntity;
import io.marine.modules.generator.service.WarnConfigService;
import io.marine.common.utils.PageUtils;
import io.marine.common.utils.R;



/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2025-05-12 20:30:07
 */
@RestController
@RequestMapping("generator/warnconfig")
public class WarnConfigController {
    @Autowired
    private WarnConfigService warnConfigService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:warnconfig:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = warnConfigService.queryPage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("/allList")
    public R allList(@RequestParam Map<String, Object> params){
        List<WarnConfigEntity> list = warnConfigService.list();
        return R.ok().put("list", list);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:warnconfig:info")
    public R info(@PathVariable("id") Long id){
		WarnConfigEntity warnConfig = warnConfigService.getById(id);

        return R.ok().put("warnConfig", warnConfig);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:warnconfig:save")
    public R save(@RequestBody WarnConfigEntity warnConfig){
		warnConfigService.save(warnConfig);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:warnconfig:update")
    public R update(@RequestBody WarnConfigEntity warnConfig){
		warnConfigService.updateById(warnConfig);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:warnconfig:delete")
    public R delete(@RequestBody Long[] ids){
		warnConfigService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
