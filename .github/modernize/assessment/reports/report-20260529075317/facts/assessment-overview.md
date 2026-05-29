# Assessment Overview

This directory contains supplementary analysis documents generated as part of the ZavaFraudDetector application assessment. These documents provide architectural context, dependency details, API contracts, data model information, configuration inventory, and business workflow documentation to support cloud migration planning.

## Documents

| Document | Description |
|----------|-------------|
| [Architecture Diagram](architecture-diagram.md) | Component architecture overview of the ZavaFraudDetector application, including runtime components, key classes, and infrastructure dependencies. Includes a Mermaid component diagram. |
| [Dependency Map](dependency-map.md) | Build dependency map derived from `build.gradle`, listing all direct and transitive dependencies with version information and a dependency graph. |
| [API & Service Contracts](api-service-contracts.md) | Struts 2 action endpoint catalog with request/response contracts, HTTP methods, and sequence diagrams for the nine exposed action routes. |
| [Data Architecture](data-architecture.md) | Database schema inferred from SQL queries in source code, covering six tables (FraudAlerts, Transactions, Accounts, FraudRules, Users, SessionTokens) with an ER diagram and data sensitivity analysis. |
| [Configuration Inventory](configuration-inventory.md) | Inventory of all configuration sources including `frauddetector.properties`, environment variables, and Docker configuration, with secrets and externalization analysis. |
| [Business Workflows](business-workflows.md) | Documentation of the five core business workflows: authentication, flagged queue review, transaction detail, alert decision, and fraud rule management, with sequence diagrams. |
