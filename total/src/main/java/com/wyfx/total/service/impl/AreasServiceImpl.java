package com.wyfx.total.service.impl;

import com.wyfx.total.entity.Area;
import com.wyfx.total.mapper.AreaMapper;
import com.wyfx.total.service.AreasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 省市区查询
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AreasServiceImpl implements AreasService {

    @Autowired
    private AreaMapper areaMapper;

    /**
     * 通过父id查询其下的子项目
     *
     * @param parentId
     * @return
     */
    @Override
    public Map getSubList(Integer parentId) {
        List<Area> list = areaMapper.selectListByParentId(parentId);
        List resp = new ArrayList();
        Map map = new HashMap();
        for (Area area : list) {
            Map map1 = new HashMap();
            map1.put("name", area.getAreaname());
            map1.put("parentId", area.getParentid());
            map1.put("id", area.getId());
            resp.add(map1);
        }
        map.put("subList", resp);
        return map;
    }
}
