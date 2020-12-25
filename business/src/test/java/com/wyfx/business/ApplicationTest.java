package com.wyfx.business;


import com.wyfx.business.dao.MenuManagerMapper;
import com.wyfx.business.dao.RoleMenuMapper;
import com.wyfx.business.entity.MenuManager;
import com.wyfx.business.entity.RoleMenu;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author muyongliang
 * @Date 2020/12/22
 * @Description SpringBootTest
 */
@SpringBootTest(classes = BusinessApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class ApplicationTest {
    @Autowired
    MenuManagerMapper menuManagerMapper;
    @Autowired
    RoleMenuMapper roleMenuMapper;

    /**
     * 添加权限
     */
    @Test
    public void test1() {
        for (int i = 2; i <= 2; i++) {
            for (int j = 3; j <= 3; j++) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId((long) i);
                roleMenu.setMenuId((long) j);
                int insert = roleMenuMapper.insert(roleMenu);
                System.out.println(insert);
            }
        }
    }

    /**
     * 清除2-5的权限
     */
    @Test
    public void test3() {
        for (int i = 1; i <= 4; i++) {
            for (int j = 2; j <= 5; j++) {
                roleMenuMapper.delete((long) i, (long) j);
            }
        }
    }

    /**
     * 添加菜单
     */
    @Test
    public void test2() {
        Long parentId = 5l;
        int begin = 27;
        int end = 32;
        int type = 1;
        for (int i = begin; i <= end; i++) {
            MenuManager menuManager = new MenuManager();
            menuManager.setMid((long) i);
            menuManager.setParentId(parentId);
            menuManager.setType(type);
            menuManager.setOrderNum(1);
            int insert = menuManagerMapper.insert(menuManager);
            System.out.println(insert);
        }

    }

}
