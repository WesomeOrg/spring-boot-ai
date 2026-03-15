<div align="center">

# Buyer Agent Service

_A Spring Boot 3.5 service that exposes a buyer-lookup agent with Spring AI tool-calling and the A2A protocol._

</div>

## Table of Contents

1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Features](#features)
4. [Tech Stack & Dependencies](#tech-stack--dependencies)
5. [Getting Started](#getting-started)
6. [Configuration](#configuration)
7. [API Reference](#api-reference)
8. [Built-in Tools](#built-in-tools)
9. [Testing](#testing)
10. [Extending the Buyer Registry](#extending-the-buyer-registry)
11. [Troubleshooting](#troubleshooting)

## Overview

This module hosts a lightweight **buyer agent** that can be embedded into agentic ecosystems via HTTP. It combines:

- **Spring Boot** for the HTTP layer and configuration management
- **Spring AI** for model orchestration, MCP tool exposure, and ChatClient plumbing
- **Ollama** (or any compatible chat model) as the default LLM backend
- **A2A SDK** to speak the Assistants-to-Agents (A2A) protocol so the service can be discovered and invoked by orchestrators

The agent currently exposes two capabilities:

1. `list-of-buyers` – returns the static registry of registered buyers
2. `buyer-information-by-name` – returns the detailed profile for a single buyer when the exact name is provided

## Architecture

```text
┌──────────────────────┐      ┌────────────────────┐      ┌─────────────────────┐
│  HTTP Client / A2A   │ ---> │ BuyerAgentController│ ---> │ Spring AI ChatClient │
└──────────────────────┘      │ /.well-known/agent │      │  + Tool callbacks    │
                              │ /task endpoint     │      └────────┬─────────────┘
                              └────────────────────┘               │
                                                                    │ invokes
                                                            ┌───────▼─────────┐
                                                            │ BuyerAgentTools │
                                                            │ (list, lookup) │
                                                            └─────────────────┘
```

### Key Components

| File | Responsibility |
|------|----------------|
| `BuyerApplication` | Entry point; wires `ChatClient` with `ToolCallbackProvider` so tool methods can be invoked by the LLM. |
| `BuyerAgentController` | Implements the A2A contract: serves agent metadata (`/.well-known/agent.json`) and accepts tasks at `/task`, enforcing guardrails and error handling. |
| `BuyerAgentTools` | Declares Spring AI tools that expose a static registry of buyers plus lookup logic with Unicode normalization. |
| `application.yml` | Configures server port, Spring AI MCP server metadata, Ollama model, and actuator endpoints. |
| `BuyerAgentToolsTest` | Covers the buyer registry behavior (list size, case-insensitive lookup, missing names, blank input). |

## Features

- ✅ Agent metadata compatible with the **A2A protocol** (via `AgentCard`).
- ✅ `/task` endpoint that forwards user prompts to the configured ChatClient with strict guardrails.
- ✅ **Tool-calling support** that lets the LLM invoke registered Java methods for deterministic data access.
- ✅ Static registry of buyers with IDs, summaries, contact numbers, and addresses.
- ✅ Clear validation, logging, and error handling for malformed payloads or empty LLM responses.
- ✅ Build + test automation via Gradle, targeting **Java 21**.

## Tech Stack & Dependencies

| Dependency | Purpose |
|------------|---------|
| Spring Boot 3.5.5 | Core web framework, actuator, dependency management. |
| Spring AI (MCP + Ollama starters) | Provides ChatClient, tool annotations, and MCP server integration. |
| A2A Java SDK | Supplies protocol models (AgentCard, AgentSkill, Message, etc.). |
| Lombok | Adds logging boilerplate via `@Slf4j`. |
| JUnit 5 + AssertJ | Testing stack for the buyer tools. |

> See `build.gradle` for the authoritative dependency list and versions.

## Getting Started

### Prerequisites

- Java 21 (configured via the Gradle toolchain)
- Gradle Wrapper (`./gradlew`) – already included
- An **Ollama** runtime with the `llama3.1:latest` model (or any other compatible chat model configured via env vars)

### Setup

```bash
git clone https://github.com/WesomeOrg/spring-boot-ai.git
cd "Spring Ai Agentic AI/buyer"

# (optional) pull the Ollama model locally
ollama pull llama3.1:latest
```

### Run the Tests

```bash
./gradlew test
```

### Launch the Service (dev mode)

```bash
./gradlew bootRun
```

The server defaults to **http://localhost:8082**. Override ports or AI settings via environment variables (see below).

### Build an Executable Jar

```bash
./gradlew clean bootJar
java -jar build/libs/buyer-0.0.1-SNAPSHOT.jar
```

## Configuration

| Property (env override) | Default | Description |
|-------------------------|---------|-------------|
| `SERVER_PORT` | `8082` | HTTP port for the agent service. |
| `MCP_SERVER_NAME` | `buyer-service` | MCP server identifier reported to clients. |
| `MCP_SERVER_VERSION` | `1.0.1` | MCP server version string. |
| `MCP_SERVER_SSE_ENDPOINT` | `/sse` | SSE endpoint exposed for MCP streaming (disabled in this project). |
| `OLLAMA_CHAT_MODEL` | `llama3.1:latest` | Model name passed to Spring AI’s Ollama chat starter. |

All other Spring Boot properties can be overridden using standard mechanisms (`application.yml`, profiles, env vars, or command-line args).

## API Reference

### `GET /.well-known/agent.json`

Returns a fully populated `AgentCard` describing the agent’s capabilities, skills, supported input/output modes, protocol version, and metadata needed by an orchestrator.

Example excerpt:

```json
{
  "name": "buyer-agent",
  "url": "http://localhost:8082",
  "skills": [
    { "id": "list-of-buyers", "name": "List Buyers", ... },
    { "id": "buyer-information-by-name", "name": "Buyer Details By Name", ... }
  ]
}
```

### `POST /task`

Accepts an A2A `Message` payload that contains text parts describing the user’s task.

**Request Body (example):**

```json
{
  "id": "msg-123",
  "parts": [
    { "type": "text", "text": "List every buyer you know" }
  ]
}
```

**Processing Pipeline:**

1. Controller validates that at least one non-blank text part exists.
2. A guardrailed prompt is built by appending pre-defined response instructions.
3. The prompt is dispatched to `ChatClient`, which may invoke registered tools.
4. The resulting natural-language answer is wrapped back into an A2A agent message via `A2A.toAgentMessage`.

**Error Responses:**

- `400 BAD REQUEST` when the payload is missing or lacks text parts.
- `502 BAD GATEWAY` when the LLM returns an empty response or the invocation fails.

## Built-in Tools

Tools are implemented in `BuyerAgentTools` using `@Tool` annotations so Spring AI can expose them automatically.

| Tool Name | Method | Description |
|-----------|--------|-------------|
| `list_buyers` | `getAllBuyerList()` | Returns the entire static list of registered buyers (id, name, summary, phone, address). |
| `buyer_details_by_name` | `getBuyerDetailsByBuyerName(String buyerName)` | Normalizes and performs a case-insensitive lookup to retrieve a single buyer. Handles blank input and unknown buyers gracefully. |

Both tools write helpful log entries (`@Slf4j`) so invocation activity is visible in application logs.

## Testing

The suite in `BuyerAgentToolsTest` covers the critical behaviors of the lookup logic:

- Static registry returns five buyers with populated names.
- Lookup is **case-insensitive** and trims/normalizes user input.
- Unknown buyers trigger a friendly “No buyer found…” response.
- Blank input prompts the caller to provide a valid name.

Run via `./gradlew test` or let your IDE execute the tests individually.

## Extending the Buyer Registry

1. Edit `BuyerAgentTools` and add/remove entries in `REGISTERED_BUYERS`.
2. Update tests if counts or expectations change.
3. (Optional) introduce a persistence layer or API integration if dynamic data is required.
4. Restart the application—no additional configuration is needed because the registry is in-memory.

## Troubleshooting

| Symptom | Resolution |
|---------|------------|
| `502` errors from `/task` | Ensure the Ollama runtime is running and the configured model is available. Check application logs for stack traces. |
| Empty/slow responses | Verify that the guardrail instructions are not overly restricting output. Consider switching models via `OLLAMA_CHAT_MODEL`. |
| Agent discovery fails | Confirm the service is running on the configured port and that `/.well-known/agent.json` is reachable. |
| Cannot change model/port | Double-check environment variable exports or pass `--server.port=XXXX --spring.ai.ollama.chat.model=...` to the JVM. |

---

Happy hacking! 🧠🤝📇
