package cours.apprentissage.springbootllama.chat;

import org.springframework.ai.bedrock.llama.BedrockLlamaChatModel;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
public class ChatController {
    private final BedrockLlamaChatModel chatModel;
    private final IChat iChat;

    public ChatController(BedrockLlamaChatModel chatModel, IChat iChat) {
        this.chatModel = chatModel;
        this.iChat = iChat;
    }

    @GetMapping(value = "/ai/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Je veux etre riche") String message) {
        return Map.of("generation", chatModel.call(message));
    }

    @GetMapping(value = "/ai/generateStream", produces = MediaType.TEXT_PLAIN_VALUE)
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return null;
    }

    @PostMapping(value = "/public/ask")
    public ResponseEntity<String> askAssistant(@RequestBody PromptChat promptChat) {

        String response = iChat.askAssistant(promptChat);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*@GetMapping(value = "/public/stream")
    public Flux<ChatResponse> askAssistantFluxStream(@RequestParam(value ="question" ,defaultValue = "Je veux etre expert en dev logiciel") String question) {
        return iChat.askAssistantFluxStream(question);
    }*/
    @GetMapping(value = "/public/stream1")
    public SseEmitter askAssistantFluxStream1(@RequestParam(value ="question" ,defaultValue = "Je veux etre expert en dev logiciel") String question) {
        SseEmitter sseEmitter=new SseEmitter();
        Flux<org.springframework.ai.chat.model.ChatResponse>responseFlux=iChat.askAssistantFluxStream(question);
        responseFlux.doOnNext(chatResponse -> {
            try {
                sseEmitter.send(responseFlux);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }).doOnComplete(sseEmitter::complete).subscribe();
        return sseEmitter;
    }

    @GetMapping(value = "/public/stream")
    public Flux<String> askAssistantFluxStream(@RequestParam(value ="question" ,defaultValue = "Je veux etre expert en dev logiciel") String question) {
        return iChat.askAssistantFluxStream2(question);
    }

}
