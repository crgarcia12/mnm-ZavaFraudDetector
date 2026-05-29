# Configuration & Externalized Settings Inventory

The application has a small configuration surface centered on one properties file, Struts XML, the servlet descriptor, Gradle build metadata, and a Dockerfile. It supports environment-variable overrides for its core connection settings but does not define explicit build or runtime profiles.

## Configuration Sources

| Source | Type | Path/Location | Notes |
|---|---|---|---|
| Gradle build | Build configuration | `build.gradle` | Declares Java, WAR, Struts, and SQL Server JDBC dependencies |
| Struts configuration | Framework XML | `src/main/resources/struts.xml` | Defines actions, interceptor stack, and result mappings |
| Application properties | Properties file | `src/main/resources/frauddetector.properties` | Holds DB and SSO settings with environment override support |
| Servlet descriptor | XML | `src/main/webapp/WEB-INF/web.xml` | Maps Struts filter to all requests and sets welcome file |
| Dockerfile | Container build config | `Dockerfile` | Builds a WAR and deploys it to Tomcat on port 8080 |
| Environment variables | External runtime config | Process environment | Override DB host, port, name, credentials, and auth gateway settings |

## Build Profiles

| Profile | Activation | Purpose | Key Dependencies/Plugins |
|---|---|---|---|
| default | Always active | Builds a Java 11 WAR archive named `ROOT.war` | Gradle `java` and `war` plugins |

## Runtime Profiles

| Profile | Activation Method | Config Files | Key Overrides |
|---|---|---|---|
| default | Implicit | `frauddetector.properties`, `struts.xml`, `web.xml` | DB host, port, name, user, password, SSO cookie name, auth gateway URL |

## Properties Inventory

| Property Key | Default | Profiles | Source |
|---|---|---|---|
| `db.host` | `sqlserver` | default | `frauddetector.properties`, override `DB_HOST` |
| `db.port` | `1433` | default | `frauddetector.properties`, override `DB_PORT` |
| `db.name` | `ZavaBankDB` | default | `frauddetector.properties`, override `DB_NAME` |
| `db.user` | `sa` | default | `frauddetector.properties`, override `DB_USER` |
| `db.password` | `[MASKED]` | default | `frauddetector.properties`, override `DB_PASSWORD` |
| `sso.cookie.name` | `.ZAVAAUTH` | default | `frauddetector.properties`, override `SSO_COOKIE_NAME` |
| `auth.gateway.whoami.url` | `http://zava-auth-gateway:8080/WhoAmI.ashx` | default | `frauddetector.properties`, override `AUTH_GATEWAY_WHOAMI_URL` |
| `struts.devMode` | `false` | default | `struts.xml` |

## Startup Parameters & Resource Requirements

| Service | JVM/Runtime Options | Memory | Instance Count |
|---|---|---|---|
| ZavaFraudDetector | No explicit JVM flags detected; starts via `catalina.sh run` | Not specified | 1 by default |

## Startup Dependency Chain

1. ZavaFraudDetector container starts Tomcat and loads `frauddetector.properties` from the classpath.
2. Authenticated workflows depend on SQL Server being reachable at `sqlserver:1433`.
3. Cookie-based authentication also depends on the auth gateway URL being reachable before protected requests can complete.
4. `/health.action` is the only endpoint that does not require the custom auth interceptor.

## Secrets & Sensitive Configuration

| Secret Reference | Type | Storage (masked) |
|---|---|---|
| `db.password` | Database password | Source-controlled properties file or `DB_PASSWORD` environment variable |
| `db.user` | Database user | Source-controlled properties file or `DB_USER` environment variable |
| `SessionTokens.Token` usage | Session credential | SQL Server table validated at runtime |

### Secrets Provisioning Workflow

The current implementation reads secrets from the local properties file first, then allows environment variables to override them at runtime. No external secret manager, managed identity, encrypted configuration, or deployment-time provisioning workflow was found, so secret distribution depends on either the checked-in properties file or container environment injection.

## Feature Flags

| Flag Name | Default | Controlled By |
|---|---|---|
| None detected | N/A | No conditional feature flags or profile-based feature toggles were found |

## Framework & Runtime Versions

| Component | Version | Source |
|---|---|---|
| Java | 11 | `build.gradle` source and target compatibility |
| Gradle build logic | Compatible with Gradle 7.x style | `build.gradle` and Docker build stage |
| Apache Struts | 2.5.30 | `build.gradle` |
| Servlet API | 4.0.1 | `build.gradle` |
| SQL Server JDBC Driver | 12.8.1.jre11 | `build.gradle` |
| Tomcat | 9.0-jdk11 | `Dockerfile` |
| Gradle image | 7.6-jdk11 | `Dockerfile` |
