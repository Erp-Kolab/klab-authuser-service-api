package com.klab.authuser_service_api.repository;

import com.klab.authuser_service_api.infrastructure.dto.response.PersonnelResponse;
import com.klab.authuser_service_api.utils.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class PeopleRepository {

   @Value("${services.people.getpeople}")
   private String urlgetPeople;


    public Mono<PersonnelResponse> findByEmail(Integer email) {
        String url = urlgetPeople+"/"+email;

        return WebClient.create()
                .get()
                .uri(url, email)
                .retrieve()
                .bodyToMono(PersonnelResponse.class)
                .doOnNext(response -> {
                    try {
                        String json = Util.objectToJson(response);
                        System.out.println("  Data recibida:\n" + json);
                    } catch (Exception e) {
                        System.err.println("  Error al convertir a JSON: " + e.getMessage());
                    }
                });
    }

//    private void showResponseFields(PersonnelResponse response) {
//        System.out.println("✅ === DATA RECIBIDA (Fallback) ===");
//        try {
//            java.lang.reflect.Field[] fields = response.getClass().getDeclaredFields();
//            for (java.lang.reflect.Field field : fields) {
//                field.setAccessible(true);
//                try {
//                    Object value = field.get(response);
//                    System.out.println("🔍 " + field.getName() + ": " + value);
//                } catch (IllegalAccessException e) {
//                    System.out.println("🔍 " + field.getName() + ": [No accessible]");
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("❌ Error en reflection: " + e.getMessage());
//        }
//        System.out.println("✅ === FIN DATA ===");
//    }
}
