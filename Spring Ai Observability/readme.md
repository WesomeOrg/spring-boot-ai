# Getting Started

Grafana
username - admin
password - SpringAi

http://localhost:8080/hello
http://localhost:8080/actuator

http://localhost:8080/actuator/metrics/gen_ai.client.operation
http://localhost:8080/actuator/metrics/gen_ai.client.operation.active
http://localhost:8080/actuator/metrics/spring.ai.chat.client.operation
http://localhost:8080/actuator/metrics/spring.ai.chat.client.operation.active

http://localhost:3000/d/OS7-NUiGz/spring-boot-statistics-and-endpoint-metrics?orgId=1&refresh=5s
http://localhost:9090/targets?search=#pool-Spring-Boot
http://localhost:9090/graph?g0.expr=up&g0.tab=1&g0.display_mode=lines&g0.show_exemplars=0&g0.range_input=1h

zipkin is not working, check that
