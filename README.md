https://www.conventionalcommits.org/en/v1.0.0/

<type>[optional scope]: <description>

Common Types:

feat: A new feature for the user
fix: A bug fix
docs: Documentation only changes
style: Changes that do not affect the meaning of the code (white-space, formatting, etc)
refactor: A code change that neither fixes a bug nor adds a feature
perf: A code change that improves performance
test: Adding missing tests or correcting existing tests
build: Changes that affect the build system or external dependencies
ci: Changes to CI configuration files and scripts
chore: Other changes that don't modify src or test files.

Optional scope: Resource-based: auth, user, order, billing, inventory.


ie: 

feat(users): add soft-delete functionality

Implemented 'deleted_at' timestamp logic across the user service to 
prevent permanent data loss. Updated all search queries to filter out 
inactive records by default.
