# JavaScript Quick Reference Guide

## Overview of JavaScript

JavaScript is the **behavioral layer** of the web. While HTML provides the structure and CSS provides the visual style, JavaScript provides the **logic and interactivity** that allows a webpage to respond to user input, fetch new data, and update the interface dynamically.

### The Web Ecosystem: A Revisit
To understand JavaScript, it must be understood through the lens of the "Separation of Concerns":

1.  **HTML (Structure):** The semantic blueprint (e.g., "This is a button").
2.  **CSS (Presentation):** The visual design (e.g., "The button is blue and rounded").
3.  **JavaScript (Behavior):** The functional logic (e.g., "When this button is clicked, submit a form").

### The JavaScript Engine & Runtime
JavaScript is unique because it can run in two primary environments, which changes what "tools" are available to you:

*   **The Browser (Client-Side):** JavaScript runs inside the browser engine (like Chrome's V8). Here, it has access to the **Web APIs**, such as the **DOM** (to change HTML), the **Window object** (to trigger alerts), and **Fetch** (to get data).
*   **Node.js (Server-Side):** JavaScript runs directly on your computer or a server. Here, it has access to the **System APIs**, such as the **File System** (to read/write files) and **Network Modules** (to build web servers).

### Key Conceptual Takeaways
*   **High-Level:** You don't need to manage computer memory manually; JavaScript handles it for you (via Garbage Collection).
*   **Dynamic Typing:** Variables are not locked into a specific type; a variable can hold a number and later be reassigned to a string.
*   **Single-Threaded but Asynchronous:** JavaScript executes one command at a time (single-threaded), but it uses an **Event Loop** to handle long-running tasks (like fetching data) in the background so the page doesn't "freeze."

---

## Datatypes

In JavaScript, data is categorized into two fundamental groups: **Primitives** (simple, immutable values) and **Objects** (complex, mutable collections). Understanding this distinction is vital for understanding how data is passed around your application.

### 1. Primitive Types
Primitives are the most basic building blocks. They are **immutable**, meaning the value itself cannot be changed (though the variable holding it can be reassigned).

| Type | Description | Example |
| :--- | :--- | :--- |
| `Number` | Both integers and floating-point (decimals). | `42`, `3.14` |
| `String` | A sequence of text characters. | `'Hello'`, `"World"` |
| `Boolean` | A logical entity: `true` or `false`. | `true` |
| `Undefined` | A variable that has been declared but not assigned a value. | `let x;` |
| `Null` | An intentional, empty assignment representing "nothing." | `let y = null;` |
| `Symbol` | A unique and immutable identifier (used in advanced object logic). | `Symbol('id')` |
| `BigInt` | Used for integers larger than the standard `Number` limit. | `9007199254740991n` |

### 2. Reference Types (Objects)
Objects are collections of properties. Unlike primitives, they are **mutable** and are stored by **reference** (the variable doesn't hold the data itself, but a "pointer" to where the data lives in memory).

*   **`Object`:** A collection of key-value pairs.
    *   *Example:* `{ name: "Alice", age: 30 }`
*   **`Array`:** A specialized object used for ordered lists.
    *   *Example:* `[1, 2, 3]`
*   **`Function`:** In JavaScript, functions are actually objects that can be stored in variables and passed as arguments.

### Summary: Primitives vs. Objects

| Feature | Primitives | Objects (Reference Types) |
| :--- | :--- | :--- |
| **Storage** | Stored directly in the variable. | Stored as a "reference" (address) in the variable. |
| **Mutability** | Immutable (cannot be changed). | Mutable (can be modified). |
| **Complexity** | Single value. | Can contain multiple values/nested structures. |

**NOTE:** When you assign an object to a new variable (`let arr2 = arr1`), you aren't making a copy of the data; you are making a second pointer to the **same** data. Changing `arr2` will also change `arr1`.

---

## Variable Declaration and Scope

To write predictable code, you must understand how JavaScript manages the "visibility" of variables. This is determined by two factors: how a variable is **declared** (`var`, `let`, or `const`) and the **scope** in which it lives.

## Variable Declaration: var, let, and const

Modern JavaScript development has moved away from `var` in favor of `let` and `const`. Choosing the right declaration is the first step in preventing bugs caused by accidental reassignment or scope leakage.

### 1. `const` (Constant)
*   **Behavior:** Declares a block-scoped variable that **cannot be reassigned**.
*   **When to use:** This should be your **default**. Use it for any value that you do not intend to change.
*   **NOTE:** While you cannot reassign a `const` variable, if the variable holds an object or an array, you **can** still modify the *contents* of that object or array.

### 2. `let`
*   **Behavior:** Declares a block-scoped variable that **can be reassigned**.
*   **When to use:** Use this only when you know the value of the variable must change (e.g., a counter in a loop or a toggle state).

### 3. `var` (Legacy)
*   **Behavior:** Declares a function-scoped variable that is "hoisted" to the top of its scope.
*   **When to use:** **Avoid entirely** in modern development. `var` does not respect block scope (like `if` statements or `for` loops), which often leads to bugs where variables "leak" out of where they were intended to stay.

| Feature | `const` | `let` | `var` |
| :--- | :--- | :--- | :--- |
| **Scope** | Block | Block | Function |
| **Reassignable** | No | Yes | Yes |
| **Hoisted** | Yes (but inaccessible) | Yes (but inaccessible) | Yes (initialized as `undefined`) |

## Variable Scopes

Scope determines the accessibility (visibility) of variables. If a variable is "in scope," you can use it; if it is "out of scope," it is unreachable.

### 1. Global Scope
Variables declared outside of any function or block reside in the global scope. They are accessible from anywhere in your entire script.
*   **NOTE:** Overusing global variables is dangerous because any part of your program can change them, making debugging extremely difficult.

### 2. Function Scope
Variables declared inside a function are only accessible within that function. This is the traditional scope used by `var`.

### 3. Block Scope
A "block" is any code wrapped in curly braces `{ ... }` (such as `if` statements, `for` loops, or `while` loops). Variables declared with `let` and `const` are restricted to this block.

```javascript
if (true) {
    var globalStyle = "I leak outside this block";
    let localStyle = "I am trapped in this block";
}

console.log(globalStyle); // Works: "I leak outside this block"
console.log(localStyle);  // ReferenceError: localStyle is not defined
```

### 4. Lexical Scope

Lexical scope (also known as **Static Scope**) means that the accessibility of variables is determined by the physical location of the variables within the source code. In simpler terms, a function's scope is defined by where it was **written**, not where it is **called**.

#### Why does Lexical Scope exist?
Lexical scoping provides a predictable way to manage data privacy and "environmental" access. It allows for the creation of a "chain" of access, where inner functions can rely on the context provided by their parents. This is the foundational mechanism that enables two of JavaScript's most powerful features: **Closures** and **Encapsulation**.

Without lexical scope, functions would be "isolated islands," unable to access any context other than global variables, making modular and complex programming nearly impossible.

#### The Scope Chain
When you attempt to access a variable, the JavaScript engine performs a lookup using the **Scope Chain**:
1. It first looks in the **immediate local scope** (inside the current function/block).
2. If not found, it moves up to the **outer (parent) scope**.
3. It continues moving up through the chain of parent scopes until it reaches the **Global Scope**.
4. If the variable is still not found, it throws a `ReferenceError`.

#### Code Example: The Lookup Process

```javascript
const globalName = "Global Context";

function outerFunction() {
    const outerVar = "Outer Context";

    function innerFunction() {
        const innerVar = "Inner Context";

        // 1. Found in Local Scope
        console.log(innerVar); 

        // 2. Not in Local, move up to Outer Scope
        console.log(outerVar); 

        // 3. Not in Local or Outer, move up to Global Scope
        console.log(globalName); 
    }

    innerFunction();
}

outerFunction();
```

**NOTE:** Because the scope is "lexical" (fixed at the time of writing), an inner function will always look at the scope where it was *defined*. Even if you pass that inner function into a completely different part of your program, it carries its "parental" scope with it. This "memory" of its original environment is exactly what creates a **Closure**.

---

### The Concept of Hoisting

**Hoisting** is JavaScript's default behavior of moving declarations to the top of the current scope before code execution.

*   **`var` hoisting:** The declaration is hoisted and initialized as `undefined`. You can technically reference a `var` before it is declared in the code without the program crashing, though it will return `undefined`.
*   **`let` and `const` hoisting:** These are also hoisted, but they are **not initialized**. They enter a "Temporal Dead Zone" (TDZ) from the start of the block until the declaration is reached.

**NOTE:** Attempting to access a `let` or `const` variable before its declaration will result in a `ReferenceError`. This is a safety feature that encourages cleaner, more linear code.

---

# Type Coercion and Truthiness

JavaScript is a **dynamically typed** language, which means it frequently performs implicit type conversions to make operations work. While this can be convenient, it can also lead to subtle, hard-to-debug errors if you do not understand the underlying rules.

---

## Type Coercion

**Coercion** is the automatic or implicit conversion of values from one data type to another (e.g., converting a string to a number).

### 1. Implicit Coercion
This occurs when JavaScript performs conversions automatically during an operation.

*   **String Coercion (The `+` operator):** When a string is added to any other type, JavaScript converts the other type to a string and performs **concatenation**.
    *   `"5" + 2` $\rightarrow$ `"52"`
    *   `"Hello " + true` $\rightarrow$ `"Hello true"`
*   **Numeric Coercion:** When using mathematical operators other than `+` (such as `-`, `*`, `/`, or `%`), JavaScript attempts to convert both operands into numbers.
    *   `"5" - 2` $\rightarrow$ `3`
    *   `"10" * "2"` $\rightarrow$ `20`
    *   `true + 1` $\rightarrow$ `2` (because `true` is coerced to `1`)
*   **Boolean Coercion:** Occurs when a value is used in a logical context (like an `if` statement).

### 2. Explicit Coercion (Type Casting)
This is when a developer manually converts a type using built-in functions. This is considered a **best practice** because it makes the code's intent clear.

*   `Number("42")` $\rightarrow$ `42`
*   `String(123)` $\rightarrow$ `"123"`
*   `Boolean(1)` $\rightarrow$ `true`

### Equality: Loose vs. Strict

The most common source of coercion bugs is the use of the "loose equality" operator.

*   **Loose Equality (`==`):** Performs type coercion before comparing. It asks: *"Are these values equivalent after conversion?"*
*   **Strict Equality (`===`):** Does **not** perform coercion. It compares both the **value** and the **type**.

| Comparison | Result | Reason |
| :--- | :--- | :--- |
| `5 == "5"` | `true` | The string `"5"` is coerced to the number `5`. |
| `5 === "5"` | `false` | Types do not match (Number vs. String). |
| `false == 0` | `true` | Both are coerced to the same numeric value. |
| `null == undefined` | `true` | A specific rule in the JS spec. |

**NOTE: Always use strict equality (`===`). Relying on loose equality (`==`) makes your code unpredictable and difficult to reason about.**

---

## Truthy and Falsy Values

In JavaScript, every single value has an inherent boolean identity. When a value is used in a boolean context (like an `if` condition), it is coerced into either `true` or `false`.

### 1. Falsy Values
There is a very short, specific list of values that always coerce to `false`. If you know these, you know all the "falsy" values in JavaScript.

1.  `false` (the keyword)
2.  `0` (the number zero)
3.  `-0` (negative zero)
4.  `0n` (BigInt zero)
5.  `""` (an empty string)
6.  `null`
7.  `undefined`
8.  `NaN` (Not-a-Number)

### 2. Truthy Values
**Everything else is truthy.** This includes empty arrays `[]`, empty objects `{}`, and the string `"0"` or `"false"`.

#### Practical Application: Short-circuiting
Understanding truthiness allows for concise code patterns, such as providing default values.

```javascript
// Using truthiness to provide a fallback
let username = ""; 
let displayName = username || "Guest"; 

console.log(displayName); // "Guest" (because "" is falsy)
```

**NOTE: Be cautious when checking for numbers. If you write `if (count) { ... }`, and `count` is `0`, the code block will not execute because `0` is falsy. In such cases, explicitly check for null/undefined: `if (count !== undefined && count !== null) { ... }`**

---

# Arrays

An **Array** is a specialized type of Object used to store an ordered collection of values. In JavaScript, arrays are dynamic, meaning they can grow or shrink in size, and they can hold a mixture of different data types simultaneously.

## Array Fundamentals

Arrays are zero-indexed, meaning the first element is located at position `0`.

### 1. Declaration and Access
```javascript
const fruits = ["apple", "banana", "cherry"];

// Accessing elements
console.log(fruits[0]); // "apple"
console.log(fruits[2]); // "cherry"

// Accessing length
console.log(fruits.length); // 3
```

### 2. Basic Manipulation (Mutating Methods)
These methods modify the original array directly.

*   **`.push(value)`**: Adds an element to the **end** of the array.
*   **`.pop()`**: Removes the **last** element from the array and returns it.
*   **`.unshift(value)`**: Adds an element to the **beginning** of the array.
*   **`.shift()`**: Removes the **first** element from the array and returns it.
*   **`.splice(start, deleteCount, item1...)`**: A powerful method used to add or remove elements at a specific index.

```javascript
let colors = ["red", "green"];

colors.push("blue");      // ["red", "green", "blue"]
colors.pop();             // ["red", "green"]
colors.unshift("yellow"); // ["yellow", "red", "green"]
```

---

## Modern Array Iteration (Non-Mutating)

In modern development, we rarely use traditional `for` loops to manipulate arrays. Instead, we use **higher-order functions**. These methods do not change the original array; instead, they return a new value or a new array.

### 1. `.forEach()`
Executes a provided function once for each array element. It is used for "side effects" (like logging to the console) rather than transforming data.
```javascript
const nums = [1, 2, 3];
nums.forEach(num => console.log(num * 2)); // Logs 2, 4, 6
```

### 2. `.map()` (The Transformer)
Creates a **new array** by applying a function to every element in the original array. Use this when you want to transform data.
```javascript
const numbers = [1, 2, 3];
const doubled = numbers.map(n => n * 2); 

console.log(doubled); // [2, 4, 6]
console.log(numbers); // [1, 2, 3] (Original is unchanged)
```

### 3. `.filter()` (The Selector)
Creates a **new array** containing only the elements that pass a specific logical test (truthy condition).
```javascript
const ages = [12, 18, 25, 30, 14];
const adults = ages.filter(age => age >= 18);

console.log(adults); // [18, 25, 30]
```

### 4. `.reduce()` (The Accumulator)
Executes a reducer function on each element, resulting in a **single output value**. It is used for calculating totals, combining data, or flattening structures.

```javascript
const prices = [10, 20, 30];
const total = prices.reduce((accumulator, currentValue) => { return accumulator + currentValue; }, 0);
console.log(total); // 60
```

#### Breakdown of the `.reduce()` Parameters:

1.  **`accumulator`**: This is the "running total" or the value that is carried over from one iteration to the next. It holds the result of everything calculated up to that point.
2.  **`currentValue`**: This is the specific element of the array currently being processed in the loop.
3.  **`initialValue` (The `0`)**: This is the starting value for the `accumulator`. 
    *   **NOTE:** Always provide an initial value. If you omit it, `reduce` will use the first element of the array as the accumulator and start the loop from the second element, which can lead to errors when working with empty arrays or complex objects.

## Summary Table: Array Methods

| Method | Mutates Original? | Returns | Primary Use Case |
| :--- | :--- | :--- | :--- |
| `.push()` / `.pop()` | **Yes** | New length / Removed element | Adding/removing from the end. |
| `.shift()` / `.unshift()` | **Yes** | Removed element / New length | Adding/removing from the start. |
| `.map()` | No | A new array | Transforming every item in a list. |
| `.filter()` | No | A new array | Selecting a subset of items. |
| `.reduce()` | No | A single value | Condensing a list into one result. |
| `.forEach()` | No | `undefined` | Performing an action for every item. |

**NOTE: When working with data in frameworks like React, always prefer non-mutating methods (`.map`, `.filter`) over mutating methods (`.push`, `.splice`). Mutating the original array can cause bugs where the application fails to recognize that the data has changed.**

---

# Functions and Execution Context

Functions are the fundamental building blocks of JavaScript logic. They allow you to encapsulate a set of instructions, assign them a name, and execute them repeatedly. In JavaScript, functions are **First-Class Citizens**, meaning they can be stored in variables, passed as arguments to other functions, and returned from functions.

## Function Syntax and Styles

There are three primary ways to define functions in modern JavaScript. Each has subtle differences in syntax and behavior.

### 1. Function Declarations
The traditional way to define a function. These are **hoisted**, meaning they can be called even before they are defined in the code.
```javascript
function greet(name) {
    return `Hello, ${name}!`;
}
```

### 2. Function Expressions
A function is created and assigned to a variable. These are **not hoisted**; you cannot call them before the line where they are defined.
```javascript
const add = function(a, b) {
    return a + b;
};
```

### 3. Arrow Functions (ES6+)
A concise syntax introduced in ES6. They are highly popular for short, one-line operations and are the standard for callback functions.
```javascript
// Standard Arrow Function
const multiply = (a, b) => {
    return a * b;
};

// Implicit Return (Concise)
// If the function is a single expression, you can omit { } and the 'return' keyword.
const square = x => x * x;

// Single Parameter
// If there is exactly one parameter, you can omit the parentheses.
const double = n => n * 2;
```

---

## The `this` Keyword

The `this` keyword is one of the most misunderstood concepts in JavaScript. It does not refer to the function itself, but rather to the **execution context**—the object that is currently "owning" or executing the code.

The value of `this` is determined by **how the function is called**, not where it is written.

### 1. Global Context
In the global execution context (outside of any function), `this` refers to the global object (e.g., `window` in browsers).

### 2. Method Context (Object Binding)
When a function is called as a method of an object, `this` refers to the object that "owns" the method.
```javascript
const user = {
    name: "Alex",
    greet() {
        console.log(`Hi, I am ${this.name}`);
    }
};

user.greet(); // "Hi, I am Alex" (this refers to the 'user' object)
```

### 3. Arrow Functions and Lexical `this`
This is the most critical distinction. **Arrow functions do not have their own `this`.** Instead, they inherit `this` from the surrounding (lexical) scope.

This makes arrow functions perfect for callbacks (like `setTimeout` or array methods) where you want to preserve the `this` context of a parent object.

```javascript
const timer = {
    seconds: 0,
    start() {
        // Using an Arrow Function preserves 'this' from the 'start' method
        setInterval(() => {
            this.seconds++; 
            console.log(this.seconds);
        }, 1000);
    }
};

timer.start(); // Works: 'this' correctly refers to the 'timer' object.
```

**NOTE: If you use a regular `function()` inside a method like the example above, `this` would become `undefined` (in strict mode) or the `window` object, because the function is being executed in a new, separate context.**

## Summary: Function Comparison

| Feature | Function Declaration | Function Expression | Arrow Function |
| :--- | :--- | :--- | :--- |
| **Syntax** | `function name() {}` | `const x = function() {}` | `const x = () => {}` |
| **Hoisting** | Yes (Full) | No | No |
| **`this` Binding** | Dynamic (based on caller) | Dynamic (based on caller) | Lexical (inherited) |
| **Best Use Case** | Top-level utility functions | When you need to control hoisting | Callbacks and preserving context |

---

# The DOM (Document Object Model)

The DOM is the browser's internal representation of your HTML document. It transforms the static HTML text into a live, hierarchical tree of objects that JavaScript can interact with. By manipulating the DOM, you can change content, styles, and structure in real-time without a page reload.

---

## 1. Selecting Elements

Before you can change something, you must first "find" it. Modern JavaScript provides several powerful methods to target specific elements.

### Common Selection Methods

| Method | Selector Type | Returns | Best Use Case |
| :--- | :--- | :--- | :--- |
| `document.getElementById('id')` | ID | A single element | When you have a unique element. |
| `document.querySelector('selector')` | CSS Selector | The **first** matching element | Most versatile; use for classes, IDs, or tags. |
| `document.querySelectorAll('selector')` | CSS Selector | A **NodeList** (collection) | When you need to target multiple elements. |

### Code Examples
```javascript
// Selecting by ID
const mainTitle = document.getElementById('main-header');

// Selecting by CSS Selector (Class)
const firstButton = document.querySelector('.btn-primary');

// Selecting by CSS Selector (Multiple)
const allListItems = document.querySelectorAll('ul > li');

// Iterating over a NodeList
allListItems.forEach(item => console.log(item.textContent));
```

---

## 2. DOM Manipulation

Once you have selected an element, you can manipulate its content, attributes, or visual appearance.

### Changing Content
*   **`.textContent`**: Sets or gets the raw text inside an element. It is safe and fast because it does not parse HTML.
*   **`.innerHTML`**: Sets or gets the HTML content. Use this when you need to inject actual HTML tags (like `<b>` or `<div>`).
	* 	**NOTE**: Avoid `.innerHTML` when inserting text that could contain user input, as it exposes your application to XSS attacks. Use `.textContent` for safer, more efficient text updates.

```javascript
const para = document.querySelector('.description');

para.textContent = "Updated text content."; // Safe text update
para.innerHTML = "<strong>Bolded</strong> text."; // Parses HTML
```

### Changing Attributes
Attributes provide metadata for elements (like `src` for images or `href` for links).
*   **`.setAttribute(name, value)`**: Sets a new value for an attribute.
*   **`.getAttribute(name)`**: Retrieves the current value.
*   **`.removeAttribute(name)`**: Removes the attribute entirely.

```javascript
const profileImg = document.querySelector('#profile-pic');

profileImg.setAttribute('src', 'avatar.png');
profileImg.setAttribute('alt', 'User Profile Picture');
```

### Changing Styles
You can modify CSS directly through the `.style` property. Note that CSS properties are written in **camelCase** in JavaScript (e.g., `background-color` becomes `backgroundColor`).

```javascript
const box = document.querySelector('.box');

box.style.backgroundColor = 'blue';
box.style.marginTop = '20px';
box.style.display = 'none'; // Hides the element
```

---

## 3. Managing CSS Classes (Best Practice)

While `.style` is useful for dynamic, single-value changes, the professional way to handle styling is by toggling **CSS Classes**. This keeps your styles in your CSS file and your logic in your JavaScript file, maintaining the **Separation of Concerns**.

Use the `.classList` property to manage classes:

*   **`.add('className')`**: Adds a class.
*   **`.remove('className')`**: Removes a class.
*   **`.toggle('className')`**: Adds the class if it's missing; removes it if it's present.
*   **`.contains('className')`**: Returns `true` or `false` if the element has that class.

```javascript
const menu = document.querySelector('.nav-menu');

// The professional way to show/hide a menu
menu.classList.toggle('is-active'); 

// Adding a specific state
menu.classList.add('visible');
```

**NOTE: Avoid using `.style` for large-scale layout changes. It creates "inline styles" which have high CSS specificity and are difficult to override later. Always prefer `.classList` to trigger pre-defined CSS rules.**

---

# Events and the DOM Event Flow

Events are signals sent by the browser to notify your code that something has happened—such as a click, a keypress, or a window resize. To make a webpage interactive, you must "listen" for these signals and execute code in response.

## 1. Event Listeners

The modern standard for handling events is the `.addEventListener()` method. It allows you to attach an event handler to an element without overwing existing event handlers.

### Syntax
```javascript
element.addEventListener(event, handler, options);
```

*   **`event`**: A string representing the event type (e.g., `'click'`, `'submit'`, `'keydown'`).
*   **`handler`**: The function that runs when the event occurs.
*   **`options`**: (Optional) An object that specifies characteristics of the event listener (like `once: true` to trigger it only once).

### Code Example
```javascript
const btn = document.querySelector('#submit-btn');

// Using an anonymous arrow function
btn.addEventListener('click', () => {
    console.log('Button was clicked!');
});

// Using a named function (Better for reusability and removing listeners)
function handleFormSubmit(event) {
    event.preventDefault(); // Prevents the default browser behavior (like page reload)
    console.log('Form submitted!');
}

const form = document.querySelector('#user-form');
form.addEventListener('submit', handleFormSubmit);
```

**NOTE:** To remove an event listener, you **cannot** use an anonymous function; you must have a reference to the original named function.

```javascript
// 1. Define a named function
function handleClick() {
    console.log('Clicked!');
}

// 2. Add the listener
btn.addEventListener('click', handleClick);

// 3. Remove the listener
btn.removeEventListener('click', handleClick);
```

---

## 2. The Event Flow: Bubbling and Capturing

When an event occurs on an element, it doesn't just happen in isolation. The event actually travels through the DOM tree in a specific sequence of three phases:

1.  **Capturing Phase:** The event starts at the `window` and travels **down** through the ancestors until it reaches the target element.
2.  **Target Phase:** The event reaches the element that was actually clicked/interacted with.
3.  **Bubbling Phase:** The event travels **up** from the target element through its ancestors back to the `window`.

### Bubbling (The Default)
By default, almost all event listeners are triggered during the **Bubbling** phase. If you click a button inside a `<div>`, the click event first hits the button, then "bubbles up" to the `<div>`, then to the `<body>`, and so on.

```javascript
const box = document.querySelector('.box');
const btn = document.querySelector('.btn');

box.addEventListener('click', () => console.log('Box clicked!'));
btn.addEventListener('click', () => console.log('Button clicked!'));

// Clicking the button will log:
// "Button clicked!"
// "Box clicked!" (because the event bubbled up)
```

### Capturing
You can instruct an event listener to trigger during the **Capturing** phase instead of the Bubbling phase by passing `true` as the third argument to `addEventListener`.

```javascript
// This listener will trigger BEFORE the button's listener
box.addEventListener('click', () => console.log('Box capturing!'), true);
```

---

## 3. Event Delegation

**Event Delegation** is a highly efficient design pattern that leverages **Bubbling** to manage events for multiple child elements using a single parent listener.

Instead of attaching 100 event listeners to 100 individual list items (`<li>`), you attach **one** listener to the parent (`<ul>`). When an `<li>` is clicked, the event bubbles up to the `<ul>`, where you can identify which child was clicked using `event.target`.

### Code Example
```javascript
const list = document.querySelector('#item-list');

list.addEventListener('click', (event) => {
    // Check if the clicked element was actually an LI
    if (event.target.tagName === 'LI') {
        console.log('List item clicked:', event.target.textContent);
    }
});
```

### Why Use Event Delegation?
1.  **Memory Efficiency:** Fewer event listeners mean less memory usage.
2.  **Dynamic Elements:** If you add new `<li>` elements to the list via JavaScript later, they will **automatically** have the click functionality because the listener is on the parent.

| Concept | Direction | Default? | Use Case |
| :--- | :--- | :--- | :--- |
| **Capturing** | Down (Root $\rightarrow$ Target) | No | Intercepting events before they reach children. |
| **Bubbling** | Up (Target $\rightarrow$ Root) | **Yes** | Standard interaction and Event Delegation. |

---

# Asynchronous JavaScript

JavaScript is single-threaded, meaning it can only do one thing at a time. To prevent the browser from "freezing" during long-running tasks—like fetching data from a server or waiting for a timer—JavaScript uses **Asynchronous Programming**. This allows the engine to start a task, move it to the background, and continue executing other code until the task is complete.

## 1. Promises: The Foundation

A **Promise** is an object representing the eventual completion (or failure) of an asynchronous operation and its resulting value. Think of it as a placeholder for a value that doesn't exist yet.

### The Three States of a Promise
1.  **Pending:** The initial state; the operation is still in progress.
2.  **Fulfilled:** The operation completed successfully.
3.  **Rejected:** The operation failed (e.g., a network error).

### Manually Creating and Consuming a Promise
While most modern APIs return promises automatically, you can create your own to wrap any asynchronous logic.

```javascript
// Manually creating a promise
const waitForData = new Promise((resolve, reject) => {
    const success = true;

    setTimeout(() => {
        if (success) {
            resolve("Data received!"); // Moves state to Fulfilled
        } else {
            reject("Error: Could not fetch data."); // Moves state to Rejected
        }
    }, 2000);
});

// Consuming the promise using .then() and .catch()
waitForData
    .then((result) => {
        console.log(result); // "Data received!"
    })
    .catch((error) => {
        console.error(error); // Runs if the promise is Rejected
    })
    .finally(() => {
        console.log("Operation complete."); // Runs regardless of outcome
    });
```

---

## 2. The Fetch API

The **Fetch API** is the modern standard for making HTTP requests. It is built entirely on Promises, meaning every `fetch()` call returns a Promise that resolves into a `Response` object.

```javascript
// A basic fetch call
fetch('https://api.example.com/data')
    .then(response => {
        if (!response.ok) throw new Error("Network response was not ok");
        return response.json(); // .json() also returns a Promise!
    })
    .then(data => console.log(data))
    .catch(error => console.error("Fetch error:", error));
```

---

## 3. Async and Await: The Modern Standard

While `.then()` and `.catch()` work perfectly, they can lead to "Promise Chaining Hell"—a messy, deeply nested structure that is hard to read and maintain. **`async` and `await`** are syntactic sugar built on top of Promises that allow you to write asynchronous code that looks and behaves like synchronous, top-to-bottom code.

### Syntax and Usage
*   **`async`**: A keyword used to declare a function as asynchronous. An `async` function **always** returns a Promise.
*   **`await`**: A keyword used only inside `async` functions. It pauses the execution of the function until the Promise settles, returning the resolved value.

```javascript
async function getUserData() {
    try {
        // Execution pauses here until fetch resolves
        const response = await fetch('https://api.example.com/user');
        
        if (!response.ok) {
            throw new Error("User not found");
        }

        // Execution pauses here until JSON is parsed
        const data = await response.json();
        
        console.log("User Data:", data);
    } catch (error) {
        // Errors from the fetch or the JSON parsing are caught here
        console.error("Error fetching user:", error.message);
    }
}

getUserData();
```

### Why `async/await` is Preferred

| Feature | `.then()` / `.catch()` | `async` / `await` |
| :--- | :--- | :--- |
| **Readability** | Can become "callback hell" with many nested steps. | Reads like standard, linear synchronous code. |
| **Error Handling** | Requires `.catch()` blocks at the end of chains. | Uses standard `try...catch` blocks, making it more intuitive. |
| **Debugging** | Stepping through nested `.then()` calls in a debugger is difficult. | Debugging is straightforward as it follows a linear execution path. |
| **Complexity** | Difficult to pass values between multiple asynchronous steps. | Variables are easily accessible in the same scope. |

**NOTE: While `await` pauses the execution of the `async` function, it does NOT block the main browser thread. Other scripts and user interactions will continue to run while the function is waiting.**

```js
// A helper function that returns a Promise that resolves after X milliseconds
const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));

async function fetchServerData() {
    console.log("1. [Async] Fetch started... (Requesting data)");
    
    // We simulate a slow network by "sleeping" for 3 seconds
    await sleep(3000); 
    
    const response = await fetch('https://jsonplaceholder.typicode.com/todos/1');
    const data = await response.json();
    
    console.log("4. [Async] Data received after 3s wait!", data.title);
}

function updateUI() {
    console.log("2. [Sync] UI update: Playing loading animation...");
}

function runAnimations() {
    console.log("3. [Sync] Animation: Moving background elements...");
}

// --- EXECUTION START ---

fetchServerData(); // Start the async task
updateUI();        // Runs immediately
runAnimations();   // Runs immediately

// EXPECTED OUTPUT ORDER:
// 1. [Async] Fetch started...
// 2. [Sync] UI update: Playing loading animation...
// 3. [Sync] Animation: Moving background elements...
// --- (3 second gap where the browser is free to do other things) ---
// 4. [Async] Data received after 3s wait! [Actual Data Here]
```

---

# Template Literals

Before ES6, combining variables with strings required cumbersome "string concatenation" using the `+` operator. **Template Literals** provide a modern, more readable, and powerful way to handle strings using **backticks** (`` ` ``) instead of single or double quotes.

## 1. String Interpolation

The most common use for template literals is **interpolation**: the ability to embed variables or expressions directly inside a string. This is done using the `${expression}` syntax.

### The Old Way (Concatenation)
This approach is error-prone, difficult to read, and becomes a nightmare when dealing with multiple variables or spaces.
```javascript
const name = "Sam";
const age = 25;

// Hard to read, easy to miss a space or a plus sign
console.log("Hello, " + name + "! You are " + age + " years old.");
```

### The Modern Way (Interpolation)
Template literals allow you to inject variables directly into the "template," making the code much more intuitive.
```javascript
const name = "Sam";
const age = 25;

// Clean, readable, and looks like the final output
console.log(`Hello, ${name}! You are ${age} years old.`);
```

## 2. Multi-line Strings

In standard strings, if you want to move to a new line, you have to use the newline character (`\n`). Template literals respect actual line breaks in your code, allowing you to write multi-line strings naturally.

```javascript
// Standard string (requires \n)
const messageOld = "This is line one.\nThis is line two.";

// Template literal (natural formatting)
const messageNew = `This is line one.
This is line two.
This is line three.`;

console.log(messageNew);
```

## 3. Expression Evaluation

The `${}` syntax isn't limited to just variables; it can evaluate **any valid JavaScript expression**, including mathematical operations, function calls, or ternary operators.

```javascript
const price = 100;
const tax = 0.07;

// Evaluating a math expression inside the string
console.log(`Total price: $${(price * (1 + tax)).toFixed(2)}`);

// Evaluating a ternary operator (conditional logic)
const isMember = true;
console.log(`Status: ${isMember ? "Premium Member" : "Standard Member"}`);
```

## Summary Table

| Feature | Standard Strings (`'` or `"`) | Template Literals (`` ` ``) |
| :--- | :--- | :--- |
| **Variable Injection** | Requires `+` (Concatenation) | Uses `${expression}` (Interpolation) |
| **Multi-line Support** | Requires `\n` escape characters | Supports natural line breaks |
| **Readability** | Low (cluttered with quotes/pluses) | High (looks like the final string) |
| **Expressions** | Not possible inside the string | Fully supported inside `${}` |

---

# The Node.js Ecosystem

While JavaScript began as a language strictly for browsers, the development of **Node.js** revolutionized the industry by allowing JavaScript to run on servers, desktops, and in command-line tools. This turned JavaScript into a "full-stack" language.

---

## 1. Node.js: The Runtime

**Node.js** is a cross-platform, open-source JavaScript **runtime environment**. It is not a programming language or a framework; rather, it is the "engine" that allows the JavaScript engine (V8) to execute outside of a web browser.

### Key Capabilities
Because Node.js runs on your operating system rather than inside a browser "sandbox," it has access to system-level resources that browser-based JavaScript does not:
*   **File System (FS):** The ability to create, read, update, and delete files on your computer.
*   **Networking:** The ability to act as a web server, listening for incoming requests from clients.
*   **Process Management:** Access to environment variables, command-line arguments, and system information.

### Module Systems: CommonJS vs. ES Modules
To keep code organized, Node.js uses modules to split code into multiple files. There are two ways to handle this:

#### A. CommonJS (The Legacy Standard)
Used for years in Node.js, this system uses `require()` to import and `module.exports` to export.
```javascript
// math.js (Exporting)
module.exports = { add: (a, b) => a + b };

// app.js (Importing)
const math = require('./math');
console.log(math.add(5, 5));
```

#### B. ES Modules (The Modern Standard)
This is the official JavaScript standard used in both modern browsers and modern Node.js. It uses `import` and `export`.
*   **To use this in Node:** Set `"type": "module"` in your `package.json` file.
```javascript
// math.js (Exporting)
export const add = (a, b) => a + b;

// app.js (Importing)
import { add } from './math.js';
console.log(add(5, 5));
```

## 2. npm (Node Package Manager)

**npm** is the world's largest software registry. It is automatically installed when you install Node.js. It serves two primary purposes: it is a **command-line tool** to manage dependencies, and a **massive library** of pre-written code (packages) that you can use in your own projects.

### The `package.json` File
Every professional Node.js project contains a `package.json` file. This is the "manifest" of your project. It tracks:
*   **Metadata:** Project name, version, and author.
*   **Dependencies:** A list of every external library your project needs to run.
*   **Scripts:** Custom commands to automate tasks (like starting your server or running tests).

### Essential npm Commands

| Command | Action |
| :--- | :--- |
| `npm init` | Initializes a new project and creates a `package.json` file. |
| `npm init -y` | Initializes a project instantly, skipping all the setup questions. |
| `npm install <package>` | Downloads a specific library (e.g., `express`) and adds it to your dependencies. |
| `npm install` | Scans your `package.json` and installs **every** library listed (essential when cloning a project). |
| `npm run <script>` | Executes a custom script defined in your `package.json`. |

### Practical Workflow Example

```bash
# 1. Create a new project folder and enter it
mkdir my-new-app && cd my-new-app

# 2. Initialize the project
npm init -y

# 3. Install a popular web server framework (Express)
npm install express

# 4. Start your application (assuming you defined a "start" script)
npm start
```

**NOTE: Always commit your `package.json` and `package-lock.json` files to version control (like Git), but **never** commit your `node_modules` folder. The `node_modules` folder is massive; instead, other developers should simply run `npm install` to recreate it based on your `package.json` file.**