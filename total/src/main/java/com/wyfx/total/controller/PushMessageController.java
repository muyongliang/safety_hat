package com.wyfx.total.controller;

import com.alibaba.fastjson.JSON;
import com.wyfx.total.entity.PullMessage;
import com.wyfx.total.service.ILogInfService;
import com.wyfx.total.service.IPushMessageService;
import com.wyfx.total.utile.HttpClientUtil;
import com.wyfx.total.utile.RespBean;
import com.wyfx.total.utile.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 发布官方消息  企业总后台向企业管理后台发送的消息
 *
 * @author wsm
 */
@RestController
@RequestMapping("/pullMessage")
public class PushMessageController extends BaseController {

    protected static Logger logger = LoggerFactory.getLogger(PushMessageController.class);
    @Autowired
    private IPushMessageService iPushMessageService;
    @Autowired
    private ILogInfService iLogInfService;

    /**
     * 添加发布官方消息
     *
     * @param pullMessage
     * @return
     */
    @PostMapping("/handleAddPullMessage")
    public RespBean handleAddPullMessage(PullMessage pullMessage, HttpSession session) {

        logger.info("添加发布官方消息");
        // 时间为空代表创建就发布 设置发布状态为已发布 同时推送给所有企业后台
        if ("".equals(pullMessage.getPtime()) || null == pullMessage.getPtime()) {
            logger.info("即时推送");
            pullMessage.setPtime(new Date());
            //状态{1:已发布;0:排队中}
            pullMessage.setFlag(1);
            pullMessage.setCreator((String) session.getAttribute("userName"));
            iPushMessageService.addOfficialMessages(pullMessage);
            //马上推送  其他的在固定时间推送
            Map map = new HashMap();
            map.put("title", pullMessage.getTitle());
            map.put("content", pullMessage.getContent());
            //消息类型{0:官方消息;其余值为报警类型的值}
            map.put("type", 0);
            map.put("token", 0);//企业的token  如果为0代表给所有企业的推送消息
            String jsonString = JSON.toJSONString(map);
            String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/messagesCenter/handleAddPushMessage", jsonString);
            if ("false".equals(s)) {
                logger.error("推送到企业后台失败，联系管理员");
                return new RespBean(501, "推送到企业后台失败，联系管理员");
            } else {
                return new RespBean(ResponseCode.SUCCESS_CODE, "发布官方消息成功同时推送到企业后台成功");
            }
        } else { //定时推送时 判断推送时间是否在当前时间前
            logger.info("定时推送");
            Date now = new Date();
            boolean boo = pullMessage.getPtime().before(now);
            if (boo) {
                return new RespBean(502, "定时推送时间不能在当前时间以前");
            }
        }
        pullMessage.setCreator((String) session.getAttribute("userName"));
        iPushMessageService.addOfficialMessages(pullMessage);
        //添加日志
        iLogInfService.addLogInfRecord("创建即发布官方消息", 0, pullMessage.getTitle());
        return new RespBean(ResponseCode.SUCCESS_CODE, "发布官方消息成功");
    }


    /**
     * 定时推送《官方消息》到企业后台
     * 每59分钟查询未来1小时内需要推送的消息
     * 同时在业务层更新官方消息的标识为已推送
     *
     * @return
     */
    @Scheduled(cron = "0 59 * * * ?")
//    @Scheduled(fixedRate = 60000)
    public void handleScheduledPush() {
        logger.info("定时推送官方消息到企业后台");
        List list = iPushMessageService.queryAllUnPushMessages();
        String jsonString = JSON.toJSONString(list);
        logger.info("定时推送的消息列表list=" + list);
        //有消息再推到企业后台
        if (list.size() == 0) {
            return;
        }
        String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/messagesCenter/handleAddScheduledPush", jsonString);
        if ("false".equals(s)) {
            logger.info("推送消息到企业后台失败 ，联系管理员");
        }
    }


    /**
     * 通过标题查询已经发布的消息
     *
     * @param title
     * @return
     */
    @PostMapping("/handleQueryPullMessageByTitle")
    public RespBean handleQueryPullMessageByTitle(String title, int pageNum, int pageSize) {
        Map map = iPushMessageService.queryPullMessageByTitle(title, pageNum, pageSize);
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }


    /**
     * 官方推送消息查看
     *
     * @param pid
     * @return
     */
    @PostMapping("/handleQueryPullMessageByPid")
    public RespBean handleQueryPullMessageByPid(String pid) {
        Long id = Long.parseLong(pid);
        PullMessage pullMessage = iPushMessageService.queryPullMessageById(id);
        return new RespBean(ResponseCode.SUCCESS_CODE, pullMessage);
    }


    /**
     * 编辑推送消息
     *
     * @param pullMessage
     * @return
     */
    @PostMapping("/handleEditPullMessage")
    public RespBean handleEditPullMessage(PullMessage pullMessage, HttpSession session) {

        logger.info("编辑推送消息");
        pullMessage.setUpdatePerson(session.getAttribute("userName").toString());
        //如果需要即时推送则立即推送到企业后台
        // 时间为空代表创建就发布 设置发布状态为已发布 同时推送给所有企业后台
        if ("".equals(pullMessage.getPtime()) || null == pullMessage.getPtime()) {
            pullMessage.setPtime(new Date());
            //状态{1:已发布;0:排队中}
            pullMessage.setFlag(1);
            pullMessage.setCreator((String) session.getAttribute("userName"));
            iPushMessageService.updatePullMessage(pullMessage);
            //马上推送  其他的在固定时间推送
            Map map = new HashMap();
            map.put("title", pullMessage.getTitle());
            map.put("content", pullMessage.getContent());
            //消息类型{0:官方消息;其余值为报警类型的值}
            map.put("type", 0);
            map.put("token", 0);//企业的token  如果为0代表给所有企业的推送消息
            String jsonString = JSON.toJSONString(map);
            String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/messagesCenter/handleAddPushMessage", jsonString);
            if ("false".equals(s)) {
                logger.error("推送到企业后台失败，联系管理员");
                return new RespBean(501, "推送到企业后台失败，联系管理员");
            } else {
                return new RespBean(ResponseCode.SUCCESS_CODE, "发布官方消息成功同时推送到企业后台成功");
            }
        } else {
            //定时推送时 判断推送时间是否在当前时间前
            Date now = new Date();
            boolean boo = pullMessage.getPtime().before(now);
            if (boo) {
                return new RespBean(502, "定时推送时间不能在当前时间以前");
            }
        }
        //更新
        iPushMessageService.updatePullMessage(pullMessage);
        //添加日志
        iLogInfService.addLogInfRecord("编辑推送消息", 2, pullMessage.getTitle());
        return new RespBean(ResponseCode.SUCCESS_CODE, "编辑成功");
    }


    /**
     * 删除推送消息
     *
     * @param pid
     * @return
     */
    @PostMapping("/handleDeletePullMessage")
    public RespBean handleDeletePullMessage(Long pid) {
        logger.info("删除推送消息");
        iPushMessageService.deletePullMessageById(pid);
        return new RespBean(ResponseCode.SUCCESS_CODE, "删除成功");
    }

    /**
     * 查询所有的推送消息 按更新时间降序排列
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @PostMapping("/handleAllPullMessage")
    public RespBean handleAllPullMessage(int pageNum, int pageSize) {
        Map map = iPushMessageService.queryAllPullMessage(pageNum, pageSize);
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }


}
