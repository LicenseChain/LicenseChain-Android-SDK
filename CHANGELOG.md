# Changelog

## [1.0.2] - 2026-10-10

- Synced published artifact metadata to `1.0.2` (`versionName`, Maven `version`, `versionCode`).
- Raised `targetSdk` to 35; aligned `compileOptions` to Java 17 with existing Kotlin `jvmTarget = 17`.
- Toolchain baseline remains AGP 8.7.3 / Kotlin 2.0.21 / compileSdk 35 (JDK 17+).

## [1.0.1] - 2026-10-10

- Relicensed public surface under Elastic License 2.0 (ELv2).
- Declared toolchain refresh: Android Gradle Plugin 8.7.3, Kotlin 2.0.21, compileSdk 35, JVM target 17 (requires JDK 17+).
- Added GitHub Actions CI skeleton and Dependabot for Gradle / Actions.
- Note: local Linux hosts without an Android SDK platform image / Gradle wrapper may still need `sdkmanager` + wrapper generation before assemble succeeds.

## 2026-04-06

- Added repository-level `ROADMAP.md` and `CHANGELOG.md` for workspace doc-gap conformance.
