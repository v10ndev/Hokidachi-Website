package com.hokidachi.setting;

import org.springframework.data.repository.CrudRepository;

import com.hokidachi.common.entity.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

}