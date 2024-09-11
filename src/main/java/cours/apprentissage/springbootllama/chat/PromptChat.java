package cours.apprentissage.springbootllama.chat;

public class PromptChat {
    private String question;
    private String responseType;
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }
}
