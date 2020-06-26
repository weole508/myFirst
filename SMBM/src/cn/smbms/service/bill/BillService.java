package cn.smbms.service.bill;

import cn.smbms.pojo.Bill;

import java.util.List;

public interface BillService {

    /**
     * 获取供应商列表
     * @return
     */
    List<Bill> getBillList(String strQueryProductName, String strQueryProviderId,Integer strIsPayment, int currentPageNo, int pageSize) throws Exception;

    /**
     * 获取供应商总数量
     * @return
     */
    int getBillCount(String strQueryProductName,String strQueryProviderId) throws Exception;

    /**
     * 添加订单
     * @param bill
     * @return
     * @throws Exception
     */
    boolean add(Bill bill) throws Exception;

    /**
     * 根据id获取单个订单
     * @param id
     * @return
     * @throws Exception
     */
    Bill getBillById(Integer id) throws Exception;

    /**
     * 修改供应商
     * @return
     */
    boolean modify(Bill bill) throws Exception;

    /**
     * 删除供应商
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteBillById(Integer id)throws Exception;
}
