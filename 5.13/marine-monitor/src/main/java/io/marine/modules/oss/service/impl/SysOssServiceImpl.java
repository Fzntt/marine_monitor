/**
 *   
 *
 *  
 *
 * 版权所有，侵权必究！
 */

package io.marine.modules.oss.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.marine.common.utils.PageUtils;
import io.marine.common.utils.Query;
import io.marine.modules.oss.dao.SysOssDao;
import io.marine.modules.oss.entity.SysOssEntity;
import io.marine.modules.oss.service.SysOssService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("sysOssService")
public class SysOssServiceImpl extends ServiceImpl<SysOssDao, SysOssEntity> implements SysOssService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<SysOssEntity> page = this.page(
			new Query<SysOssEntity>().getPage(params)
		);

		return new PageUtils(page);
	}

}
