package com.api.lacunaapi.service;

import com.api.lacunaapi.model.AssinantesModel;
import com.api.lacunaapi.util.CommonsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AssinarDocumentoService {

    @Value("${URL2}")
    private String urlDomain;

    @Value("${API_KEY}")
    private String value;

    private final String key = "X-Api-Key";
    private final RestTemplate restTemplate;

    public AssinarDocumentoService() {
        this.restTemplate = new RestTemplate();
    }

    public String getUrlDocumento(String documentId, List<AssinantesModel> assinantesModelList) {
        if (CommonsUtil.semValor(assinantesModelList) || assinantesModelList.isEmpty()) {
            throw new IllegalArgumentException("A lista de assinantes não pode ser vazia.");
        }

        AssinantesModel requestBody = assinantesModelList.get(0);

        String url = String.format(urlDomain + "api/documents/%s/action-url", documentId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", value);

        HttpEntity<AssinantesModel> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

}
