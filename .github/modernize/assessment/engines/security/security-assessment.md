# Security Assessment Report

**Generated:** 2026-05-28T23:22:06.0000000Z

## Summary

| Metric | Count |
|--------|-------|
| Total Findings | 15 |
| CVE Vulnerabilities | 7 |
| CWE Vulnerabilities | 8 |
| Total Rules Assessed | 59 |
| Rules Passed | 51 |

### By Severity

| Severity | Count |
|----------|-------|
| mandatory | 7 |
| optional | 2 |
| potential | 6 |

## CVE Findings (Dependency Vulnerabilities)

### CVE-2025-68493: Apache Struts 2 is Missing XML Validation
- **Severity:** mandatory
- **Story Points:** 1
- **Files:** build.gradle:18

[CVE-2025-68493](https://github.com/advisories/GHSA-qcfc-hmrc-59x7): Apache Struts 2 is Missing XML Validation

Severity: HIGH

Affected dependencies:
  - org.apache.struts:struts2-core:2.5.30 (declared at build.gradle:18)

Recommended fix:
  - No patched version available for the 2.x branch; consider migrating to Struts 6.x

### CVE-2025-66675: Apache Struts has a Denial of Service vulnerability
- **Severity:** mandatory
- **Story Points:** 1
- **Files:** build.gradle:18

[CVE-2025-66675](https://github.com/advisories/GHSA-rg58-xhh7-mqjw): Apache Struts has a Denial of Service vulnerability

Severity: HIGH

Affected dependencies:
  - org.apache.struts:struts2-core:2.5.30 (declared at build.gradle:18)

Recommended fix:
  - Upgrade org.apache.struts:struts2-core to 6.8.0 or later

### CVE-2025-64775: Apache Struts is Vulnerable to DoS via File Leak
- **Severity:** mandatory
- **Story Points:** 1
- **Files:** build.gradle:18

[CVE-2025-64775](https://github.com/advisories/GHSA-xx7v-hqxh-cjr9): Apache Struts is Vulnerable to DoS via File Leak

Severity: HIGH

Affected dependencies:
  - org.apache.struts:struts2-core:2.5.30 (declared at build.gradle:18)

Recommended fix:
  - No patched version available for the 2.x branch; consider migrating to Struts 6.x

### CVE-2024-53677: Apache Struts file upload logic is flawed
- **Severity:** mandatory
- **Story Points:** 1
- **Files:** build.gradle:18

[CVE-2024-53677](https://github.com/advisories/GHSA-43mq-6xmg-29vm): Apache Struts file upload logic is flawed

Severity: CRITICAL

Affected dependencies:
  - org.apache.struts:struts2-core:2.5.30 (declared at build.gradle:18)

Recommended fix:
  - Upgrade org.apache.struts:struts2-core to 6.4.0 or later

### CVE-2023-50164: Apache Struts vulnerable to path traversal
- **Severity:** mandatory
- **Story Points:** 1
- **Files:** build.gradle:18

[CVE-2023-50164](https://github.com/advisories/GHSA-2j39-qcjm-428w): Apache Struts vulnerable to path traversal

Severity: CRITICAL

Affected dependencies:
  - org.apache.struts:struts2-core:2.5.30 (declared at build.gradle:18)

Recommended fix:
  - Upgrade org.apache.struts:struts2-core to 2.5.33 or later

### CVE-2023-41835: Apache Struts Improper Control of Dynamically-Managed Code Resources vulnerability
- **Severity:** mandatory
- **Story Points:** 1
- **Files:** build.gradle:18

[CVE-2023-41835](https://github.com/advisories/GHSA-729q-fcgp-r5xh): Apache Struts Improper Control of Dynamically-Managed Code Resources vulnerability

Severity: HIGH

Affected dependencies:
  - org.apache.struts:struts2-core:2.5.30 (declared at build.gradle:18)

Recommended fix:
  - Upgrade org.apache.struts:struts2-core to 2.5.32 or later

### CVE-2023-34396: Apache Struts vulnerable to memory exhaustion
- **Severity:** mandatory
- **Story Points:** 1
- **Files:** build.gradle:18

[CVE-2023-34396](https://github.com/advisories/GHSA-4g42-gqrg-4633): Apache Struts vulnerable to memory exhaustion

Severity: HIGH

Affected dependencies:
  - org.apache.struts:struts2-core:2.5.30 (declared at build.gradle:18)

Recommended fix:
  - Upgrade org.apache.struts:struts2-core to 2.5.31 or later

## CWE Findings (Code-Level Vulnerabilities)

### CWE-772: Missing Release of Resource after Effective Lifetime
- **Category:** Code Quality
- **Severity:** potential
- **Story Points:** 3
- **Files:** src/main/java/com/zavabank/frauddetector/FraudConfig.java

In FraudConfig.java (static initializer block, lines 10-18), an InputStream is opened via ClassLoader.getResourceAsStream() at line 12 and manually closed at line 15. However, the close() call is not protected by a finally block or try-with-resources. If Properties.load() at line 14 throws an IOException (caught at line 17) or any other RuntimeException, the stream is never closed, resulting in a resource leak.

### CWE-775: Missing Release of File Descriptor or Handle after Effective Lifetime
- **Category:** Code Quality
- **Severity:** potential
- **Story Points:** 3
- **Files:** src/main/java/com/zavabank/frauddetector/FraudConfig.java

In FraudConfig.java (static initializer block, lines 10-18), the InputStream at line 12 is closed manually at line 15 but is not wrapped in a try-with-resources construct. If an exception is raised by Properties.load() at line 14, the close() call at line 15 is skipped and the underlying file descriptor is never released.

### CWE-567: Unsynchronized Access to Shared Data in a Multithreaded Context
- **Category:** Concurrency & Synchronization
- **Severity:** potential
- **Story Points:** 5
- **Files:** src/main/java/com/zavabank/frauddetector/FlaggedQueueAction.java, src/main/java/com/zavabank/frauddetector/TransactionDetailAction.java

FlaggedQueueAction declares two non-thread-safe static fields without synchronization: DATE_FORMAT (SimpleDateFormat, line 14) and MONEY_FORMAT (DecimalFormat, line 13). These fields are accessed by DATE_FORMAT.format() at line 44 and MONEY_FORMAT.format() at line 47, both inside the execute() method. In a servlet/Struts 2 environment, multiple request-handler threads invoke execute() concurrently and share these static instances, causing race conditions that corrupt the internal state of SimpleDateFormat and DecimalFormat (both documented as not thread-safe). TransactionDetailAction has the same pattern with DATE_FORMAT at line 9, used at line 45.

### CWE-662: Improper Synchronization
- **Category:** Concurrency & Synchronization
- **Severity:** potential
- **Story Points:** 8
- **Files:** src/main/java/com/zavabank/frauddetector/FlaggedQueueAction.java, src/main/java/com/zavabank/frauddetector/TransactionDetailAction.java

The static fields MONEY_FORMAT (DecimalFormat) and DATE_FORMAT (SimpleDateFormat) in FlaggedQueueAction and the DATE_FORMAT field in TransactionDetailAction are shared resources that must be accessed exclusively by one thread at a time due to their non-thread-safe internal calendar/number-buffer state. No synchronization mechanism (synchronized block, ReentrantLock, or ThreadLocal) guards access to these fields, leading to improper concurrent access.

### CWE-820: Missing Synchronization
- **Category:** Concurrency & Synchronization
- **Severity:** potential
- **Story Points:** 8
- **Files:** src/main/java/com/zavabank/frauddetector/FlaggedQueueAction.java, src/main/java/com/zavabank/frauddetector/TransactionDetailAction.java

The static SimpleDateFormat DATE_FORMAT (FlaggedQueueAction line 14, TransactionDetailAction line 9) and static DecimalFormat MONEY_FORMAT (FlaggedQueueAction line 13) are accessed concurrently in a web application where multiple threads invoke execute() simultaneously. There is no synchronization whatsoever — no synchronized block, no ThreadLocal wrapper, no lock — around calls to DATE_FORMAT.format() and MONEY_FORMAT.format().

### CWE-259: Use of Hard-coded Password
- **Category:** Credentials & Secrets
- **Severity:** optional
- **Story Points:** 5
- **Files:** src/main/resources/frauddetector.properties

The file frauddetector.properties contains a hard-coded database password at line 5: 'db.******'. Although FraudConfig.java (getDbPassword method) prefers the DB_PASSWORD environment variable, the properties file with a real credential is committed to the repository, exposing the password to anyone with repository access.

### CWE-778: Insufficient Logging
- **Category:** Credentials & Secrets
- **Severity:** potential
- **Story Points:** 3
- **Files:** src/main/java/com/zavabank/frauddetector/LoginAction.java

The application has no logging framework (no SLF4J, Log4j, or java.util.logging). Security-critical events such as failed authentication attempts (LoginAction.execute), unauthorized access, fraud alert decisions (AlertDecisionAction.execute), and database errors are handled only via addActionError() which returns messages to the UI but produces no persistent audit log. This prevents forensic investigation of security incidents.

### CWE-798: Use of Hard-coded Credentials
- **Category:** Credentials & Secrets
- **Severity:** optional
- **Story Points:** 5
- **Files:** src/main/resources/frauddetector.properties

The file frauddetector.properties contains hard-coded database credentials committed to the repository: 'db.user=sa' (line 4) and 'db.******' (line 5). Using the 'sa' (SQL Server system administrator) account with a hard-coded password poses a critical security risk, as it grants full DBA-level access to the database server using a credential that cannot be rotated without modifying the source code.
