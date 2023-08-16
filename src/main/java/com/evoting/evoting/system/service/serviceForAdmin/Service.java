package com.evoting.evoting.system.service.serviceForAdmin;

import com.evoting.evoting.system.dto.request.AdminRequest;
import com.evoting.evoting.system.dto.response.Response;

import java.util.List;

public interface Service {

    Response registerAdmin (AdminRequest adminRequest);
    List<Response> fetchAllAdmin();
    Response updateAdmin(AdminRequest adminRequest);
    Response delete(Long id);

}
