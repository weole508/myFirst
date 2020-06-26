package cn.smbms.service.user;

import cn.smbms.dao.user.UserMapper;
import cn.smbms.pojo.User;
import cn.smbms.tools.MD5encryption;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author：yusiyu
 * @Description：用户业务层实现类
 * @Date：Created in 9:55 2019/6/26
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    // 用户持久层映射
    @Resource
    private UserMapper userMapper;

    /**
     * 用户登陆功能实现
     * @param userCode
     * @param userPassword
     * @return
     * @throws Exception
     */
    @Override
    public User login(String userCode, String userPassword) throws Exception {
        User user = null;
        user = userMapper.getLoginUser(userCode);
        //匹配密码
        if(null != user){
            userPassword = MD5encryption.string2MD5(userPassword);
            if(!user.getUserPassword().equals(userPassword)) {
                user = null;
            }
        }
        return user;
    }

    /**
     * 获取用户列表实现
     * @param queryUserName
     * @param queryUserRole
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public List<User> getUserList(String queryUserName, Integer queryUserRole, Integer currentPageNo, Integer pageSize) throws Exception {
        currentPageNo = (currentPageNo - 1)*pageSize;
        return userMapper.getUserList(queryUserName,queryUserRole,currentPageNo,pageSize);
    }

    /**
     * 获取用户总数量实现
     * @param queryUserName
     * @param queryUserRole
     * @return
     * @throws Exception
     */
    @Override
    public int getUserCount(String queryUserName, Integer queryUserRole) throws Exception {
        return userMapper.getUserCount(queryUserName,queryUserRole);
    }

    /**
     * 添加用户实现
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public boolean add(User user) throws Exception {
        boolean flag = false;
        int updateRows = userMapper.add(user);
        if(updateRows > 0){
            flag = true;
        }
        return flag;
    }

    /**
     * 根据用户编码查询用户实现
     * @param userCode
     * @return
     * @throws Exception
     */
    @Override
    public User selectUserCodeExist(String userCode) throws Exception{
        return userMapper.getLoginUser(userCode);
    }

    /**
     * 根据用户id获取单个用户实现
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public User getuserById(Integer id) throws Exception {
        return userMapper.getUserById(id);
    }

    /**
     * 修改用户实现
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public boolean modify(User user) throws Exception {
        boolean flag = false;
        if (userMapper.modify(user) > 0){
            flag = true;
        }
        return flag;
    }

    /**
     * 修改用户密码实现
     * @param id
     * @param pwd
     * @return
     * @throws Exception
     */
    @Override
    public boolean updatePwd(int id, String pwd) throws Exception {
        boolean flag = false;
        if (userMapper.updatePwd(id,pwd) > 0){
            flag = true;
        }
        return flag;
    }

    /**
     * 根据用户id删除用户实现
     * @param delId
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteUserById(Integer delId) throws Exception {
        boolean flag = false;
         if(userMapper.deleteUserById(delId) > 0){
             flag = true;
         }
         return flag;
    }


}
