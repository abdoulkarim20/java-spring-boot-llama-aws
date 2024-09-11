package cours.apprentissage.springbootllama.chat;

import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

public interface IChat {
    String askAssistant(PromptChat promptChat);
    Flux<ChatResponse> askAssistantFluxStream(String promptChat);
    Flux<String> askAssistantFluxStream2(String promptChat);
}
