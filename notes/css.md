# CSS Quick Reference Guide

## Overview of CSS
CSS (Cascading Style Sheets) is the language used to style and layout web pages. It controls the look and feel of HTML elements and separates content (HTML) from presentation (CSS).

**The "C" in CSS: The Cascade**
The "Cascading" nature of CSS means that when multiple rules apply to the same element, the browser follows a specific hierarchy to decide which rule "wins." This is determined by:
1.  **Specificity:** How targeted the selector is (e.g., an ID is more powerful than a class).
2.  **Order of Appearance:** If specificity is equal, the rule written *last* in the code wins.
3.  **Importance:** Rules marked with `!important` override almost everything else (use sparingly!).

**Specificity Hierarchy (Low to High):**
`Element Selector` $\rightarrow$ `Class Selector` $\rightarrow$ `ID Selector` $\rightarrow$ `Inline Style`

```css
/* Example of the Cascade */

p { color: blue; }       /* 1. Low specificity (Element) */
.text-red { color: red; } /* 2. Higher specificity (Class) */
#main-title { color: green; } /* 3. Highest specificity (ID) */

/* If all three applied to one <p id="main-title" class="text-red">, 
   the text would be GREEN. */
```

---

## The CSS Box Model
Every element in CSS is treated as a rectangular box. Understanding how these layers interact is critical for controlling layout and preventing elements from "overflowing" their containers.

**The Layers (Inside to Out):**
1.  `content`: The actual text, images, or video.
2.  `padding`: Transparent space *inside* the border; pushes content away from the edges.
3.  `border`: The line surrounding the padding and content.
4.  `margin`: Transparent space *outside* the border; separates the element from its neighbors.

**The `box-sizing` Essential**
By default, adding padding or borders makes an element **wider** than the width you set. To fix this, we almost always use `border-box`.

```css
/* The Global Reset: The industry standard approach */
* {
  /* Includes padding and border in the element's total width/height */
  box-sizing: border-box; 
}

.card {
  width: 300px;
  padding: 20px;
  border: 5px solid black;
  /* With border-box: The total width stays 300px.
     Without border-box: The total width would be 350px! */
}
```
---

## CSS Properties: The Instruction Set

If HTML provides the **structure** (the "what"), CSS properties provide the **instructions** (the "how"). A CSS property is a specific aspect of an element that you wish to modify. Every property consists of a **name** and a **value**.

### The Anatomy of a Declaration
A single instruction is called a **declaration**. A collection of declarations applied to a selector is called a **declaration block**.

```css
/* Selector */
h1 {
    /* Property : Value ; */
    color: #2c3e50;        /* Declaration 1 */
    font-size: 2rem;       /* Declaration 2 */
}
```

## Categorizing Properties: Functional Roles

Rather than memorizing individual properties, it is more effective to categorize them by the **role** they play in transforming the HTML structure.

### 1. Typography (Text Styling)
These properties control the readability and visual hierarchy of written content. They primarily affect the "content" layer of the box model.

*   **`color`**: Sets the foreground color of the text.
*   **`font-family`**: Defines the typeface (e.g., `Arial`, `sans-serif`). Always provide a "fallback" font.
*   **`font-size`**: Determines the scale of the text. 
    *   *Best Practice:* Use relative units (`rem`, `em`) to ensure accessibility and scalability.
*   **`font-weight`**: Controls the thickness of characters (e.g., `bold`, `400`, `700`).
*   **`line-height`**: Controls the vertical spacing between lines of text (leading). Crucial for readability.
*   **`text-align`**: Controls the horizontal alignment (`left`, `center`, `right`, `justify`).

### 2. The Box Model (Spatial Control)
These properties are the "engine" of layout. They control the dimensions, spacing, and borders of every element.

*   **Dimension Properties**:
    *   `width` / `height`: The size of the content area.
    *   `max-width` / `min-width`: Sets boundaries to prevent elements from becoming too large or too small (essential for **Responsive Design**).
*   **Spacing Properties**:
    *   `padding`: The internal space between the content and the border.
    *   `margin`: The external space between this element and its neighbors.
*   **Border Properties**:
    *   `border`: A shorthand for `border-width`, `border-style`, and `border-color`.
    *   `border-radius`: Rounds the corners of the element's edge.

### 3. Visual Decoration (The "Skin")
These properties change the aesthetic appearance of the element without affecting its physical size or position in the document flow.

*   **`background-color`**: Sets the color behind the content.
*   **`background-image`**: Places an image behind the content.
*   **`opacity`**: Sets the transparency level of the entire element (from `0.0` to `1.0`).
*   **`box-shadow`**: Adds depth by applying a shadow effect to the element's frame.

## Values and Units: The "How Much"

A property is useless without a value, and values are governed by the type of **unit** you use. Choosing the wrong unit is the most common cause of broken layouts.

### 1. Absolute Units (Fixed)
These are fixed values that do not change based on the environment. Use these sparingly.
*   `px` (Pixels): The standard unit for fine-grained control. However, `px` is not inherently responsive.

### 2. Relative Units (Fluid/Responsive)
These units calculate their size based on something else (the parent, the root, or the viewport). **These are the foundation of modern, responsive web design.**

| Unit | Relative To... | Best Use Case |
| :--- | :--- | :--- |
| **`rem`** | The **Root** (`<html>`) font size. | Font sizes and consistent spacing. |
| **`em`** | The **Parent** element's font size. | Spacing that should scale with text. |
| **`%`** | The **Parent** element's dimensions. | Defining widths of containers. |
| **`vw`** | The **Viewport Width** (1vw = 1% of screen width). | Elements that must scale with the screen. |
| **`vh`** | The **Viewport Height** (1vh = 1% of screen height). | Full-screen sections or hero images. |

### Summary: The Designer's Mental Checklist

When applying properties to an element, ask yourself these three questions to ensure high-quality code:

1.  **Is it Semantic?** (e.g., Am I using `font-weight: bold` to create importance, or just to make it look pretty? If it's for importance, use `<strong>` in HTML instead.)
2.  **Is it Responsive?** (e.g., Did I use `width: 500px` (Fixed) or `max-width: 500px; width: 100%` (Fluid)?)
3.  **Is the Unit Appropriate?** (e.g., Should this spacing be `px` for precision, or `rem` for accessibility?)

---

## Responsive Web Design: The Fluid System

Responsive Web Design (RWD) is the practice of building websites that "respond" to the user's environment. This includes screen size, device orientation (portrait vs. landscape), and even the device's capabilities.

Modern RWD is built on three core pillars: **Fluid Grids**, **Flexible Media**, and **Media Queries**.

### 1. The Fluid Mindset: Relative vs. Absolute
The most common mistake in web design is "Hard-Coding" layouts. A hard-coded layout uses fixed values (like `width: 1200px`), which creates a "broken" experience on any screen smaller than 1200px.

To create a fluid system, we move from **Fixed Dimensions** to **Proportional Dimensions**.

*   **Fixed (The "Rigid" Approach):** Uses `px`. The element stays the same size regardless of the screen.
*   **Fluid (The "Responsive" Approach):** Uses `%`, `vw`, `rem`, or `fr`. The element grows or shrinks relative to its container or the viewport.

**The "Max-Width" Safety Net:**
A powerful way to combine these is using `max-width`. This allows an element to be fluid (shrink) but prevents it from becoming awkwardly large on massive monitors.

```css
.container {
    width: 90%;           /* Be fluid on small screens */
    max-width: 1200px;    /* Stop growing once you hit 1200px */
    margin: 0 auto;       /* Center the container */
}
```

### 2. Flexible Media: The "Image Problem"
Images are "heavy" assets with fixed aspect ratios. If an image is 2000px wide and you put it in a 400px container, it will "overflow" and break your layout.

**The Universal Responsive Image Rule:**
To ensure images never break their containers, we use a combination of `max-width` and `height: auto`.

```css
img {
    max-width: 100%; /* Never get wider than your parent container */
    height: auto;    /* Maintain aspect ratio so you don't look "squished" */
}
```

### 3. Media Queries: The "Conditional Logic" of CSS
If Fluidity is about *stretching*, Media Queries are about *reconfiguring*. 

Media Queries allow you to apply different sets of CSS rules based on specific conditions (usually the width of the device). This is where you change a **horizontal layout** (for Desktop) into a **vertical stack** (for Mobile).

#### The "Mobile-First" Strategy
Modern developers use a **Mobile-First** workflow. This means you write your base CSS for the smallest screens first (simpler, single-column layouts), and then use Media Queries to add complexity as the screen gets larger.

*   **Why?** It results in cleaner code and better performance on mobile devices (which often have slower processors/data).

```css
/* 1. Base Styles: Mobile-First (Single Column) */
.content-wrapper {
    display: flex;
    flex-direction: column; /* Stack items vertically */
}

/* 2. Tablet/Desktop Styles: Scaling Up */
@media (min-width: 768px) {
    .content-wrapper {
        flex-direction: row; /* Switch to side-by-side columns */
    }
}
```

### Summary of Responsive Strategies

| Strategy | Concept | Goal |
| :--- | :--- | :--- |
| **Fluidity** | Use `%`, `rem`, `vw` instead of `px`. | Allow elements to scale smoothly. |
| **Containment** | Use `max-width` and `box-sizing`. | Prevent elements from breaking the layout. |
| **Adaptation** | Use `@media` queries. | Change the actual layout structure for different devices. |
| **Mobile-First** | Base CSS = Mobile $\rightarrow$ Media Queries = Desktop. | Write cleaner, more performant, and scalable code. |

### The "Responsive Checklist"
Before finalizing a layout, ask:
1.  **The Squish Test:** If I shrink my browser window slowly, do elements overlap or disappear?
2.  **The Image Test:** Do my images scale down, or do they force a horizontal scrollbar?
3.  **The Touch Test:** On a mobile view, are my buttons/links large enough to be tapped by a thumb? (Usability)

---

## CSS Implementation: Methods of Application

There are three ways to apply CSS to an HTML document. While they all achieve the same visual result, they differ significantly in terms of **scope**, **maintainability**, and **performance**.

### 1. Inline Styling (The "Exception")
Inline styles are applied directly to a single HTML element using the `style` attribute.

*   **Scope:** Extremely limited. It only affects the specific element it is attached to.
*   **Use Case:** Generally discouraged in modern development. It is reserved for "emergency" overrides or when applying dynamic styles via JavaScript.
*   **Pros:** Highest **specificity** (it will override almost any other rule).
*   **Cons:** Extremely difficult to maintain; violates the "Separation of Concerns" principle by mixing structure with presentation.

```html
<!-- Inline Style: Hard to reuse, high specificity -->
<p style="color: green; font-weight: bold;">This is an inline style.</p>
```

### 2. Internal Styling (The "Page-Specific" Approach)
Internal styles are defined within a `<style>` tag located inside the `<head>` section of an HTML document.

*   **Scope:** The entire HTML document it is contained within.
*   **Use Case:** Best for single-page websites or when a specific page requires a unique set of styles that should not affect the rest of the site.
*   **Pros:** Keeps all styles for a single page in one place without needing an extra file.
*   **Cons:** Does not scale. If you have a 10-page website, you would have to copy-paste these styles into every single file, making site-wide changes a nightmare.

```html
<head>
    <style>
        /* Internal Style: Affects this document only */
        body {
            background-color: #f4f4f4;
        }
        p {
            color: purple;
        }
    </style>
</head>
```

### 3. External Styling (The "Industry Standard")
External CSS involves writing all your styles in a separate `.css` file and "linking" it to your HTML documents using the `<link>` tag in the `<head>`.

*   **Scope:** Global. One CSS file can style an unlimited number of HTML pages across an entire website.
*   **Use Case:** The standard for all professional web development.
*   **Pros:** 
    *   **Maintainability:** Change one line in the CSS file, and the entire website updates instantly.
    *   **Performance:** Browsers "cache" (save) the external CSS file after the first load, making subsequent pages on your site load much faster.
    *   **Cleanliness:** Maintains a perfect "Separation of Concerns."
*   **Cons:** Requires an additional HTTP request to fetch the file (though this is mitigated by caching).

```html
<!-- External Style: The professional standard -->
<head>
    <link rel="stylesheet" href="styles.css">
</head>
```

---

### Summary: Comparison of Methods

| Method | Location | Scope | Maintainability | Best For... |
| :--- | :--- | :--- | :--- | :--- |
| **Inline** | Inside the HTML tag | Single element | Very Low | Quick JS overrides |
| **Internal** | Inside `<style>` in `<head>` | Single document | Medium | One-off landing pages |
| **External** | Separate `.css` file | Entire Website | **Very High** | **All professional projects** |

---

## CSS Selectors: The Targeting System

Selectors are the instructions that tell the browser which HTML elements to style. In CSS, not all selectors are created equal; they vary in their **specificity** (their "strength" or ability to override other rules).

### 1. Basic Selectors (Low Specificity)
These are the foundational selectors used for general styling.

*   **Universal Selector (`*`)**: Targets every single element on the page. Usually used for global resets.
    *   *Example:* `* { box-sizing: border-box; }`
*   **Element (Type) Selector**: Targets all elements of a specific HTML tag.
    *   *Example:* `p { color: blue; }` (Targets all `<p>` tags).
*   **Class Selector (`.`)**: Targets all elements that have a specific `class` attribute. Classes are **reusable** and can be applied to many elements.
    *   *Example:* `.highlight { background: yellow; }`
*   **ID Selector (`#`)**: Targets the single, unique element with a specific `id` attribute. IDs are **not reusable** on a single page.
    *   *Example:* `#main-header { font-size: 2rem; }`

### 2. Combinators (Relationship Selectors)
Combinators allow you to target elements based on how they are positioned in the **DOM tree** relative to other elements.

*   **Descendant Selector (`space`)**: Targets an element that is *anywhere* inside a specified parent, regardless of how deep it is nested.
    *   *Syntax:* `parent descendant`
    *   *Example:* `nav ul { list-style: none; }` (Targets all `<ul>` inside a `<nav>`).
*   **Child Selector (`>`)**: Targets only the **immediate** children of a parent. It will not select "grandchildren."
    *   *Syntax:* `parent > child`
    *   *Example:* `ul > li { margin: 5px; }` (Targets only `<li>` that are direct children of `<ul>`).
*   **Adjacent Sibling Selector (`+`)**: Targets an element that is the **very next** sibling of another element.
    *   *Syntax:* `element + sibling`
    *   *Example:* `h2 + p { margin-top: 0; }` (Targets only the first `<p>` that immediately follows an `<h2>`).
*   **General Sibling Selector (`~`)**: Targets **all** siblings that follow a specified element, even if they aren't immediately next to it.
    *   *Syntax:* `element ~ sibling`
    *   *Example:* `h2 ~ p { color: gray; }` (Targets all `<p>` tags that share the same parent as an `<h2>` and come after it).

### 3. Advanced & Compound Selectors
These allow for highly surgical targeting by combining multiple criteria.

*   **Compound Selector (`element.class`)**: Targets an element only if it matches both the tag and the class.
    *   *Example:* `p.note { font-style: italic; }` (Only targets `<p>` tags that also have the class `note`).
*   **Grouping Selector (`,`)**: Applies the same styles to multiple different selectors at once to keep code DRY (Don't Repeat Yourself).
    *   *Example:* `h1, h2, h3 { font-family: sans-serif; }`

### Summary: Selector Specificity & Strength

When multiple selectors target the same element, the browser uses the **Specificity Hierarchy** to decide which rule wins.

| Selector Type | Specificity Rank | Strength | Use Case |
| :--- | :--- | :--- | :--- |
| **Universal (`*`)** | 0 | Lowest | Global resets/defaults. |
| **Element (`p`)** | 1 | Low | General styling for all tags. |
| **Class (`.btn`)** | 10 | Medium | Most common way to style components. |
| **Compound (`p.note`)** | 11 | Medium-High | Targeting specific types of elements. |
| **ID (`#header`)** | 100 | High | Unique, one-of-a-kind elements. |
| **Inline Style** | 1000 | Highest | Emergency overrides/Dynamic JS. |

---

## CSS Motion: Transitions and Animations

In web development, motion is used to provide feedback, guide the user's eye, and make an interface feel "alive." CSS provides two distinct ways to create motion: **Transitions** (for simple state changes) and **Keyframe Animations** (for complex, continuous sequences).

### 1. CSS Transitions (State-to-State)
Transitions are used to smooth the change from one CSS property value to another. They are "reactive"—they only trigger when a state changes (e.g., when a user hovers over a button).

*   **The Concept:** If you change a button's color from blue to red on `:hover`, a transition tells the browser: *"Don't just snap to red; take 0.3 seconds to fade there."*
*   **The Four Components of a Transition:**
    1.  `transition-property`: Which property to animate (e.g., `background-color`, `opacity`, or `all`).
    2.  `transition-duration`: How long the change takes (e.g., `0.3s`, `500ms`).
    3.  `transition-timing-function`: The "acceleration" of the change (`ease`, `linear`, `ease-in`, `ease-out`).
    4.  `transition-delay`: How long to wait before starting the animation.

```css
/* Transition Example */
.button {
    background-color: blue;
    transition: background-color 0.3s ease; /* Smoothly fade the color */
}

.button:hover {
    background-color: red; /* The transition triggers here */
}
```

### 2. CSS Keyframe Animations (Complex Sequences)
Keyframes are used for more complex motion that doesn't necessarily rely on a user interaction. They allow you to define multiple stages of an animation.

*   **The Concept:** Unlike transitions, which only have a "Start" and an "End," keyframes allow you to define "Middle" stages (e.g., "At 50% of the way through, be blue; at 100%, be red").
*   **The Two-Step Process:**
    1.  **Define the `@keyframes`**: Create the timeline of styles.
    2.  **Apply the `animation` property**: Attach that timeline to an element.

```css
/* 1. Define the Timeline */
@keyframes slideAndFade {
    0% {
        /* Start: Positioned at the origin, completely invisible */
        transform: translateX(0);
        opacity: 0;
    }
    50% {
        /* Midpoint: Fully visible at the original position */
        opacity: 1;
    }
    100% {
        /* End: Moved 100px to the right and faded out again */
        transform: translateX(100px);
        opacity: 0;
    }
}

/* 2. Apply to the Element */
.box {
    width: 100px;
    height: 100px;
    background: coral;
    /* shorthand: name | duration | timing-function | iteration-count */
    animation: slideAndFade 3s ease-in-out infinite;
}
```

### 3. Essential Animation Properties

When working with Keyframes, these properties allow you to fine-tune the behavior:

*   **`animation-iteration-count`**: How many times the animation plays (`1`, `5`, or `infinite`).
*   **`animation-direction`**: Whether the animation plays forward, backward (`reverse`), or alternates back and forth (`alternate`).
*   **`animation-fill-mode`**: Defines what happens to the element when the animation ends. 
    *   `forwards`: The element stays in the final state of the animation.
    *   `backwards`: The element snaps back to the original state immediately.


### Summary: Choosing the Right Tool

| Feature | **Transitions** | **Keyframe Animations** |
| :--- | :--- | :--- |
| **Complexity** | Simple (A $\rightarrow$ B) | Complex (A $\rightarrow$ B $\rightarrow$ C $\rightarrow$ A) |
| **Trigger** | Requires a state change (e.g., `:hover`) | Can run automatically on page load |
| **Control** | Limited to start/end | Full control over every "frame" |
| **Best Use Case** | Button hovers, menu dropdowns | Loading spinners, entrance effects, looping backgrounds |

---

## Modern Layouts: Flexbox vs. CSS Grid

In modern web development, we have moved away from using floats or positioning for layout. Instead, we use two powerful systems: **Flexbox** (for one-dimensional alignment) and **CSS Grid** (for two-dimensional structures).

### 1. Flexbox (The 1D Layout Model)
Flexbox is designed for laying out items in a single dimension—either a **row** or a **column**. It is best used when you want to distribute space between items or align them along a single axis.

**Core Concept: The Relationship**
Flexbox relies on a parent-child relationship:
*   **Flex Container:** The parent element (where `display: flex;` is applied).
*   **Flex Items:** The direct children of that container.

#### Essential Container Properties
| Property | Function | Common Values |
| :--- | :--- | :--- |
| `flex-direction` | Defines the main axis (direction of items). | `row`, `column` |
| `justify-content` | Aligns items along the **main axis**. | `flex-start`, `center`, `space-between` |
| `align-items` | Aligns items along the **cross axis**. | `stretch`, `center`, `flex-end` |
| `gap` | Sets the spacing between items. | `1rem`, `20px` |

#### Essential Item Properties
*   **`flex: 1;`**: A shorthand that tells an item to grow and take up all available remaining space.

```css
/* Flexbox Example: A Navigation Bar */
.navbar {
    display: flex;
    justify-content: space-between; /* Logo on left, links on right */
    align-items: center;            /* Vertically centered */
    padding: 1rem;
}

.nav-links {
    display: flex;
    gap: 2rem; /* Consistent spacing between links */
}
```

### 2. CSS Grid (The 2D Layout Model)
CSS Grid is a two-dimensional system. It allows you to align items in both **rows and columns** at the same time. It is best used for the overall "skeleton" of a webpage.

**Core Concept: Tracks and Cells**
*   **Grid Tracks:** The columns and rows that make up the grid.
*   **Grid Cells:** The individual "boxes" created by the intersection of a row and a column.
*   **Fractional Unit (`fr`):** A special unit that represents a fraction of the available space in the grid container.

#### Essential Container Properties
| Property | Function | Example |
| :--- | :--- | :--- |
| `grid-template-columns` | Defines the number and width of columns. | `1fr 2fr` (One small, one large) |
| `grid-template-rows` | Defines the height of the rows. | `auto 100px` |
| `gap` | Sets the spacing between rows and columns. | `20px` |

```css
/* Grid Example: A Magazine-Style Layout */
.main-layout {
    display: grid;
    /* Create 3 columns: two equal, one double-sized */
    grid-template-columns: 1fr 1fr 2fr; 
    /* Create two rows: one for header, one for content */
    grid-template-rows: auto 1fr;
    gap: 20px;
}

.header {
    /* Make the header span across all 3 columns */
    grid-column: span 3;
}
```

### Summary: When to use which?

The "Golden Rule" of modern layout is to use the right tool for the dimension you are managing.

| Feature | **Flexbox** | **CSS Grid** |
| :--- | :--- | :--- |
| **Dimension** | **1D** (Row *or* Column) | **2D** (Row *and* Column) |
| **Logic** | **Content-First**: Items dictate their own size/position. | **Layout-First**: The container dictates the structure. |
| **Best For...** | Alignment, navbars, centering a single item, small components. | Full-page layouts, complex galleries, dashboard interfaces. |

**NOTE:** You don't have to choose one! A common professional pattern is to use **CSS Grid** to build the large-scale page structure (Header, Sidebar, Main, Footer) and then use **Flexbox** inside those sections to align the individual items (like links in the header).

---

## CSS Variables: Design Tokens & Theming

**CSS Variables** (officially called **Custom Properties**) allow you to store specific values—like colors, spacing, or font sizes—in a single location and reuse them throughout your entire stylesheet.

### 1. The Syntax: Declaration and Usage
Variables are defined using two unique symbols: `--` to declare them, and `var()` to use them.

*   **Declaration:** Must start with two dashes (`--`).
*   **Usage:** Must be wrapped in the `var()` function.

```css
/* 1. Declaration */
:root {
    --primary-color: #3498db;
    --accent-color: #e74c3c;
    --standard-padding: 1rem;
}

/* 2. Usage */
.button {
    background-color: var(--primary-color);
    padding: var(--standard-padding);
}

.card {
    border: 2px solid var(--primary-color);
}
```

### 2. The `:root` Scope (Global Variables)
While you can declare variables inside any selector, they are most commonly declared inside the `:root` pseudo-class.

*   **What is `:root`?** It is a high-specificity selector that represents the highest-level element in the document (the `<html>` tag).
*   **Why use it?** Variables declared in `:root` are **globally scoped**. This means they are available to every single element in your entire website.

---

### 3. Advanced Concepts: The Power of Variables

Variables are more than just "nicknames" for colors; they allow for dynamic, programmatic styling.

#### A. The "Fallback" Value
If a variable fails to load or isn't defined, you can provide a "fallback" value as a second argument in the `var()` function. This prevents your layout from breaking.

```css
/* If --brand-color is missing, it defaults to black */
.text {
    color: var(--brand-color, black);
}
```

#### B. Theming (Dark Mode)
The most powerful use case for CSS variables is **Theming**. Instead of rewriting every single CSS rule for "Dark Mode," you simply redefine the values of your variables within a specific class or media query.

```css
/* Default: Light Mode */
:root {
    --bg-color: #ffffff;
    --text-color: #333333;
}

/* Dark Mode: Redefine the same variable names */
@media (prefers-color-scheme: dark) {
    :root {
        --bg-color: #1a1a1a;
        --text-color: #f0f0f0;
    }
}

/* The actual component code never changes! */
body {
    background-color: var(--bg-color);
    color: var(--text-color);
}
```

---

### Summary: Variables vs. Hard-Coded Values

| Feature | Hard-Coded (e.g., `color: #3498db`) | CSS Variables (`var(--primary)`) |
| :--- | :--- | :--- |
| **Maintenance** | **Low**: Must find/replace every instance. | **High**: Change once, update everywhere. |
| **Consistency** | **Risky**: Easy to accidentally use `#3498dc`. | **Safe**: One source of truth. |
| **Theming** | **Impossible**: Requires massive rewrites. | **Easy**: Just swap the variable values. |
| **Readability** | **Low**: What does `#3498db` actually mean? | **High**: `--brand-blue` is self-explanatory. |

