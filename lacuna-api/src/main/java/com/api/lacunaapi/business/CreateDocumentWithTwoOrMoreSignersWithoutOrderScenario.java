package com.api.lacunaapi.business;

import com.api.lacunaapi.model.AssinantesModel;
import com.api.lacunaapi.service.AssinarDocumentoService;
import com.api.lacunaapi.util.CommonsUtil;
import com.api.lacunaapi.util.Scenario;
import com.lacunasoftware.signer.FileUploadModel;
import com.lacunasoftware.signer.FlowActionType;
import com.lacunasoftware.signer.documents.CreateDocumentRequest;
import com.lacunasoftware.signer.documents.CreateDocumentResult;
import com.lacunasoftware.signer.flowactions.FlowActionCreateModel;
import com.lacunasoftware.signer.javaclient.builders.FileUploadModelBuilder;
import com.lacunasoftware.signer.javaclient.exceptions.RestException;
import com.lacunasoftware.signer.javaclient.models.UploadModel;
import com.lacunasoftware.signer.users.ParticipantUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class CreateDocumentWithTwoOrMoreSignersWithoutOrderScenario extends Scenario {

    @Autowired
    private AssinarDocumentoService assinarDocumentoService;

    @Override
    public String signDocument(String nomeArquivo, List<AssinantesModel> assinantesModelList, String documento) throws IOException, RestException {
        try {
            if (CommonsUtil.semValor(signerClient)) {
                throw new IllegalStateException("SignerClient não inicializado corretamente pelo Spring.");
            }

            if (CommonsUtil.semValor(assinantesModelList) || assinantesModelList.size() < 2) {
                throw new IllegalArgumentException("É necessário uma lista de pelo menos dois signatários.");
            }

            if (CommonsUtil.semValor(documento)) {
                throw new IllegalArgumentException("O documento está vazio ou null.");
            }

            byte[] content = Base64.getDecoder().decode(documento);
            UploadModel uploadModel = signerClient.uploadFile("sample.pdf", content, "application/pdf");

            FileUploadModelBuilder fileUploadModelBuilder = new FileUploadModelBuilder(uploadModel);
            fileUploadModelBuilder.setDisplayName("Two Signers Without Order Sample");

            List<FlowActionCreateModel> flowActionCreateModels = new ArrayList<>();

            for (AssinantesModel assinante : assinantesModelList) {
                ParticipantUserModel participantUser = new ParticipantUserModel();
                participantUser.setName(assinante.getNome());
                participantUser.setEmail(assinante.getEmail());
                participantUser.setIdentifier(assinante.getCpfCnpj());

                FlowActionCreateModel flowActionCreateModel = new FlowActionCreateModel();
                flowActionCreateModel.setType(FlowActionType.SIGNER);
                flowActionCreateModel.setUser(participantUser);

                flowActionCreateModels.add(flowActionCreateModel);
            }

            List<FileUploadModel> fileUploadModels = new ArrayList<>();
            fileUploadModels.add(fileUploadModelBuilder.toModel());

            CreateDocumentRequest documentRequest = new CreateDocumentRequest();
            documentRequest.setFiles(fileUploadModels);
            documentRequest.setFlowActions(flowActionCreateModels);

            CreateDocumentResult result = signerClient.createDocument(documentRequest).get(0);

            System.out.println(String.format("Document %s created", result.getDocumentId().toString()));

           return assinarDocumentoService.getUrlDocumento(result.getDocumentId().toString(), assinantesModelList);

        } catch (Exception e) {
            System.err.println("Erro na assinatura lacuna: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado ao tentar assinar o documento", e);
        }
    }

}