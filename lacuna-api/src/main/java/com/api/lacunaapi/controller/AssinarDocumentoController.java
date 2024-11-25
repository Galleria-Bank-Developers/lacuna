package com.api.lacunaapi.controller;

import com.api.lacunaapi.business.CreateDocumentWithTwoOrMoreSignersWithoutOrderScenario;
import com.api.lacunaapi.model.AssinantesModel;
import com.lacunasoftware.signer.javaclient.exceptions.RestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/lacuna")
@CrossOrigin(origins = "*")
public class AssinarDocumentoController {

    @Autowired
    CreateDocumentWithTwoOrMoreSignersWithoutOrderScenario createDocumentWithTwoOrMoreSignersWithoutOrderScenario;

    @PostMapping("/assinardocumento")
    public String signDocument(@RequestBody AssinantesModel request, @RequestBody byte[] file) {
        try {
            createDocumentWithTwoOrMoreSignersWithoutOrderScenario.Init();
            createDocumentWithTwoOrMoreSignersWithoutOrderScenario.signDocument(request.getNome(), request.getListaAssinantes(), request.getDocumento());

            return "Documento enviado com sucesso!";
        } catch (IOException | RestException e) {
            e.printStackTrace();
            return "Erro ao assinar o documento: " + e.getMessage();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
