package com.function;

import com.microsoft.azure.functions.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


/**
 * Unit test for Function class.
 */
public class FunctionTest {
    /**
     * Unit test for HttpTriggerJava method.
     */
    @Test
    public void testHttpTriggerJava() throws Exception {

        final ExecutionContext context = mock(ExecutionContext.class);
        doReturn(Logger.getGlobal()).when(context).getLogger();

        try{
            new Function().run("{\r\n" + //
                                "    \"topic\": \"/subscriptions/{subscription-id}/resourceGroups/Storage/providers/Microsoft.Storage/storageAccounts/xstoretestaccount\",\r\n" + //
                                "    \"subject\": \"/blobServices/default/containers/oc2d2817345i200097container/blobs/oc2d2817345i20002296blob\",\r\n" + //
                                "    \"eventType\": \"Microsoft.Storage.BlobCreated\",\r\n" + //
                                "    \"eventTime\": \"2017-06-26T18:41:00.9584103Z\",\r\n" + //
                                "    \"id\": \"831e1650-001e-001b-66ab-eeb76e069631\",\r\n" + //
                                "    \"data\": {\r\n" + //
                                "      \"api\": \"PutBlockList\",\r\n" + //
                                "      \"clientRequestId\": \"6d79dbfb-0e37-4fc4-981f-442c9ca65760\",\r\n" + //
                                "      \"requestId\": \"831e1650-001e-001b-66ab-eeb76e000000\",\r\n" + //
                                "      \"eTag\": \"0x8D4BCC2E4835CD0\",\r\n" + //
                                "      \"contentType\": \"application/octet-stream\",\r\n" + //
                                "      \"contentLength\": 524288,\r\n" + //
                                "      \"blobType\": \"BlockBlob\",\r\n" + //
                                "      \"url\": \"https://oc2d2817345i60006.blob.core.windows.net/oc2d2817345i200097container/oc2d2817345i20002296blob\",\r\n" + //
                                "      \"sequencer\": \"00000000000004420000000000028963\",\r\n" + //
                                "      \"storageDiagnostics\": {\r\n" + //
                                "        \"batchId\": \"b68529f3-68cd-4744-baa4-3c0498ec19f0\"\r\n" + //
                                "      }\r\n" + //
                                "    },\r\n" + //
                                "    \"dataVersion\": \"\",\r\n" + //
                                "    \"metadataVersion\": \"1\"\r\n" + //
                                "  }", context);
        } catch(Exception e) {
            fail("Prueba fallida, causa: ", e);
        }
    }
}
