# Architecture Diagram

ZavaFraudDetector is a Java EE web application built with Apache Struts 2, deployed as a WAR on Tomcat 9, using direct JDBC to SQL Server and an external SSO auth gateway for authentication.

## Application Architecture

```mermaid
flowchart TD
    subgraph Client["Client Layer"]
        Browser["Web Browser"]
    end
    subgraph App["Application Layer - Struts 2.5.30 on Tomcat 9"]
        Filter["StrutsPrepareAndExecuteFilter"]
        Interceptor["AuthTokenInterceptor"]
        Actions["Struts 2 Actions"]
        JSPs["JSP Views"]
    end
    subgraph Business["Business Logic"]
        SsoSvc["SsoSessionService"]
        FraudConfig["FraudConfig"]
    end
    subgraph DataAccess["Data Access Layer"]
        ConnFactory["FraudConnectionFactory - JDBC"]
        DB[("SQL Server - ZavaBankDB")]
    end
    subgraph External["External Services"]
        AuthGW["zava-auth-gateway\nWhoAmI.ashx"]
    end

    Browser -->|"HTTP requests"| Filter
    Filter -->|"routes"| Interceptor
    Interceptor -->|"auth check"| SsoSvc
    SsoSvc -->|"resolve .ZAVAAUTH cookie"| AuthGW
    SsoSvc -->|"validate token"| ConnFactory
    Interceptor -->|"authorized"| Actions
    Actions -->|"JDBC queries"| ConnFactory
    ConnFactory -->|"SQL"| DB
    FraudConfig -->|"config"| ConnFactory
    FraudConfig -->|"config"| SsoSvc
    Actions -->|"render"| JSPs
    JSPs -->|"HTML response"| Browser
```

### Technology Stack Summary

| Layer | Technology | Version | Purpose |
|-------|-----------|---------|---------|
| Presentation | Apache Struts 2 + JSP | 2.5.30 | MVC web framework, server-side rendering |
| Web Container | Apache Tomcat | 9.0 | Servlet container hosting the WAR |
| Business Logic | Plain Java | 11 | Fraud alert review, rule management |
| Authentication | SsoSessionService + AuthTokenInterceptor | Custom | SSO token resolution via external gateway |
| Data Access | JDBC (mssql-jdbc) | 12.8.1 | Direct SQL Server access, no ORM |
| Database | Microsoft SQL Server | — | Persistent storage for alerts, transactions, rules |
| Build | Gradle | 7.6 (JDK 11) | WAR packaging |
| Containerization | Docker | — | Multi-stage build deploying to Tomcat 9 image |

### Data Storage & External Services

The application uses a single Microsoft SQL Server database (`ZavaBankDB`) accessed via direct JDBC with the Microsoft JDBC driver. There is no ORM or connection pool library—each action opens a JDBC connection through `FraudConnectionFactory` using credentials resolved from either environment variables or the `frauddetector.properties` file. The external dependency is the `zava-auth-gateway` service (a .NET application), whose `WhoAmI.ashx` endpoint is called over HTTP to resolve `.ZAVAAUTH` FormsAuthentication cookies into session tokens when the Java application cannot decrypt them natively.

### Key Architectural Decisions

- **Direct JDBC without ORM**: All database interactions use raw `PreparedStatement` calls, avoiding any ORM framework (Hibernate, EclipseLink), which simplifies the dependency footprint but requires manual SQL management.
- **Struts 2 interceptor for cross-cutting auth**: Authentication is enforced via a custom `AuthTokenInterceptor` injected into the Struts interceptor stack, keeping auth logic separate from business actions.
- **Dual-source configuration**: `FraudConfig` reads from environment variables first and falls back to `frauddetector.properties`, enabling container-native deployment without code changes.

## Component Relationships

```mermaid
flowchart LR
    subgraph Presentation["Presentation"]
        LoginAction["LoginAction"]
        FlaggedQueueAction["FlaggedQueueAction"]
        TransactionDetailAction["TransactionDetailAction"]
        AlertDecisionAction["AlertDecisionAction"]
        RuleListAction["RuleListAction"]
        RuleEditAction["RuleEditAction"]
        RuleDeleteAction["RuleDeleteAction"]
        HealthAction["HealthAction"]
    end
    subgraph Interceptors["Cross-Cutting"]
        AuthInterceptor["AuthTokenInterceptor"]
    end
    subgraph Services["Business Logic"]
        SsoSvc["SsoSessionService"]
        FraudConfig["FraudConfig"]
    end
    subgraph DataAccess["Data Access"]
        ConnFactory["FraudConnectionFactory"]
    end
    subgraph DTOs["Data Transfer Objects"]
        SessionUser["SessionUser"]
        FraudAlertRecord["FraudAlertRecord"]
        FraudRuleRecord["FraudRuleRecord"]
    end

    AuthInterceptor -->|"uses"| SsoSvc
    SsoSvc -->|"reads config"| FraudConfig
    ConnFactory -->|"reads config"| FraudConfig
    SsoSvc -->|"opens connection"| ConnFactory
    LoginAction -->|"uses"| SsoSvc
    FlaggedQueueAction -->|"queries"| ConnFactory
    TransactionDetailAction -->|"queries"| ConnFactory
    AlertDecisionAction -->|"updates"| ConnFactory
    RuleListAction -->|"queries"| ConnFactory
    RuleEditAction -->|"reads/writes"| ConnFactory
    RuleDeleteAction -->|"deletes"| ConnFactory
    FlaggedQueueAction -->|"populates"| FraudAlertRecord
    TransactionDetailAction -->|"populates"| FraudAlertRecord
    RuleListAction -->|"populates"| FraudRuleRecord
    RuleEditAction -->|"populates"| FraudRuleRecord
    AuthInterceptor -.->|"intercepts all actions"| Presentation
    SsoSvc -->|"creates"| SessionUser
```

### Component Inventory

| Component | Layer | Type | Responsibility |
|-----------|-------|------|---------------|
| LoginAction | Presentation | Struts Action | Handles SSO login and session establishment |
| FlaggedQueueAction | Presentation | Struts Action | Retrieves and displays pending fraud alerts |
| TransactionDetailAction | Presentation | Struts Action | Displays detail of a single fraud alert/transaction |
| AlertDecisionAction | Presentation | Struts Action | Records approve/reject decisions on fraud alerts |
| RuleListAction | Presentation | Struts Action | Lists all fraud detection rules |
| RuleEditAction | Presentation | Struts Action | Creates and edits fraud detection rules |
| RuleDeleteAction | Presentation | Struts Action | Deletes a fraud detection rule |
| HealthAction | Presentation | Struts Action | Exposes health check endpoint (bypasses auth) |
| AuthTokenInterceptor | Cross-Cutting | Struts Interceptor | Enforces authentication on all actions except login/health |
| SsoSessionService | Business Logic | Service | Resolves session tokens from request (header, cookie, WhoAmI gateway) |
| FraudConfig | Business Logic | Configuration | Provides database and SSO config from env vars or properties file |
| FraudConnectionFactory | Data Access | JDBC Factory | Opens SQL Server connections using configured credentials |
| SessionUser | DTOs | DTO | Carries authenticated user ID and username in HTTP session |
| FraudAlertRecord | DTOs | DTO | Carries fraud alert data between DB and JSP views |
| FraudRuleRecord | DTOs | DTO | Carries fraud rule data between DB and JSP views |
