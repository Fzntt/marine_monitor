package io.marine.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.marine.common.utils.PageUtils;
import io.marine.common.utils.Query;

import io.marine.modules.generator.dao.WarnConfigDao;
import io.marine.modules.generator.entity.WarnConfigEntity;
import io.marine.modules.generator.service.WarnConfigService;


@Service("warnConfigService")
public class WarnConfigServiceImpl extends ServiceImpl<WarnConfigDao, WarnConfigEntity> implements WarnConfigService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WarnConfigEntity> page = this.page(
                new Query<WarnConfigEntity>().getPage(params),
                new QueryWrapper<WarnConfigEntity>()
        );

        return new PageUtils(page);
    }

}
