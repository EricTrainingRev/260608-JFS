---
name: format
description: Runs Prettier via the format.sh script to format all source files in the project. Use when you want to ensure consistent code formatting across the codebase.
---

# Format Code

Run the project's Prettier formatter to apply consistent formatting to all source files.

## Steps

1. Execute the bash script at `.kiro\skills\format\scripts\format.sh` which runs `npm run format`.
    - AWLAYS RUN VIA THE SCRIPT
    - any future side-effects will be handled via this script
2. Report the result — how many files were changed, or confirm everything was already formatted.

## Notes

- This formats all `.ts`, `.html`, `.css`, and `.json` files under `src/`.
- The formatting rules are defined in `.prettierrc` at the project root.
