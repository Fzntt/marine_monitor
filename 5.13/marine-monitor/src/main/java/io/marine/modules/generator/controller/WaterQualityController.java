package io.marine.modules.generator.controller;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.marine.modules.generator.model.InferiorVO;
import io.marine.modules.generator.model.LegendVO;
import io.marine.modules.generator.model.PositionValueVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.marine.modules.generator.entity.WaterQualityEntity;
import io.marine.modules.generator.service.WaterQualityService;
import io.marine.common.utils.PageUtils;
import io.marine.common.utils.R;



/**
 * 水质监测数据表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2025-05-10 10:45:03
 */
@RestController
@RequestMapping("generator/waterquality")
public class WaterQualityController {
    @Autowired
    private WaterQualityService waterQualityService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:waterquality:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = waterQualityService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:waterquality:info")
    public R info(@PathVariable("id") Integer id){
		WaterQualityEntity waterQuality = waterQualityService.getById(id);

        return R.ok().put("waterQuality", waterQuality);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:waterquality:save")
    public R save(@RequestBody WaterQualityEntity waterQuality){
		waterQualityService.save(waterQuality);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:waterquality:update")
    public R update(@RequestBody WaterQualityEntity waterQuality){
		waterQualityService.updateById(waterQuality);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:waterquality:delete")
    public R delete(@RequestBody Integer[] ids){
		waterQualityService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @RequestMapping("/asyncData/{year}")
    public R asyncData(@PathVariable("year") String year){
        waterQualityService.insertYearData(year);

        return R.ok();
    }

    @RequestMapping("/selectList")
    public R selectList(@RequestParam("year") String year,
                        @RequestParam("month") String month,
                        @RequestParam("type") String type){
        List<PositionValueVO> list = waterQualityService.selectList(year,month,type);

        return R.ok().put("list", list);
    }

    @RequestMapping("/selectProvinceList")
    public R selectProvinceList(){
        List<String> list = waterQualityService.selectProvinceList();

        return R.ok().put("list", list);
    }

    @RequestMapping("/selectLegendList")
    public R selectLegendList(
            @RequestParam("year") String year,
            @RequestParam("province") String province,
            @RequestParam("type") String type
    ){
        List<LegendVO> list = waterQualityService.selectLegendList(year,province,type);

        return R.ok().put("list", list);
    }

    @RequestMapping("/selectInferiorList")
    public R selectInferiorList(@RequestParam("year") String year,
                              @RequestParam("month") String month
    ){
        List<InferiorVO> returnedList = new ArrayList<>();

        LambdaQueryWrapper<WaterQualityEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WaterQualityEntity::getYear, year);
        wrapper.eq(WaterQualityEntity::getMonth, month);
        wrapper.eq(WaterQualityEntity::getSzlb, "劣四类");

        List<WaterQualityEntity> list = waterQualityService.list(wrapper);
        if(CollectionUtils.isNotEmpty(list)){
            Map<String, List<WaterQualityEntity>> collect = list.stream().collect(Collectors.groupingBy(WaterQualityEntity::getCity));
            for(String city : collect.keySet()){
                InferiorVO inferiorVO = new InferiorVO();
                WaterQualityEntity waterQualityEntity = collect.get(city).get(0);
                inferiorVO.setCity(waterQualityEntity.getCity());
                inferiorVO.setProvince(waterQualityEntity.getProvince());
                inferiorVO.setSea(waterQualityEntity.getSea());
                inferiorVO.setCount(collect.get(city).size());
                returnedList.add(inferiorVO);
            }
        }
        returnedList.sort(Comparator.comparing(InferiorVO::getCount).reversed());
        return R.ok().put("list", returnedList);
    }
}
