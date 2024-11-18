package com.api.lacunaapi.util;

import com.api.lacunaapi.model.AssinantesModel;
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

    @PostConstruct
    public void Init() throws URISyntaxException {
        String domain = env.getProperty("URL2");
        String token = env.getProperty("API_KEY");
        signerClient = new SignerClient(domain, token);
    }

    // Creates a generic document, useful for certain scenarios.
    public abstract void signDocument(String nomearquivo, List<AssinantesModel> assinantesModelList, String documento) throws IOException, RestException, Exception;

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
}
