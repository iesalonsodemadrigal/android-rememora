package com.iesam.rememora.features.home.domain

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.logging.LogLevel
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.LoggingConfig
import com.aallam.openai.client.OpenAI
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.Key
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import javax.inject.Inject

class GetIntentionIAUseCase @Inject constructor() {

    //Constante o dentro de GptDataSource si solo se uno para todos
    private val openAI = OpenAI(
        token = Key.OPENAI_API_KEY,
        logging = LoggingConfig(LogLevel.All)
    )

    suspend operator fun invoke(phrase: String): Either<ErrorApp, String> {

        //GptDataSource - IaDataRepository
        //Si se hace general, se tendria que pasar el prompt (el content). Es lo único que cambia de una a otra feature.
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.Assistant,
                    content = "Quiero saber cuál es la intención del usuario en la pantalla de inicio " +
                            "de mi aplicación. Mi aplicación se llama Rememora y sirve para reproducir " +
                            "diferente contenido multimedia. En el inicio muestra un menú con las " +
                            "siguientes opciones: fotos, vídeos, imágenes y audio. Necesito que me " +
                            "digas cual es el botón que debe pulsar el usuario cuando quiere lo " +
                            "siguiente: \"${phrase}\".\n" +
                            "\n" +
                            "Tu respuesta debe ser el nombre del botón, es decir, solo la palabra " +
                            "\"fotos\" o \"vídeos\" o \"música\" o \"audios\", o una pregunta/frase " +
                            "corta si hay confusión. Cíñete a este tipo de respuesta, no me muestres " +
                            "información.\n"

                )
            )
        )
        return try {
            val response =
                openAI.chatCompletion(chatCompletionRequest).choices.first().message.content!!
            response.right()

        } catch (e: Error) {
            ErrorApp.DataError.left()
        }

        //Llamar a IaRepository y ejecutar el método (devolve un string con el nombre del comando a ejecutar)

    }

}