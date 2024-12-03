package br.com.galleriabank.lacuna.cliente.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AssinantesModel {

    public AssinantesModel(){

    }

    public AssinantesModel(String nome, String email, String cpfCnpj) {
        this.nome = nome;
        this.email = email;
        this.cpfCnpj = cpfCnpj;
    }

    @SerializedName("nome")
    private String nome;

    @SerializedName("cpfCnpj")
    private String cpfCnpj;

    @SerializedName("email")
    private String email;

    @SerializedName("listaAssinantes")
    private List<AssinantesModel> listaAssinantes;

    @SerializedName("documento")
    private String documento;

    @SerializedName("urlAssinatura")
    private String urlAssinatura;

    @SerializedName("documentoBase64")
    private String documentoBase64;
}
