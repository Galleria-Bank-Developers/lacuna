package com.api.lacunaapi.controller;

import br.com.galleriabank.lacuna.cliente.model.AssinantesModel;
import ch.qos.logback.classic.pattern.Util;
import com.api.lacunaapi.business.CreateDocumentWithTwoOrMoreSignersWithoutOrderScenario;
import com.api.lacunaapi.util.GsonUtil;
import com.lacunasoftware.signer.javaclient.exceptions.RestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class AssinarDocumentoController {

    @Autowired
    CreateDocumentWithTwoOrMoreSignersWithoutOrderScenario createDocumentWithTwoOrMoreSignersWithoutOrderScenario;

    @PostMapping("/assinardocumento")
    public ResponseEntity<String> signDocument(@RequestBody String request) {
        try {
            AssinantesModel assinantesModel = GsonUtil.fromJson(request, AssinantesModel.class);
            createDocumentWithTwoOrMoreSignersWithoutOrderScenario.Init(assinantesModel);

            String signDocumentResponse = createDocumentWithTwoOrMoreSignersWithoutOrderScenario.signDocument(assinantesModel,assinantesModel.getListaAssinantes());

        if(signDocumentResponse.startsWith("REST action POST https://assinador.galleriabank.com.br/api/documents returned HTTP error 400: ")){
            signDocumentResponse = signDocumentResponse.replace("REST action POST https://assinador.galleriabank.com.br/api/documents returned HTTP error 400: ", "");
            return ResponseEntity.badRequest().body(signDocumentResponse);
        }
            return ResponseEntity.ok(signDocumentResponse);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (RestException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
