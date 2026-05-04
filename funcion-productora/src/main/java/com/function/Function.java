package com.function;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import com.azure.messaging.eventgrid.EventGridPublisherClient;
import com.azure.messaging.eventgrid.EventGridEvent;
import com.azure.messaging.eventgrid.EventGridPublisherClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.logging.Logger;

public class Function {

    @FunctionName("FuncionProductoraEventos")
    public HttpResponseMessage run(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<String> request,
        final ExecutionContext context) {

        Logger logger = context.getLogger();
        logger.info("Recibida petición para notificar a usuarios con reservas pendientes.");

        String body = request.getBody();
        if (body == null || body.isEmpty()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                .body("El cuerpo de la petición está vacío.")
                .build();
        }

        String eventGridTopicEndpoint = "https://topic-rl1.brazilsouth-1.eventgrid.azure.net/api/events";
        String eventGridTopicKey = System.getenv("AZURE_EVENT_GRID_KEY");

        try {
            EventGridPublisherClient<EventGridEvent> client = new EventGridPublisherClientBuilder()
                .endpoint(eventGridTopicEndpoint)
                .credential(new AzureKeyCredential(eventGridTopicKey))
                .buildEventGridEventPublisherClient();
            
            Gson gson = new Gson();
            JsonArray usuarios = gson.fromJson(body, JsonArray.class);

            logger.info("Procesando " + usuarios.size() + " usuarios...");

            for (JsonElement element : usuarios) {
                JsonObject usuario = element.getAsJsonObject();
                String email = usuario.get("email").getAsString();
                String id = usuario.get("id").getAsString();

                // Armar mensaje
                String mensaje = "Aviso de entrega pendiente para el usuario " + email + " (ID: " + id + "). " +
                                 "Por favor devuelva el libro prestado a la brevedad. " +
                                 "Consecuencias de no devolver el libro: suspensión temporal de la cuenta y posibles recargos.";

                logger.info("Generando evento para " + email);

                JsonObject payloadEvento = new JsonObject();
                payloadEvento.addProperty("userId", id);
                payloadEvento.addProperty("email", email);
                payloadEvento.addProperty("mensaje", mensaje);

                EventGridEvent event = new EventGridEvent("/EventGridEvents/NotificacionReserva",
                "Reservas.Notificacion", BinaryData.fromObject(payloadEvento), "1.0");

                client.sendEvent(event);
                logger.info("Evento enviado exitosamente a Event Grid para " + email);
            }

            return request.createResponseBuilder(HttpStatus.OK)
                .body("Eventos creados y enviados correctamente.")
                .build();
        } catch (Exception e) {
            logger.severe("Error al publicar eventos: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al publicar eventos: " + e.getMessage())
                .build();
        }
    }
}
