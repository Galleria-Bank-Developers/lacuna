package br.com.galleriabank.lacuna.cliente.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class TokenResponse {
    private String accessToken;
    private String tokenType;
    private String expiresIn;
    private List<String> scope;

}