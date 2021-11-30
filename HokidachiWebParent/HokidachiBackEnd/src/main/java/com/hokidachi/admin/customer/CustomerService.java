package com.hokidachi.admin.customer;

import com.hokidachi.admin.brand.BrandNotFoundException;
import com.hokidachi.admin.paging.PagingAndSortingHelper;
import com.hokidachi.admin.paging.SearchRepository;
import com.hokidachi.admin.setting.city.CityRepository;
import com.hokidachi.common.entity.Brand;
import com.hokidachi.common.entity.City;
import com.hokidachi.common.entity.Customer;
import com.hokidachi.common.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CustomerService {
    public static final int CUSTOMERS_PER_PAGE = 5;

    @Autowired private CustomerRepository customerRepo;
    @Autowired private CityRepository cityRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    public void listByPage(int pageNum, PagingAndSortingHelper helper){
        helper.listEntities(pageNum, CUSTOMERS_PER_PAGE, customerRepo);
    }

    public void updateCustomerEnabledStatus(Integer id, boolean enabled){
        customerRepo.updateEnabledStatus(id, enabled);
    }

    public Customer get(Integer id) throws CustomerNotFoundException {
        try {
            return customerRepo.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new CustomerNotFoundException("Không thể tìm thấy bất kỳ khách hàng nào có id " + id);
        }
    }

    public List<City> listAllCities(){
        return cityRepo.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(Integer id, String email){
        Customer existCustomer = customerRepo.findByEmail(email);

        if(existCustomer != null && existCustomer.getId() != id){           
            return false;
        }

        return true;
    }

    public void save(Customer customerInForm){
        Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();
        if(!customerInForm.getPassword().isEmpty()){
            String encodePassword = passwordEncoder.encode(customerInForm.getPassword());
            customerInForm.setPassword(encodePassword);
        } else {
            customerInForm.setPassword(customerInDB.getPassword());
        }
        customerInForm.setEnabled(customerInDB.isEnabled());
        customerInForm.setCreatedTime(customerInDB.getCreatedTime());
        customerInForm.setVerificationCode(customerInDB.getVerificationCode());
        customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
        customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());

        customerRepo.save(customerInForm);
    }

    public void delete(Integer id) throws CustomerNotFoundException {
        Long countById = customerRepo.countById(id);
        if(countById == null || countById == 0) {
            throw new CustomerNotFoundException("Không thể tìm thấy bất kỳ khách hàng nào có id " + id);
        }
        customerRepo.deleteById(id);
    }
}
