package cn.smbms.dao.role;

import cn.smbms.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    /**
     * 查询供应商列表
     * @return
     * @throws Exception
     */
    List<Role> getRoleList() throws Exception;

    /**
     * 查询用户角色总数量
     * @return
     * @throws Exception
     */
    int getRoleCount() throws Exception;

    /**
     * 添加新的角色
     * @param role
     * @return
     * @throws Exception
     */
    boolean add(Role role) throws Exception;

    /**
     * 根据角色编码获取角色查询角色
     * @param roleCode
     * @return
     * @throws Exception
     */
    Role selectRoleCodeExist(String roleCode) throws Exception;

    /**
     * 根据id获取角色
     * @param id
     * @return
     * @throws Exception
     */
    Role getRoleById(@Param("id") String id) throws Exception;

    /**
     * 修改供应商信息
     * @param role
     * @return
     * @throws Exception
     */
    int modify(Role role) throws Exception;

    /**
     * 根据id删除用户
     * @param id
     * @return
     * @throws Exception
     */
    int deleteRoleById(@Param("id") Integer id) throws Exception;
}
