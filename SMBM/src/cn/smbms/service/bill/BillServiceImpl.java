package cn.smbms.service.bill;

import cn.smbms.dao.bill.BillMapper;
import cn.smbms.pojo.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillServiceImpl  implements BillService{

    @Autowired
    private BillMapper billMapper;

    @Override
    public List<Bill> getBillList(String strQueryProductName, String strQueryProName, Integer strIsPayment ,int currentPageNo, int pageSize) throws Exception {
        currentPageNo = (currentPageNo - 1)*pageSize;
        return billMapper.getBillList(strQueryProductName,strQueryProName,strIsPayment,currentPageNo,pageSize);
    }

    @Override
    public int getBillCount(String strQueryProductName, String serQueryProName) throws Exception {
        return billMapper.getBillCount(strQueryProductName,serQueryProName);
    }

    @Override
    public boolean add(Bill bill) throws Exception {
        boolean flag = false;
        int updateRows = billMapper.add(bill);
        if(updateRows > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public Bill getBillById(Integer id) throws Exception {
        return billMapper.getBillById(id);
    }

    @Override
    public boolean modify(Bill bill) throws Exception {
        boolean flag = false;
        if (billMapper.modify(bill) > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean deleteBillById(Integer id) throws Exception {
        boolean flag = false;
        if (billMapper.deleteBillById(id) > 0){
            flag = true;
        }
        return flag;
    }

}
