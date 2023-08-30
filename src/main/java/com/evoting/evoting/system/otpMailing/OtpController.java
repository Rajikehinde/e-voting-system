package com.evoting.evoting.system.otpMailing;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OtpController {
    @Autowired
    private OtpService otpService;
    @PostMapping("otp/verify")
    public DtoResponse2 verify(@RequestBody OtpRequest otpRequest) throws UnirestException{
        return otpService.verify(otpRequest);
    }
}
