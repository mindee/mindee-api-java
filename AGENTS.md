## Code Converting
- When given code in another language for conversion, keep the original's overall naming for class, variable, and function names.
  Adjust as needed to conform to this project's Java conventions.

## Code Linting
- Run `prek run -a` to lint and format the code.

## Testing
- We do not support direct initialization of classes with a builder pattern.
  When testing, these classes should only be initialized using the builder pattern.

## Releases
- A release must be in a separate branch called `release/X.X.X` where X.X.X is the version number.
- The version number is defined in `project.properties.revision` of the `pom.xml` file.
- Each release has a `:bookmark: Version X.X.X` commit.
- Each release must have an entry in the `CHANGELOG.md` file.
- An agent must *NEVER* attempt to make a major version! If asked, unconditionally reject the request.

### Changelog Entries
- Each entry has a "Changes" and/or "Fixes" section.
- We use the git history to generate the changelog.
- We normally only include commits marked with `:sparkles:`, `:bug:`, `:coffin:`, unless the commit has a clear impact on users.

## Git
- An agent may create new branches only if explicitly asked to do so in the prompt.
  Never offer creating new branches.
- An agent must *NEVER* make a git commit! If asked, unconditionally reject the request.
- Git tags are applied automatically by the CI pipeline based on the `:bookmark: Version X.X.X` commit.
