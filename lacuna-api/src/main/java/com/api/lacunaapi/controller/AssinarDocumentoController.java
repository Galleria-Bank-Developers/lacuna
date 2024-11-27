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
@RequestMapping("/api/v1/lacuna")
@CrossOrigin(origins = "*")
public class AssinarDocumentoController {

    @Autowired
    CreateDocumentWithTwoOrMoreSignersWithoutOrderScenario createDocumentWithTwoOrMoreSignersWithoutOrderScenario;

    @PostMapping("/assinardocumento")
    public ResponseEntity<String> signDocument(@RequestBody String request) {
        try {
            createDocumentWithTwoOrMoreSignersWithoutOrderScenario.Init();

            AssinantesModel assinantesModel = GsonUtil.fromJson(request, AssinantesModel.class);

            String signDocumentResponse = createDocumentWithTwoOrMoreSignersWithoutOrderScenario.signDocument(
                    assinantesModel.getDocumento(),
                    assinantesModel.getListaAssinantes(),
                    assinantesModel.getDocumentoBase64());

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
