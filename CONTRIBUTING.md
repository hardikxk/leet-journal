# Contributing to Leet Journal

Thank you for your interest in contributing to Leet Journal!

## Before Contributing

- Check existing issues before opening a new one.
- For larger changes, open an issue first to discuss the approach.
- Beginners are encouraged to start with documentation or small fixes.
- Do not spam issues or request assignment for issues that are already assigned to someone else.

### How to contribute

1. Fork the repository.
2. Create a feature branch from `master`.
3. Make the smallest focused change that solves the problem.
4. Run the relevant service tests before opening a pull request.
5. Open a pull request with a clear description of the change and any setup or verification steps.

## Contribution Workflow

1. Fork the repository

2. Clone your fork locally

```bash
git clone https://github.com/your-username/leet-journal.git
```

3. Create a new branch

```bash
git checkout -b feature-name
```

4. Make your changes

5. Commit your changes

```bash
git commit -m "Describe your changes"
```

6. Push your branch

```bash
git push origin feature-name
```

7. Open a Pull Request

## Pull Request Guidelines

- Keep pull requests small and focused.
- Write clear commit messages and PR descriptions.
- Update documentation when setup or behavior changes.
- Test your changes whenever practical before opening a PR.
- Pull requests for unassigned issues are allowed, but may be closed if a better solution already exists or the issue has already been assigned.
- Mention the related issue number in pull requests using `#issue-number`.

## Small Changes

For very small fixes, pull requests may be opened directly without creating an issue first, though opening an issue is preferred.

## Coding Conventions

- Follow the existing Spring Boot structure.
- Keep controllers lightweight.
- Move business logic into service classes.
- Avoid unnecessary dependencies.
