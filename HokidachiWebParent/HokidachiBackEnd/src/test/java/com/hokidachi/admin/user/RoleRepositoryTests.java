package com.hokidachi.admin.user;

import com.hokidachi.common.entity.Role;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {

    @Autowired private RoleRepository repo;

    @Test
    public void testCreateFirstRole(){
        Role roleAdmin = new Role("Admin","Quản lý mọi thứ");
        Role savedRole = repo.save(roleAdmin);
    }

    @Test
    public void testCreatRestRoles() {
        Role roleSalesperson = new Role("Nhân viên bán hàng","Quản lý giá sản phẩm, khách hàng, vận chuyển, đơn đặt hàng và báo cáo bán hàng");

        Role roleEditor = new Role("Người chỉnh sửa","Quản lý danh mục, sản phẩm, nhãn hiệu, bài viết và menu");

        Role roleShipper = new Role("Người giao hàng","Xem sản phẩm, xem đơn đặt hàng và cập nhật trạng thái đơn hàng");

        Role roleAssistant = new Role("Phụ tá","Quản lý câu hỏi và đánh giá");

        repo.saveAll(List.of(roleSalesperson,roleEditor,roleShipper, roleAssistant));
    }
}
