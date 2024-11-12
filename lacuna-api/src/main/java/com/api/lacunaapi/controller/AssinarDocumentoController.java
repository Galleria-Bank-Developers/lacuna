package com.api.lacunaapi.controller;

import com.api.lacunaapi.business.CreateDocumentWithTwoOrMoreSignersWithoutOrderScenario;
import com.api.lacunaapi.model.AssinantesModel;
import com.lacunasoftware.signer.javaclient.exceptions.RestException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/lacuna")
@CrossOrigin(origins = "*")
public class AssinarDocumentoController {

    @PostMapping("/assinardocumento")
    public String signDocument(@RequestBody AssinantesModel request) {
        try {
            CreateDocumentWithTwoOrMoreSignersWithoutOrderScenario createDocumentWithTwoOrMoreSignersWithoutOrderScenario = new CreateDocumentWithTwoOrMoreSignersWithoutOrderScenario();
            createDocumentWithTwoOrMoreSignersWithoutOrderScenario.Init();
            createDocumentWithTwoOrMoreSignersWithoutOrderScenario.signDocument(request.getNome(), request.getListaAssinantes(), request.getDocumento());

            return "Documento assinado com sucesso!";
        } catch (IOException | RestException e) {
            e.printStackTrace();
            return "Erro ao assinar o documento: " + e.getMessage();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
