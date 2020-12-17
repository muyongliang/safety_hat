package com.wyfx.total.entity.vo;

/**
 * 向企业后台插入数据的vo类
 * 与企业后台的BusinessInfo类一致
 * create by wsm on 2019-12-5
 */
public class BusinessInfo {

    private Long businessId;

    private String businessName;//企业名

    private String token;//企业的唯一uuid

    private String mainAccount;//主账号

    private Integer clientNum;//终端限制数量

    private Integer status;//企业状态

    private Integer flag;//0普通企业 1第三方企业 add 2020-1-19

    public BusinessInfo(Long businessId, String businessName, String token, String mainAccount, Integer clientNum, Integer status, Integer flag) {
        this.businessId = businessId;
        this.businessName = businessName;
        this.token = token;
        this.mainAccount = mainAccount;
        this.clientNum = clientNum;
        this.status = status;
        this.flag = flag;
    }

    public BusinessInfo() {
        super();
    }

    public Integer getClientNum() {
        return clientNum;
    }

    public void setClientNum(Integer clientNum) {
        this.clientNum = clientNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName == null ? null : businessName.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(String mainAccount) {
        this.mainAccount = mainAccount == null ? null : mainAccount.trim();
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "BusinessInfo{" +
                "businessId=" + businessId +
                ", businessName='" + businessName + '\'' +
                ", token='" + token + '\'' +
                ", mainAccount='" + mainAccount + '\'' +
                ", clientNum=" + clientNum +
                ", status=" + status +
                ", flag=" + flag +
                '}';
    }
}