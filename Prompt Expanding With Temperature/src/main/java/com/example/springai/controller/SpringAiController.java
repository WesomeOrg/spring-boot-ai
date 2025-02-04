package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/prompt")
    public String prompt() {
        var systemMessageText = """
                You are a customer service AI assistant. Your task is to send an email reply to a valued customer. Given the customer email delimited by ```,
                Generate a reply to thank the customer for their review. If the sentiment is positive or neutral, thank them for their review. 
                If the sentiment is negative, apologize and suggest that they can reach out to customer service. Make sure to use specific details from the review.
                Write in a concise and professional tone.
                Sign the email as `AI customer agent`.
                
                Customer review: ```{userMessageText}```
                
                Review sentiment: {sentiment}
                
                """;
        var sentiment = "negative";

        var userMessageText = """
                So, they still had the 17 piece system on seasonal  sale for around $49 in the month of November, about  half off, but for some reason (call it price gouging) around the second week of December the prices all went up to about anywhere from between $70-$89 for the same system. And the 11 piece system went up around $10 or so in price also from the earlier sale price of $29. So it looks okay, but if you look at the base, the part where the blade locks into place does’t look as good as in previous editions from a few years ago, but I plan to be very gentle with it (example, I crush very hard items like beans, ice, rice, etc. in the blender first then pulverize them in the serving size I want in the blender then switch to the whipping blade for a finer flour, and use the cross cutting blade first when making smoothies, then use the flat blade if I need them finer/less pulpy). Special tip when making smoothies, finely cut and freeze the fruits and vegetables (if using spinach-lightly stew soften the spinach then freeze until ready for use-and if making sorbet, use a small to medium sized food processor) that you plan to use that way you can avoid adding so much ice if at all-when making your smoothie. After about a year, the motor was making a funny noise. I called customer service but the warranty expired already, so I had to buy another one. FYI: The overall quality has gone done in these types of products, so they are kind of counting on brand recognition and consumer loyalty to maintain sales. Got it in about two days.
                """;

        var systemMessage = new SystemMessage(systemMessageText);
        var userMessage = new UserMessage(userMessageText);
        var sentimentMessage = new UserMessage(sentiment);
        var prompt = new Prompt(List.of(systemMessage, userMessage, sentimentMessage), OllamaOptions.builder()
                .temperature(1.0)
                .build());
        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();

    }
}