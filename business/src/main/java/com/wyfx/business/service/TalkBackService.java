package com.wyfx.business.service;

import com.github.pagehelper.PageInfo;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.TalkBackGroup;
import com.wyfx.business.entity.vo.TalkBackGroupVo;

import java.util.List;

public interface TalkBackService {

    void insertMember(Long bid, Long groupId);

    TalkBackGroup findDefaultByProjectId(Long projectId);

    TalkBackGroup findByGroupId(Long groupId);

    /**
     * 查询终端用户当前所在对讲组
     *
     * @param businessUser
     * @return
     */
    TalkBackGroup findClientTalkBack(BusinessUser businessUser);

    /**
     * 添加或编辑对讲组
     *
     * @param id
     */
    void updateTalkBack(Long id, TalkBackGroup talkBackGroup, BusinessUser user, boolean fromApp);

    /**
     * 更新对讲组成员
     *
     * @param groupId
     * @param memberIds
     */
    void updateMember(Long groupId, Long businessId, Object[] memberIds, Long projectId);

    /**
     * 切换对讲组激活状态
     *
     * @param id
     */
    void switchActivateStatus(Long id, Integer projectId, BusinessUser user);

    /**
     * 删除对讲组
     *
     * @param id
     */
    void deleteTalkBack(Long id);

    /**
     * 查询对讲组
     */
    PageInfo findTalkBack(Integer projectId, Integer pageSize, Integer pageNum);

    List<TalkBackGroupVo> findTalkBack(Integer projectId);

    List<TalkBackGroupVo> findTalkBackByBusinessId(Long businessId);

    /**
     * 根据对讲组名称查询
     *
     * @return
     */
    PageInfo findTalkBackByName(Integer projectId, String name, Integer pageSize, Integer pageNum);

}
