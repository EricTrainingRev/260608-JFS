# Code Smells Checklist

Look for the following common code smells:

- **Long methods/functions** — functions doing too much or exceeding ~30 lines
- **Large classes/components** — classes with too many responsibilities
- **Duplicate code** — repeated logic that should be extracted
- **Dead code** — unused variables, imports, functions, or unreachable branches
- **Magic numbers/strings** — literal values without named constants
- **Deep nesting** — excessive if/for/switch nesting (more than 3 levels)
- **God objects** — a single class/service that knows or does too much
- **Feature envy** — a function that uses another class's data more than its own
- **Primitive obsession** — overuse of primitives instead of small domain objects
- **Long parameter lists** — functions taking more than 3-4 parameters
- **Shotgun surgery** — a single change requiring edits in many unrelated places
- **Inappropriate intimacy** — classes that are too tightly coupled
- **Speculative generality** — abstractions or parameters added "just in case"
- **Refused bequest** — subclass ignoring most of what it inherits
- **Comments explaining bad code** — comments that exist only because the code is unclear
