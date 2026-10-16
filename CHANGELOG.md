# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

- Fixed webhook signature verification to use constant-time comparison.
- Bumped Kotlin Gradle plugin to 2.2.21 so CI compiles against AndroidX-pulled kotlin-stdlib 2.2.x.

## [1.0.2] - 2026-10-10

- Synced published artifact metadata to `1.0.2` (`versionName`, Maven `version`, `versionCode`).
- Raised `targetSdk` to 35; aligned `compileOptions` to Java 17 with Kotlin `jvmTarget = 17`.
- Toolchain baseline: AGP 8.7.3 / compileSdk 35 (JDK 17+).

## [1.0.1] - 2026-10-10

- Relicensed public surface under Elastic License 2.0 (ELv2).
- Declared AGP 8.7.3, Kotlin 2.0.21 (later raised on Unreleased), compileSdk 35, JVM 17.
- Added GitHub Actions CI and Dependabot for Gradle / Actions.

## [1.0.0] - 2026-04-06

- Initial public SDK surface with repository documentation kit.
