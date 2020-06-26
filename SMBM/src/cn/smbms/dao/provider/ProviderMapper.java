package cn.smbms.dao.provider;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProviderMapper {
    /**
     * 获取供应商列表
     * @param proCode
     * @param proName
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    List<Provider> getProviderList(@Param("proCode") String proCode,
                                   @Param("proName") String proName,
                                   @Param("from") int currentPageNo,
                                   @Param("pageSize") int pageSize) throws Exception;
    /**
     * 获取供应商总数量
     * @param proCode
     * @param proName
     * @return
     * @throws Exception
     */
    int getProviderCount(@Param("proCode") String proCode,
                         @Param("proName") String proName) throws Exception;

    /**
     * 添加供应商
     * @param provider
     * @return
     * @throws Exception
     */
    int add(Provider provider) throws Exception;

    /**
     * 根据供应商id获取供应商
     * @param id
     * @return
     */
    Provider getProviderById(@Param("id") String id) throws Exception;

    /**
     * 修改供应商信息
     * @param provider
     * @return
     * @throws Exception
     */
    int modify(Provider provider) throws Exception;

    /**
     * 根据id删除用户
     * @param id
     * @return
     * @throws Exception
     */
    int deleteProviderById(@Param("id") Integer id) throws Exception;

    List<Provider> getAllProvider() throws Exception;
}
