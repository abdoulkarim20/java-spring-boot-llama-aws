package cours.apprentissage.springbootllama.chat;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.ai.bedrock.llama.BedrockLlamaChatModel;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelWithResponseStreamRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelWithResponseStreamResponseHandler;

import java.util.concurrent.atomic.AtomicReference;
@Slf4j
@Service
public class ChatImpl implements IChat {
    private static final String CLAUDE = "anthropic.claude-v2";
    private final BedrockRuntimeAsyncClient bedrockAsyncClient;
    private final BedrockLlamaChatModel bedrockLlamaChatModel;

    public ChatImpl(BedrockRuntimeAsyncClient bedrockAsyncClient, BedrockLlamaChatModel bedrockLlamaChatModel) {
        this.bedrockAsyncClient = bedrockAsyncClient;
        this.bedrockLlamaChatModel = bedrockLlamaChatModel;
    }

    @Override
    public String askAssistant(PromptChat promptChat) {
        String response = "";
        // Claude requires you to enclose the prompt as follows:
        String enclosedPrompt = "Human: " + promptChat.getQuestion() + "\n\nAssistant:";
        if (promptChat.getResponseType().equals("ASYNC")) response = asyncResponse(enclosedPrompt);
        return response;
    }

    @Override
    public Flux<ChatResponse> askAssistantFluxStream(String promptChat) {
        Prompt prompt=new Prompt(new UserMessage(promptChat));
        return bedrockLlamaChatModel.stream(prompt);
    }

    @Override
    public Flux<String> askAssistantFluxStream2(String promptChat) {
        Flux<String>content=bedrockLlamaChatModel.stream(promptChat);
        return null;
    }

    private String asyncResponse(String enclosedPrompt) {
        var finalCompletion = new AtomicReference<>("");
        var silent = false;
        var payload = new JSONObject().put("prompt", enclosedPrompt).put("temperature", 0.8).put("max_tokens_to_sample", 300).toString();
        var request = InvokeModelWithResponseStreamRequest.builder().body(SdkBytes.fromUtf8String(payload)).modelId(CLAUDE).contentType("application/json").accept("application/json").build();
        var visitor = InvokeModelWithResponseStreamResponseHandler.Visitor.builder().onChunk(chunk -> {
            var json = new JSONObject(chunk.bytes().asUtf8String());
            var completion = json.getString("completion");
            finalCompletion.set(finalCompletion.get() + completion);
            if (!silent) {
                System.out.print(completion);
            }
        }).build();
        var handler = InvokeModelWithResponseStreamResponseHandler.builder().onEventStream(stream -> stream.subscribe(event -> event.accept(visitor))).onComplete(() -> {
        }).onError(e -> System.out.println("\n\nError: " + e.getMessage())).build();
        bedrockAsyncClient.invokeModelWithResponseStream(request, handler).join();
        return finalCompletion.get();
    }
}
