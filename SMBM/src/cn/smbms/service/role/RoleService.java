package cn.smbms.service.role;

import cn.smbms.pojo.Role;

import java.util.List;

public interface RoleService {
    /**
     * 获取角色列表
     * @return
     * @throws Exception
     */
    public List<Role> getRoleList() throws Exception;

    /**
     * 获取角色总数量
     * @return
     * @throws Exception
     */
    public int getRoleCount() throws Exception;

    /**
     * 添加新角色
     * @param role
     * @return
     * @throws Exception
     */
    public boolean add(Role role) throws Exception;

    /**
     * 根据角色编码获取角色
     * @param roleCode
     * @return
     * @throws Exception
     */
    Role selectRoleCodeExist(String roleCode) throws Exception;

    /**
     * 修改供应商
     * @return
     */
    boolean modify(Role role) throws Exception;

    /**
     * 删除供应商
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteRoleById(Integer id)throws Exception;

    /**
     * 根据id获取用户角色
     * @param id
     * @return
     * @throws Exception
     */
    Role getRoleById(String id) throws Exception;
}
