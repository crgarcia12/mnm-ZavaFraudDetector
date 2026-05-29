# Configuration & Externalized Settings Inventory

ZavaFraudDetector uses a single flat properties file (`frauddetector.properties`) as its configuration source, with environment-variable overrides for all sensitive and environment-specific values — no external config server, secret store, or runtime profiles are configured.

## Configuration Sources

| Source | Type | Path/Location | Notes |
|--------|------|---------------|-------|
| `frauddetector.properties` | Java properties file | `src/main/resources/frauddetector.properties` | Bundled inside the WAR; loaded at class initialization by `FraudConfig`. Contains defaults for all 7 properties |
| Environment variables | OS/container env | Injected at runtime | Read first by `FraudConfig.read()`; override the properties file value when set. Keys: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `SSO_COOKIE_NAME`, `AUTH_GATEWAY_WHOAMI_URL` |
| `web.xml` | Servlet deployment descriptor | `src/main/webapp/WEB-INF/web.xml` | Registers the Struts 2 filter; no externalized properties |
| `struts.xml` | Struts 2 configuration | `src/main/resources/struts.xml` | Action-to-class mapping, interceptor stack definition; no externalized properties |
| Dockerfile | Container build config | `Dockerfile` | Multi-stage build; no runtime ENV declarations or config injection |

No Spring Cloud Config, Azure App Configuration, AWS AppConfig, HashiCorp Vault, Azure Key Vault, or Consul KV are used.

## Build Profiles

| Profile | Activation | Purpose | Key Dependencies/Plugins |
|---------|-----------|---------|--------------------------|
| _(default — no profiles)_ | Always active | Compiles Java 11 sources, packages WAR as `ROOT.war` | `java`, `war` Gradle plugins; dependencies: `struts2-core`, `mssql-jdbc`, `javax.servlet-api` |

Gradle does not define explicit build profiles (no `buildTypes`, no Maven-style `<profiles>`). There is a single build configuration targeting Java 11 with a WAR artifact named `ROOT`.

## Runtime Profiles

| Profile | Activation Method | Config Files | Key Overrides |
|---------|------------------|--------------|---------------|
| _(none — single profile)_ | N/A | `frauddetector.properties` | All overrides via environment variables at container/runtime startup |

The application has no Spring Boot profile system, no `@Profile` annotations, no `application-dev.yml` / `application-prod.yml`, and no `ASPNETCORE_ENVIRONMENT` equivalent. The single `FraudConfig` class provides all runtime configuration.

## Properties Inventory

### ZavaFraudDetector

| Property Key | Default Value | Env Var Override | Type | Notes |
|-------------|--------------|------------------|------|-------|
| `db.host` | `sqlserver` | `DB_HOST` | String | SQL Server hostname |
| `db.port` | `1433` | `DB_PORT` | Integer | SQL Server port |
| `db.name` | `ZavaBankDB` | `DB_NAME` | String | Database name |
| `db.user` | `sa` | `DB_USER` | String | Database login username |
| `db.password` | `Zava123!` | `DB_PASSWORD` | String | **Sensitive** — see Secrets section |
| `sso.cookie.name` | `.ZAVAAUTH` | `SSO_COOKIE_NAME` | String | Name of the .NET FormsAuth cookie to inspect |
| `auth.gateway.whoami.url` | `http://zava-auth-gateway:8080/WhoAmI.ashx` | `AUTH_GATEWAY_WHOAMI_URL` | URL String | WhoAmI endpoint on the external auth gateway; 3 s connect/read timeout hardcoded in `SsoSessionService` |

The JDBC connection string is assembled programmatically in `FraudConfig.getDbUrl()` with `encrypt=false;trustServerCertificate=true` hardcoded — these TLS settings are **not** configurable via properties.

## Startup Parameters & Resource Requirements

| Service | JVM/Runtime Options | Memory | CPU | Instance Count | Notes |
|---------|---------------------|--------|-----|----------------|-------|
| ZavaFraudDetector | Tomcat defaults (`catalina.sh run`) — no custom `-Xms`/`-Xmx` or `-D` flags configured | Not specified | Not specified | 1 (single container) | Docker image `tomcat:9.0-jdk11`; no `CATALINA_OPTS` or JVM tuning present in Dockerfile |

No Kubernetes manifests, Docker Compose file, Helm charts, or resource limit declarations are present in the repository. JVM memory is unconstrained and governed only by container limits applied at deployment time.

## Startup Dependency Chain

The application has no automated startup dependency mechanism:

1. **SQL Server** (`ZavaBankDB`) — must be available before the WAR starts; the application fails individual requests with `SQLException` if the database is unreachable (no startup-time readiness check)
2. **ZavaFraudDetector (Tomcat/WAR)** — starts independently; no `dockerize`, no `depends_on` health check, no Kubernetes readiness probe
3. **zava-auth-gateway** — the application attempts to contact this service only when a `.ZAVAAUTH` cookie is presented; unavailability causes a silent 3-second timeout per unauthenticated request, not a startup failure

No Spring Cloud Config retry, no `WAIT_FOR` scripts, no liveness/readiness probes are configured.

## Secrets & Sensitive Configuration

| Secret Reference | Type | Stored As | Risk |
|-----------------|------|-----------|------|
| `db.password` / `DB_PASSWORD` | Database password | Plaintext in `frauddetector.properties` (`Zava123!`) | **Critical** — default password committed to source control |
| `db.user` / `DB_USER` | Database username | Plaintext in `frauddetector.properties` (`sa`) | **High** — `sa` is the SQL Server system administrator account |
| `DB_PASSWORD` env var | Database password | Container environment variable at runtime | Medium if injected securely; high if hardcoded in orchestration config |

### Secrets Provisioning Workflow

No secret management workflow is implemented. The database password (`Zava123!`) is committed in plaintext to `frauddetector.properties` inside the WAR artifact. The intended override path is the `DB_PASSWORD` environment variable, but:

- No HashiCorp Vault, Azure Key Vault, AWS Secrets Manager, or Kubernetes Secrets binding is configured
- No identity/access model (managed identity, service principal, RBAC) exists
- Secret rotation is not supported — changing the password requires redeploying the container with a new environment variable

**Recommended remediation**: Remove the default credentials from `frauddetector.properties`, enforce injection via environment variable (or a secret store), and rotate the `sa` password immediately.

## Feature Flags

No feature flag framework, `@ConditionalOnProperty`, `@ConditionalOnExpression`, or custom toggle mechanism is present.

| Flag Name | Default | Controlled By |
|-----------|---------|--------------|
| _(none)_ | N/A | N/A |

## Framework & Runtime Versions

| Component | Version | Source |
|-----------|---------|--------|
| Java (source/target compatibility) | 11 | `build.gradle` (`sourceCompatibility = JavaVersion.VERSION_11`) |
| Gradle (build tool) | 7.6 | `Dockerfile` (`gradle:7.6-jdk11` base image) |
| Apache Struts 2 | 2.5.30 | `build.gradle` dependency |
| Microsoft JDBC Driver (mssql-jdbc) | 12.8.1.jre11 | `build.gradle` dependency |
| javax.servlet-api | 4.0.1 | `build.gradle` (compileOnly) |
| Apache Tomcat | 9.0 | `Dockerfile` (`tomcat:9.0-jdk11` base image) |
| JDK (build stage) | 11 (Temurin) | `Dockerfile` (`gradle:7.6-jdk11`) |
| JDK (runtime stage) | 11 | `Dockerfile` (`tomcat:9.0-jdk11`) |
| Docker base image (build) | `gradle:7.6-jdk11` | `Dockerfile` |
| Docker base image (runtime) | `tomcat:9.0-jdk11` | `Dockerfile` |
