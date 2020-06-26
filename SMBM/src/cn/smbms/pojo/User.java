package cn.smbms.pojo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * @Author：yusiyu
 * @Description：
 * @Date：Created in 9:51 2019/6/26
 * @Version 1.0
 */
public class User {
    //id
    private Integer id;

    //用户编码
    private String userCode;

    //用户名称
    private String userName;

    //用户密码
    private String userPassword;

    //出生日期
    @JSONField(format="yyyy-MM-dd")
    private Date birthday;

    //性别
    private Integer gender;

    //电话
    private String phone;

    //地址
    private String address;

    //用户角色
    private Integer userRole;

    //创建者
    private Integer createdBy;

    //创建时间
    private Date creationDate;

    //更新者
    private Integer modifyBy;

    //更新时间
    private Date modifyDate;

    //年龄
    private Integer age;

    //用户角色名称
    private String userRoleName;

    //证件照路径
    private String idPicPath;

    //工作证照片路径
    private String workPicPath;
    public String getWorkPicPath() {
        return workPicPath;
    }

    public void setWorkPicPath(String workPicPath) {
        this.workPicPath = workPicPath;
    }

    public String getIdPicPath() {
        return idPicPath;
    }

    public void setIdPicPath(String idPicPath) {
        this.idPicPath = idPicPath;
    }

    /**
     * 无参构造
     */
    public User(){}

    /**
     * 全参数构造
     * @param id
     * @param userCode
     * @param userName
     * @param userPassword
     * @param gender
     * @param birthday
     * @param phone
     * @param address
     * @param userRole
     * @param createdBy
     * @param creationDate
     * @param modifyBy
     * @param modifyDate
     */
    public User(Integer id,String userCode,String userName,String userPassword,Integer gender,Date birthday,String phone,
                String address,Integer userRole,Integer createdBy,Date creationDate,Integer modifyBy,Date modifyDate){
        this.id = id;
        this.userCode = userCode;
        this.userName = userName;
        this.userPassword = userPassword;
        this.gender = gender;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
        this.userRole = userRole;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.modifyBy = modifyBy;
        this.modifyDate = modifyDate;
    }
    public String getUserRoleName() {
        return userRoleName;
    }
    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }
    public Integer getAge() {
        Date date = new Date();
        Integer age = date.getYear()-birthday.getYear();
        return age;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getUserCode() {
        return userCode;
    }
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public Integer getGender() {
        return gender;
    }
    public void setGender(Integer gender) {
        this.gender = gender;
    }
    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Integer getUserRole() {
        return userRole;
    }
    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }
    public Integer getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public Integer getModifyBy() {
        return modifyBy;
    }
    public void setModifyBy(Integer modifyBy) {
        this.modifyBy = modifyBy;
    }
    public Date getModifyDate() {
        return modifyDate;
    }
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}
