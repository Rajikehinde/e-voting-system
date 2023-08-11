package com.evoting.evoting.system.controller;

import com.evoting.evoting.system.dto.AdminRequest;
import com.evoting.evoting.system.dto.Response;
import com.evoting.evoting.system.service.serviceForAdmin.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminController {
    @Autowired
    private Service service;
    @PostMapping("/register/Admin")
    public Response registerAdmin(@RequestBody AdminRequest adminRequest){
        return service.registerAdmin(adminRequest);
    }
    @GetMapping("/fetchAllAdmin")
    public List<Response> fetchAllAdmin(){
        return service.fetchAllAdmin();
    }
    @PutMapping("/admin/update")
    Response updateAdmin(@RequestBody AdminRequest adminRequest){
        return service.updateAdmin(adminRequest);
    }
    @DeleteMapping("/delete/Admin/{delete}")
    Response delete(@PathVariable("delete") Long id){
        return service.delete(id);
    }
}
