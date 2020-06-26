package cn.smbms.dao.bill;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BillMapper {
    /**
     * 获取订单列表
     * @param productName
     * @param providerId
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    List<Bill> getBillList(@Param("productName") String productName, @Param("providerId") String providerId,@Param("isPayment") Integer isPayment,
                           @Param("from") Integer currentPageNo, @Param("pageSize") Integer pageSize) throws Exception;

    /**
     * 获取订单总数量
     * @param productName
     * @param providerId
     * @return
     * @throws Exception
     */
    int getBillCount(@Param("productName") String productName,@Param("providerId") String providerId) throws Exception;

    /**
     * 添加订单
     * @param bill
     * @return
     * @throws Exception
     */
    int add(Bill bill) throws Exception;

    /**
     * 根据id获取单个订单信息
     * @param id
     * @return
     * @throws Exception
     */
    Bill getBillById(Integer id) throws Exception;

    /**
     * 修改订单信息
     * @param bill
     * @return
     * @throws Exception
     */
    int modify(Bill bill) throws Exception;

    /**
     * 根据订单id删除订单
     * @param id
     * @return
     * @throws Exception
     */
    int deleteBillById(@Param("id") Integer id) throws Exception;
}
