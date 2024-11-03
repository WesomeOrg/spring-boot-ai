package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
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
                Determine whether each item in the following list of topics is a topic in the text below, which is delimited with triple backticks.

                Give your answer as list with 0 or 1 for each topic.

                List of topics:
                ```{topic}```

                Review:
                ```{userMessageText}```
                """;
        var userMessageText = """
                In a recent survey conducted by the government, public sector employees were asked to rate their level of satisfaction with the department they work at. The results revealed that NASA was the most popular department with a satisfaction rating of 95%.

                One NASA employee, John Smith, commented on the findings, stating, "I'm not surprised that NASA came out on top. It's a great place to work with amazing people and incredible opportunities. I'm proud to be a part of such an innovative organization."

                The results were also welcomed by NASA's management team, with Director Tom Johnson stating, "We are thrilled to hear that our employees are satisfied with their work at NASA. We have a talented and dedicated team who work tirelessly to achieve our goals, and it's fantastic to see that their hard work is paying off."

                The survey also revealed that the Social Security Administration had the lowest satisfaction rating, with only 45% of employees indicating they were satisfied with their job. The government has pledged to address the concerns raised by employees in the survey and work towards improving job satisfaction across all departments.
                """;

        String topicList = "nasa, local government, engineering, employee satisfaction, federal government";

        var systemMessage = new SystemMessage(systemMessageText);
        var userMessage = new UserMessage(userMessageText);
        var topic = new UserMessage(topicList);
        var prompt = new Prompt(List.of(systemMessage, userMessage, topic));
        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent();
    }
}