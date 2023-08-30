package com.evoting.evoting.system.service.serviceForAdmin;

import com.evoting.evoting.system.dto.request.AdminRequest;
import com.evoting.evoting.system.dto.response.Response;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public interface Service {

    Response registerAdmin (AdminRequest adminRequest) throws UnirestException;
    List<Response> fetchAllAdmin();
    Response updateAdmin(AdminRequest adminRequest);
    Response delete(Long id);

}
