package cn.smbms.controller;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.tools.CommonUtils;
import cn.smbms.tools.ConstUtils;
import cn.smbms.tools.PageSupport;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/sys/role")
public class RoleController {

    private Logger logger = Logger.getLogger(RoleController.class);

    @Resource
    private RoleService roleService;

    /**
     * 获取角色列表
     * @param model
     * @param pageIndex
     * @return
     */
    @RequestMapping("/list.html")
    public String getProviderList(Model model,
                                  @RequestParam(value = "pageIndex",required = false)String pageIndex){
        logger.info("pageIndex ----------------- > pageIndex :" + pageIndex);
        List<Role> roleList = null;
        int pageSize = ConstUtils.PAGE_SIZE;
        int currentPageNo = 1;
        if (pageIndex != null){
            try{
                currentPageNo = Integer.valueOf(pageIndex);
            } catch (NumberFormatException e){
                return "redirect:/syserror.html";
            }
        }
        //总量表
        int totalCount = 0;
        try{
            totalCount = roleService.getRoleCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //总页数
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);
        int totalPageCount = pageSupport.getTotalPageCount();
        //控制首页和尾页
        if(currentPageNo < 1){
            currentPageNo = 1;
        } else if(currentPageNo > totalCount){
            currentPageNo = totalPageCount;
        }
        try{
            roleList = roleService.getRoleList();
        } catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute("roleList",roleList);
        for (Role role : roleList) {
            System.out.println(role.getRoleName());
        }
        model.addAttribute("totalPageCount",totalPageCount);
        model.addAttribute("totalCount",totalCount);
        model.addAttribute("currentPageNo",currentPageNo);
        return "rolelist";
    }

    /**
     * 添加角色页面
     * @param role
     * @return
     */
    @RequestMapping(value = "/add.html",method = RequestMethod.GET)
    public String addRole(@ModelAttribute("role") Role role){
        return "roleadd";
    }

    @RequestMapping(value = "/addsave.html",method = RequestMethod.POST)
    public String addProviderAddSave(Role role, HttpSession session) {
            try {
                role.setCreatedBy(((User)session.getAttribute(ConstUtils.USER_SESSION)).getId());
                role.setCreationDate(CommonUtils.newDate());
                if (roleService.add(role)){
                    return "redirect:/sys/role/list.html";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        return "roleadd";
    }

    /**
     * 根据角色编码查询用户是否存在
     * @param roleCode
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/rcexist.json")
    @ResponseBody
    public Object userCodeIsExist(@RequestParam String roleCode) throws Exception{
        logger.debug("userCodeIsExist roleCode ================= " + roleCode);
        HashMap<String ,String> resultMap = new HashMap<>();
        if(StringUtils.isNullOrEmpty(roleCode)){
            resultMap.put("roleCode","exist");
        }else {
            Role role = roleService.selectRoleCodeExist(roleCode);
            if (role != null){
                resultMap.put("roleCode","exist");
            }else {
                resultMap.put("roleCode","noexist");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    /**
     * 修改角色信息
     * @param rid
     * @param model
     * @return
     */
    @RequestMapping(value = "/modify/{rid}",method = RequestMethod.GET)
    public String getRoleById(@PathVariable String rid,Model model){
        logger.info("getRoleById rid ===============" + rid);
        Role role = null;
        try {
            role = roleService.getRoleById(rid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(role);
        return "rolemodify";

    }

    @RequestMapping(value = "/modifysave.html",method = RequestMethod.POST)
    public String modifyrolesave(Role role,HttpSession session){
        logger.info("modifyrolesave roleid =================" + role.getId());
        role.setModifyBy(((User)session.getAttribute(ConstUtils.USER_SESSION)).getId());
        role.setModifyDate(new Date());
        try {
            if(roleService.modify(role)){
                return "redirect:/sys/role/list.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "rolemodify";
    }

    /**
     * 删除角色
     * @param roleid
     * @return
     */
    @RequestMapping(value = "/delrole.json",method = RequestMethod.GET)
    @ResponseBody
    public Object delRole(@RequestParam String roleid){
        System.out.println(roleid);
        HashMap<String ,String> resultMap = new HashMap<>();
        if (StringUtils.isNullOrEmpty(roleid)){
            resultMap.put("delResult","notexist");
        }else {
            try {
                if (roleService.deleteRoleById(Integer.parseInt(roleid))){
                    resultMap.put("delResult","true");
                }else {
                    resultMap.put("delResult","false");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return JSONArray.toJSONString(resultMap);
    }
}
