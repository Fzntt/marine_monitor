package io.marine.modules.generator.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.marine.modules.generator.model.LegendVO;
import io.marine.modules.generator.model.PositionValueVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.marine.common.utils.PageUtils;

import io.marine.modules.generator.dao.WaterQualityDao;
import io.marine.modules.generator.entity.WaterQualityEntity;
import io.marine.modules.generator.service.WaterQualityService;


@Service("waterQualityService")
public class WaterQualityServiceImpl extends ServiceImpl<WaterQualityDao, WaterQualityEntity> implements WaterQualityService {

    private static String WATER_QUALITY_URL = "http://ep.nmemc.org.cn:8888/Water/water%s.json";

    @Autowired
    private WaterQualityDao waterQualityDao;


    public List<WaterQualityEntity> fetchWaterQualityData(String year) {
        String url = String.format(WATER_QUALITY_URL, year);

        HttpRequest request = new HttpRequest(url);
        HttpResponse httpResponse = request.execute();

        return JSONArray.parseArray(httpResponse.body(), WaterQualityEntity.class);
    }

    public void insertYearData(String year) {

        LambdaQueryWrapper<WaterQualityEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WaterQualityEntity::getYear, year);
        List<WaterQualityEntity> list = this.list(wrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            return;
        }

        List<WaterQualityEntity> waterQualityEntities = fetchWaterQualityData(year);
        waterQualityEntities = waterQualityEntities.stream()
                .peek(item -> {
                    item.setYear(year);
                    item.setMonth(StringUtils.substringAfterLast(item.getMinitorMonth(), "-"));
                })
                .collect(Collectors.toList());
        this.saveBatch(waterQualityEntities);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<WaterQualityEntity> page = new Page<>(Long.parseLong((String)params.get("page")), Long.parseLong((String)params.get("limit")));
        Page<WaterQualityEntity> resultPage = waterQualityDao.selectWaterQualityPage(page, params);

        return new PageUtils(resultPage);
    }

    @Override
    public List<PositionValueVO> selectList(String year, String month, String type) {
        List<PositionValueVO> positionValueVOS = waterQualityDao.selectWaterQualityData(year, month, type);

        positionValueVOS = positionValueVOS.stream().peek(item -> {
            BigDecimal bigDecimal = null;
            try {
                bigDecimal = new BigDecimal(item.getValue());
                //bigDecimal = bigDecimal.divide(new BigDecimal(10), 2, RoundingMode.HALF_UP);
                item.setValue(String.valueOf(bigDecimal));
            } catch (Exception e) {
                item.setValue("0");
            }
        }).collect(Collectors.toList());

        return positionValueVOS;
    }

    @Override
    public List<String> selectProvinceList() {
        List<WaterQualityEntity> list = this.list();
        Map<String, List<WaterQualityEntity>> collect = list.stream().collect(Collectors.groupingBy(WaterQualityEntity::getProvince));

        return new ArrayList<>(collect.keySet());
    }

    @Override
    public List<LegendVO> selectLegendList(String year, String province, String type) {
        return waterQualityDao.selectLegendList(year, province, type);
    }
}
