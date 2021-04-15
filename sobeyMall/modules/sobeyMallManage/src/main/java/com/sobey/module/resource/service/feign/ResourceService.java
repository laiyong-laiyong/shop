package com.sobey.module.resource.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.sobey.module.resource.model.request.ResourceRequest;

/**
 * @Description 资源相关api
 * @Author WuChenYang
 * @Since 2020/2/3 13:52
 */
@FeignClient(name = "${address.auth.name}", url = "${address.auth.url}")
public interface ResourceService {

    @PostMapping(value = "/v1.0/resource")
    Long add(ResourceRequest resource, @RequestHeader(value = "sobeyhive-http-token") String value,
    		@RequestHeader(value = "sobeyhive-http-site") String site);
    
    @PutMapping(value = "/v1.0/resource")
    boolean update(ResourceRequest resource, @RequestHeader(value = "sobeyhive-http-token") String value,@RequestHeader(value = "sobeyhive-http-site") String site);

    @DeleteMapping(value = "/v1.0/resource/{id}")
    void delete(@PathVariable(name = "id") String id,
                    @RequestHeader(value = "sobeyhive-http-token") String value,@RequestHeader(value = "sobeyhive-http-site") String site);

}
