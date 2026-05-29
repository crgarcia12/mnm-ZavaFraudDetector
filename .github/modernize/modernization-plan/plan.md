# Modernization Plan: modernization-plan

**Project**: mnm-ZavaFraudDetector

---

## Technical Framework

- **Language**: Java 11
- **Framework**: Apache Struts 2 (v2.5.30)
- **Build Tool**: Gradle
- **Database**: Microsoft SQL Server (via mssql-jdbc 12.8.1)
- **Key Dependencies**: Apache Struts 2 Core, Microsoft SQL Server JDBC Driver, javax.servlet-api

---

## Overview

> This migration modernizes the ZavaFraudDetector application — a Java-based bank fraud detection web application — by remediating known security vulnerabilities and removing hardcoded credentials. The application currently uses Apache Struts 2 with direct JDBC connections to SQL Server, with database credentials stored as plaintext in a properties file. The new architecture will:
>
> - Eliminate critical and high-severity CVEs in Apache Struts 2 by upgrading vulnerable dependency versions.
> - Replace all hardcoded plaintext credentials (database password, connection details) with secure storage in Azure Key Vault.
>
> The migration is phased: credential externalization first, followed by CVE remediation, ensuring a secure and cloud-ready application.

---

## Migration Impact Summary

| Application            | Original Service          | New Azure Service      | Authentication     | Comments                                      |
|------------------------|---------------------------|------------------------|--------------------|-----------------------------------------------|
| mnm-ZavaFraudDetector  | Plaintext credentials      | Azure Key Vault        | Managed Identity   | Migrate hardcoded DB password and credentials |
| mnm-ZavaFraudDetector  | Apache Struts 2 (v2.5.30) | Apache Struts 2 (patched) | N/A             | Remediate 7 mandatory CVEs in Struts 2        |

---

## Open Questions & Questionnaire

- [x] Q: Should hardcoded credentials in `frauddetector.properties` be migrated to Azure Key Vault? → A: Yes, using Managed Identity for secure, passwordless access.
- [x] Q: Should CVEs in Apache Struts 2 be remediated as part of this plan? → A: Yes, all 7 mandatory CVEs are addressed by the security task.
