package com.wyfx.total.utile;

/**
 * 响应码
 */
public class ResponseCode {
    public final static int SUCCESS_CODE = 200;  //成功
    public final static int FAILURE = 502;//失败
    public final static int ACCESS_LIMIT = 1;  //权限限制
    public final static int NO_USER = 3; //用户名不存在
    public final static int ERROR_PASS_WORD = 4; //密码错误
    public final static int ERROR_CODE_VALIDATE = 5; //验证码错误
    public final static int EMPTY_FILE = 6; //上传文件为空
    public final static int OLD_ERROR_PASS = 7; //旧密码错误
    public final static int NOT_LOGIN = 8; //用户未登陆
    public final static int LOGIN = 9; //用户登陆
    public final static int DUPLICATE_NAME = 12;  //名称不能重复
    public final static int NOT_DEL = 13;  //部分被关联，不能删除


    /**
     * 附件类型
     */
    public final static int PAPER_ATTACHMENT = 1;  //论文
    public final static int PATENT_ATTACHMENT = 2;  //专利
    public final static int SOFTWARE_ATTACHMENT = 3;  //软件著作全
    public final static int AWARD_ATTACHMENT = 4;  //获奖证书
    public final static int TREATISE_ATTACHMENT = 5;  //专著
    public final static int STANDARD_ATTACHMENT = 6;  //标准
    public final static int PROJECT_ATTACHMENT = 7;  //项目
    public final static int ASSESSMENT_INDEX_ATTACHMENT = 8;  //项目考核指标
    public final static int PERIODIC_REPORT_ATTACHMENT = 9;  //项目课题阶段性报告
    public final static int CHARGE_ATTACHMENT = 10;  //费用
    public final static int RESULTS_INFORMATION_ATTACHMENT = 0;  //成果信息


    public final static Long ADMIN_ID = 1l; //管理员id

    /**
     * 专利,软件著作权 1，已授权  2，待提交 3，已申请 4，已受理
     */
    public final static int ACCREDIT_TYPE = 1;
    public final static int SUBMIT_TYPE = 2;
    public final static int APPLY_TYPE = 3;
    public final static int ACCEPT_TYPE = 4;

    /**
     * 论文类型 1，期刊论文 2，会议论文
     */
    public final static int JOURNAL_PAPER = 1;
    public final static int MEETING_PAPER = 2;

    /**
     * 标准状态 1，编撰中 2，已获批
     */
    public final static int COMPILATION_STATUS = 1;
    public final static int APPROVED_STATUS = 2;

    /**
     * 项目状态 1，进行中 2，结项
     */
    public final static int UNDERWAY_STATUS = 1;
    public final static int PDSP_STATUS = 2;

    /**
     * 费用状态 1，带报销 2，已报销
     */
    public final static int STAY_REIMBURSEMENT_STATUS = 1;
    public final static int END_REIMBURSEMENT_STATUS = 2;

    /**
     * 1,一级菜单 2，2级菜单
     */
    public final static int FIRST_MENU = 1;
    public final static int SENCOND_MENU = 2;

    public final static int YES = 1;
    public final static int NO = 0;

    /**
     * 用户初始密码
     */
    public final static String INITIAL_PASSWORD = "123456";
}
