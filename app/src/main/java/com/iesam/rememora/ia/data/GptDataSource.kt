package com.iesam.rememora.ia.data

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.logging.LogLevel
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.LoggingConfig
import com.aallam.openai.client.OpenAI
import com.iesam.rememora.app.Either
import com.iesam.rememora.ia.Key
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.left
import com.iesam.rememora.app.right
import javax.inject.Inject

class GptDataSource @Inject constructor() {
    private val openAI = OpenAI(
        token = Key.OPENAI_API_KEY,
        logging = LoggingConfig(LogLevel.All)
    )

    suspend fun getIntention(prompt: String): Either<ErrorApp, String> {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.Assistant,
                    content = prompt

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