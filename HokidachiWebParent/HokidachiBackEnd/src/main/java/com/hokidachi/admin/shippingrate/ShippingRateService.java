package com.hokidachi.admin.shippingrate;

import com.hokidachi.admin.paging.PagingAndSortingHelper;
import com.hokidachi.admin.product.ProductRepository;
import com.hokidachi.admin.setting.city.CityRepository;
import com.hokidachi.common.entity.City;
import com.hokidachi.common.entity.ShippingRate;
import com.hokidachi.common.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ShippingRateService {
    public static final int RATES_PER_PAGE = 5;
    private static final int DIM_DIVISOR = 139;
    @Autowired private ShippingRateRepository shipRepo;
    @Autowired private CityRepository cityRepo;
    @Autowired private ProductRepository productRepo;

    public void listByPage(int pageNum, PagingAndSortingHelper helper){
        helper.listEntities(pageNum, RATES_PER_PAGE, shipRepo);
    }

    public List<City> listAllCities(){
        return cityRepo.findAllByOrderByNameAsc();
    }

    public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
        ShippingRate rateInDB = shipRepo.findByCityAndDistrict(
                rateInForm.getCity().getId(), rateInForm.getDistrict());
        boolean foundExistingRateInNewMode = rateInForm.getId() == null && rateInDB != null;
        boolean foundDifferentExistingRateInEditMode = rateInForm.getId() != null && rateInDB != null && !rateInDB.equals(rateInForm);

        if(foundExistingRateInNewMode || foundDifferentExistingRateInEditMode){
            throw new ShippingRateAlreadyExistsException("Đã có một tỷ lệ cho điểm đến "
                    + rateInForm.getCity().getName() + ", " + rateInForm.getDistrict());
        }
        shipRepo.save(rateInForm);
    }

    public ShippingRate get(Integer id) throws ShippingRateNotFoundException {
        try {
            return shipRepo.findById(id).get();
        } catch (NoSuchElementException ex){
            throw new ShippingRateNotFoundException("Không thể tìm thấy tỷ lệ vận chuyển với ID " + id);
        }
    }

    public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException {
        Long count = shipRepo.countById(id);
        if(count == null || count == 0){
            throw new ShippingRateNotFoundException("Không thể tìm thấy tỷ lệ vận chuyển với ID " + id);
        }

        shipRepo.updateCODSupport(id, codSupported);
    }

    public void delete(Integer id) throws ShippingRateNotFoundException {
        Long count = shipRepo.countById(id);
        if(count == null || count == 0){
            throw new ShippingRateNotFoundException("Không thể tìm thấy tỷ lệ vận chuyển với ID " + id);
        }
        shipRepo.deleteById(id);
    }

    public float calculateShippingCost(Integer productId, Integer cityId, String district)
            throws ShippingRateNotFoundException {
        ShippingRate shippingRate = shipRepo.findByCityAndDistrict(cityId, district);

        if (shippingRate == null) {
            throw new ShippingRateNotFoundException("Không có tỷ lệ vận chuyển được tìm thấy cho các cho "
                    + "điểm đến. Bạn phải nhập chi phí vận chuyển bằng tay.");
        }

        Product product = productRepo.findById(productId).get();

        float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
        float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;

        return finalWeight * shippingRate.getRate();
    }
}
