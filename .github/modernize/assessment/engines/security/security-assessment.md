# Security Assessment Report

**Generated:** 2026-05-29T07:53:17Z

## Summary

| Metric | Count |
|--------|-------|
| Total Findings | 16 |
| CVE Vulnerabilities | 7 |
| CWE Vulnerabilities | 9 |
| Total Rules Assessed | 59 |
| Rules Passed | 50 |

### By Severity

| Severity | Count |
|----------|-------|
| mandatory | 7 |
| optional | 4 |
| potential | 5 |

### By Category

| Category | Count |
|----------|-------|
| CVE | 7 |
| Credentials & Secrets | 4 |
| Code Quality | 3 |
| Concurrency & Synchronization | 2 |

---

## CVE Findings (Dependency Vulnerabilities)

### CVE-2024-53677: Apache Struts file upload logic is flawed
- **Severity:** mandatory (CRITICAL)
- **Story Points:** 1
- **Files:** build.gradle:13

[CVE-2024-53677](https://github.com/advisories/GHSA-43mq-6xmg-29vm): Apache Struts file upload logic is flawed

Severity: CRITICAL

Affected dependencies:
  - org.apache.struts:struts2-core:2.5.30 (declared at build.gradle:13)

Vulnerable range: < 6.4.0

Recommended fix:
  - Upgrade org.apache.struts:struts2-core to 6.4.0 or later (note: 2.5.x branch is EOL; a full framework migration is required)

---

### CVE-2023-50164: Apache Struts vulnerable to path traversal
- **Severity:** mandatory (CRITICAL)
- **Story Points:** 1
- **Files:** build.gradle:13

[CVE-2023-50164](https://github.com/advisories/GHSA-2j39-qcjm-428w): Apache Struts vulnerable to path traversal

Severity: CRITICAL

Affected dependencies:
  - org.apache.struts:struts2-core:2.5.30 (declared at build.gradle:13)

Vulnerable range: >= 2.0.0, < 2.5.33

Recommended fix:
  - Upgrade org.apache.struts:struts2-core to 2.5.33 or later (note: 2.5.x branch is EOL; a full framework migration is recommended)

---

### CVE-2025-68493: Apache Struts 2 is Missing XML Validation
- **Severity:** mandatory (HIGH)
- **Story Points:** 1
- **Files:** build.gradle:13

[CVE-2025-68493](https://github.com/advisories/GHSA-qcfc-hmrc-59x7): Apache Struts 2 is Missing XML Validation

Severity: HIGH

Affected dependencies:
  - org.apache.struts:struts2-core:2.5.30 (declared at build.gradle:13)

Vulnerable range: >= 2.5.0, <= 2.5.33 (no fix available for the 2.5.x branch)

Recommended fix:
  - Migrate to org.apache.struts:struts2-core 6.1.1 or later (requires full framework upgrade from EOL 2.5.x)

---

### CVE-2025-66675: Apache Struts has a Denial of Service vulnerability
- **Severity:** mandatory (HIGH)
- **Story Points:** 1
- **Files:** build.gradle:13

[CVE-2025-66675](https://github.com/advisories/GHSA-rg58-xhh7-mqjw): Apache Struts has a Denial of Service vulnerability

Severity: HIGH

Affected dependencies:
  - org.apache.struts:struts2-core:2.5.30 (declared at build.gradle:13)

Vulnerable range: >= 2.0.0, < 6.8.0

Recommended fix:
  - Upgrade org.apache.struts:struts2-core to 6.8.0 or later

---

### CVE-2025-64775: Apache Struts is Vulnerable to DoS via File Leak
- **Severity:** mandatory (HIGH)
- **Story Points:** 1
- **Files:** build.gradle:13

[CVE-2025-64775](https://github.com/advisories/GHSA-xx7v-hqxh-cjr9): Apache Struts is Vulnerable to DoS via File Leak

Severity: HIGH

Affected dependencies:
  - org.apache.struts:struts2-core:2.5.30 (declared at build.gradle:13)

Vulnerable range: >= 2.5.0, <= 2.5.33 (no fix available for the 2.5.x branch)

Recommended fix:
  - Migrate to org.apache.struts:struts2-core 6.8.0 or later (requires full framework upgrade)

---

### CVE-2023-41835: Apache Struts Improper Control of Dynamically-Managed Code Resources vulnerability
- **Severity:** mandatory (HIGH)
- **Story Points:** 1
- **Files:** build.gradle:13

[CVE-2023-41835](https://github.com/advisories/GHSA-729q-fcgp-r5xh): Apache Struts Improper Control of Dynamically-Managed Code Resources vulnerability

Severity: HIGH

Affected dependencies:
  - org.apache.struts:struts2-core:2.5.30 (declared at build.gradle:13)

Vulnerable range: < 2.5.32

Recommended fix:
  - Upgrade org.apache.struts:struts2-core to 2.5.32 or later (note: 2.5.x branch is EOL)

---

### CVE-2023-34396: Apache Struts vulnerable to memory exhaustion
- **Severity:** mandatory (HIGH)
- **Story Points:** 1
- **Files:** build.gradle:13

[CVE-2023-34396](https://github.com/advisories/GHSA-4g42-gqrg-4633): Apache Struts vulnerable to memory exhaustion

Severity: HIGH

Affected dependencies:
  - org.apache.struts:struts2-core:2.5.30 (declared at build.gradle:13)

Vulnerable range: < 2.5.31

Recommended fix:
  - Upgrade org.apache.struts:struts2-core to 2.5.31 or later (note: 2.5.x branch is EOL)

---

## CWE Findings (Code-Level Vulnerabilities)

### CWE-259: Use of Hard-coded Password
- **Category:** Credentials & Secrets
- **Severity:** optional
- **Story Points:** 5
- **Files:** src/main/resources/frauddetector.properties

frauddetector.properties (committed to source control and bundled into the deployable WAR under WEB-INF/classes/) contains db.****** in plain text. FraudConfig.getDbPassword() reads this property as a fallback when the DB_PASSWORD environment variable is not set, meaning the hard-coded password is always present in the classpath and the deployed artifact.

---

### CWE-477: Use of Obsolete Function
- **Category:** Code Quality
- **Severity:** optional
- **Story Points:** 1
- **Files:** src/main/java/com/zavabank/frauddetector/FraudConnectionFactory.java

FraudConnectionFactory uses Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver") in a static initializer to manually register the JDBC driver. Since JDBC 4.0 (Java 6+), the Service Provider mechanism automatically discovers and loads JDBC drivers from the classpath without requiring explicit Class.forName() calls. This pattern is obsolete for Java 11 applications.

---

### CWE-567: Unsynchronized Access to Shared Data in a Multithreaded Context
- **Category:** Concurrency & Synchronization
- **Severity:** potential
- **Story Points:** 5
- **Files:** src/main/java/com/zavabank/frauddetector/FlaggedQueueAction.java, src/main/java/com/zavabank/frauddetector/TransactionDetailAction.java

FlaggedQueueAction declares two static final fields: DecimalFormat MONEY_FORMAT and SimpleDateFormat DATE_FORMAT (lines 13-14). TransactionDetailAction declares a static final SimpleDateFormat DATE_FORMAT (line 9). Both SimpleDateFormat and DecimalFormat are not thread-safe — they hold internal mutable calendar/number-parsing state. These static instances are shared across all concurrent Tomcat request-handling threads without synchronization.

---

### CWE-732: Incorrect Permission Assignment for Critical Resource
- **Category:** Credentials & Secrets
- **Severity:** optional
- **Story Points:** 5
- **Files:** src/main/resources/frauddetector.properties, Dockerfile

frauddetector.properties containing database credentials (db.user=sa, db.****** is bundled inside the WAR as WEB-INF/classes/frauddetector.properties with no file permission restrictions. The Dockerfile is based on tomcat:9.0-jdk11 and contains no USER directive, so the container runs as root and all files inherit world-readable permissions.

---

### CWE-772: Missing Release of Resource after Effective Lifetime
- **Category:** Code Quality
- **Severity:** potential
- **Story Points:** 3
- **Files:** src/main/java/com/zavabank/frauddetector/SsoSessionService.java

In SsoSessionService.resolveTokenFromAuthGateway(), an HttpURLConnection is opened but conn.disconnect() is never called. When the HTTP status is not 200, the method returns early without disconnecting. Even in the success path, the connection is not explicitly disconnected after the response body is read.

---

### CWE-775: Missing Release of File Descriptor or Handle after Effective Lifetime
- **Category:** Code Quality
- **Severity:** potential
- **Story Points:** 3
- **Files:** src/main/java/com/zavabank/frauddetector/SsoSessionService.java

In SsoSessionService.resolveTokenFromAuthGateway(), the HttpURLConnection holds an underlying socket (file descriptor). conn.disconnect() is never called in any code path — neither when status != 200 (early return) nor after successfully reading the response body.

---

### CWE-778: Insufficient Logging
- **Category:** Credentials & Secrets
- **Severity:** potential
- **Story Points:** 3
- **Files:** src/main/java/com/zavabank/frauddetector/SsoSessionService.java, src/main/java/com/zavabank/frauddetector/AuthTokenInterceptor.java, src/main/java/com/zavabank/frauddetector/FraudConfig.java

The application contains no logging framework (no SLF4J, Log4j, java.util.logging, or System.out calls). Security-critical events are silently swallowed: authentication failures, auth gateway errors, configuration load failures, and data access errors are all caught with ignored exceptions and never recorded.

---

### CWE-798: Use of Hard-coded Credentials
- **Category:** Credentials & Secrets
- **Severity:** optional
- **Story Points:** 5
- **Files:** src/main/resources/frauddetector.properties

frauddetector.properties committed to source control contains hard-coded database credentials: db.user=sa (the SQL Server system administrator account) and db.****** These credentials are bundled into the WAR artifact at build time and are always available at runtime as fallback values.

---

### CWE-820: Missing Synchronization
- **Category:** Concurrency & Synchronization
- **Severity:** potential
- **Story Points:** 8
- **Files:** src/main/java/com/zavabank/frauddetector/FlaggedQueueAction.java, src/main/java/com/zavabank/frauddetector/TransactionDetailAction.java

FlaggedQueueAction.MONEY_FORMAT (DecimalFormat) and FlaggedQueueAction.DATE_FORMAT / TransactionDetailAction.DATE_FORMAT (SimpleDateFormat) are static fields shared across all concurrent request threads. No synchronization, lock, or ThreadLocal is used when calling format() on these instances.
