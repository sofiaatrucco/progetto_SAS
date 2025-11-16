# CatERing - Sistema di Gestione Catering

Sistema di gestione per eventi catering: analisi dei requisiti, progettazione UML completa e implementazione business logic in Java.

## Descrizione

Il progetto implementa un sistema completo per la gestione di eventi catering, sviluppato seguendo la metodologia object-oriented. Include:

- **Gestione Menù**: creazione, modifica e pubblicazione di menù con sezioni e voci
- **Gestione Compiti della Cucina**: generazione e assegnazione di task per la preparazione delle ricette

Il sistema è stato progettato per chef, cuochi e organizzatori di eventi, permettendo la pianificazione e il coordinamento delle attività di cucina.

## Struttura del Progetto

### Documentazione

- **Allegato Tecnico**: requisiti e progettazione UML
  - Use Case dettagliati
  - System Sequence Diagrams (SSD)
  - Modello di dominio
  - Contratti delle operazioni
  - Design Class Diagram (DCD)
  - Detailed Sequence Diagrams (DSD)

### Implementazione

```
catering/
├── businesslogic/          # Business logic
│   ├── menu/              # Gestione menù
│   ├── summarySheet/      # Gestione compiti cucina
│   ├── event/             # Gestione eventi
│   ├── recipe/            # Gestione ricette
│   ├── user/              # Gestione utenti
│   └── calendar/          # Gestione turni
├── persistence/           # Strato di persistenza
└── TestCatERing*.java    # Test di validazione
```

## Funzionalità Principali

### Use Case "Gestire Menù"

- Creazione e modifica menù
- Definizione di sezioni
- Inserimento e gestione voci (ricette)
- Pubblicazione menù
- Copia e eliminazione menù
- Configurazione caratteristiche (buffet, finger food, ecc.)

### Use Case "Gestire Compiti della Cucina"

- Generazione foglio riepilogativo da menù
- Apertura e reset foglio riepilogativo
- Definizione e rimozione task
- Assegnazione task a turni e cuochi
- Gestione porzioni e stato preparazione

## Test di Validazione

Il progetto include test completi per validare tutti gli scenari degli use case:

### Gestire Menù

- `TestCatERing.java` - Scenario principale
- `TestCatERing1a/1b/1c.java` - Estensioni al passo 1
- `TestCatERing2a/2b/2c/2d/2ef.java` - Estensioni al passo 2
- `TestCatERing4a/4b/4c.java` - Estensioni al passo 4

### Gestire Compiti

- `Test1a.java` - Aprire foglio riepilogativo
- `Test1b.java` - Reset foglio riepilogativo
- `Test2a.java` - Eliminare task
- `Test5a.java` - Eliminare assegnazione
- `Test5b.java` - Impostare porzioni a pronto

## Configurazione Database

Il sistema utilizza MySQL. Configurare la connessione in `PersistenceManager.java`:

```java
private static String url = "jdbc:mysql://localhost:3307/catering?serverTimezone=UTC";
private static String username = "root";
private static String password = "";
```

## Pattern Utilizzati

- **Singleton**: CatERing (facade)
- **Observer**: Event receiver per sincronizzazione persistenza
- **Facade**: CatERing per accesso ai manager
- **Data Mapper**: Classi di persistenza

## Note Tecniche

- **Linguaggio**: Java
- **Database**: MySQL
- **Pattern architetturale**: Layered Architecture (Business Logic + Persistence)
- **Metodologia**: Object-Oriented Design con UML

## Note

Progetto sviluppato per il corso di Sviluppo Applicazioni Software
