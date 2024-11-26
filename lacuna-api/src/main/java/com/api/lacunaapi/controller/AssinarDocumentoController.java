package com.api.lacunaapi.controller;

import br.com.galleriabank.lacuna.cliente.model.AssinantesModel;
import com.api.lacunaapi.business.CreateDocumentWithTwoOrMoreSignersWithoutOrderScenario;
import com.api.lacunaapi.util.GsonUtil;
import com.lacunasoftware.signer.javaclient.exceptions.RestException;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String signDocument(@RequestBody String request) {
        try {
            createDocumentWithTwoOrMoreSignersWithoutOrderScenario.Init();
            AssinantesModel assinantesModel = GsonUtil.fromJson(request, AssinantesModel.class);

            createDocumentWithTwoOrMoreSignersWithoutOrderScenario.signDocument(assinantesModel.getNome(), assinantesModel.getListaAssinantes(), assinantesModel.getDocumentoBase64());

            return "Documento enviado com sucesso!";
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (RestException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
