package com.hokidachi.admin.setting;

import org.springframework.data.repository.CrudRepository;

import com.hokidachi.common.entity.Currency;

import java.util.List;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {
    public List<Currency> findAllByOrderByNameAsc();
}