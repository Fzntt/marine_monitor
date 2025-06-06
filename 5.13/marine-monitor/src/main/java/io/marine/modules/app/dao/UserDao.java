/**
 *   
 *
 *  
 *
 * 版权所有，侵权必究！
 */

package io.marine.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.marine.modules.app.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

}
