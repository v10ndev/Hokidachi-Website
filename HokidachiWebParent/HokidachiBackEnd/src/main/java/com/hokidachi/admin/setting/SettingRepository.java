package com.hokidachi.admin.setting;

import com.hokidachi.common.entity.setting.SettingCategory;
import org.springframework.data.repository.CrudRepository;

import com.hokidachi.common.entity.setting.Setting;

import java.util.List;

public interface SettingRepository extends CrudRepository<Setting, String> {
    public List<Setting> findByCategory(SettingCategory category);
}