# LicenseChain Android SDK Roadmap

## Current Focus

- Keep default API base usage aligned with `https://api.licensechain.app/v1`.
- Maintain verify-with-details and JWT/JWKS helper compatibility on Android.
- Keep Gradle/Kotlin toolchain and integration guidance aligned with Core API contracts.

## Next Operational Tasks

- Re-run conformance checks after Core API verify/JWKS contract updates.
- Keep docs and examples synchronized with https://docs.licensechain.app/en/sdk.
- Record release-impacting changes in `CHANGELOG.md`; develop experiments on `dev`, release from `main`.


## Active Milestone (2026-10-16)

- **Conformance re-run**: workspace `sdk-conformance` validate reports `errors=0` (remaining WARN = optional README PATCH license lifecycle aliases; DELETE/update paths cover revoke).
- **Docs sync**: README points at `https://docs.licensechain.app/en/sdk`; default API base documented as `https://api.licensechain.app/v1`.
- **Branch opsec**: land experiments on `dev`; promote to `main` only for release-ready changes.

## Ecosystem Notes

- Validate AGP/Kotlin bumps on CI before promoting from `dev` to `main`.
- Local Linux hosts still need Android SDK platform images / Gradle wrapper for assemble.
- Local assemble requires **JDK 17** (Gradle 8.11 / AGP 8.7 fail on JDK 27 with `Unsupported class file major version 71`).
