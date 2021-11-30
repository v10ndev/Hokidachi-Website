package com.hokidachi.admin.user;

import com.hokidachi.common.entity.Role;
import com.hokidachi.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewUserWithOneRole() {
        Role roleAdmin = entityManager.find(Role.class, 1);
        User userAdmin = new User("lucnguyen.haui@gmail.com","123456","Luc","Nguyen Van");
        userAdmin.addRole(roleAdmin);

        repo.save(userAdmin);
    }

    @Test
    public void testCreateNewUserWithTwoRoles() {
        User user1 = new User("triacma@gmail.com","123456","Tri","Nguyen Huu");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        user1.addRole(roleEditor);
        user1.addRole(roleAssistant);

        repo.save(user1);;
    }

    @Test
    public void testListAllUsers() {
        Iterable<User> listUsers = repo.findAll();
        listUsers.forEach(user->System.out.println(user));
    }

    @Test
    public void testGetUserById() {
        User user = repo.findById(1).get();
        System.out.println(user);
    }

    @Test
    public void testUpdateUserDetails() {
        User user = repo.findById(1).get();
        user.setEnabled(true);
        user.setEmail("tin2921988@gmail.com");

        repo.save(user);
    }

    @Test
    public void testUpdateUserRoles() {
        User user = repo.findById(2).get();
        Role roleEditor = new Role(3);
        Role roleSalesperson = new Role(2);

        user.getRoles().remove(roleEditor);
        user.addRole(roleSalesperson);

        repo.save(user);
    }

    @Test
    public void testDeleteUser() {
        Integer userId = 4;
        repo.deleteById(userId);
    }

    @Test
    public void testGetUserByEmail(){
        String email = "lucnguyen@gmail.com";
        User user = repo.getUserByEmail(email);

        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById() {
        Integer id = 1;
        Long countById = repo.countById(id);

        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisableUser() {
        Integer id = 1;
        repo.updateEnabledStatus(id, false);

    }

    @Test
    public void testEnableUser() {
        Integer id = 1;
        repo.updateEnabledStatus(id, true);

    }

    @Test
    public void testListFirstPage() {
        int pageNumber = 1;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(pageable);

        List<User> listUsers = page.getContent();

        listUsers.forEach(user -> System.out.println(user));

        assertThat(listUsers.size()).isEqualTo(pageSize);

    }

    @Test
    public void testSearchUsers() {
        String keyword = "gmail";
        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(keyword, pageable);

        List<User> listUsers = page.getContent();

        listUsers.forEach(user -> System.out.println(user));

        assertThat(listUsers.size()).isGreaterThan(0);
    }

}
