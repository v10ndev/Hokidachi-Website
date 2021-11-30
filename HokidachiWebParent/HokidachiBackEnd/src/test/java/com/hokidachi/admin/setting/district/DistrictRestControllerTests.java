package com.hokidachi.admin.setting.district;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import com.hokidachi.admin.setting.city.CityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hokidachi.common.entity.City;
import com.hokidachi.common.entity.District;

@SpringBootTest
@AutoConfigureMockMvc
public class DistrictRestControllerTests {

    @Autowired MockMvc mockMvc;

    @Autowired ObjectMapper objectMapper;

    @Autowired
    CityRepository cityRepo;

    @Autowired DistrictRepository districtRepo;

    @Test
    @WithMockUser(username = "nam", password = "something", roles = "Admin")
    public void testListDistricts() throws Exception {
        Integer cityId = 2;
        String url = "/districts/list_by_city/" + cityId;

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String jsobResponse = result.getResponse().getContentAsString();
        District[] districts = objectMapper.readValue(jsobResponse, District[].class);

        assertThat(districts).hasSizeGreaterThan(0);
    }

    @Test
    @WithMockUser(username = "nam", password = "something", roles = "Admin")
    public void testCreateDistrict() throws JsonProcessingException, Exception {
        String url = "/districts/save";
        Integer cityId = 2;
        City city = cityRepo.findById(cityId).get();
        District district = new District("Arizona", city);

        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
                .content(objectMapper.writeValueAsString(district))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer districtId = Integer.parseInt(response);
        Optional<District> findById = districtRepo.findById(districtId);

        assertThat(findById.isPresent());
    }

    @Test
    @WithMockUser(username = "nam", password = "something", roles = "Admin")
    public void testUpdateDistrict() throws JsonProcessingException, Exception {
        String url = "/districts/save";
        Integer districtId = 9;
        String districtName = "Alaska";

        District district = districtRepo.findById(districtId).get();
        district.setName(districtName);

        mockMvc.perform(post(url).contentType("application/json")
                .content(objectMapper.writeValueAsString(district))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(districtId)));

        Optional<District> findById = districtRepo.findById(districtId);
        assertThat(findById.isPresent());

        District updateDistrict = findById.get();
        assertThat(updateDistrict.getName()).isEqualTo(districtName);
    }

    @Test
    @WithMockUser(username = "nam", password = "something", roles = "Admin")
    public void testDeleteDistrict() throws Exception {
        Integer districtId = 6;
        String url ="/districts/delete/" + districtId;
        mockMvc.perform(get(url)).andExpect(status().isOk());

        Optional<District> findById = districtRepo.findById(districtId);

        assertThat(findById.isPresent());
    }
}