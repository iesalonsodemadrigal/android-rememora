package com.iesam.rememora.features.images.domain

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.logging.LogLevel
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.LoggingConfig
import com.aallam.openai.client.OpenAI
import com.iesam.rememora.app.Either
import com.iesam.rememora.app.Env
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import javax.inject.Inject

class GetIntentionUseCase @Inject constructor() {

    private val openAI = OpenAI(
        token = Env.OPENAI_API_KEY,
        logging = LoggingConfig(LogLevel.All)
    )

    suspend operator fun invoke(phrase: String): Either<ErrorApp, String> {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.Assistant,
                    content = "Dime las tres palabras más importantes de esta frase: ${phrase}"

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

    }
}