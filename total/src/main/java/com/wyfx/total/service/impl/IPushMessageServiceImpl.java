package com.wyfx.total.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.total.entity.PullMessage;
import com.wyfx.total.exception.DataNotAllowedToOperationException;
import com.wyfx.total.exception.InsertDataException;
import com.wyfx.total.exception.UpdateDataException;
import com.wyfx.total.mapper.BusinessManagerMapper;
import com.wyfx.total.mapper.PullMessageMapper;
import com.wyfx.total.service.ILogInfService;
import com.wyfx.total.service.IPushMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class IPushMessageServiceImpl implements IPushMessageService {

    private static final Logger logger = LoggerFactory.getLogger(IPushMessageServiceImpl.class);
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private PullMessageMapper pullMessageMapper;
    @Autowired
    private BusinessManagerMapper businessManagerMapper;
    @Autowired
    private ILogInfService iLogInfService;

    /**
     * 发布官方消息
     *
     * @param pullMessage
     * @return
     */
    @Override
    public boolean addOfficialMessages(PullMessage pullMessage) {
        pullMessage.setCtime(new Date());
        //消息推送精确到分钟
        Integer row = pullMessageMapper.insertSelective(pullMessage);
        if (row < 0) {
            throw new InsertDataException("插入数据异常，联系管理员！");
        }
        iLogInfService.addLogInfRecord("添加", 0, "添加官方消息");
        return true;
    }

    /**
     * 通过标题筛选查询推送消息
     *
     * @return
     */
    @Override
    public Map queryPullMessageByTitle(String title, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map> pullMessageList = pullMessageMapper.selectByTitle(title);
        PageInfo pageInfo = new PageInfo(pullMessageList);
        System.err.println("allTotal=" + pageInfo.getTotal());
        Map map = new HashMap();
        map.put("pullMessageList", pageInfo);
        return map;
    }

    /**
     * 企业管理推送消息查看详情
     *
     * @param pid
     * @return
     */
    @Override
    public PullMessage queryPullMessageById(Long pid) {
        PullMessage pullMessage = pullMessageMapper.selectByPrimaryKey(pid);
        pullMessage.setToUsers(null);
        return pullMessage;
    }

    /**
     * 编辑推送消息
     *
     * @param pullMessage
     * @return
     */
    @Override
    public boolean updatePullMessage(PullMessage pullMessage) throws DataNotAllowedToOperationException, UpdateDataException {
        //查询该条推送消息
        PullMessage pullMessage1 = pullMessageMapper.selectByPrimaryKey(pullMessage.getPid());
        //1 已经推送 0 排队中
        if (pullMessage1.getFlag() == 1) {
            throw new DataNotAllowedToOperationException("消息已经推送，不可编辑");
        }
        pullMessage.setUtime(new Date());
        Integer row = pullMessageMapper.updateByPrimaryKeySelective(pullMessage);
        if (row < 0) {
            throw new UpdateDataException("更新异常，联系管理员");
        }

        iLogInfService.addLogInfRecord("编辑", 2, "编辑官方消息");
        return true;
    }


    /**
     * 删除 官方推送消息
     *
     * @param pid
     * @return
     */
    @Override
    public boolean deletePullMessageById(Long pid) throws UpdateDataException {

        Integer row = pullMessageMapper.deleteByPrimaryKey(pid);
        if (row < 0) {
            throw new UpdateDataException("删除消息不成功 联系管理员");
        }
        iLogInfService.addLogInfRecord("删除", 1, "删除官方消息");
        return true;
    }


    /**
     * 查询所有的推送消息 按更新时间降序排列
     *
     * @param pageNum  页数
     * @param pageSize 每页数据量
     * @return
     */
    @Override
    public Map queryAllPullMessage(int pageNum, int pageSize) {
        //分页插件 在查询数据前使用，否则不会生效
        PageHelper.startPage(pageNum, pageSize);
        //查询所有数据
        List<Map> mapList = pullMessageMapper.selectAllPullMessage();
        //总条数
        Integer count = pullMessageMapper.selectPullMessageCount();
        //将查询的数据存入分页插件并返回给前端
        PageInfo pageInfo = new PageInfo(mapList);
        //创建响应map
        Map respMap = new HashMap();
        respMap.put("allPullMessage", pageInfo);
        respMap.put("AllCount", count);
        return respMap;
    }


    /**
     * 查询今天需要推送的未推送消息
     *
     * @return
     */
    @Override
    public List queryAllUnPushMessages() {
        //查询未来24小时需要推送而未推送的消息 状态{1:已发布;0:排队中} todo 查询未来60分钟内需要推送的消息
        Date now = new Date();
        // flag 消息读取状态{0:未读取;1:已读取}
        List<PullMessage> pullMessageList = pullMessageMapper.selectAllPullMessageByCondition(0, now, returnEndTime(60, now));
        List respList = new ArrayList();
        List list = new ArrayList();
        for (PullMessage pullMessage : pullMessageList) {
            list.add(pullMessage.getPid());
            Map map = new HashMap();
            map.put("title", pullMessage.getTitle());
            map.put("content", pullMessage.getContent());
            //消息类型{0:官方消息;其余值为报警类型的值}
            map.put("type", 0);
            map.put("token", 0);//企业的token  如果为0代表给所有企业的推送消息
            respList.add(map);
        }
        //批量修改标识状态为已发送
        if (pullMessageList.size() > 0) {
            pullMessageMapper.updateFlagByKeyListSelective(pullMessageList, 1, "系统定时推送消息", new Date());
        }
        return respList;
    }


    /**
     * 返回开始时间
     *
     * @param setTime 未来某个时间量
     * @param now     当前时间
     * @return 相对于当前时间的未来几小时时间
     */
    public Date returnEndTime(Integer setTime, Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, +setTime);
        Date endT = calendar.getTime();
        logger.info("推送" + sdf.format(now) + "到" + sdf.format(endT) + "之间的消息");
        return endT;
    }


}
