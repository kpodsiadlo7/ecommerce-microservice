# Opis aplikacji

Aplikacja mikroserwisowa w języku **Java**, wykorzystująca technologię **Spring Boot**, **Spring Security**, **Eureka**, **Api-Gateway**, **RabbitMQ**, **Database H2** oraz **OpenFeign**. Aplikacja została zaprojektowana zgodnie z architekturą mikroserwisową i wspiera komunikację zarówno synchroniczną, jak i asynchroniczną, w oparciu o zdarzenia.

## Kluczowe funkcjonalności i cechy

1. **Własna biblioteka do weryfikacji systemów system2system (S2S):**  
   - Biblioteka umożliwia kontrolowanie, które mikroserwisy mogą się ze sobą komunikować.  
   - Dzięki temu system zapewnia, że tylko wybrane aplikacje, takie jak np. `OrderManagement` i `PaymentManagement`, mają dostęp do siebie nawzajem.  
   - Mikroserwisy, które nie zostały wskazane jako uprawnione, nie mogą nawiązywać ze sobą komunikacji.

2. **Zarządzanie ruchem i integracja:**
   - Użycie **API Gateway** do zarządzania ruchem i centralnej obsługi żądań.
   - Wykorzystanie **RabbitMQ** jako systemu kolejkowego do obsługi zdarzeń w komunikacji asynchronicznej.
   - Implementacja **Eureka Server** jako systemu rejestracji i wykrywania usług (**service discovery**).

3. **Bezpieczeństwo:**
   - Całość aplikacji została zabezpieczona przy użyciu **Spring Security oraz tokenów JWT (JSON Web Token)**, co zapewnia bezpieczne uwierzytelnianie i autoryzację.
   - System obsługuje **rejestrację nowych użytkowników** oraz **logowanie**.

4. **Komunikacja i model działania:**
   - Obsługa zdarzeń **asynchronicznych** za pomocą RabbitMQ.
   - Możliwość działania **synchronicznego**, np. dla kluczowych operacji wymagających bezpośrednich odpowiedzi.

---

  Prosty schemat architektoniczny

   ![image](https://github.com/user-attachments/assets/c7696dfa-4d1d-4ec5-adcd-d428bbe284bb)
