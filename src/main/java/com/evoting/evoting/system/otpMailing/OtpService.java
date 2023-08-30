package com.evoting.evoting.system.otpMailing;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OtpService {
    @Autowired
    private OtpRepository otpRepository;
    @Value("${termii.api.api-key}")
    private String apiKey;
    @Value("${termii.api.sender-id}")
    private String senderId;
    @Value("${termii.api.base-url}")
    private String baseUrl;

    public DtoResponse sendOtpTrial(String phoneNumber) throws UnirestException {
String apiUrl = baseUrl + "/api/sms/otp/send";

JsonObject jsonPayload = new JsonObject();
jsonPayload.addProperty("api_key",apiKey);
jsonPayload.addProperty("message_type","ALPHANUMERIC");
jsonPayload.addProperty("to",phoneNumber);
jsonPayload.addProperty("from","E-voting");
jsonPayload.addProperty("channel","generic");
jsonPayload.addProperty("pin_attempts","5");
jsonPayload.addProperty("pin_time_to_live","60");
jsonPayload.addProperty("pin_length","4");
jsonPayload.addProperty("pin_placeholder","1234");
jsonPayload.addProperty("message_text","1234");
jsonPayload.addProperty("pin_type","NUMERIC");

        HttpResponse<JsonNode> response = Unirest.post(apiUrl)
                .header("Content-Type", "application/json")
                .body(jsonPayload.toString()).asJson();

        DtoResponse dtoResponse = new Gson().fromJson(response.getBody().toString(),DtoResponse.class);
        OtpEntity sentOtp = OtpEntity.builder()
                .pinId(dtoResponse.getPinId())
                .destinationNumber(phoneNumber)
                .smsStatus(dtoResponse.getSmsStatus())
                .build();
        otpRepository.save(sentOtp);
        if (response.getStatus() == 200) {
            return dtoResponse;
        } else {
            throw new RuntimeException("Failed to send OTP. Status code: " + response.getStatus());
        }
    }
    public DtoResponse2 verify(OtpRequest otpRequest) throws UnirestException {
        OtpEntity otpEntity = otpRepository.findByDestinationNumber(otpRequest.getPhoneNumber()).orElseThrow(()-> new UsernameNotFoundException("not found"));
        String apiUrl = baseUrl + "/api/sms/otp/verify";
        JsonObject jsonPayload = new JsonObject();
        jsonPayload.addProperty("api_key",apiKey);
        jsonPayload.addProperty("pin_id",otpEntity.getPinId());
        jsonPayload.addProperty("pin",otpRequest.getPin());
        HttpResponse<JsonNode> response = Unirest.post(apiUrl)
                .header("Content-Type", "application/json")
                .body(jsonPayload.toString()).asJson();
        DtoResponse2 dtoResponse = new Gson().fromJson(response.getBody().toString(),DtoResponse2.class);

        if (response.getStatus() == 200) {
            otpEntity.setVerificationStatus("verified");
            return dtoResponse;
        } else {
            otpEntity.setVerificationStatus("Invalid");
            throw new RuntimeException("Invalid Otp: " + response.getStatus());
        }
    }
}
