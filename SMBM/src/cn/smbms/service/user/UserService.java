package cn.smbms.service.user;

import cn.smbms.pojo.User;

import java.util.List;

/**
 * @Author：yusiyu
 * @Description： 用户业务层接口
 * @Date：Created in 9:55 2019/6/26
 * @Version 1.0
 */
public interface UserService {
    /**
     * 用户登录
     * @param userCode
     * @param userPassword
     * @return
     */
    User login(String userCode, String userPassword) throws Exception;

    /**
     * 根据用户名称和角色名称查询用户
     * @param queryUserName
     * @param queryUserRole
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    List<User> getUserList(String queryUserName,Integer queryUserRole,Integer currentPageNo,Integer pageSize) throws Exception;

    /**
     * 获取用户总数量
     * @param queryUserName
     * @param queryUserRole
     * @return
     * @throws Exception
     */
    int getUserCount(String queryUserName,Integer queryUserRole) throws Exception;

    /**
     * 添加新用户
     * @param user
     * @return
     * @throws Exception
     */
    boolean add(User user) throws Exception;

    /**
     * 根据用户编码查询单个用户
     * @param userCode
     * @return
     * @throws Exception
     */
    User selectUserCodeExist(String userCode) throws Exception;

    /**
     * 获取单个用户通过用户id
     * @param id
     * @return
     * @throws Exception
     */
    User getuserById(Integer id) throws Exception;

    /**
     * 修改用户信息
     * @param user
     * @return
     * @throws Exception
     */
    boolean modify(User user) throws Exception;

    /**
     * 修改用户密码
     * @param id
     * @param pwd
     * @return
     * @throws Exception
     */
    boolean updatePwd(int id,String pwd)throws Exception;

    /**
     * 通过用户id删除用户
     * @param delId
     * @return
     * @throws Exception
     */
    boolean deleteUserById(Integer delId)throws Exception;
}
