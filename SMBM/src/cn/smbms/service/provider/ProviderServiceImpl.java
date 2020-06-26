package cn.smbms.service.provider;

import cn.smbms.dao.provider.ProviderMapper;
import cn.smbms.pojo.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProviderServiceImpl implements ProviderService{
    @Autowired
    private ProviderMapper providerMapper;

    @Override
    public List<Provider> getProviderList(String queryProCode, String queryProName, int currentPageNo, int pageSize) throws Exception{
        currentPageNo = (currentPageNo - 1)*pageSize;
        return providerMapper.getProviderList(queryProCode,queryProName,currentPageNo,pageSize);
    }

    @Override
    public int getProviderCount(String queryProCode, String queryProName) throws Exception{
        return providerMapper.getProviderCount(queryProCode,queryProName);
    }

    @Override
    public boolean add(Provider provider) throws Exception{
        boolean flag = false;
        int updateRows = providerMapper.add(provider);
        if(updateRows > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public Provider getProviderById(String id) throws Exception {
        return providerMapper.getProviderById(id);
    }

    @Override
    public boolean selectProCodeExist(String proCode) throws Exception {
        List<Provider> providerList = providerMapper.getProviderList(proCode,null,0,0);
        boolean flag = false;
        int pSize = providerList.size();
        if (pSize > 0){
            flag = true;
        }
        return flag;

    }

    @Override
    public boolean modify(Provider provider) throws Exception{
        boolean flag = false;
        if (providerMapper.modify(provider) > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean deleteProviderById(Integer id) throws Exception {
        boolean flag = false;
        if(providerMapper.deleteProviderById(id) > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public List<Provider> getAllProvider() throws Exception {
        return providerMapper.getAllProvider();
    }
}
