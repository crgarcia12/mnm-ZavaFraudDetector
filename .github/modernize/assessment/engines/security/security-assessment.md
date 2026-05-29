# Security Assessment Report

**Generated:** 2026-05-29T04:12:31.0000000Z

## Summary

| Metric | Count |
|--------|-------|
| Total Findings | 3 |
| CVE Vulnerabilities | 0 |
| CWE Vulnerabilities | 3 |
| Total Rules Assessed | 30 |
| Rules Passed | 27 |

### By Severity

| Severity | Count |
|----------|-------|
| mandatory | 0 |
| optional | 2 |
| potential | 1 |

## CVE Findings (Dependency Vulnerabilities)

None.

## CWE Findings (Code-Level Vulnerabilities)

### CWE-778: Insufficient Logging
- **Category:** Code Quality
- **Severity:** potential
- **Story Points:** 3
- **Files:** src/main/java/com/zavabank/frauddetector/SsoSessionService.java:38, src/main/java/com/zavabank/frauddetector/SsoSessionService.java:112

SQLException and generic exceptions are caught and ignored without security logging in session resolution paths.

### CWE-259: Use of Hard-coded Password
- **Category:** Credentials & Secrets
- **Severity:** optional
- **Story Points:** 5
- **Files:** src/main/resources/frauddetector.properties:5

A static database password value is committed in frauddetector.properties.

### CWE-798: Use of Hard-coded Credentials
- **Category:** Credentials & Secrets
- **Severity:** optional
- **Story Points:** 5
- **Files:** src/main/resources/frauddetector.properties:4, src/main/resources/frauddetector.properties:5

Hard-coded database username and password are present in the repository configuration.
