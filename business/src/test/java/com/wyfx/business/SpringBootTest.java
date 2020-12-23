package com.wyfx.business;


import com.wyfx.business.dao.MenuManagerMapper;
import com.wyfx.business.dao.RoleMenuMapper;
import com.wyfx.business.entity.MenuManager;
import com.wyfx.business.entity.RoleMenu;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author muyongliang
 * @Date 2020/12/22
 * @Description SpringBootTest
 */
@org.springframework.boot.test.context.SpringBootTest
@RunWith(SpringRunner.class)
public class SpringBootTest {
    @Autowired
    MenuManagerMapper menuManagerMapper;
    @Autowired
    RoleMenuMapper roleMenuMapper;
    @Test
    public void test1() {
        for (int i = 101; i <= 122; i++) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(1l);
            roleMenu.setMenuId((long)i);
            int insert = roleMenuMapper.insert(roleMenu);
            System.out.println(insert);
        }

    }

    @Test
    public void test2() {
        Long parentId = 20l;
        int begin = 108;
        int end=109;
        int type=1;
        for (int i = begin; i <= end; i++) {
            MenuManager menuManager = new MenuManager();
            menuManager.setMid((long)i);
            menuManager.setParentId(parentId);
            menuManager.setType(type);
            menuManager.setOrderNum(1);
            int insert = menuManagerMapper.insert(menuManager);
            System.out.println(insert);
        }

    }

}
