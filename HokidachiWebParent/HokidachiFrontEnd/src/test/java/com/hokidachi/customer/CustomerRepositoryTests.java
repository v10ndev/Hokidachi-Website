package com.hokidachi.customer;

import com.hokidachi.common.entity.AuthenticationType;
import com.hokidachi.common.entity.City;
import com.hokidachi.common.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {

    @Autowired private CustomerRepository repo;
    @Autowired private TestEntityManager entityManager;

    @Test
    public void testCreateCustomer1(){
        Integer cityId = 2;// USA
        City city = entityManager.find(City.class, cityId);

        Customer customer = new Customer();
        customer.setCity(city);
        customer.setFirstName("David");
        customer.setLastName("Fountaine");
        customer.setPassword("123456");
        customer.setEmail("david@gmail.com");
        customer.setPhoneNumber("312-642-7518");
        customer.setAddressLine1("1927 West Drive");
        customer.setVillage("Sacramento");
        customer.setDistrict("California");
        customer.setPostalCode("95867");
        customer.setCreatedTime(new Date());

        Customer savedCustomer = repo.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateCustomer2(){
        Integer cityId = 1;// India
        City city = entityManager.find(City.class, cityId);

        Customer customer = new Customer();
        customer.setCity(city);
        customer.setFirstName("Sanya");
        customer.setLastName("Lad");
        customer.setPassword("123456");
        customer.setEmail("sanya@gmail.com");
        customer.setPhoneNumber("0222484589");
        customer.setAddressLine1("173, A, Shah & Nahar India, Sunmill Road");
        customer.setAddressLine2("Dhanraj Mill Compound, Lower Parel (west)");
        customer.setVillage("Mumbai");
        customer.setDistrict("Maharashtra");
        customer.setPostalCode("400013");
        customer.setCreatedTime(new Date());

        Customer savedCustomer = repo.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void testListCustomers(){
        Iterable<Customer> customers = repo.findAll();
        customers.forEach(System.out::println);

        assertThat(customers).hasSizeGreaterThan(1);
    }

    @Test
    public void testUpdateCustomer(){
        Integer customerId = 1;
        String lastName = "Stanfield";

        Customer customer = repo.findById(customerId).get();
        customer.setLastName(lastName);
        customer.setEnabled(true);

        Customer updatedCustomer = repo.save(customer);
        assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);
    }

    @Test
    public void testGetCustomer(){
        Integer customerId = 2;
        Optional<Customer> findById = repo.findById(customerId);

        assertThat(findById).isPresent();

        Customer customer = findById.get();
        System.out.println(customer);
    }

    @Test
    public void testDeleteCustomer(){
        Integer customerId = 2;
        repo.deleteById(customerId);

        Optional<Customer> findById = repo.findById(customerId);
        assertThat(findById).isNotPresent();
    }

    @Test
    public void testFindByEmail(){
        String email = "david@gmail.com";
        Customer customer = repo.findByEmail(email);

        assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testFindByVerificationCode(){
        String code = "code_123";
        Customer customer = repo.findByVerificationCode(code);

        assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testEnableCustomer(){
        Integer customerId = 1;
        repo.enable(customerId);

        Customer customer = repo.findById(customerId).get();
        assertThat(customer.isEnabled()).isTrue();
    }

    @Test
    public void testUpdateAuthenticationType(){
        Integer id = 1;
        repo.updateAuthenticationType(id, AuthenticationType.GOOGLE);

        Customer customer = repo.findById(id).get();
        assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.GOOGLE);
    }
}
