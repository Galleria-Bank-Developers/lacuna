package br.com.galleriabank.lacuna.cliente.model;

import com.lacunasoftware.signer.documents.CreateDocumentResult;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseDocumentLacuna {
    private CreateDocumentResult createDocumentResult;
    private List<AssinantesModel> listAssinantesModels;

}
