package io.marine.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 水质监测数据表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2025-05-10 10:45:03
 */
@Data
@TableName("water_quality")
public class WaterQualityEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer id;
	/**
	 * 海域名称
	 */
	private String sea;
	/**
	 * 省份
	 */
	private String province;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 监测站点编号
	 */
	private String site;
	/**
	 * 经度
	 */
	private String lon;
	/**
	 * 纬度
	 */
	private String lat;
	/**
	 * 监测月份(YYYY-MM)
	 */
	private String minitorMonth;

	private String year;

	private String month;
	/**
	 * pH值
	 */
	private String ph;
	/**
	 * 溶解氧(mg/L)
	 */
	private String rjy;
	/**
	 * 化学需氧量(mg/L)
	 */
	private String hxxyl;
	/**
	 * 无机氮(mg/L)
	 */
	private String wjd;
	/**
	 * 活性磷酸盐
	 */
	private String hxlxy;
	/**
	 * 石油类(mg/L)
	 */
	private String syl;
	/**
	 * 水质类别
	 */
	private String szlb;

}
