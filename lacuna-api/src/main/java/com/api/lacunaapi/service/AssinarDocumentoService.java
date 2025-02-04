package com.api.lacunaapi.service;

import br.com.galleriabank.lacuna.cliente.model.AssinantesModel;
import br.com.galleriabank.lacuna.cliente.model.ResponseDocumentLacuna;
import com.api.lacunaapi.model.UrlAssinanteRequest;
import com.api.lacunaapi.model.UrlResponseModel;
import com.api.lacunaapi.util.CommonsUtil;
import com.api.lacunaapi.util.GsonUtil;
import com.lacunasoftware.signer.documents.CreateDocumentRequest;
import com.lacunasoftware.signer.documents.CreateDocumentResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AssinarDocumentoService {

    @Value("${URL}")
    private String urlDomain;

    @Value("${API_KEY_CONTRATOS}")
    private String value;

    @Value("${API_KEY_CARTORIO}")
    private String keyValueCatorio;

    private final String key = "X-Api-Key";
    private final RestTemplate restTemplate;

    public AssinarDocumentoService() {
        this.restTemplate = new RestTemplate();
    }

    public String getUrlDocumento(CreateDocumentResult createDocumentResult, List<AssinantesModel> assinantesModelList) {
        if (CommonsUtil.semValor(assinantesModelList) || assinantesModelList.isEmpty()) {
            throw new IllegalArgumentException("A lista de assinantes não pode ser vazia.");
        }

        for (AssinantesModel assinantesModel : assinantesModelList) {
            UrlAssinanteRequest urlAssinanteRequest = new UrlAssinanteRequest(assinantesModel);

            String url = String.format(urlDomain + "api/documents/%s/action-url", createDocumentResult.getDocumentId());

            HttpHeaders headers = new HttpHeaders();

            if (assinantesModel.getTipoEmissaoSelecionadoOnr().equals("contratos")) {
                headers.set("X-Api-Key", value);
            } else {
                headers.set("X-Api-Key", keyValueCatorio);
            }

            HttpEntity<UrlAssinanteRequest> entity = new HttpEntity<>(urlAssinanteRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            UrlResponseModel urlResponseModel = GsonUtil.fromJson(response.getBody(), UrlResponseModel.class);

            assinantesModel.setUrlAssinatura(urlResponseModel.getUrl());

        }

        ResponseDocumentLacuna responseDocumentLacuna = new ResponseDocumentLacuna();
        responseDocumentLacuna.setCreateDocumentResult(createDocumentResult);
        responseDocumentLacuna.setListAssinantesModels(assinantesModelList);

        return GsonUtil.toJson(responseDocumentLacuna);
    }
}
