package io.marine.modules.generator.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.marine.modules.generator.entity.WaterQualityEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.marine.modules.generator.model.LegendVO;
import io.marine.modules.generator.model.PositionValueVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 水质监测数据表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2025-05-10 10:45:03
 */
@Mapper
public interface WaterQualityDao extends BaseMapper<WaterQualityEntity> {

    Page<WaterQualityEntity> selectWaterQualityPage(
            Page<?> page,
            @Param("params") Map<String, Object> params
    );

    List<PositionValueVO> selectWaterQualityData(@Param("year") String year,@Param("month") String month,@Param("fieldType") String fieldType);

    List<LegendVO> selectLegendList(@Param("year") String year,@Param("province") String province,@Param("type") String type);
}
