package com.evoting.evoting.system.service.serviceForAdmin;

import com.evoting.evoting.system.domain.Administration;
import com.evoting.evoting.system.dto.AdminRequest;
import com.evoting.evoting.system.dto.Response;

import java.util.List;

public interface Service {

    Response registerAdmin (AdminRequest adminRequest);
    List<Response> fetchAllAdmin();
    Response updateAdmin(AdminRequest adminRequest);
    Response delete(Long id);

}
