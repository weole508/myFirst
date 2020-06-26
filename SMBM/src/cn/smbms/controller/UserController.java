package cn.smbms.controller;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.*;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author：yusiyu
 * @Description：用户表现层
 * @Date：Created in 10:00 2019/6/26
 * @Version 2.0
 */

@Controller
// 定义一级目录
@RequestMapping("/sys/user")
public class UserController extends BaseController {

    private Logger logger = Logger.getLogger(UserController.class);

    //注入用户业务层对象
    @Resource
    private UserService userService;

    // 注入角色业务层对象
    @Resource
    RoleService roleService;

    /**
     * 查询用户列表
     * @param model
     * @param queryUserName
     * @param queryUserRole
     * @param pageIndex
     * @return
     */
    // 映射查询用户列表
    @RequestMapping("/list.html")
    public String getUserList(Model model,
                              @RequestParam(value = "queryname",required = false) String queryUserName,
                              @RequestParam(value = "queryUserRole",required = false)String queryUserRole,
                              @RequestParam(value = "pageIndex",required = false)String pageIndex){
        logger.info("getUserList ----------------- > queryname :" + queryUserName);
        logger.info("queryUserRole ----------------- > queryUserRole :" + queryUserRole);
        logger.info("pageIndex ----------------- > pageIndex :" + pageIndex);
        Integer _queryUserRole = null;
        List<User> userList = null;
        List<Role> roleList = null;
        int pageSize = ConstUtils.PAGE_SIZE;
        int currentPageNo = 1;
        if (queryUserName == null){
            queryUserName = "";
        }
        if(!StringUtils.isNullOrEmpty(queryUserRole)){
            _queryUserRole = Integer.parseInt(queryUserRole);
        }
        if (pageIndex != null){
            try{
                currentPageNo = Integer.valueOf(pageIndex);
            } catch (NumberFormatException e){
                //异常发生时跳转到错误页面
                return "redirect:/syserror.html";
            }
        }
        //总量表
        int totalCount = 0;
        try{
            totalCount = userService.getUserCount(queryUserName,_queryUserRole);
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
            userList = userService.getUserList(queryUserName,_queryUserRole,currentPageNo,pageSize);
            roleList = roleService.getRoleList();
        } catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute("userList",userList);
        model.addAttribute("roleList",roleList);
        model.addAttribute("queryUserName",queryUserName);
        model.addAttribute("queryUserRole",queryUserRole);
        model.addAttribute("totalPageCount",totalPageCount);
        model.addAttribute("totalCount",totalCount);
        model.addAttribute("currentPageNo",currentPageNo);
        return "userlist";
    }

    /**
     * 添加用户页面
     * @param user
     * @return
     */
    @RequestMapping(value = "/add.html",method = RequestMethod.GET)
    public String addUser(@ModelAttribute("user") User user){
        return "useradd";
    }

    /**
     * 保存用户添加的信息
     * @param user
     * @param session
     * @param request
     * @param attachs
     * @return
     */
    //多文件上传
    @RequestMapping(value = "/addsave.html",method = RequestMethod.POST)
    public String addUserSave(User user, HttpSession session, HttpServletRequest request,
                              @RequestParam(value = "attachs",required =false) MultipartFile[] attachs){
        String idPicPath = null;
        String workPicPath = null;
        String errorInfo = null;
        boolean flag = true;
        String path = request.getSession().getServletContext().getRealPath("statics" + File.separator + "uploadfiles");
        logger.info("uploadFile path ==============> " + path);
        for (int i = 0; i < attachs.length; i++) {
            MultipartFile attach = attachs[i];
            if (!attach.isEmpty()){
                if (i == 0){
                    errorInfo = "uploadFileError";
                } else if( i == 1) {
                    errorInfo = "uploadWpError";
                }
                String oldFileName = attach.getOriginalFilename();
                logger.info("uploadFile oldFileName ========== >" + oldFileName);
                String prefix = FilenameUtils.getExtension(oldFileName);
                logger.info("uploadFile prefix ============>" + prefix);
                int filesize = 500000;
                logger.info("uploadFile filesize ========>" + filesize);
                if(attach.getSize() > filesize){
                    request.setAttribute(errorInfo, MessageUtils.SMBMS_0401_001);
                    flag = false;
                } else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") ||
                        prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){
                    String fileName = System.currentTimeMillis() + RandomUtils.nextInt() + "_Personal.jpg";
                    logger.info("uploadFile fileName ========>" + fileName);
                    File targetFile = new File(path,fileName);
                    if (!targetFile.exists()){
                        targetFile.mkdirs();
                    }
                    //保存
                    try{
                        attach.transferTo(targetFile);
                    } catch (Exception e){
                        e.printStackTrace();
                        request.setAttribute(errorInfo,MessageUtils.SMBMS_0401_002);
                        flag = false;
                    }
                    if ( i == 0 ) {
                        idPicPath = path + File.separator + fileName;
                    } else if( i == 1){
                        workPicPath = path + File.separator + fileName;
                    }
                    logger.debug("idPicPath ============ >" + idPicPath);
                    logger.debug("workPicPath ===========>" + workPicPath);
                }else {
                    request.setAttribute(errorInfo,MessageUtils.SMBMS_0401_003);
                    flag = false;
                }
            }
        }
        System.out.println(flag);
        if (flag){
            user.setCreatedBy(((User)session.getAttribute(ConstUtils.USER_SESSION)).getId());
            user.setCreationDate(CommonUtils.newDate());
            user.setIdPicPath(idPicPath);
            user.setWorkPicPath(workPicPath);
            user.setUserPassword(MD5encryption.string2MD5(user.getUserPassword()));
            System.out.println(user.getUserPassword());

            boolean a  = false;
            try {
                a = userService.add(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (a){
                return "redirect:/sys/user/list.html";
            }
        }
        return "useradd";
    }

    /**
     * 根据用户编码查询用户是否存在
     * @param userCode
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ucexist.json")
    @ResponseBody
    public Object userCodeIsExist(@RequestParam String userCode) throws Exception{
        logger.debug("userCodeIsExist userCode ================= " + userCode);
        HashMap<String ,String> resultMap = new HashMap<>();
        if(StringUtils.isNullOrEmpty(userCode)){
            resultMap.put("userCode","exist");
        }else {
            User user = userService.selectUserCodeExist(userCode);
            if (user != null){
                resultMap.put("userCode","exist");
            }else {
                resultMap.put("userCode","noexist");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    /**
     * 获取用户角色信息
     * @return
     */
    @RequestMapping(value = "/rolelist.json",method = RequestMethod.GET)
    @ResponseBody
    public Object getRole(){
        List<Role> roleList = null;
        try {
            roleList = roleService.getRoleList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("roleList size =======>" +roleList);
        return JSONArray.toJSONString(roleList);
    }

    /**
     * 根据用户id修改对应用户的信息
     * @param id
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/modify/{id}",method = RequestMethod.GET)
    public String getUserId(@PathVariable String id, Model model,HttpServletRequest request){
        logger.debug("getUserById:" + id);
        User user = null;
        try {
            user = userService.getuserById(Integer.parseInt(id));
            if (!StringUtils.isNullOrEmpty(user.getIdPicPath())){
                String[] paths = user.getIdPicPath().split("\\" + File.separator);
                logger.debug("view picPath" + paths[paths.length - 1]);
                user.setIdPicPath(request.getContextPath() + "/statics/uploadfiles/" + paths[paths.length - 1]);
            }
            if (!StringUtils.isNullOrEmpty(user.getWorkPicPath())){
                String[] paths = user.getWorkPicPath().split("\\" + File.separator);
                logger.debug("view workPicPath" + paths[paths.length - 1]);
                user.setWorkPicPath(request.getContextPath() + "/statics/uploadfiles/" + paths[paths.length - 1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(user);
        return "usermodify";
    }

    /**
     * 用户修改信息保存
     * @param user
     * @param session
     * @param request
     * @param attachs
     * @return
     */
    @RequestMapping(value="/modifysave.html",method=RequestMethod.POST)
    public String modifyUserSave(User user,HttpSession session,HttpServletRequest request,
                                 @RequestParam(value ="attachs", required = false) MultipartFile[] attachs){
        logger.debug("modifyUserSave id===================== "+user.getId());
        String idPicPath = null;
        String workPicPath = null;
        String errorInfo = null;
        boolean flag = true;
        String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
        logger.info("uploadFile path ============== > "+path);
        if(attachs != null){
            for(int i = 0;i < attachs.length ;i++){
                MultipartFile attach = attachs[i];
                if(!attach.isEmpty()){
                    if(i == 0){
                        errorInfo = "uploadFileError";
                    }else if(i == 1){
                        errorInfo = "uploadWpError";
                    }

                    //原文件名
                    String oldFileName = attach.getOriginalFilename();
                    logger.info("uploadFile oldFileName ============== > "+oldFileName);

                    //原文件后缀
                    String prefix= FilenameUtils.getExtension(oldFileName);
                    logger.debug("uploadFile prefix============> " + prefix);
                    int filesize = 500000;
                    logger.debug("uploadFile size============> " + attach.getSize());
                    if(attach.getSize() >  filesize){//上传大小不得超过 500k
                        request.setAttribute(errorInfo, MessageUtils.SMBMS_0401_001);
                        flag = false;

                        //上传图片格式不正确
                    }else if("jpg".equalsIgnoreCase(prefix) || "png".equalsIgnoreCase(prefix)
                            || "jpeg".equalsIgnoreCase(prefix) || "pneg".equalsIgnoreCase(prefix)){
                        String fileName = System.currentTimeMillis()+ RandomUtils.nextInt(1000000)+"_Personal.jpg";
                        logger.debug("new fileName======== " + attach.getName());
                        File targetFile = new File(path, fileName);
                        if(!targetFile.exists()){
                            targetFile.mkdirs();
                        }

                        //保存
                        try {
                            attach.transferTo(targetFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                            request.setAttribute(errorInfo, MessageUtils.SMBMS_0401_002);
                            flag = false;
                        }
                        if(i == 0){
                            idPicPath = path+File.separator+fileName;
                        }else if(i == 1){
                            workPicPath = path+File.separator+fileName;
                        }
                        logger.debug("idPicPath: " + idPicPath);
                        logger.debug("workPicPath: " + workPicPath);

                    }else{
                        request.setAttribute(errorInfo, MessageUtils.SMBMS_0401_003);
                        flag = false;
                    }
                }
            }
        }
        if(flag){
            user.setModifyBy(((User)session.getAttribute(ConstUtils.USER_SESSION)).getId());
            user.setModifyDate(new Date());
            user.setIdPicPath(idPicPath);
            user.setWorkPicPath(workPicPath);
            user.setUserPassword(MD5encryption.string2MD5(user.getUserPassword()));
            try {
                if(userService.modify(user)){
                    return "redirect:/sys/user/list.html";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "usermodify";
    }

    /**
     * 用户修改密码页面跳转
     * @param session
     * @return
     */
    @RequestMapping(value = "/pwdmodify.html",method = RequestMethod.GET)
    public String pwdModify(HttpSession session){
        if(session.getAttribute(ConstUtils.USER_SESSION) == null){
            return "redirect:/sys/user/login.html";
        }
        return "pwdmodify";
    }

    /**
     * 用户密码修改前确认原密码
     * @param oldpassword
     * @param session
     * @return
     */
    @RequestMapping(value = "/pwdmodify.json",method = RequestMethod.GET)
    @ResponseBody
    public Object getPwdByUserId(@RequestParam String oldpassword,HttpSession session){
        logger.debug("getPwdByUserId ==============" + oldpassword);
        HashMap<String,String> resultMap = new HashMap<>();
        if (null == session.getAttribute(ConstUtils.USER_SESSION)){
            resultMap.put("result","sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){
            resultMap.put("result","error");
        }else {
            String sessionPwd = ((User)session.getAttribute(ConstUtils.USER_SESSION)).getUserPassword();
            System.out.println(sessionPwd);
            oldpassword =MD5encryption.string2MD5(oldpassword);
            if (oldpassword.equals(sessionPwd)){
                resultMap.put("result","true");
            }else {
                resultMap.put("result","false");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    /**
     * 保存用户新密码
     * @param newPassword
     * @param session
     * @param request
     * @return
     */
    @RequestMapping("/pwdsave.html")
    public String pwdSave(@RequestParam(value = "newpassword") String newPassword,
                          HttpSession session,HttpServletRequest request){
        boolean flag = false;
        Object o = session.getAttribute(ConstUtils.USER_SESSION);
        if (o != null && !StringUtils.isNullOrEmpty(newPassword)){
            try {
                newPassword = MD5encryption.string2MD5(newPassword);
                flag = userService.updatePwd(((User)o).getId(),newPassword);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (flag){
                request.setAttribute(ConstUtils.SYS_MESSAGE,MessageUtils.SMBMS_0401_004);

                //注销sessison
                session.removeAttribute(ConstUtils.SYS_MESSAGE);
                return "login";
            }else {
                request.setAttribute(ConstUtils.SYS_MESSAGE,MessageUtils.SMBMS_0401_005);
            }
        }else {
            request.setAttribute(ConstUtils.SYS_MESSAGE,MessageUtils.SMBMS_0401_005);
        }
        return "pwdmodify";
    }

    /**
     * 用户删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/deluser.json",method = RequestMethod.GET)
    @ResponseBody
    public Object deluser(@RequestParam String id){
        HashMap<String ,String> resultMap = new HashMap<>();
        if (StringUtils.isNullOrEmpty(id)){
            resultMap.put("delResult","notexist");
        }else {
            try {
                if (userService.deleteUserById(Integer.parseInt(id))){
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

    /**
     * 获取单个用户信息
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/view/{id}",method = RequestMethod.GET)
    public String view(@PathVariable String id,Model model){
        logger.info("view id ===============" + id);
        User user = null;
        try {
            user = userService.getuserById(Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(user);
        return "userview";
    }

}
