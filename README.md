![Latest release](https://img.shields.io/github/v/release/petersonolbi/code-challenge_capital-gain?label=Latest%20release&style=social)

[![Nubank](https://img.shields.io/badge/Nubank-820AD1?logo=nubank&logoColor=white)](https://nubank.com.br)
[![LinkedIn](https://custom-icon-badges.demolab.com/badge/LinkedIn-0A66C2?logo=linkedin-white&logoColor=fff)](https://www.linkedin.com/in/petersonolbi)
[![Eclipse IDE](https://img.shields.io/badge/Eclipse%20IDE-blue?logo=eclipseide)](https://eclipseide.org)
[![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-blue?logo=intellijidea)](https://www.jetbrains.com/idea)
[![Build](https://github.com/petersonolbi/code-challenge_capital-gain/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/petersonolbi/code-challenge_capital-gain/actions/workflows/gradle.yml)
[![Codacy](https://app.codacy.com/project/badge/Grade/f18ec2fb81884127b5862c73a48b67b6)](https://app.codacy.com/gh/petersonolbi/code-challenge_capital-gain/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=code-challenge_capital-gain&metric=coverage&token=a02a5d6bd1791866ec558de9dce684a643b3a2ec)](https://sonarcloud.io/summary/new_code?id=code-challenge_capital-gain)
[![Last Commit](https://img.shields.io/github/last-commit/petersonolbi/code-challenge_capital-gain/main)](https://github.com/petersonolbi/code-challenge_capital-gain/commits/main)
[![License](https://img.shields.io/github/license/petersonolbi/code-challenge_capital-gain)](LICENSE)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=code-challenge_capital-gain&metric=ncloc&token=a02a5d6bd1791866ec558de9dce684a643b3a2ec)](https://sonarcloud.io/summary/new_code?id=code-challenge_capital-gain)
[![Quality gate status](https://sonarcloud.io/api/project_badges/measure?project=code-challenge_capital-gain&metric=alert_status&token=a02a5d6bd1791866ec558de9dce684a643b3a2ec)](https://sonarcloud.io/summary/new_code?id=code-challenge_capital-gain)
[![Repo Size](https://img.shields.io/github/repo-size/petersonolbi/code-challenge_capital-gain)](https://github.com/petersonolbi/code-challenge_capital-gain)

## Code Challenge: Capital Gain

This project implements the Capital Gains challenge ([*described here*](https://github.com/petersonolbi/code-challenge_capital-gain/blob/main/spec-ptbr.pdf)), processing buy and sell transactions and calculating taxes according to specific rules. The solution prioritizes precision by using `BigDecimal` and functional programming with `BiFunction`, while maintaining a clear separation between the domain and business rules.

📥**Input & Output**

```json
[{"operation":"buy","unit-cost":1,"quantity":1},{"operation":"sell","unit-cost":1,"quantity":1}]
```
```json
[{"tax":0.00},{"tax":0.00}]
```

## ⚡️Run the project
Below are instructions for running the project.

1. **Install [JDK 21](https://www.oracle.com/java/technologies/downloads)**: _To validate the installation, open the command line and type: `java --version`._
2. _Navigate to the project root folder._
3. **Run the Capital Gain**: _Open the command line and type: `gradlew run`._

🧩 **Dependencies:**
1. _[**GSON**](https://github.com/google/gson): JSON serialization._
3. _[**JUnit 5**](https://junit.org/junit5): unit testing._
4. _[**Mockito**](https://site.mockito.org): mocking framework._
5. _[**Project Lombok**](https://projectlombok.org) boilerplate reduction._

## 👨‍🎓 About the Architecture

In the architecture of this project, I chose to avoid directly coupling business rules to domain classes. Although I understand the points raised by [Martin Fowler in the article "Anemic Domain Model"](https://martinfowler.com/bliki/AnemicDomainModel.html), in most of my projects I tend to centralize rules within specific business classes. As previously mentioned, I used the `BigDecimal` type to represent monetary values, which added an extra layer of complexity.
Finally, the rules for each operation were isolated into two classes: `BuyOperationUseCase` and `SellOperationUseCase`:

```mermaid
flowchart LR
    Main --> OL --> BF --> BOUC & SOUC --> T --> OL
    OL --> I & O
    Processing --> A["Account<br><i>{domain}</i>"]

    subgraph Begin
        OL["OperationListener<br><i>{adapter}</i>"]
    end

    subgraph IO["Input & Output"]
        I["Supplier<br><i>{interface}</i>"]
        O["Consumer<br><i>{interface}</i>"]
    end

    subgraph Processing
        BF["BiFunction<br><i>{interface}</i>"]
        BOUC["BuyOperationUseCase<br><i>{business}</i>"]
        SOUC["SellOperationUseCase<br><i>{business}</i>"]
    end

    subgraph Result
        T["Tax<br><i>{domain}</i>"]
    end

    style Main stroke-width:2px,stroke-dasharray: 2
```

> **Attention** the `OperationListenerTest` class runs tests for all scenarios presented in the challenge documentation.

- The application flow begins with the `Main` class, which is responsible for configuring the initial context and instantiating the available operation types.
- As can be seen in the `OperationListener` class constructor, data input and output are defined through [Java](https://www.oracle.com/java/technologies/downloads)'s functional interfaces, exploiting the functional programming capabilities offered by the language.
- The `OperationListener` class then acts as a listener, purposefully simulating the behavior of an event queue.
- This listener invokes implementations of the `BiFunction<Account, Operation, Tax>` interface, which are responsible for applying tax rules based on the operation type.
- It's worth noting that the `Account` class is intentionally modified by reference during operation processing, a deliberate breach of the concept of immutability.