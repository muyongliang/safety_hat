package com.wyfx.business.app.controller;

import com.github.pagehelper.PageInfo;
import com.wyfx.business.app.service.ContactService;
import com.wyfx.business.controller.BaseController;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.GroupInfo;
import com.wyfx.business.entity.vo.MemberVo;
import com.wyfx.business.service.shiro.BusinessUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author johnson liu
 * @date 2019/11/15
 * @description 通讯录
 */
@RestController
@RequestMapping("/contact")
public class ContactController extends BaseController {

    @Autowired
    private ContactService contactService;
    @Autowired
    private BusinessUserService businessUserService;


    /**
     * 查询通讯录中成员
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "/findContact", method = RequestMethod.POST)
    public Object findContact(Integer pageSize, Integer pageNum, HttpServletRequest request) {
        // TODO: 2020/10/10
        BusinessUser businessUser = getCurrentUser();
        if (businessUser.getUserType() == 0) {
            return new MyResponseEntity(401, "管理员无查询通讯录权限");
        }
        PageInfo pageInfo = contactService.findContactByProjectId(businessUser.getBid(), businessUser.getProjectId().intValue(), pageSize, pageNum);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * @param clientId
     * @param type     用户类型{1:调度员;2:终端用户}
     * @return
     */
    @RequestMapping(value = "/findContactDetail", method = RequestMethod.POST)
    public Object findContactDetail(Integer clientId, Integer type) {
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), contactService.findMemberInfo(clientId.longValue(), type));
    }

    /**
     * @param userName 用户账号
     * @param type     用户类型{1:调度员;2:终端用户}
     * @return
     */
    @RequestMapping(value = "/findContactDetailByUserName", method = RequestMethod.POST)
    public Object findContactDetailByUserName(String userName, Integer type) {
        BusinessUser businessUser = businessUserService.findByUserName(userName);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), contactService.findMemberInfo(businessUser.getBid().longValue(), type));
    }

    /**
     * 根据工种查询调度员的通讯录
     */
    @RequestMapping(value = "/findWorkType", method = RequestMethod.GET)
    public Object findWorkType(Integer workType) {
        BusinessUser businessUser = getCurrentUser();
        PageInfo pageInfo = contactService.findContactByWorkType(businessUser.getProjectId().intValue(), workType, null, null);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 根据姓名查询通讯录成员
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/findName", method = RequestMethod.GET)
    public Object findName(String name) {
        BusinessUser businessUser = getCurrentUser();
        PageInfo pageInfo = contactService.findContactByName(businessUser.getProjectId().intValue(), name, null, null);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 创建通讯录分组
     *
     * @return
     */
    @RequestMapping(value = "/createContactGroup", method = RequestMethod.POST)
    public Object createContactGroup(@RequestBody MemberVo memberVo) {
        BusinessUser businessUser = getCurrentUser();
        /*if(businessUser.getUserType()!=1){
            return new MyResponseEntity(HttpStatus.METHOD_NOT_ALLOWED.value(),HttpStatus.METHOD_NOT_ALLOWED.toString());
        }*/
        if (memberVo.getGroupName() == null) {
            return new MyResponseEntity(ResponseCode.ERROR_PARAM.getValue(), "参数错误");
        }
        contactService.addContactGroup(businessUser.getProjectId().intValue(), memberVo);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 编辑通讯录分组
     *
     * @param memberVo
     * @return
     */
    @RequestMapping(value = "/editContactGroup", method = RequestMethod.POST)
    public Object editContactGroup(@RequestBody MemberVo memberVo) {
        BusinessUser businessUser = getCurrentUser();
        /*if(businessUser.getUserType()!=1){
            return new MyResponseEntity(HttpStatus.METHOD_NOT_ALLOWED.value(),HttpStatus.METHOD_NOT_ALLOWED.toString());
        }*/
        if (memberVo.getGroupName() == null || memberVo.getGroupId() == null || memberVo.getGroupName().equals("")) {
            return new MyResponseEntity(ResponseCode.ERROR_PARAM.getValue(), "参数错误");
        }
        contactService.updateContactGroup(businessUser.getProjectId(), memberVo);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 获取成员列表
     *
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/getContactGroupMember", method = RequestMethod.GET)
    public Object getContactGroupMember(Integer groupId) {
        BusinessUser user = getCurrentUser();
        Long id = (groupId == null) ? null : groupId.longValue();
        try {
            MemberVo memberVo = contactService.getMemberList(id, user.getProjectId(), user.getBid());
            return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), memberVo);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "查询异常");
        }
    }

    @RequestMapping(value = "/findContactGroupMember", method = RequestMethod.GET)
    public Object findContactGroupMember(Integer groupId, Integer pageSize, Integer pageNum) {
        try {
            if (groupId == null) {
                return new MyResponseEntity(ResponseCode.ERROR_PARAM.getValue(), "参数错误");
            }
            PageInfo pageInfo = contactService.findContactGroupMember(groupId.longValue(), pageSize, pageNum);
            return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "查询异常");
        }
    }


    /**
     * 删除通讯录群组
     *
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/deleteContactGroup", method = RequestMethod.GET)
    public Object deleteContactGroup(Integer groupId) {
        if (groupId == null) {
            return new MyResponseEntity(ResponseCode.ERROR_PARAM.getValue(), "参数错误");
        }
        contactService.deleteContactGroup(groupId);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 根据群组名称查询群组列表,参数可以为null，为null时，查询调度员的所有群组列表
     *
     * @param groupName
     * @return
     */
    @RequestMapping(value = "/findGroupByName", method = RequestMethod.GET)
    public Object findGroupByName(String groupName) {
        //根据群组名称查询通讯录群组
        BusinessUser user = getCurrentUser();
        List<GroupInfo> list = contactService.findByGroupName(user.getProjectId(), groupName);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }


}
