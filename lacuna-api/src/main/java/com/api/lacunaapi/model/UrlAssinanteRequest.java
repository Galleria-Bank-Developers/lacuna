package com.api.lacunaapi.model;

import br.com.galleriabank.lacuna.cliente.model.AssinantesModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlAssinanteRequest {

    private String identifier;
    private String emailAddress;
    private boolean requireEmailAuthentication = false;
    private String flowActionId ;

    public UrlAssinanteRequest(AssinantesModel assinantesModel) {
        identifier = assinantesModel.getCpfCnpj();
        emailAddress = assinantesModel.getEmail();
    }
}
