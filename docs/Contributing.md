# Contributing Guidelines

Thank you for considering contributing to the Overseerr Android Client!

## Getting Started

1. Fork the repository.
2. Clone your fork: `git clone https://github.com/YOUR_USERNAME/overseerr-requests.git`.
3. Create a feature branch: `git checkout -b feature/my-new-feature`.

## Code Style

* **Kotlin**: We follow the official [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html).
* **Formatting**: The project uses `ktlint` (via Gradle plugins) to enforce style.

## Commit Messages

We follow the **Conventional Commits** specification. This allows us to automatically generate changelogs and version bumps.

**Format:**

```
<type>(<scope>): <subject>
```

**Types:**

* `feat`: A new feature
* `fix`: A bug fix
* `docs`: Documentation only changes
* `style`: Changes that do not affect the meaning of the code (white-space, formatting, etc)
* `refactor`: A code change that neither fixes a bug nor adds a feature
* `perf`: A code change that improves performance
* `test`: Adding missing tests or correcting existing tests
* `build`: Changes that affect the build system or external dependencies
* `ci`: Changes to our CI configuration files and scripts

**Examples:**

* `feat(auth): implement login screen`
* `fix(nav): crash when clicking back button`
* `build(deps): upgrade compose to 1.6.0`

## Pull Request Process

1. Ensure your code builds locally: `./gradlew assembleDebug`.
2. Run tests: `./gradlew testDebugUnitTest`.
3. Run lint: `./gradlew lintDebug`.
4. Open a Pull Request against the `main` branch.
5. Provide a clear description of your changes.
