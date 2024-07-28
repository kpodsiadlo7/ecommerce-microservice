package com.example.apigateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-management", url = "${user-management.url}")
public interface UserManagementClient {

    @GetMapping("/check-user")
    boolean checkUser(@RequestParam String token);
}
