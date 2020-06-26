package cn.smbms.dao.user;

import cn.smbms.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.sql.Connection;
import java.util.List;

/**
 * @Description：用户持久层接口
 */
public interface UserMapper {
    /**
     * 通过userCode获取User
     * @param userCode
     * @return
     * @throws Exception
     */
    User getLoginUser(@Param("userCode")String userCode)throws Exception;

    /**
     * 根据用户的名称与角色模糊查询获取用户列表
     * @param userName
     * @param userRole
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    List<User> getUserList(@Param("userName") String userName,@Param("userRole") Integer userRole,
                                  @Param("from") Integer currentPageNo,@Param("pageSize") Integer pageSize) throws Exception;

    /**
     *  获取用户总数量
     * @param userName
     * @param userRole
     * @return
     * @throws Exception
     */
    int getUserCount(@Param("userName") String userName,@Param("userRole") Integer userRole) throws Exception;

    /**
     * 添加新的用户
     * @param user
     * @return
     * @throws Exception
     */
    int add(User user) throws Exception;

    /**
     * 根据用户id获取用户信息
     * @param id
     * @return
     * @throws Exception
     */
    User getUserById(@Param("id") Integer id) throws Exception;

    /**
     * 修改用户信息
     * @param user
     * @return
     * @throws Exception
     */
    int modify(User user) throws Exception;

    /**
     * 更新用户密码
     * @param id
     * @param userPassword
     * @return
     * @throws Exception
     */
    int updatePwd(@Param("id") Integer id,@Param("userPassword") String userPassword) throws Exception;

    /**
     * 根据用户id删除用户
     * @param id
     * @return
     * @throws Exception
     */
    int deleteUserById(@Param("id") Integer id) throws Exception;
}
