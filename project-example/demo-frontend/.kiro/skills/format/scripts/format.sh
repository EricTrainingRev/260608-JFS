#!/bin/bash
# Run Prettier formatting via the npm script
cd "$(dirname "$0")/.." || exit 1
npm run format
