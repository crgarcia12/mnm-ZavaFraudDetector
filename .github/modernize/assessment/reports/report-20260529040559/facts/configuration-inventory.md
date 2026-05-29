# Configuration & Externalized Settings Inventory

Configuration is primarily file-based through Gradle build metadata, Struts XML routing, and a single properties file containing runtime connectivity settings.

## Configuration Sources

| Source | Type | Path or Location | Notes |
|---|---|---|---|
| Gradle build | Build config | build.gradle | Dependency and packaging setup |
| App properties | Runtime config | src/main/resources/frauddetector.properties | DB and auth gateway settings |
| Struts config | Routing config | src/main/resources/struts.xml | Action mapping and interceptors |
| Web descriptor | Servlet config | src/main/webapp/WEB-INF/web.xml | Web app bootstrap |
| Container config | Deployment config | Dockerfile | Build and runtime image definitions |

## Build Profiles

| Profile | Activation | Purpose | Key Dependencies or Plugins |
|---|---|---|---|
| default | automatic | Build WAR artifact | java/war plugins; Struts and SQL Server JDBC deps |

## Runtime Profiles

| Profile | Activation Method | Config Files | Key Overrides |
|---|---|---|---|
| default | application startup | frauddetector.properties | DB host/port/name, SSO cookie name, WhoAmI URL |

## Properties Inventory

| Property Key | Default | Profiles | Source |
|---|---|---|---|
| db.host | sqlserver | default | frauddetector.properties |
| db.port | 1433 | default | frauddetector.properties |
| db.name | ZavaBankDB | default | frauddetector.properties |
| db.user | sa | default | frauddetector.properties |
| db.password | [MASKED] | default | frauddetector.properties |
| sso.cookie.name | .ZAVAAUTH | default | frauddetector.properties |
| auth.gateway.whoami.url | http://zava-auth-gateway:8080/WhoAmI.ashx | default | frauddetector.properties |

## Startup Parameters & Resource Requirements

| Service | JVM or Runtime Options | Memory | Instance Count |
|---|---|---|---|
| Build stage (Gradle) | defaults | not specified | 1 |
| Runtime stage (Tomcat 9) | catalina.sh run | not specified | 1 |

## Startup Dependency Chain

1. Tomcat starts the deployed ROOT.war.
2. Application expects SQL Server to be reachable for DB operations.
3. Protected actions also depend on auth gateway availability for cookie token resolution.

## Secrets & Sensitive Configuration

| Secret Reference | Type | Storage |
|---|---|---|
| db.password | Database credential | file-based property [MASKED] |

### Secrets Provisioning Workflow

Secrets are currently sourced from local properties at runtime. No external secret manager, managed identity, or automated provisioning pipeline integration is defined in this repository.

## Feature Flags

| Flag Name | Default | Controlled By |
|---|---|---|
| None detected | N/A | N/A |

## Framework & Runtime Versions

| Component | Version | Source |
|---|---|---|
| Java | 11 | build.gradle |
| Gradle (build script target) | project config | build.gradle |
| Struts | 2.5.30 | build.gradle |
| SQL Server JDBC Driver | 12.8.1.jre11 | build.gradle |
| Tomcat base image | 9.0-jdk11 | Dockerfile |
