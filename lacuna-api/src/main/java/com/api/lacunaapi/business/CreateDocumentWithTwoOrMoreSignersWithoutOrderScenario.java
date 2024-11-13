package com.api.lacunaapi.business;

import com.api.lacunaapi.model.AssinantesModel;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class CreateDocumentWithTwoOrMoreSignersWithoutOrderScenario extends Scenario {

    @Override
    public void signDocument(String nomearquivo, List<AssinantesModel> assinantesModelList, String documento) throws IOException, RestException {
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

        ParticipantUserModel participantUserOne = new ParticipantUserModel();
        participantUserOne.setName(assinantesModelList.get(0).getNome());
        participantUserOne.setEmail(assinantesModelList.get(0).getEmail());
        participantUserOne.setIdentifier(assinantesModelList.get(0).getCpfCnpj());

        ParticipantUserModel participantUserTwo = new ParticipantUserModel();
        participantUserTwo.setName(assinantesModelList.get(assinantesModelList.size() - 1).getNome());
        participantUserTwo.setEmail(assinantesModelList.get(assinantesModelList.size() - 1).getEmail());
        participantUserTwo.setIdentifier(assinantesModelList.get(assinantesModelList.size() - 1).getCpfCnpj());

        FlowActionCreateModel flowActionCreateModelOne = new FlowActionCreateModel();
        flowActionCreateModelOne.setType(FlowActionType.SIGNER);
        flowActionCreateModelOne.setUser(participantUserOne);

        FlowActionCreateModel flowActionCreateModelTwo = new FlowActionCreateModel();
        flowActionCreateModelTwo.setType(FlowActionType.SIGNER);
        flowActionCreateModelTwo.setUser(participantUserTwo);

        // Criar a lista de FileUploadModel
        List<FileUploadModel> fileUploadModels = new ArrayList<>();
        fileUploadModels.add(fileUploadModelBuilder.toModel());

        // Criar a lista de FlowActionCreateModel
        List<FlowActionCreateModel> flowActionCreateModels = new ArrayList<>();
        flowActionCreateModels.add(flowActionCreateModelOne);
        flowActionCreateModels.add(flowActionCreateModelTwo);

        CreateDocumentRequest documentRequest = new CreateDocumentRequest();
        documentRequest.setFiles(fileUploadModels);
        documentRequest.setFlowActions(flowActionCreateModels);

        CreateDocumentResult result = signerClient.createDocument(documentRequest).get(0);

        System.out.println(String.format("Document %s created", result.getDocumentId().toString()));
    }
}