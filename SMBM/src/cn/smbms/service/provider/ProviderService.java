package cn.smbms.service.provider;

import cn.smbms.pojo.Provider;

import java.util.List;

public interface ProviderService {
    /**
     * 获取供应商列表
     * @param queryProCode
     * @param queryProName
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    List<Provider> getProviderList(String queryProCode, String queryProName, int currentPageNo, int pageSize) throws Exception;

    /**
     * 获取供应商总数量
     * @param queryProCode
     * @param queryProName
     * @return
     */
    int getProviderCount(String queryProCode,String queryProName) throws Exception;

    /**
     * 添加用户
     * @param provider
     * @return
     */
    boolean add(Provider provider) throws Exception;

    /**
     * 根据id获取供应商
     * @param id
     * @return
     */
    Provider getProviderById(String id) throws Exception;

    /**
     * 查询供应商是否存在
     * @param proCode
     * @return
     * @throws Exception
     */
    boolean selectProCodeExist(String proCode) throws Exception;

    /**
     * 修改供应商
     * @return
     */
    boolean modify(Provider provider) throws Exception;

    /**
     * 删除供应商
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteProviderById(Integer id)throws Exception;

    /**
     * 获取所有供应商
     * @return
     */
    List<Provider> getAllProvider() throws Exception;
}
