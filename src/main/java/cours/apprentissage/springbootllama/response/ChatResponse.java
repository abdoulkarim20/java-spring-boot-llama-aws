package cours.apprentissage.springbootllama.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatResponse {
    private String message;
    private Integer promptTokenCount;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getPromptTokenCount() {
        return promptTokenCount;
    }

    public void setPromptTokenCount(Integer promptTokenCount) {
        this.promptTokenCount = promptTokenCount;
    }
}
