package br.com.galleriabank.lacuna.cliente.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AssinantesModel {

    private String nome;
    private String cpfCnpj;
    private String email;
    private List<AssinantesModel> listaAssinantes;
    private String documento;
    private String urlAssinatura;
}