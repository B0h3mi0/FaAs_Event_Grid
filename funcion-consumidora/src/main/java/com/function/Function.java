package com.function;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.EventGridTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;

import java.util.logging.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Function {

    @FunctionName("FuncionConsumidoraEventos")
    public void run(
        @EventGridTrigger(name = "eventGridEvent") String content,
        final ExecutionContext context
    ) {
        Logger logger = context.getLogger();
        logger.info("Función con Event Grid trigger ejecutada.");

        try {
            Gson gson = new Gson();
            JsonObject eventGridEvent = gson.fromJson(content, JsonObject.class);

            logger.info("Evento recibido: " + eventGridEvent.toString());

            String eventType = eventGridEvent.get("eventType").getAsString();
            String data = eventGridEvent.get("data").toString();

            logger.info("Tipo de evento: " + eventType);
            logger.info("Data del evento: " + data);

            procesarEvento(eventType, data, logger);

        } catch (Exception e) {
            // Captura y loguea cualquier excepción
            logger.severe("Error al procesar el evento: " + e.getMessage());
            e.printStackTrace(); // Imprime el stacktrace en consola
        }
    }

    private void procesarEvento(String eventType, String data, Logger logger) {
        if ("Reservas.Notificacion".equals(eventType)) {
            try {
                Gson gson = new Gson();
                JsonObject payload = gson.fromJson(data, JsonObject.class);
                
                String email = payload.has("email") ? payload.get("email").getAsString() : "desconocido";
                String mensaje = payload.has("mensaje") ? payload.get("mensaje").getAsString() : "Sin mensaje";
                
                logger.info("===============================================");
                logger.info("SIMULANDO ENVIO DE CORREO ELECTRONICO");
                logger.info("Destinatario: " + email);
                logger.info("Contenido: \n" + mensaje);
                logger.info("Estado: ENVIADO CON EXITO");
                logger.info("===============================================");

            } catch (Exception e) {
                logger.severe("Error parseando data del evento: " + e.getMessage());
            }
        } else {
            logger.info("Evento no es de tipo Reservas.Notificacion, ignorando. Tipo: " + eventType);
        }
    }
}
