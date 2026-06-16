# TypeScript Quick Reference Guide

## Introduction

### What is TypeScript?
TypeScript is a **strongly typed superset** of JavaScript. It builds upon JavaScript by adding an optional type system that exists during development but is stripped away during compilation.

Think of it as **"JavaScript with Guardrails."** It doesn't change how your code runs in the browser; it changes how you write and catch errors in your editor.

**The Core Value Loop:**
1.  **Write:** Use types to define exactly what your data looks like.
2.  **Catch:** The compiler identifies logic errors (e.g., passing a string to a math function) *before* you even run the code.
3.  **Refactor:** Use the type system to safely rename variables or restructure objects across large codebases with confidence.

---

### JavaScript vs. TypeScript

The fundamental difference lies in **when** errors are discovered.

| Feature | JavaScript (Dynamic) | TypeScript (Static) |
| :--- | :--- | :--- |
| **Typing** | **Dynamic:** Types are resolved at runtime. | **Static:** Types are checked at compile-time. |
| **Error Detection** | **Runtime:** Errors crash your app while it's running. | **Compile-time:** Errors are flagged in your IDE/Terminal. |
| **Autocompletion** | **Limited:** Editor "guesses" based on patterns. | **Superior:** Editor "knows" the exact shape of your data. |
| **Scalability** | Difficult to maintain in large, complex teams. | Built for scale; types act as living documentation. |

#### Comparison in Action

**The JavaScript Way (Silent Failures):**
In JS, errors often remain hidden until a user triggers a specific path in your app.
```javascript
function calculateTotal(price, tax) {
  return price + tax;
}

// Logic error: Passing a string accidentally
console.log(calculateTotal(100, "10")); // Result: "10010" (No error thrown, but math is broken)
```

**The TypeScript Way (Immediate Feedback):**
In TS, the developer is notified of the mistake instantly.
```typescript
function calculateTotal(price: number, tax: number): number {
  return price + tax;
}

// @ts-expect-error: Argument of type 'string' is not assignable to parameter of type 'number'
calculateTotal(100, "10"); 
```

---

## TypeScript Setup

## Phase 1: Setup & Configuration

### Installation
TypeScript can be installed globally or locally. For professional development, **local installation is the standard** to ensure version consistency across different environments and team members.

**Global (for quick testing):**
```bash
npm install -g typescript
```

**Local (for projects):**
```bash
npm install --save-dev typescript
```
*Note: When installed locally, run the compiler using `npx tsc`.*

### Initializing a Project
To turn a directory into a TypeScript project, you must generate a `tsconfig.json` file. This file acts as the "brain" of your project, instructing the compiler how to behave.

**Create config file:**
```bash
npx tsc --init
```

### The `tsconfig.json` Structure
The `tsconfig.json` file contains a `compilerOptions` object that dictates how your code is transformed and how strictly it is checked.

#### Essential Compiler Options

**1. Target (`target`)**
Specifies the version of JavaScript that the compiler will output. Choosing a modern target (like `ESNext` or `ES2020`) allows you to use newer features, while older targets (like `ES5`) ensure compatibility with legacy browsers.
```json
"target": "ES2020"
```

**2. Strict Mode (`strict`)**
The most important setting for code quality. Enabling `strict: true` turns on a suite of advanced type-checking behaviors that prevent common bugs (such as `null` or `undefined` errors). 
*   **Recommendation:** Always keep this enabled for new projects.
```json
"strict": true
```

**3. Module System (`module`)**
Defines how your files interact with one another (e.g., `CommonJS` for Node.js or `ESNext` for modern browser-based bundlers like Vite).
```json
"module": "ESNext"
```

**4. Directory Management (`outDir` & `rootDir`)**
To keep your project organized, use `rootDir` to define where your `.ts` source files live and `outDir` to define where the compiled `.js` files should be placed.
```json
"rootDir": "./src",
"outDir": "./dist"
```

### Complete Example Configuration
A standard, robust configuration for a modern TypeScript project often looks like this:

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "module": "ESNext",
    "moduleResolution": "node",
    "rootDir": "./src",
    "outDir": "./dist",
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true
  }
}
```

---

## Types

### Foundational Data Primitives
In TypeScript, every piece of data has a "type" that defines what kind of value it holds. This is the most fundamental part of the type system; it tells the compiler whether a piece of data is a piece of text, a number, or a logical true/false value.

By explicitly defining these types, you prevent "Type Mismatch" errors—the most common source of bugs in JavaScript.

#### 1. The `string` Type
The `string` type represents textual data. Strings are used for anything that consists of characters (letters, numbers, symbols, or whitespace).

* **Usage:** Names, descriptions, addresses, or any text-based input.
* **Syntax:** Strings are wrapped in single quotes (`'`), double quotes (`"`), or backticks (`` ` ``) for template literals.

```typescript
let username: string = "Alice";
let greeting: string = 'Hello, world!';
let template: string = `Welcome, ${username}`; // Template literal
```

#### 2. The `number` Type
Unlike many other languages that distinguish between integers (whole numbers) and floats (decimals), TypeScript treats all numeric values as the `number` type.

* **Usage:** Ages, prices, counts, coordinates, or any mathematical value.
* **Note:** This includes integers, floating-point numbers, and even special numeric values like `Infinity` or `NaN` (Not a Number).

```typescript
let age: number = 30;
let price: number = 19.99;
let temperature: number = -5;
```

#### 3. The `boolean` Type
The `boolean` type is the simplest form of logic. It represents a binary state: either `true` or `false`.

* **Usage:** Flags, toggles, or the results of comparisons (e.g., `isLoggedIn`, `hasPermission`).

```typescript
let isActive: boolean = true;
let isSubscribed: boolean = false;
let isAdult: boolean = age >= 18; // Result is a boolean
```

---

### Summary of Primitives

| Type | Represents | Real-World Example |
| :--- | :--- | :--- |
| `string` | Textual data | A user's email address |
| `number` | Numeric values | The total price in a shopping cart |
| `boolean` | Logical state | Whether a user is currently logged in |

---

### Type Inference
While we have been explicitly writing types (e.g., `let age: number = 30`), TypeScript is smart enough to "guess" the type based on the value you assign to it. This is called **Type Inference**.

```typescript
let city = "New York"; // TypeScript automatically knows 'city' is a string.
// city = 123;         // Error: Type 'number' is not assignable to type 'string'.
```

**When to be explicit vs. when to infer:**
* **Use Explicit Types:** When declaring variables without immediate assignment, in function parameters, or when you want to ensure a variable stays a specific type.
* **Use Inference:** For simple, local variable declarations where the type is obvious. This keeps your code clean and readable.

---

## Functions

### The Logic Engines
If variables are the "data" of your application, functions are the "machinery" that processes that data. In TypeScript, functions are strictly typed to ensure that the "input" matches what the engine expects and the "output" is exactly what the rest of your code anticipates.

A fully typed function consists of three parts:
1.  **Parameters:** The inputs the function receives.
2.  **Return Type:** The type of value the function sends back.
3.  **Function Signature:** The overall shape of the function.

#### 1. Parameter Typing (The Input)
Every parameter must have an explicit type. This prevents the function from attempting to perform operations on incompatible data (e.g., trying to multiply a string).

```typescript
// Without types, this is dangerous. 
// With types, we define a strict "contract" for the input.
function add(a: number, b: number) {
    return a + b;
}
```

#### 2. Return Type Annotation (The Output)
The return type tells the rest of your program what to expect after the function finishes. This is vital for "chaining" functions together. If a function doesn't return anything, we use the special `void` type.

```typescript
// Explicitly stating this function MUST return a number
function multiply(a: number, b: number): number {
    return a * b;
}

// Explicitly stating this function returns nothing (it only performs an action)
function logMessage(message: string): void {
    console.log(message);
}
```

#### 3. Arrow Functions
In modern development, arrow functions are frequently used. The typing rules remain the same, but the syntax is more compact.

```typescript
const greet = (name: string): string => {
    return `Hello, ${name}!`;
};

// Concise version (implicit return)
const square = (n: number): number => n * n;
```

### Function Patterns & Edge Cases

#### Optional Parameters
Sometimes, a function doesn't strictly require every piece of information. We can mark a parameter as **optional** using the `?` symbol. 
*Note: Optional parameters must always come **after** required parameters.*

```typescript
function buildUser(name: string, age?: number) {
    console.log(`User: ${name}`);
    if (age) {
        console.log(`Age: ${age}`);
    }
}

buildUser("Alice");      // Valid
buildUser("Bob", 25);    // Valid
```

#### Void vs. Never
It is important to distinguish between functions that "do nothing" and functions that "never finish."

*   **`void`:** The function finishes its execution but does not return a value (e.g., a logger).
*   **`never`:** The function **never** reaches a return statement because it either throws an error or enters an infinite loop.

```typescript
function notifyUser(): void {
    console.log("Notification sent.");
}

function throwError(message: string): never {
    throw new Error(message);
}
```

### Summary of Function Components

| Component | Syntax | Purpose |
| :--- | :--- | :--- |
| **Parameter** | `(name: type)` | Defines the required input "contract." |
| **Optional Parameter** | `(name?: type)` | Defines an input that can be omitted. |
| **Return Type** | `): type` | Defines what the engine produces as output. |
| **Void** | `: void` | Indicates the function performs an action but returns no data. |
| **Never** | `: never` | Indicates the function will crash or never exit. |

---

## Objects

### Object Types: Defining Data Shapes
In TypeScript, an object is more than just a collection of values; it is a structured entity with a predictable "shape." An **Object Type** defines exactly which properties an object must have, what types those properties must be, and whether they are required or optional.

Defining object shapes allows the compiler to catch errors when you try to access a property that doesn't exist or assign the wrong kind of data to a specific field.

#### 1. Literal Object Types
The simplest way to define an object is to declare its shape inline. This is useful for one-off objects or local data.

```typescript
// Defining the shape of a 'user' object
let user: { name: string; age: number; isAdmin: boolean };

user = {
    name: "Alice",
    age: 30,
    isAdmin: true
};

// Error: Property 'email' does not exist on type '{ name: string; age: number; isAdmin: boolean }'
// user.email = "alice@example.com"; 
```

#### 2. Optional Properties
Not every object will have every piece of data. We can mark specific properties as optional using the `?` symbol. This tells TypeScript, "This property might be there, or it might be `undefined`."

```typescript
type UserProfile = {
    username: string;
    bio?: string;      // This property is optional
    website?: string;  // This property is optional
};

const userA: UserProfile = { username: "coder_123" }; // Valid
const userB: UserProfile = { username: "dev_pro", bio: "Building the web." }; // Also valid
```

### The Anatomy of an Object Type

To build a reliable object, you must consider three dimensions: **Identity** (the property name), **Type** (the data it holds), and **Requirement** (whether it must exist).

| Component | Syntax | Purpose |
| :--- | :--- | :--- |
| **Property Name** | `propertyName` | The unique key used to access the value. |
| **Property Type** | `: type` | The "contract" for what value the property holds. |
| **Optional Flag** | `?:` | Indicates the property can be omitted. |


### Object Access & Safety
When working with objects, especially those with **optional properties**, you must be careful when accessing them. Because an optional property might be `undefined`, accessing it directly can lead to runtime errors in JavaScript.

**The "Safe Access" Pattern:**
Use **Optional Chaining (`?.`)** to safely navigate through objects that might have missing data.

```typescript
type User = {
    id: number;
    profile?: {
        email: string;
    };
};

const user: User = { id: 1 };

// Dangerous: This will crash if 'profile' is undefined
// console.log(user.profile.email); 

// Safe: If 'profile' is undefined, this returns 'undefined' instead of crashing
console.log(user.profile?.email); 
```

---

## Arrays

### Ordered Collections: Arrays and Tuples
While primitives represent single values, we often need to group multiple values together. TypeScript provides two primary ways to handle ordered collections, depending on whether the collection is meant to be flexible or strictly structured.

### 1. Arrays (Homogenous Collections)
An **Array** is a list of elements where every element follows the same type "contract." Arrays are dynamic, meaning they can grow or shrink in size, but the type of data they contain remains consistent.

#### Syntax Options
There are two ways to write array types in TypeScript. They are functionally identical, but one is often preferred for readability.

*   **Square Bracket Notation (Most Common):** `type[]`
*   **Generic Notation:** `Array<type>`

```typescript
// Using Square Bracket Notation
let scores: number[] = [95, 82, 88, 100];

// Using Generic Notation
let names: Array<string> = ["Alice", "Bob", "Charlie"];

// Error: Type 'string' is not assignable to type 'number'
// scores.push("Excellent"); 
```

### 2. Tuples (Heterogeneous Collections)
A **Tuple** is a specialized, fixed-length array where each position has a specific, predefined type. Unlike a standard array, a tuple is used when the **order** and **position** of the elements are just as important as the data itself.

*   **Usage:** Representing coordinates `[x, y]`, key-value pairs `[string, number]`, or complex return values from a function.
*   **Constraint:** Once a tuple is defined, you cannot add elements to it without violating its fixed-length structure.

```typescript
// A tuple representing a HTTP Response: [Status Code, Message]
let response: [number, string] = [200, "Success"];

// A tuple representing a 2D Coordinate
let point: [number, number] = [10, -5];

// Error: Type 'string' is not assignable to type 'number' at position 1
// point = [10, "top"]; 

// Error: Property 'length' is incompatible (Tuples have a fixed length)
// response.push(500); 
```

### Array vs. Tuple

The choice between an array and a tuple depends on the **intent** of your data structure.

| Feature | Array | Tuple |
| :--- | :--- | :--- |
| **Data Type** | **Homogenous:** All elements must be the same type. | **Heterogeneous:** Each position can have a different type. |
| **Size** | **Dynamic:** Can grow or shrink indefinitely. | **Fixed:** Designed to have a specific, predefined length. |
| **Primary Goal** | To store a list of similar items. | To store a structured "record" of related data. |
| **Example** | `string[]` (A list of names) | `[string, number]` (A name and an age) |

### Read-Only Tuples
Because tuples are often used to represent fixed structures (like a coordinate or a configuration pair), you may want to prevent them from being modified at all. You can use the `readonly` modifier to ensure the collection remains immutable.

```typescript
// This tuple cannot be modified after it is created
const point: readonly [number, number] = [10, 20];

// Error: Cannot assign to 'point' because it is a read-only tuple
// point[0] = 15; 
```

---

## Types & Interfaces

### Defining Custom Types: Type Aliases & Interfaces
In professional development, you rarely use inline object definitions. Instead, you create reusable "blueprints." TypeScript provides two primary mechanisms for this: **Type Aliases** and **Interfaces**.

#### 1. Type Aliases
A Type Alias allows you to create a new name for *any* type definition. This includes primitives, union types, tuples, and object shapes. It is the most versatile way to define a type.

* **Best Use Case:** When you need to define non-object types (like Unions) or when you want a simple, lightweight name for a shape.
* **Syntax:** `type Name = Type;`

```typescript
// 1. Aliasing a Union (The most common use for 'type')
type Status = "success" | "error" | "loading";
let currentStatus: Status = "success";

// 2. Aliasing a Primitive
type Email = string;
let userEmail: Email = "test@example.com";

// 3. Aliasing an Object
type User = {
    id: number;
    username: string;
};
```

#### 2. Interfaces
An Interface is a dedicated tool specifically for defining the structure of **Objects** and **Classes**. While they look similar to Type Aliases, they have unique capabilities, most notably **Declaration Merging**.

* **Best Use Case:** When defining the public API of an object, or when building complex object hierarchies (especially in Object-Oriented Programming).
* **Syntax:** `interface Name { ... }`

```typescript
interface Person {
    name: string;
    age: number;
}

const employee: Person = {
    name: "Jane",
    age: 28
};
```

---

### Key Differences: Type vs. Interface

Choosing between them is a matter of capability and intent.

| Feature | Type Alias | Interface |
| :--- | :--- | :--- |
| **Primary Purpose** | General-purpose name for *any* type. | Specifically for defining *object shapes*. |
| **Union Types** | **Yes.** Can define `string \| number`. | **No.** Cannot directly define a union. |
| **Extending** | Via Intersections (`&`). | Via `extends` keyword. |
| **Declaration Merging** | **No.** Cannot define the same type twice. | **Yes.** Multiple interfaces with the same name merge. |

#### 1. Extending (Inheritance)
Both can be extended to create more specific versions of a type.

**Extending with Interface (`extends`):**
```typescript
interface Animal {
    species: string;
}

interface Dog extends Animal {
    breed: string;
}
```

**Extending with Type (`&` Intersection):**
```typescript
type Animal = {
    species: string;
};

type Dog = Animal & {
    breed: string;
};
```

#### 2. Declaration Merging (The Interface Superpower)
If you declare an `interface` with the same name multiple times, TypeScript automatically merges them into one. **Type aliases cannot do this** and will throw an error if a duplicate name is used. This is critical when working with third-party libraries where you want to add properties to an existing global object.

```typescript
interface Window {
    title: string;
}

interface Window {
    isLoaded: boolean;
}

// The resulting 'Window' type now has both properties
const myWindow: Window = {
    title: "Home",
    isLoaded: true
};
```

### Summary Recommendation

*   **Use `interface`** for defining the "shape" of objects and classes, especially if you expect them to be extended or if you are building a library.
*   **Use `type`** for everything else: Unions, Primitives, Tuples, and complex logic that an Interface cannot handle.

---

## Unions

### Union Types
A Union Type allows a value to be one of several different types. It is represented by the pipe (`|`) symbol. This is one of TypeScript's most powerful features, allowing you to model real-world scenarios where a value might change its form or represent different states.

#### 1. Basic Unions
You can combine primitives or objects to allow a variable to hold multiple types of data.

```typescript
// A variable that can be either a string or a number
let identifier: string | number;

identifier = "ID_123"; // Valid
identifier = 456;      // Valid
// identifier = true;  // Error: Type 'boolean' is not assignable to type 'string | number'
```

#### 2. Literal Unions (String/Numeric Literals)
A common pattern is to use a union of specific string or number values. This acts as a much stricter version of an `enum`, allowing you to restrict a variable to a specific set of allowed values.

```typescript
type Direction = "North" | "South" | "East" | "West";

let move: Direction = "North";
// move = "Up"; // Error: Type '"Up"' is not assignable to type 'Direction'
```

### Type Narrowing: The "Safety" Mechanism
The biggest challenge with Union Types is that **TypeScript only allows you to perform operations that are valid for ALL types in the union.**

If you have `string | number`, you cannot call `.toUpperCase()` on it immediately, because a `number` doesn't have that method. To fix this, you must perform **Type Narrowing**—the process of proving to the compiler which specific type the variable is at a given moment.

#### 1. Narrowing with `typeof`
For primitive unions, the `typeof` operator is the most common way to narrow the type.

```typescript
function processInput(input: string | number) {
    // At this point, input could be anything.
    // input.toUpperCase(); // Error: Property 'toUpperCase' does not exist on type 'number'

    if (typeof input === "string") {
        // Inside this block, TypeScript knows 'input' is definitely a string.
        console.log(input.toUpperCase()); // Safe
    } else {
        // Inside this block, TypeScript knows 'input' must be a number.
        console.log(input.toFixed(2));    // Safe
    }
}
```

#### 2. Narrowing with `in` (Object Unions)
When dealing with a union of different object shapes, use the `in` operator to check if a specific property exists on the object.

```typescript
interface Bird {
    fly: () => void;
}

interface Fish {
    swim: () => void;
}

function moveAnimal(animal: Bird | Fish) {
    if ("fly" in animal) {
        // TypeScript narrows 'animal' to the Bird interface
        animal.fly();
    } else {
        // TypeScript narrows 'animal' to the Fish interface
        animal.swim();
    }
}
```

### Summary of Union Logic

| Concept | Syntax | Purpose |
| :--- | :--- | :--- |
| **Union** | `TypeA \| TypeB` | Allows a variable to be one of several types. |
| **Literal Union** | `"A" \| "B"` | Restricts a variable to specific, allowed values. |
| **Type Narrowing** | `if (typeof ...)` | Proves a specific type to the compiler so you can access its properties. |

### Discriminated Unions
The most robust way to handle complex unions is through **Discriminated Unions**. This involves adding a common "tag" property (often called `kind`, `type`, or `status`) to every object in the union. This makes narrowing extremely predictable and easy for the compiler to follow.

```typescript
interface Success {
    status: "success"; // The "Discriminant"
    data: string;
}

interface Failure {
    status: "error";   // The "Discriminant"
    error: Error;
}

type ApiResponse = Success | Failure;

function handleResponse(response: ApiResponse) {
    // Using the 'status' property to narrow the type
    switch (response.status) {
        case "success":
            console.log(response.data); // TypeScript knows this is Success
            break;
        case "error":
            console.error(response.error.message); // TypeScript knows this is Failure
            break;
    }
}
```

---

## Enums
An `enum` (short for enumeration) is a way to define a set of named constants. Enums allow you to create a collection of related values that are easier to read and maintain than raw strings or numbers.

While TypeScript allows for both numeric and string enums, they behave differently under the hood.

#### 1. Numeric Enums
By default, enums are number-based. If you don't assign values, they auto-increment starting from `0`.

```typescript
enum Direction {
    Up,    // 0
    Down,  // 1
    Left,  // 2
    Right  // 3
}

let move: Direction = Direction.Up;
console.log(move); // Output: 0
```

You can also manually assign starting values, and the subsequent members will increment from that value.

```typescript
enum StatusCode {
    Success = 200,
    NotFound = 404,
    Error = 500
}
```

#### 2. String Enums
String enums are often preferred in modern development because they provide better readability during debugging. When you log a string enum, you see the actual string value rather than an arbitrary number.

```typescript
enum OrderStatus {
    Pending = "PENDING",
    Shipped = "SHIPPED",
    Delivered = "DELIVERED"
}

let currentStatus: OrderStatus = OrderStatus.Shipped;
console.log(currentStatus); // Output: "SHIPPED"
```

### Enums vs. Literal Unions
A common architectural question in TypeScript is whether to use an `enum` or a **Literal Union** (e.g., `type Status = "open" | "closed"`).

| Feature | Enum | Literal Union |
| :--- | :--- | :--- |
| **Runtime Presence** | Exists as a real object in the compiled JavaScript. | Disappears; it is purely a compile-time check. |
| **Usage** | Use as a value: `Status.Open`. | Use as a literal: `"open"`. |
| **Complexity** | Higher (adds more code to the final bundle). | Lower (zero runtime overhead). |
| **Intent** | Best for complex, related constants. | Best for simple, lightweight value restrictions. |

### Summary of Enum Behavior

| Type | Assignment | Benefit |
| :--- | :--- | :--- |
| **Numeric** | Automatic or Manual | Compact; useful for bitwise flags or simple counters. |
| **String** | Explicitly assigned | Highly readable; provides clear context during debugging. |

### Implementation Note: The "Constant Enums" Alternative
Because standard enums generate extra JavaScript code (an object), some developers prefer using `const enum`. A `const enum` is completely removed during compilation, and its values are inlined wherever they are used. This provides the same developer experience without the runtime overhead.

```typescript
const enum Role {
    Admin,
    User
}

let myRole: Role = Role.Admin;
// The compiled JS will simply be: let myRole = 0;
```

---

## Classes
Classes in TypeScript are blueprints for creating objects. While they follow the same structure as JavaScript classes, TypeScript adds a powerful layer of type safety by allowing you to strictly define the types of class properties, constructor parameters, and method return values.

#### 1. Basic Class Structure
A class typically consists of properties (data), a constructor (initialization logic), and methods (behavior).

```typescript
class Animal {
    // Properties must be explicitly typed
    name: string;

    constructor(name: string) {
        this.name = name;
    }

    // Methods must specify their return type
    makeSound(): void {
        console.log(`${this.name} makes a noise.`);
    }
}

const dog = new Animal("Rex");
dog.makeSound();
```

#### 2. Access Modifiers
Access modifiers control the visibility of class members, determining which parts of your code can interact with specific properties or methods. This is the foundation of **Encapsulation**.

*   **`public` (Default):** The property/method can be accessed from anywhere.
*   **`private`:** The property/method can only be accessed within the class itself.
*   **`protected`:** The property/method can be accessed within the class and by its subclasses (derived classes).

```typescript
class BankAccount {
    public accountHolder: string;
    private balance: number;
    protected accountType: string;

    constructor(holder: string, initialBalance: number) {
        this.accountHolder = holder;
        this.balance = initialBalance;
        this.accountType = "Savings";
    }

    public deposit(amount: number): void {
        this.balance += amount;
    }

    // This method can be used by subclasses, but not externally
    protected getAccountType(): string {
        return this.accountType;
    }
}

const myAccount = new BankAccount("Alice", 1000);
myAccount.deposit(500);       // OK
console.log(myAccount.accountHolder); // OK
// console.log(myAccount.balance);    // Error: Property 'balance' is private
```

#### 3. Parameter Properties (Shorthand Syntax)
TypeScript offers a highly efficient shorthand to reduce boilerplate. By adding an access modifier directly to a constructor parameter, TypeScript automatically creates the property and assigns the value for you.

```typescript
// Traditional Way
class User {
    username: string;
    constructor(username: string) {
        this.username = username;
    }
}

// Shorthand Way (Produces identical results)
class UserShorthand {
    constructor(public username: string) {}
}
```

#### 4. Inheritance and `super`
Classes can inherit properties and methods from other classes using the `extends` keyword. When a subclass has its own constructor, it **must** call `super()` to execute the constructor of the parent class.

```typescript
class Employee extends Animal {
    salary: number;

    constructor(name: string, salary: number) {
        super(name); // Calls the Animal constructor
        this.salary = salary;
    }

    // Overriding a method
    makeSound(): void {
        console.log(`${this.name} is working.`);
    }
}
```

---

### Summary of Access Modifiers

| Modifier | Accessible in Class? | Accessible in Subclass? | Accessible Externally? |
| :--- | :--- | :--- | :--- |
| `public` | Yes | Yes | Yes |
| `protected` | Yes | Yes | No |
| `private` | Yes | No | No |

---

### Implementation Note: Interface vs. Class
It is important to distinguish between an **Interface** and a **Class**:
*   An **Interface** is a virtual contract used only for type-checking during development; it disappears entirely at runtime.
*   A **Class** is a real blueprint that generates actual JavaScript code (functions and objects) that exists when your app is running.

---

## Casting

### Type Assertion (Casting)
In TypeScript, "casting" is technically referred to as **Type Assertion**. It is a way to tell the compiler: *"I know more about the type of this variable than you do; treat it as this specific type."*

Assertions do not perform any actual data conversion (like converting a string to a number). Instead, they are purely a way to override the compiler's type inference when you are certain of the data's shape.

#### 1. The `as` Syntax
The most common and recommended way to perform an assertion is using the `as` keyword.

```typescript
// Scenario: Working with an unknown value from an API
let someValue: unknown = "This is a string";

// The compiler doesn't know 'someValue' has a .length property.
// We assert that it is a string so we can access string-specific properties.
let strLength = (someValue as string).length;

console.log(strLength); // Output: 16
```

#### 2. Angle-Bracket Syntax
TypeScript also supports an alternative syntax using angle brackets. However, this is **not compatible with JSX/React** (as it conflicts with HTML tag syntax), so the `as` syntax is generally preferred for modern web development.

```typescript
let someValue: unknown = "This is a string";

// Angle-bracket syntax
let strLength = (<string>someValue).length;
```

### When to Use (and When to Avoid) Assertion

Type assertion is a powerful tool, but it bypasses the very safety features that make TypeScript valuable. It should be used sparingly and with caution.

#### When to Use
*   **DOM Manipulation:** When you select an element from the DOM, TypeScript often treats it as a generic `HTMLElement`. If you know it is specifically an `HTMLInputElement`, you must assert it to access properties like `.value`.
    ```typescript
    const input = document.getElementById("user-email") as HTMLInputElement;
    console.log(input.value); 
    ```
*   **External Data:** When receiving data from a third-party library or an API where the type is typed as `any` or `unknown`, but you are confident in its structure.

#### When to Avoid
*   **Forcing invalid types:** Never use assertion to "lie" to the compiler. If you assert that a `number` is a `string`, you aren't fixing the error; you are simply hiding it until the application crashes at runtime.
*   **As a substitute for proper typing:** If you find yourself using `as` constantly, it is a sign that your initial type definitions (Interfaces or Types) are likely incorrect or incomplete.

### Summary of Assertion Mechanics

| Feature | Detail |
| :--- | :--- |
| **Technical Name** | Type Assertion |
| **Primary Syntax** | `value as Type` |
| **Runtime Impact** | **Zero.** It is stripped away during compilation. |
| **Core Risk** | It bypasses compile-time safety checks. |

### Implementation Note: Assertion vs. Conversion
It is critical to distinguish between **Assertion** and **Conversion**:

*   **Assertion (Casting):** Telling the compiler "Treat this as X." (e.g., `val as string`). **Does not change the data.**
*   **Conversion:** Actually changing the data from one type to another. (e.g., `Number("123")` or `String(123)`). **Changes the data.**

---

## Generics

### The Concept of Generics
Generics are a tool for creating **reusable, type-safe components**. In standard programming, you often write functions or classes that work with many different types of data. Without generics, you are often forced to use the `any` type, which destroys your type safety.

Generics allow you to capture the type provided by the user and "pass it through" to the rest of your code. Instead of saying "this function takes any type," you say, "this function takes a type `T`, and whatever `T` is, it will return that same type `T`."

#### 1. Generic Functions
The most common use of generics is in functions that process data and return it, or move it from one place to another.

```typescript
// The <T> is a Type Variable. It acts as a placeholder.
function identity<T>(value: T): T {
    return value;
}

// Usage:
let num = identity<number>(42);      // T is captured as number
let str = identity<string>("Hello"); // T is captured as string

// TypeScript can also infer the type automatically:
let autoStr = identity("World");     // T is inferred as string
```

#### 2. Generic Interfaces and Classes
Generics are not limited to functions; they can be used to define the shape of objects or the structure of classes, making them highly flexible.

**Generic Interfaces:**
```typescript
interface Box<T> {
    content: T;
}

const stringBox: Box<string> = { content: "A secret message" };
const numberBox: Box<number> = { content: 100 };
```

**Generic Classes:**
```typescript
class StorageContainer<T> {
    private contents: T[] = [];

    add(item: T): void {
        this.contents.push(item);
    }

    get(index: number): T {
        return this.contents[index];
    }
}

const nameStorage = new StorageContainer<string>();
nameStorage.add("Alice");
// nameStorage.add(123); // Error: Argument of type 'number' is not assignable to 'string'
```

---

### Generic Constraints
Sometimes, a generic type is *too* flexible. If you want to use a generic type, but you need it to have certain properties (like a `.length` property), you must use a **Constraint**.

You apply a constraint using the `extends` keyword. This tells TypeScript: "You can use any type `T`, as long as it satisfies this specific requirement."

```typescript
interface HasLength {
    length: number;
}

// T must satisfy the HasLength interface
function logLength<T extends HasLength>(item: T): void {
    console.log(`Length is: ${item.length}`);
}

logLength("Hello");        // OK: strings have a .length property
logLength([1, 2, 3]);      // OK: arrays have a .length property
// logLength(123);         // Error: number does not have a .length property
```

---

### Summary of Generics

| Concept | Syntax | Purpose |
| :--- | :--- | :--- |
| **Type Variable** | `<T>` | A placeholder for a type that will be defined at runtime. |
| **Inference** | (Automatic) | TypeScript's ability to guess `T` based on the argument passed. |
| **Constraint** | `<T extends Type>` | Restricting the generic type to only those that match a specific shape. |

---

### Implementation Note: Naming Conventions
While you can use any name for a generic placeholder (like `<Value>` or `<Item>`), the industry standard is to use single, uppercase letters. Common conventions include:
*   `T`: Type (the most common default)
*   `K`: Key (used when referring to object keys)
*   `V`: Value (used when referring to object values)
*   `E`: Element (used when referring to array or list elements)

---

## Special Types

### Advanced Type Logic: any, unknown, void, and never
While the foundational primitives (string, number, boolean) cover most basic needs, TypeScript provides "Special Types" to handle edge cases, complex function logic, and scenarios where type safety needs to be intentionally bypassed or strictly enforced.

#### 1. The `any` Type (The Escape Hatch)
The `any` type effectively turns off the TypeScript type checker for a specific variable. It tells the compiler to allow any operation on the value, no matter what it is.

* **Usage:** Primarily used during migration from JavaScript to TypeScript or when dealing with highly unpredictable data.
* **Risk:** Overusing `any` defeats the purpose of using TypeScript. It can hide bugs and lead to runtime crashes that the compiler should have caught.

```typescript
let data: any = 42;
data = "Now I am a string";
data.toUpperCase(); // No error from compiler, but could crash if type changes
```

#### 2. The `unknown` Type (The Safer Alternative)
`unknown` is the type-safe counterpart to `any`. Like `any`, a variable of type `unknown` can hold any value. However, unlike `any`, TypeScript **will not allow you to perform any operations on it** until you first prove what the type is through type narrowing.

* **Usage:** When you receive data from an external source (like an API) and you want to force yourself to validate it before using it.

```typescript
let value: unknown = "Hello World";

// value.toUpperCase(); // ❌ Error: Object is of type 'unknown'

if (typeof value === "string") {
    console.log(value.toUpperCase()); // ✅ Safe: Type narrowed to string
}
```

#### 3. The `void` Type (The Absence of Return)
The `void` type is used almost exclusively as a return type for functions. It indicates that a function performs an action (a side effect) but does not return a value to the caller.

* **Usage:** Functions that log to a console, modify a global variable, or write to a database.

```typescript
function logStatus(message: string): void {
    console.log(`Status: ${message}`);
    // No return statement here
}
```

#### 4. The `never` Type (The Impossible State)
The `never` type represents values that **should never occur**. It is used for functions that never reach a completion point.

* **Usage:** Functions that always throw an error or functions that contain infinite loops.

```typescript
function throwError(message: string): never {
    throw new Error(message);
}

function infiniteLoop(): never {
    while (true) {
        // This function never returns
    }
}
```

### Summary of Special Types

| Type | Meaning | Main Use Case |
| :--- | :--- | :--- |
| `any` | "I don't care about type safety." | Rapid prototyping or legacy JS migration. |
| `unknown` | "I don't know the type yet." | Handling unpredictable external data safely. |
| `void` | "This function returns nothing." | Describing side-effect functions. |
| `never` | "This code path is impossible/unreachable." | Describing functions that throw or loop forever. |

### Comparison: `any` vs. `unknown`

The distinction between these two is a critical part of writing professional TypeScript:

| Feature | `any` | `unknown` |
| :--- | :--- | :--- |
| **Assignment** | Can be assigned anything. | Can be assigned anything. |
| **Operations** | Can call any method/property on it. | Cannot call any method/property on it. |
| **Type Safety** | **Low:** Bypasses the compiler. | **High:** Forces type checking/narrowing. |

---

## Utility Types

### Transforming Existing Types
Utility types are built-in tools in TypeScript that allow you to derive new types from existing ones. Instead of manually redefining an object or interface every time you need a slight variation, you can use these "transformation" types to modify properties, pick subsets, or change their accessibility.

They are highly efficient for maintaining a **Single Source of Truth**: you define your core interface once and use utility types to create the variations needed for different parts of your application.

#### 1. `Partial<T>` (Making properties optional)
`Partial` takes an existing type and makes every single one of its properties optional. This is incredibly useful for "Update" operations where a user might only change one or two fields of a larger object.

```typescript
interface User {
    id: number;
    name: string;
    email: string;
}

// Partial<User> is equivalent to: { id?: number; name?: string; email?: string; }
function updateUser(id: number, updates: Partial<User>) {
    // Logic to merge updates into the existing user
}

updateUser(1, { name: "Alice" }); // Valid: only providing name
```

#### 2. `Readonly<T>` (Making properties immutable)
`Readonly` creates a version of a type where all properties are set to read-only. Once an object of this type is created, its properties cannot be reassigned.

```typescript
interface Config {
    apiKey: string;
    endpoint: string;
}

const myConfig: Readonly<Config> = {
    apiKey: "ABC-123",
    endpoint: "https://api.example.com"
};

// myConfig.apiKey = "NEW-KEY"; // ❌ Error: Cannot assign to 'apiKey' because it is a read-only property.
```

#### 3. `Pick<T, K>` (Selecting a subset)
`Pick` allows you to create a new type by selecting only a specific set of keys (`K`) from an existing type (`T`). This is perfect for when you want to expose only a small, safe portion of a large object.

```typescript
interface Product {
    id: number;
    name: string;
    price: number;
    description: string;
    stockCount: number;
}

// We only want to show the name and price in the UI list
type ProductSummary = Pick<Product, "name" | "price">;

const summary: ProductSummary = {
    name: "Laptop",
    price: 999
};
```

#### 4. `Omit<T, K>` (Removing a subset)
`Omit` is the opposite of `Pick`. It creates a new type by taking an existing type and **removing** the specified keys (`K`).

```typescript
interface User {
    id: number;
    username: string;
    passwordHash: string; // Sensitive data
}

// We want a version of User that does NOT include the password
type UserPublicProfile = Omit<User, "passwordHash">;

const publicProfile: UserPublicProfile = {
    id: 1,
    username: "jdoe"
};
```

#### 5. `Record<K, T>` (Defining key-value maps)
`Record` is used to define the structure of an object where you know the types of the keys and the types of the values. It is the standard way to define "dictionaries" or "maps."

```typescript
// A map where keys are strings (usernames) and values are numbers (IDs)
const userMap: Record<string, number> = {
    "alice": 1,
    "bob": 2,
    "charlie": 3
};
```

### Summary of Utility Types

| Utility | Action | Common Use Case |
| :--- | :--- | :--- |
| `Partial<T>` | Makes all properties `?` (optional). | Update/Patch forms. |
| `Readonly<T>` | Makes all properties immutable. | Configuration/Constant objects. |
| `Pick<T, K>` | Keeps only the keys specified in `K`. | Displaying specific data in UI. |
| `Omit<T, K>` | Removes the keys specified in `K`. | Stripping sensitive data from objects. |
| `Record<K, T>` | Defines a map of keys `K` to values `T`. | Creating dictionaries/lookup tables. |

### Implementation Note: Composition
Utility types can be **nested** to create highly specific types. This allows you to build complex data requirements from simple building blocks.

```typescript
interface Task {
    id: string;
    title: string;
    completed: boolean;
}

// A type for a form that updates a task, but the ID cannot be changed
type TaskUpdateForm = Pick<Task, "id"> & Partial<Omit<Task, "id">>;

const update: TaskUpdateForm = {
    id: "task-001",
    title: "New Title" // 'completed' is optional because of Partial
};
```

---

## Keyof

### The Type Query Operator
The `keyof` operator is used to extract the keys of an object type into a **Union of string literals**. Instead of manually typing out a list of property names, `keyof` allows you to programmatically grab them from an existing interface or type.

This is a cornerstone of "Type-Safe Programming"—it ensures that if you change a property name in your interface, all your code that relies on those keys will automatically update or throw an error.

#### 1. Basic Usage
When applied to an interface or type, `keyof` returns a union of the property names as strings.

```typescript
interface Car {
    make: string;
    model: string;
    year: number;
}

// CarKeys is now the union: "make" | "model" | "year"
type CarKeys = keyof Car;

let myKey: CarKeys;
myKey = "make";  // OK
myKey = "year";  // OK
// myKey = "color"; // Error: Type '"color"' is not assignable to type 'CarKeys'
```

#### 2. Real-World Application: Type-Safe Getters
`keyof` is most powerful when used to create functions that interact with objects. It allows you to ensure a function only accepts valid property names of a specific object.

```typescript
function getProperty<T, K extends keyof T>(obj: T, key: K) {
    return obj[key];
}

const user = { id: 1, name: "Alice", email: "alice@example.com" };

// OK: 'name' is a valid key of user
const name = getProperty(user, "name"); 

// Error: 'age' is not a valid key of user
// const age = getProperty(user, "age"); 
```

---

### Summary of Keyof

| Feature | Detail |
| :--- | :--- |
| **Result** | A Union of string literals (e.g., `"a" \| "b" \| "c"`). |
| **Primary Use** | To create dynamic, type-safe relationships between objects and their properties. |
| **Relationship** | Acts as the foundation for many Utility Types like `Pick` and `Record`. |

---

## Decorators

### Advanced Metadata Annotations
Decorators are a special kind of declaration that can be attached to a class, method, accessor, property, or parameter. They use the `@expression` syntax and allow you to "wrap" existing code to add extra behavior, logging, or metadata without modifying the original logic.

**Note:** Decorators are currently a Stage 3 proposal in ECMAScript. In TypeScript, you must enable `"experimentalDecorators": true` in your `tsconfig.json` to use them.

#### 1. Class Decorators
A class decorator is applied to the constructor of the class. It can be used to observe, modify, or replace a class definition.

```typescript
function Frozen(constructor: Function) {
    Object.freeze(constructor);
    Object.freeze(constructor.prototype);
}

@Frozen
class IceCream {
    flavor: string = "Vanilla";
}

// The class is now immutable; you cannot add new properties to it at runtime.
```

#### 2. Method Decorators
Method decorators are used to intercept calls to a method. They are frequently used for logging, permission checking, or measuring performance.

```typescript
function Log(target: any, propertyKey: string, descriptor: PropertyDescriptor) {
    const originalMethod = descriptor.value;

    descriptor.value = function (...args: any[]) {
        console.log(`Calling ${propertyKey} with arguments:`, args);
        const result = originalMethod.apply(this, args);
        console.log(`Result of ${propertyKey}:`, result);
        return result;
    };
}

class Calculator {
    @Log
    add(a: number, b: number): number {
        return a + b;
    }
}

const calc = new Calculator();
calc.add(5, 10); 
// Console Output:
// Calling add with arguments: [5, 10]
// Result of add: 15
```

#### 3. Property Decorators
Property decorators are used to observe or modify a property's behavior. They are often used to attach metadata to a property for later use by other parts of the system (common in frameworks like NestJS or Angular).

```typescript
function ReadOnly(target: any, propertyKey: string) {
    Object.defineProperty(target, propertyKey, {
        writable: false
    });
}

class User {
    @ReadOnly
    id: number = 1;
}

const user = new User();
// user.id = 2; // Error: Cannot assign to 'id' because it is a read-only property.
```

---

### Summary of Decorator Usage

| Decorator Type | Target | Common Use Case |
| :--- | :--- | :--- |
| **Class** | The entire class | Modifying the class definition or freezing it. |
| **Method** | A function within a class | Logging, validation, or access control. |
| **Property** | A variable within a class | Attaching metadata or enforcing immutability. |
| **Parameter**| An argument in a function | Validating or transforming specific inputs. |

---

### Implementation Note: The New Standard (TS 5.0+)
With the release of TypeScript 5.0, support for the official ECMAScript Decorators standard was added. 

*   **Experimental Decorators:** Uses the older, non-standard syntax (common in older projects and Angular). Requires `experimentalDecorators: true`.
*   **Standard Decorators:** Uses the new official standard. These do not require the experimental flag and have a different internal API for how they handle arguments. 

When starting a new project today, it is important to check which decorator specification your framework (e.g., NestJS vs. Vanilla TS) expects.