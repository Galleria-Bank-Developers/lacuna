package br.com.galleriabank.lacuna.cliente.model;

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