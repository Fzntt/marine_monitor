package io.marine.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.marine.common.utils.PageUtils;
import io.marine.modules.generator.entity.WaterQualityEntity;
import io.marine.modules.generator.model.LegendVO;
import io.marine.modules.generator.model.PositionValueVO;

import java.util.List;
import java.util.Map;

/**
 * 水质监测数据表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2025-05-10 10:45:03
 */
public interface WaterQualityService extends IService<WaterQualityEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void insertYearData(String year);

    List<PositionValueVO> selectList(String year,String month,String type);

    List<String> selectProvinceList();

    List<LegendVO> selectLegendList(String year,String province,String type);
}

