package com.evoting.evoting.system.service.serviceForElection;

import com.evoting.evoting.system.domain.Election;
import com.evoting.evoting.system.domain.enmPackage.VoteCategory;
import com.evoting.evoting.system.dto.request.ElectionRequest;
import com.evoting.evoting.system.dto.response.Response;
import com.evoting.evoting.system.repository.ElectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;

@Service
public class ElectionServiceImpl implements ElectionService {
    @Autowired
    private ElectionRepository electionRepository;

    @Override
    public boolean Time(ElectionRequest electionRequest) {
        Election time = Election.builder()
                .electionName(electionRequest.getElectionName())
                .electionDate(electionRequest.getElectionDate())
                .electionTimeStart(electionRequest.getElectionTimeStart())
                .electionTimeOut(electionRequest.getElectionTimeOut())
                .build();

        Election savedTime = electionRepository.save(time);
        return true;

    }
}