package cn.smbms.controller;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillService;
import cn.smbms.service.provider.ProviderService;
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
@RequestMapping("sys/bill")
public class BillController {

    private Logger logger = Logger.getLogger(BillController.class);

    @Resource
    private BillService billService;

    @Resource
    ProviderService providerService;

    @RequestMapping("/list.html")
    public String getBillList(Model model,
                                  @RequestParam(value = "strQueryProductName",required = false) String strQueryProductName,
                                  @RequestParam(value = "strQueryProviderId",required = false)String strQueryProviderId,
                                  @RequestParam(value = "strIsPayment",required = false)String strIsPayment,
                                  @RequestParam(value = "pageIndex",required = false)String pageIndex){
        logger.info("getBillList ----------------- > strQueryProductName :" + strQueryProductName);
        logger.info("getBillList ----------------- > strQueryProName :" + strQueryProviderId);
        logger.info("getBillList ----------------- > strIsPayment :" + strIsPayment);
        logger.info("pageIndex ---------------- > pageIndex :" + pageIndex);
        List<Bill> billList = null;
        List<Provider> providerList = null;
        Integer _intIsPayment = null;
        int pageSize = ConstUtils.PAGE_SIZE;
        int currentPageNo = 1;
        if (strQueryProductName == null){
            strQueryProductName = "";
        }
        if (strQueryProviderId == null){
            strQueryProviderId = "";
        }
        if (strIsPayment == null){
            strIsPayment = "";
        }
        if(!StringUtils.isNullOrEmpty(strIsPayment)){
            _intIsPayment = Integer.parseInt(strIsPayment);
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
            totalCount = billService.getBillCount(strQueryProductName,strQueryProviderId);
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
            billList = billService.getBillList(strQueryProductName,strQueryProviderId,_intIsPayment,currentPageNo,pageSize);
            providerList = providerService.getAllProvider();
        } catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute("billList",billList);
        model.addAttribute("providerList",providerList);
        model.addAttribute("queryProductName",strQueryProductName);
        model.addAttribute("strQueryProviderId",strQueryProviderId);
        model.addAttribute("queryIsPayment",strIsPayment);
        model.addAttribute("totalPageCount",totalPageCount);
        model.addAttribute("totalCount",totalCount);
        model.addAttribute("currentPageNo",currentPageNo);
        return "billlist";
    }

    /**
     * 添加订单页面
     * @param bill
     * @return
     */
    @RequestMapping(value = "/add.html",method = RequestMethod.GET)
    public String addRole(@ModelAttribute("bill") Bill bill){
        return "billadd";
    }

    @RequestMapping(value = "/addsave.html",method = RequestMethod.POST)
    public String addBillAddSave(Bill bill, HttpSession session) {
        try {
            bill.setCreatedBy(((User)session.getAttribute(ConstUtils.USER_SESSION)).getId());
            bill.setCreationDate(CommonUtils.newDate());
            if (billService.add(bill)){
                return "redirect:/sys/bill/list.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "billadd";
    }

    /**
     * 获取供应商信息
     * @return
     */
    @RequestMapping(value = "/providerlist.json",method = RequestMethod.GET)
    @ResponseBody
    public Object getRole(){
        List<Provider> providerList = null;
        try {
            providerList = providerService.getAllProvider();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("providerList size =======>" +providerList);
        return JSONArray.toJSONString(providerList);
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
        Bill bill = null;
        try {
            bill = billService.getBillById(Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(bill);
        return "billview";
    }

    /**
     * 修改订单信息
     * @param bid
     * @param model
     * @return
     */
    @RequestMapping(value = "/modify/{bid}",method = RequestMethod.GET)
    public String getBillById(@PathVariable String bid,Model model){
        logger.info("getBillById rid ===============" + bid);
        Bill bill = null;
        try {
            bill = billService.getBillById(Integer.parseInt(bid));
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(bill);
        return "billmodify";

    }

    @RequestMapping(value = "/modifysave.html",method = RequestMethod.POST)
    public String modifyBillsave(Bill bill,HttpSession session){
        logger.info("modifyBillsave billid =================" + bill.getId());
        bill.setModifyBy(((User)session.getAttribute(ConstUtils.USER_SESSION)).getId());
        bill.setModifyDate(new Date());
        try {
            if(billService.modify(bill)){
                return "redirect:/sys/bill/list.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "billodify";
    }

    /**
     * 删除角色
     * @param billid
     * @return
     */
    @RequestMapping(value = "/delbill.json",method = RequestMethod.GET)
    @ResponseBody
    public Object delBill(@RequestParam String billid){
        HashMap<String ,String> resultMap = new HashMap<>();
        if (StringUtils.isNullOrEmpty(billid)){
            resultMap.put("delResult","notexist");
        }else {
            try {
                if (billService.deleteBillById(Integer.parseInt(billid))){
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
