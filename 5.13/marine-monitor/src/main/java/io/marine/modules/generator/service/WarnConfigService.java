package io.marine.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.marine.common.utils.PageUtils;
import io.marine.modules.generator.entity.WarnConfigEntity;

import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2025-05-12 20:30:07
 */
public interface WarnConfigService extends IService<WarnConfigEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

