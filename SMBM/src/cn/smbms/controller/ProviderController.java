package cn.smbms.controller;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.tools.ConstUtils;
import cn.smbms.tools.PageSupport;
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

@Controller
@RequestMapping("/sys/provider")
public class ProviderController {
    private Logger logger = Logger.getLogger(ProviderController.class);

    @Resource
    private ProviderService providerService;

    /**
     * 获取供应商列表
     * @param model
     * @param queryProCode
     * @param queryProName
     * @param pageIndex
     * @return
     */
    @RequestMapping("/list.html")
    public String getProviderList(Model model,
                              @RequestParam(value = "queryProCode",required = false) String queryProCode,
                              @RequestParam(value = "queryProName",required = false)String queryProName,
                              @RequestParam(value = "pageIndex",required = false)String pageIndex){
        logger.info("getProviderList ----------------- > queryProCode :" + queryProCode);
        logger.info("getProviderList ----------------- > queryProName :" + queryProName);
        logger.info("pageIndex ----------------- > pageIndex :" + pageIndex);
        List<Provider> providerList = null;
        int pageSize = ConstUtils.PAGE_SIZE;
        int currentPageNo = 1;
        if (queryProCode == null){
            queryProCode = "";
        }
        if (queryProName != null && !"".equals(queryProName)){
            queryProName = "";
        }
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
            totalCount = providerService.getProviderCount(queryProCode,queryProName);
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
            providerList = providerService.getProviderList(queryProCode,queryProName,currentPageNo,pageSize);
        } catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute("providerList",providerList);
        model.addAttribute("queryProCode",queryProCode);
        model.addAttribute("queryProName",queryProName);
        model.addAttribute("totalPageCount",totalPageCount);
        model.addAttribute("totalCount",totalCount);
        model.addAttribute("currentPageNo",currentPageNo);
        return "providerlist";
    }

    /**
     * 添加供应商
     * @param provider
     * @return
     */
    @RequestMapping("/add.html")
    public String addProvider(@ModelAttribute("provider") Provider provider){
        return "provideradd";
    }

    @RequestMapping(value = "/addsave.html",method = RequestMethod.POST)
    public String addProviderAddSave(Provider provider, HttpSession session, HttpServletRequest request,
                                     @RequestParam(value = "attachs",required =false) MultipartFile[] attachs) {
        String companyLicPicPath = null;
        String orgCodePicPath = null;
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
                    request.setAttribute(errorInfo,"上传大小不得超过500k");
                    flag = false;
                } else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") ||
                        prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){
                    String fileName = System.currentTimeMillis() + RandomUtils.nextInt() + "_Provider.jpg";
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
                        request.setAttribute(errorInfo,"上传失败");
                        flag = false;
                    }
                    if ( i == 0 ) {
                        companyLicPicPath = path + File.separator + fileName;
                    } else if( i == 1){
                        orgCodePicPath = path + File.separator + fileName;
                    }
                    logger.debug("idPicPath ============ >" + companyLicPicPath);
                    logger.debug("workPicPath ===========>" + orgCodePicPath);
                }else {
                    request.setAttribute(errorInfo,"* 上传图片格式不正确");
                    flag = false;
                }
            }
        }
        System.out.println(flag);
        if (flag){
            provider.setCreatedBy(((User)session.getAttribute(ConstUtils.USER_SESSION)).getId());
            provider.setCreationDate(new Date());
            provider.setCompanyLicPicPath(companyLicPicPath);
            provider.setOrgCodePicPath(orgCodePicPath);
            try {
                if (providerService.add(provider)){
                    return "redirect:/sys/provider/list.html";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "provideradd";
    }

    @RequestMapping(value = "/proCode.json")
    @ResponseBody
    public Object proCodeIsExist(@RequestParam String proCode) throws Exception{
        logger.debug("proCodeIsExist proCode ================= " + proCode);
        HashMap<String ,String> resultMap = new HashMap<>();
        if(StringUtils.isNullOrEmpty(proCode)){
            resultMap.put("proCode","exist");
        }else {
            boolean flag = providerService.selectProCodeExist(proCode);
            System.out.println(flag);
            if (flag){
                resultMap.put("proCode","exist");
            }else {
                resultMap.put("proCode","noexist");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    /**
     * 修改供应商
     * @param pid
     * @param model
     * @return
     */
    @RequestMapping(value = "/modify/{pid}",method = RequestMethod.GET)
    public String getProviderById(@PathVariable String pid,Model model){
        logger.info("getProviderById pid ===============" + pid);
        Provider provider = null;
        try {
            provider = providerService.getProviderById(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(provider);
        return "providermodify";

    }

    @RequestMapping(value = "/modifysave.html",method = RequestMethod.POST)
    public String modifyprovidersave(Provider provider,HttpSession session){
        logger.info("modifyprovidersave providerid =================" + provider.getId());
        provider.setModifyBy(((User)session.getAttribute(ConstUtils.USER_SESSION)).getId());
        provider.setModifyDate(new Date());
        try {
            if(providerService.modify(provider)){
                return "redirect:/sys/provider/list.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "providermodify";
    }

    /**
     * 查看供应商
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/view/{id}",method = RequestMethod.GET)
    public String view(@PathVariable String id, Model model){
        logger.info("view id ===============" + id);
        Provider provider = null;
        try {
            provider = providerService.getProviderById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(provider);
        return "providerview";
    }

    /**
     * 删除供应商
     * @param proid
     * @return
     */
    @RequestMapping(value = "/delprovider.json",method = RequestMethod.GET)
    @ResponseBody
    public Object delProvider(@RequestParam String proid){
        System.out.println(proid);
        HashMap<String ,String> resultMap = new HashMap<>();
        if (StringUtils.isNullOrEmpty(proid)){
            resultMap.put("delResult","notexist");
        }else {
            try {
                if (providerService.deleteProviderById(Integer.parseInt(proid))){
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
