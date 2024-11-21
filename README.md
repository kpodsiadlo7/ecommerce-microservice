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

5. **Spersonalizowana komunikacja na podstawie tokena JWT:**  
   - Każde żądanie użytkownika zawiera **token JWT**, w którym zakodowana jest informacja o tożsamości użytkownika.  
   - Dzięki temu aplikacja może jednoznacznie określić, który użytkownik wysyła zapytanie, i dostarczyć mu tylko te dane, do których ma uprawnienia.  
   - Rozwiązanie to eliminuje konieczność ciągłego przekazywania danych logowania przy każdym żądaniu, co zwiększa wygodę użytkowania i bezpieczeństwo systemu.

---

Prosty schemat architektoniczny

   ![image](https://github.com/user-attachments/assets/c7696dfa-4d1d-4ec5-adcd-d428bbe284bb)

---

User story

# Story - Przepływ działania aplikacji

Aplikacja została zaprojektowana z myślą o spójnej i bezpiecznej komunikacji między mikroserwisami, opierając się na **API Gateway**, tokenach **JWT** oraz **RabbitMQ**. Poniżej przedstawiono krok po kroku główne przypadki użycia:

## 1. Logowanie użytkownika
1. Użytkownik wysyła żądanie logowania do **API Gateway**.  
2. **API Gateway** kieruje żądanie do mikroserwisu **UserManagement**, który:
   - Weryfikuje, czy użytkownik istnieje.  
   - Jeśli tak, generuje i zwraca **token JWT**.  
   - Jeśli nie, zwraca odpowiedź odmowy dostępu.  

## 2. Pobieranie listy produktów
1. Użytkownik, posiadając token JWT, wysyła żądanie do **API Gateway** o pobranie listy produktów.  
2. **API Gateway** przesyła żądanie do **UserManagement**, aby:
   - Zweryfikować token JWT (czy użytkownik ma uprawnienia do tego endpointu i czy token nie wygasł).  
   - Jeśli token jest poprawny, **UserManagement** generuje nowy token, uprawniony do komunikacji z **ProductManagement**, oraz przesyła informacje o użytkowniku do **API Gateway** w tokenie JWT.  
3. **API Gateway** przesyła żądanie do **ProductManagement** wraz z nowym tokenem, a **ProductManagement** zwraca listę dostępnych produktów dla danego użytkownika.

## 3. Dodawanie produktu do koszyka
1. Użytkownik wysyła żądanie na endpoint `addProductToCart` przez **API Gateway**.  
2. Proces weryfikacji wygląda tak samo jak w przypadku pobierania produktów (weryfikacja tokena i generowanie nowego).  
3. Na podstawie tokena:
   - **CartManagement** dodaje produkt do koszyka konkretnego użytkownika.  
   - Jeśli koszyk jeszcze nie istnieje, jest automatycznie tworzony.  
4. **CartManagement** sprawdza, czy produkt jest dostępny w magazynie (**ProductManagement**) przed dodaniem go do koszyka.

## 4. Składanie zamówienia
1. Po złożeniu zamówienia koszyk przechodzi w nowy status **"Pending"**.  
2. W przypadku kolejnych prób dodania produktów do koszyka:
   - Tworzony jest nowy koszyk, ponieważ poprzedni jest już w trakcie przetwarzania.  
   - Produkty z koszyka w statusie **"Pending"** są automatycznie rezerwowane w **ProductManagement** poprzez asynchroniczną komunikację **RabbitMQ**.

## 5. Obsługa płatności
1. **CartManagement** i **ProductManagement** nasłuchują na kolejkę **RabbitMQ** w celu obsługi zdarzeń związanych z płatnościami:
   - Jeśli płatność zakończy się niepowodzeniem (**"Failed"**):
     - **CartManagement** zmienia status koszyka na **"Failed"**.
     - **ProductManagement** zwraca produkty do magazynu, aby były ponownie dostępne do zakupu.  
   - Jeśli płatność zakończy się sukcesem (**"Completed"**):
     - Koszyk zmienia swój status na **"Completed"**.
     - Produkty zostają usunięte z rezerwacji w **ProductManagement**.

---

Dzięki temu procesowi aplikacja zapewnia:
- Bezpieczeństwo i autoryzację na każdym kroku dzięki **JWT**.  
- Automatyzację zarządzania koszykiem (tworzenie koszyków, zmiana statusów).  
- Obsługę zdarzeń w czasie rzeczywistym za pomocą **RabbitMQ**, co pozwala na synchronizację między mikroserwisami.  

