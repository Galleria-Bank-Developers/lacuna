package com.api.lacunaapi.util;

import br.com.galleriabank.lacuna.cliente.model.AssinantesModel;
import com.lacunasoftware.signer.FileUploadModel;
import com.lacunasoftware.signer.FlowActionType;
import com.lacunasoftware.signer.documents.CreateDocumentRequest;
import com.lacunasoftware.signer.documents.CreateDocumentResult;
import com.lacunasoftware.signer.flowactions.FlowActionCreateModel;
import com.lacunasoftware.signer.javaclient.SignerClient;
import com.lacunasoftware.signer.javaclient.builders.FileUploadModelBuilder;
import com.lacunasoftware.signer.javaclient.exceptions.RestException;
import com.lacunasoftware.signer.javaclient.models.UploadModel;
import com.lacunasoftware.signer.users.ParticipantUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public abstract class Scenario {

    protected SignerClient signerClient;

    @Autowired
    protected Environment env;

    public void Init(AssinantesModel assinantesModel) throws URISyntaxException {
        String token;
        String domain = env.getProperty("URL");

        if (assinantesModel.getTipoEmissaoSelecionadoOnr().equals("contratos")){
             token = env.getProperty("API_KEY_CONTRATOS");
        } else{
             token = env.getProperty("API_KEY_CARTORIO");
        }

        signerClient = new SignerClient(domain, token);
    }

    // Creates a generic document, useful for certain scenarios.
    public abstract String signDocument(String nomearquivo, List<AssinantesModel> assinantesModelList, String documento) throws IOException, RestException, Exception;

    protected CreateDocumentResult createDocument() throws IOException, RestException {
        byte[] content = Util.getInstance().getResourceFile("sample.pdf");
        UploadModel uploadModel = signerClient.uploadFile("sample.pdf", content, "application/pdf");

        FileUploadModelBuilder fileUploadModelBuilder = new FileUploadModelBuilder(uploadModel);
        fileUploadModelBuilder.setDisplayName("Simple Document");

        ParticipantUserModel user = new ParticipantUserModel();
        user.setName("Jack Bauer");
        user.setEmail("jack.bauer@mailinator.com");
        user.setIdentifier("75502846369");

        FlowActionCreateModel flowActionCreateModel = new FlowActionCreateModel();
        flowActionCreateModel.setType(FlowActionType.SIGNER);
        flowActionCreateModel.setUser(user);

        CreateDocumentRequest documentRequest = new CreateDocumentRequest();
        documentRequest.setFiles(new ArrayList<FileUploadModel>() {
            private static final long serialVersionUID = 1L;

            {
                add(fileUploadModelBuilder.toModel());
            }
        });
        documentRequest.setFlowActions(new ArrayList<FlowActionCreateModel>() {
            private static final long serialVersionUID = 1L;

            {
                add(flowActionCreateModel);
            }
        });
        List<CreateDocumentResult> documentResults = signerClient.createDocument(documentRequest);

        return documentResults.get(0);
    }

    public abstract String signDocument(AssinantesModel assinantesModel, List<AssinantesModel> assinantesModelList) throws IOException, RestException;
}
