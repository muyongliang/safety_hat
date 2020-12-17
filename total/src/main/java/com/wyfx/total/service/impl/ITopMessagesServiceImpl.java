package com.wyfx.total.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.total.entity.TopMessages;
import com.wyfx.total.exception.InsertDataException;
import com.wyfx.total.exception.UpdateDataException;
import com.wyfx.total.mapper.TopMessagesMapper;
import com.wyfx.total.service.ITopMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ITopMessagesServiceImpl implements ITopMessagesService {


    @Autowired
    private TopMessagesMapper topMessagesMapper;

    /**
     * 根据条件{企业及第三方企业服务到期时间，储存空间已经满}生成系统消息
     *
     * @param topMessages
     */
    @Override
    @Transactional
    public boolean addTopMessages(TopMessages topMessages) throws InsertDataException {
        int row = topMessagesMapper.insertSelective(topMessages);
        if (row < 0) {
            throw new InsertDataException("添加失败");
        }
        return true;
    }

    /**
     * /**
     * 通过flag查询所有已读或未读消息
     *
     * @param flag 已读0 未读1
     * @return
     */
    @Override
    public Map QueryAllTopMessagesByFlag(Integer flag, Integer pageNum, Integer pageSize) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //总数
        Integer count = topMessagesMapper.selectTomMessagesCount(flag);
        //分页插件 在查询数据前使用，否则不会生效
        PageHelper.startPage(pageNum, pageSize);
        List<TopMessages> topMessages = topMessagesMapper.selectAllTopMessagesByFlag(flag);
        List<Map> mapList = new ArrayList<>();
        for (TopMessages topMessage : topMessages) {
            Map map = new HashMap();
            map.put("bname", topMessage.getCompany());

            if (topMessage.getType() == 0) {
                map.put("title", "企业服务到期提醒");
                map.put("content", "企业服务时间即将到期，到期时间：" + sdf.format(topMessage.getEndTime()));
            } else if (topMessage.getType() == 1) {
                map.put("title", "第三方企业服务到期提醒");
                map.put("content", "第三方企业服务时间即将到期，到期时间：" + sdf.format(topMessage.getEndTime()));
            } else {
                map.put("title", "企业存储空间报警提醒");
                map.put("content", "存储空间已达到该企业" + topMessage.getStore() + "的限制，请进行处理");
            }
            map.put("flag", topMessage.getFlag());
            map.put("mid", topMessage.getMid());
            map.put("ctime", sdf.format(topMessage.getCtime()));
            mapList.add(map);
        }
        //将查询的数据存入分页插件并返回给前端
        PageInfo pageInfo = new PageInfo(mapList);
        Map resMap = new HashMap();
        resMap.put("repMap", pageInfo);
        resMap.put("count", count);
        return resMap;
    }


    /**
     * 更新已读消息状态标识 flag
     *
     * @param mids
     * @return
     */
    @Override
    @Transactional
    public boolean updateWhoIsRead(List<Long> mids) throws UpdateDataException {
        // 无数据则不执行更新
        if (mids.size() != 0) {
            int i = topMessagesMapper.updateByMids(mids);
            if (i == 0) {
                throw new UpdateDataException("更新数据失败，联系管理员");
            }
        }
        return true;
    }

    /**
     * 查询是否存在未读消息
     * 1已读 0未读    空值则查询未读消息
     *
     * @return
     */
    @Override
    public List<Long> handleSelectAllUnreadMessages() {
        List<TopMessages> topMessages = topMessagesMapper.selectAllTopMessagesByFlag(0);
        List<Long> list = new ArrayList<>();
        for (TopMessages topMessage : topMessages) {
            list.add(topMessage.getMid());
        }
        return list;

    }
}
