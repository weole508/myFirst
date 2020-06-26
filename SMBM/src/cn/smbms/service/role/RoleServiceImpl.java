package cn.smbms.service.role;

import cn.smbms.dao.role.RoleMapper;
import cn.smbms.pojo.Role;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRoleList() throws Exception {
        return roleMapper.getRoleList();
    }

    @Override
    public int getRoleCount() throws Exception {
        return roleMapper.getRoleCount();
    }

    @Override
    public boolean add(Role role) throws Exception {
        return roleMapper.add(role);
    }

    @Override
    public Role selectRoleCodeExist(String roleCode) throws Exception {
        return roleMapper.selectRoleCodeExist(roleCode);
    }

    @Override
    public boolean modify(Role role) throws Exception {
        boolean flag = false;
        if (roleMapper.modify(role) > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean deleteRoleById(Integer id) throws Exception {
        boolean flag = false;
        if (roleMapper.deleteRoleById(id) > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public Role getRoleById(String id) throws Exception {
        return roleMapper.getRoleById(id);
    }
}
