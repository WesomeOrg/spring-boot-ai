package com.example.springai.config;

import io.a2a.spec.AgentCapabilities;
import io.a2a.spec.AgentCard;
import io.a2a.spec.AgentSkill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BuyerConfiguration {

    public static final String AGENT_NAME = "buyer-agent";
    private static final String AGENT_DESCRIPTION = "Provides Buyer Information";
    private static final String AGENT_VERSION = "1.0.0";
    private static final String PROTOCOL_VERSION = "1.0";

    @Bean
    public AgentCard agentCard(@Value("${server.port:8080}") int port, @Value("${server.servlet.context-path:/}") String contextPath) {
        List<AgentSkill> SUPPORTED_SKILLS = List.of(createBuyerListSkill(), createBuyerInfoSkill());
        return new AgentCard.Builder().name(AGENT_NAME).description(AGENT_DESCRIPTION).url("http://localhost:" + port + contextPath).version(AGENT_VERSION).capabilities(new AgentCapabilities.Builder().streaming(false).build()).defaultInputModes(List.of("text")).defaultOutputModes(List.of("json", "text")).protocolVersion(PROTOCOL_VERSION).skills(SUPPORTED_SKILLS).build();
    }

    AgentSkill createBuyerListSkill() {
        return new AgentSkill.Builder().id("list_of_buyers").name("List Buyers").description("Returns all registered buyers with their summaries").tags(List.of("buyer", "buyers", "directory", "catalog")).examples(List.of("Give me a list of buyers", "List every buyer in your store", "Show the buyer directory")).inputModes(List.of("text")).outputModes(List.of("json", "text")).build();
    }

    //
    AgentSkill createBuyerInfoSkill() {
        return new AgentSkill.Builder().id("buyer_information_by_name").name("Buyer Details By Name").description("Returns full buyer profile when given an exact name").tags(List.of("buyer info", "buyer details", "contact")).examples(List.of("Get buyer info for Anukul Shrivastava", "Share the profile of Deepak Yadav", "Provide buyer contact details for Rohit Raja")).inputModes(List.of("text")).outputModes(List.of("json", "text")).build();
    }
}
