package com.klab.authuser_service_api.service;

import com.klab.authuser_service_api.infrastructure.dto.request.PeopleRequest;
import com.klab.authuser_service_api.infrastructure.dto.response.PersonnelResponse;
import com.klab.authuser_service_api.repository.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PeopleService {

    @Autowired
    JwtService jwtService;
    @Autowired
    PeopleRepository peopleRepository;

    public Mono<PersonnelResponse> getPeopleByEmail(PeopleRequest peopleRequest) {
        return peopleRepository.findByEmail(peopleRequest.getEmail());
    }
}