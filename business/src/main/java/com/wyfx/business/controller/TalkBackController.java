package com.wyfx.business.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.app.vo.ContactsVo;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.controller.commons.SafetyHatException;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.TalkBackGroup;
import com.wyfx.business.entity.vo.TalkBackGroupVo;
import com.wyfx.business.service.ClientAccountService;
import com.wyfx.business.service.TalkBackGroupMemberService;
import com.wyfx.business.service.TalkBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author johnson liu
 * @date 2019/11/18
 * @description 对讲组控制器
 */
@RestController
@RequestMapping("/talkBack")
public class TalkBackController extends BaseController {

    @Autowired
    private TalkBackService talkBackService;

    @Autowired
    private TalkBackGroupMemberService talkBackGroupMemberService;
    @Autowired
    private ClientAccountService clientAccountService;


    /**
     * 添加对讲组
     *
     * @return
     */
    @RequestMapping(value = "/addTalkBack", method = RequestMethod.POST)
//    @RequiresPermissions("talkBackGroup:add")
    public Object addTalkBack(String name, Integer isRecording) {
        BusinessUser businessUser = getCurrentUser();
        /*TalkBackGroup talkBackGroup=new TalkBackGroup(null,name,0,isRecording,0,new Date(),businessUser.getUserName(),businessUser.getBusinessId(),businessUser.getProjectId());*/
        TalkBackGroup talkBackGroup = new TalkBackGroup(null, name, 0, isRecording, 0, new Date(), businessUser.getUserName(), businessUser.getBid(), businessUser.getProjectId());
        try {
            talkBackService.updateTalkBack(null, talkBackGroup, businessUser, false);
        } catch (SafetyHatException e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        }

        //添加对讲组:默认分组成员(调度员)
        /*talkBackService.updateMember(talkBackGroup.getId(),businessUser.getBusinessId(),new Object[0],businessUser.getProjectId());*/
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 从app上添加对讲组
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/fromApp/addTalkBackOfApp", method = RequestMethod.POST)
    public Object addTalkBackOfApp(String params) {
        BusinessUser businessUser = getCurrentUser();
        JSONObject jsonObject = JSONObject.parseObject(params);
        String name = jsonObject.getString("name");

        Object[] members = ((JSONArray) jsonObject.get("members")).toArray();
        TalkBackGroup talkBackGroup = new TalkBackGroup(null, name, 0, 0, 0, new Date(), businessUser.getUserName(), businessUser.getBusinessId(), businessUser.getProjectId());
        talkBackService.updateTalkBack(null, talkBackGroup, businessUser, true);
        Long id = talkBackGroup.getId();
        System.out.println("添加成功:" + id);

        talkBackService.updateMember(talkBackGroup.getId(), getCurrentUser().getBusinessId(), members, businessUser.getProjectId());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 编辑对讲组
     *
     * @return
     */
    @RequestMapping(value = "/updateTalkBack", method = RequestMethod.POST)
//    @RequiresPermissions("talkBackGroup:edit")
    public Object updateTalkBack(Integer groupId, String name, Integer isRecording) {
        TalkBackGroup talkBackGroup = new TalkBackGroup(groupId.longValue(), name, null, isRecording, null, null, null, null, null);
        talkBackService.updateTalkBack(groupId.longValue(), talkBackGroup, null, false);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 成员管理
     *
     * @param groupId
     * @param params
     * @return
     */
    @RequestMapping(value = "/updateMember", method = RequestMethod.POST)
//    @RequiresPermissions("talkBackGroup:memberManager")
    public Object updateMember(Integer groupId, String params) {
        BusinessUser businessUser = getCurrentUser();
        JSONObject jsonObject = JSONObject.parseObject(params);

        Object[] members = jsonObject.getJSONArray("members").toArray();
        talkBackService.updateMember(groupId.longValue(), getCurrentUser().getBusinessId(), members, businessUser.getProjectId());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 切换激活状态
     *
     * @param groupId 选中激活的对讲组ID
     * @return
     */
    @RequestMapping(value = "/switchActivateStatus", method = RequestMethod.GET)
//    @RequiresPermissions("talkBackGroup:switch")
    public Object switchActivateStatus(Integer groupId) {
        BusinessUser businessUser = getCurrentUser();
        talkBackService.switchActivateStatus(groupId.longValue(), businessUser.getProjectId().intValue(), businessUser);

        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 删除对讲组
     *
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/deleteTalkBack", method = RequestMethod.GET)
//    @RequiresPermissions("talkBackGroup:delete")
    public Object deleteTalkBack(Integer groupId) {
        talkBackService.deleteTalkBack(groupId.longValue());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 查询对讲组
     *
     * @return
     */
    @RequestMapping(value = "/findTalkBack", method = RequestMethod.GET)
    public Object findTalkBack(Integer pageSize, Integer pageNum) {
        BusinessUser businessUser = getCurrentUser();
        PageInfo pageInfo = talkBackService.findTalkBack(businessUser.getProjectId().intValue(), pageSize, pageNum);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 管理员查询所有对讲组列表;调度员查询本项目的对讲组
     *
     * @return
     */
    @RequestMapping(value = "/findTalkBackList", method = RequestMethod.GET)
    public Object findTalkBackList(Integer projectId) {
        if (projectId == null) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "请先选择项目");
        }
        List<TalkBackGroupVo> list = talkBackService.findTalkBack(projectId);
        /*List<TalkBackGroupVo> list=null;
        if(businessUser.getUserType()==1){
            list=talkBackService.findTalkBack(businessUser.getProjectId().intValue());
        }else if(businessUser.getUserType()==0) {
            list=talkBackService.findTalkBackByBusinessId(businessUser.getBusinessId());
        }*/
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    /**
     * 调度员app获取对讲组列表
     *
     * @return
     */
    @RequestMapping(value = "/fromApp/findTalkBackOfApp", method = RequestMethod.GET)
    public Object findTalkBackOfApp() {
        BusinessUser businessUser = getCurrentUser();
        List<TalkBackGroupVo> list = talkBackService.findTalkBack(businessUser.getProjectId().intValue());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    /**
     * 按对讲组名称查询
     *
     * @return
     */
    @RequestMapping(value = "/findTalkBackByName", method = RequestMethod.POST)
//    @RequiresPermissions("talkBackGroup:view")
    public Object findTalkBackByName(String name, Integer pageSize, Integer pageNum) {
        BusinessUser businessUser = getCurrentUser();
        PageInfo pageInfo = talkBackService.findTalkBackByName(businessUser.getProjectId().intValue(), name, pageSize, pageNum);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 查询对讲组所有成员
     *
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/findTalkBackMemBer", method = RequestMethod.GET)
    public Object findTalkBackMemBer(Integer groupId) {
        BusinessUser businessUser = getCurrentUser();
        List<ContactsVo> list = talkBackGroupMemberService.findByGroupIdAndProjectId(businessUser.getProjectId().intValue(), groupId);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    /**
     * 查询未在对讲组的成员列表
     *
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/findNotInTalkBackMemBer", method = RequestMethod.GET)
    public Object findNotInTalkBackMemBer(Integer groupId) {
        BusinessUser businessUser = getCurrentUser();
        List<ContactsVo> list = talkBackGroupMemberService.findByProjectIdFromBusinessUser(groupId.longValue(), businessUser.getProjectId());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    @RequestMapping(value = "/sayHello", method = RequestMethod.GET)
    public Object sayHello() {
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "这个一个测试sayHello的方法");
    }

}
