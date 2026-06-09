# HTML Quick Reference Guide

## Overview of HTML

HTML (HyperText Markup Language) is the fundamental structural layer of the web. Rather than being a programming language that performs logic, it is a **markup language** used to annotate text, images, and other content to define their role and meaning within a document.

### Web Separation of Concerns
To understand HTML, it must be viewed as one part of a three-part ecosystem. Modern web development relies on the "Separation of Concerns" to keep code organized and maintainable:

1.  **HTML (Structure):** The "skeleton." It defines *what* the content is (e.g., "This is a heading," "This is a button").
2.  **CSS (Presentation):** The "skin/clothing." It defines *how* the content looks (e.g., colors, fonts, layout).
3.  **JavaScript (Behavior):** The "muscles." It defines *how* the content acts (e.g., what happens when a button is clicked).

### The DOM (Document Object Model)
When a browser reads your HTML, it doesn't just display text; it builds a live, hierarchical model called the **DOM**. 

*   **The Tree Structure:** The browser converts HTML tags into a "tree of objects" (nodes). Every element is a branch or a leaf in this tree.
*   **Interactivity:** Because the DOM is a structured object model, programming languages like JavaScript can "reach into" the tree to add, remove, or change elements, styles, and content in real-time without reloading the page.

**Key Conceptual Takeaways:**
*   **Structure over Style:** HTML should define the *meaning* of content, not its appearance.
*   **Machine Readable:** Well-structured HTML is not just for humans; it provides a roadmap for Search Engines (SEO) and Assistive Technologies (Accessibility).
*   **The Live Document:** The DOM is the bridge that allows static HTML to become a dynamic, interactive application.

---

## HTML Document Structure

Every HTML document follows a standardized hierarchy. This structure is divided into two distinct functional layers: the **Metadata Layer** (instructions for the browser) and the **Content Layer** (what the user actually sees).

```html
<!DOCTYPE html>
<html lang="en">
    <head>
        <!-- The Metadata Layer (The "Brain") -->
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Page Title</title>
    </head>
    <body>
        <!-- The Content Layer (The "Body") -->
        <h1>Main Heading</h1>
        <p>Visible content goes here.</p>
    </body>
</html>
```

### Breakdown of the Hierarchy

#### 1. The Document Declaration
*   `<!DOCTYPE html>`: This is not an HTML tag, but a "preamble" that tells the browser to render the page in **Standards Mode** (using the modern HTML5 specification). Without this, browsers may enter "Quirks Mode," which can break your layout.

#### 2. The Root Element
*   `<html lang="en">`: The container for everything else. The `lang` attribute is an accessibility feature, telling screen readers which language to use for pronunciation.

#### 3. The Metadata Layer (`<head>`)
The `<head>` contains information about the page that is **not displayed directly** in the main browser window. It acts as the "configuration center" for the document.
*   **Character Encoding (`<meta charset="UTF-8">`):** Tells the browser how to interpret text characters (essential for displaying symbols and non-English characters correctly).
*   **Viewport Settings (`<meta name="viewport">`):** It tells the browser how to scale the page on mobile devices.
*   **Document Title (`<title>`):** Sets the name of the page shown on the browser tab and is the primary link text used by Search Engines.
*   **External Links:** This is also where you "hook" in your CSS files and JavaScript logic.

#### 4. The Content Layer (`<body>`)
The `<body>` contains everything the user **interacts with**. If a user can see it, click it, or read it on the page, it should reside within the body tags.

**Conceptual Summary:**
| Section | Role | Visibility |
| :--- | :--- | :--- |
| **`<!DOCTYPE>`** | Document Type Declaration | Invisible |
| **`<head>`** | Configuration & Metadata | Invisible |
| **`<body>`** | Structure & Content | Visible |

---

## HTML Tags: Syntax and Semantics

Tags are the building blocks of HTML. While they provide the syntax for the document, their most important role is to provide **meaning** to the content.

### Tag Syntax
Most HTML elements consist of three parts: an opening tag, the content, and a closing tag.

*   **Container Tags (Paired):** Used to wrap content.
    *   *Syntax:* `<tagname>Content goes here</tagname>`
    *   *Example:* `<p>This is a paragraph.</p>`
*   **Void Tags (Self-Closing):** Used for elements that do not wrap content, such as images or line breaks. They do not require a closing tag.
    *   *Syntax:* `<tagname>` or `<tagname />`
    *   *Example:* `<img src="image.jpg">`, `<br>`, `<hr>`

---

### The Concept of Semantics
The most important distinction in modern HTML is between **Semantic** and **Non-Semantic** tags.

#### 1. Semantic Tags (Meaningful)
A semantic tag clearly describes its purpose to both the browser and the developer. When you use semantic tags, you are providing a "map" of your content's importance.
*   **Why it matters:** Semantic HTML is the foundation of **Accessibility (A11y)** (helping screen readers navigate) and **SEO** (helping search engines understand your page structure).
*   **Common Examples:**
    *   `<header>`: Defines the introductory content or navigation.
    *   `<nav>`: Defines a set of navigation links.
    *   `<main>`: Specifies the unique, primary content of the document.
    *   `<article>`: Represents a self-contained composition (like a blog post).
    *   `<footer>`: Defines the bottom section of a page or section.

#### 2. Non-Semantic Tags (Generic)
Non-semantic tags tell the browser nothing about their content. They are used purely as "containers" for styling or grouping purposes.
*   **Common Examples:**
    *   `<div>`: A generic block-level container.
    *   `<span>`: A generic inline container.

### Summary: Choosing the Right Tag

| Goal | Approach | Example |
| :--- | :--- | :--- |
| **Define Structure** | Use **Semantic** tags to describe the "what." | `<nav>...</nav>` |
| **Group for Styling** | Use **Non-Semantic** tags for layout/CSS. | `<div class="wrapper">...</div>` |
| **Add Content** | Use the specific tag that matches the data type. | `<h1>...</h1>` (Heading) |

---

## Common Tags: Functional Categorization

Rather than memorizing tags in isolation, it is more effective to view them by their functional roles within a web interface.

### 1. Text Content & Hierarchy
These tags define the structure of written information. Proper use of these tags is critical for document flow and accessibility.

*   **Headings (`<h1>` to `<h6>`):** Define the hierarchy of information. 
    *   `<h1>` is the most important (usually the page title) and should be used sparingly.
    *   Subsequent levels (`<h2>` through `<h6>`) create a logical outline. 
*   **Paragraphs (`<p>`):** The standard container for blocks of text.
*   **Formatting (Inline):** 
    *   `<strong>`: Indicates strong importance (usually rendered in **bold**).
    *   `<em>`: Indicates emphasis (usually rendered in *italics*).

### 2. Navigation & Hyperlinks
The "Hypertext" in HTML comes from the ability to link documents together.

*   **Anchors (`<a>`):** Creates a hyperlink to another page, file, or location.
    *   Requires the `href` (Hypertext Reference) attribute to function.
    *   *Example:* `<a href="https://google.com">Search</a>`

### 3. Media & Embedded Content
These tags allow you to bring non-textual elements into the document.

*   **Images (`<img>`):** Embeds an image into the page.
    *   *Note:* This is a **void tag** (self-closing) and requires an `alt` attribute for accessibility.
*   **Line Breaks (`<br>`) & Horizontal Rules (`<hr>`):** Used for structural breaks in text or thematic separations.

### 4. Lists
Lists are used to group related items, whether they are ordered or unordered.

*   **Unordered Lists (`<ul>`):** A collection of items where the order doesn't matter (typically displayed with bullet points).
*   **Ordered Lists (`<ol>`):** A collection of items where the sequence is important (typically displayed with numbers).
*   **List Items (`<li>`):** The actual content items that must live inside a `<ul>` or `<ol>`.

### 5. Structural Containers (Layout)
These are used to group other elements together to create layouts or apply CSS styles.

*   **Block Containers (`<div>`):** A generic container used to group large sections of content. It always starts on a new line.
*   **Inline Containers (`<span>`):** A generic container used to wrap small portions of text or elements within a line. It does *not* start on a new line.

### Quick Reference Table

| Category | Tags | Primary Use Case |
| :--- | :--- | :--- |
| **Hierarchy** | `<h1>`-`<h6>` | Establishing a logical outline. |
| **Text** | `<p>`, `<strong>`, `<em>` | Structuring written content. |
| **Links** | `<a>` | Connecting the web (navigation). |
| **Lists** | `<ul>`, `<ol>`, `<li>` | Grouping related items. |
| **Layout** | `<div>`, `<span>` | Organizing the DOM for CSS/JS. |
| **Media** | `<img>` | Adding visual context. |

---

## Elements and Attributes: Identity and Information

To master HTML, you must understand the distinction between an **Element** (the object itself) and an **Attribute** (the data that describes that object).

### 1. The Element: The "What"
An **Element** is a complete component of the web page. It is the combination of the opening tag, the content, and the closing tag. 

*   **The Concept:** Think of an element as a physical object in a room. A "Chair" is an element. It has a presence and occupies space in the room (the DOM).

### 2. The Attribute: The "How" or "Which"
**Attributes** provide additional information or instructions about an element. They are always placed within the **opening tag** and usually consist of a name and a value (e.g., `name="value"`).

*   **The Concept:** If the "Chair" is the element, the attributes are its properties: `color="red"`, `material="wood"`, or `position="corner"`. These attributes don't change the fact that it's a chair, but they define its specific characteristics.

### Essential Attribute Categories

Attributes generally fall into three functional categories: **Functional**, **Identification**, and **Accessibility**.

#### A. Functional Attributes
These provide the specific data required for the element to perform its primary job.
*   `href`: (For `<a>`) The destination URL.
*   `src`: (For `<img>` or `<script>`) The path to the resource.
*   `type`: (For `<input>`) Defines what kind of data the input expects (e.g., `text`, `checkbox`).

#### B. Identification Attributes (The "Hooks")
These are used to uniquely identify or group elements so that **CSS** can style them and **JavaScript** can manipulate them.
*   `id`: A **unique** identifier for a single specific element on the entire page. (e.g., `<div id="main-nav">`). 
    *   *Rule:* An ID must never be reused on the same page.
*   `class`: A non-unique identifier used to group multiple elements together. (e.g., `<button class="btn-primary">`).
    *   *Rule:* Multiple elements can share the same class.

#### C. Accessibility Attributes
These provide context to machines and assistive technologies that cannot "see" the visual design.
*   `alt`: (For `<img>`) Provides a text description of an image for screen readers and as a fallback if the image fails to load.
*   `placeholder`: (For `<input>`) Provides a hint to the user about what to type.
*   `required`: (For `<input>`) Tells the browser the field must be filled out before a form can be submitted.

### Summary of Syntax

| Attribute | Target Element | Purpose |
| :--- | :--- | :--- |
| `href` | `<a>` | Defines the link destination. |
| `src` | `<img>`, `<script>` | Defines the source path of a file. |
| `alt` | `<img>` | Provides text for accessibility/SEO. |
| `id` | Any | Unique "hook" for CSS/JS. |
| `class` | Any | Grouping "hook" for CSS/JS. |

---

## Inline and Block Elements: The Flow of the Document

In HTML, every element has a default display behavior that determines how it sits in relation to other elements. This is known as the **Document Flow**. Elements are categorized into two primary types: **Block-level** and **Inline-level**.

### 1. Block-Level Elements (The "Stackers")
Block-level elements are the structural heavy-lifters. They are designed to hold large chunks of content and create the primary layout of the page.

*   **Behavior:** 
    *   They always start on a **new line**.
    *   They automatically take up the **full width** available (stretching to the left and right as far as possible).
    *   They create a "stacking" effect, where each new block sits below the previous one.
*   **Usage:** Used for structural sections, headings, and paragraphs.
*   **Common Examples:** `<div>`, `<h1>` through `<h6>`, `<p>`, `<ul>`, `<ol>`, `<section>`, `<header>`, `<footer>`.

### 2. Inline-Level Elements (The "Flow-ins")
Inline elements are designed to sit inside other elements without disrupting the flow of text or content.

*   **Behavior:** 
    *   They **do not** start on a new line.
    *   They only take up as much width as their **content requires**.
    *   They "flow" horizontally within the line of the parent element.
*   **Usage:** Used to style or add functionality to specific pieces of content *within* a larger block.
*   **Common Examples:** `<span>`, `<a>`, `<strong>`, `<em>`, `<img>`, `<button>`.

### The Concept of Containment (Nesting Rules)

Understanding the relationship between these two types is vital for building stable layouts. A fundamental rule of "correct" HTML is the hierarchy of containment:

1.  **Block elements can contain both Block and Inline elements.** 
    *   *Example:* A `<div>` (block) can contain a `<p>` (block) which contains a `<span>` (inline).
2.  **Inline elements should generally ONLY contain other Inline elements.** 
    *   *Rule:* You should avoid putting a Block element (like a `<div>` or `<h1>`) inside an Inline element (like a `<span>` or `<a>`). Doing so breaks the natural flow and can lead to unpredictable styling and accessibility issues.

### Visualizing the Difference

Imagine a page as a series of physical containers:

| Feature | Block-Level | Inline-Level |
| :--- | :--- | :--- |
| **Visual Analogy** | A shipping crate (takes up space/stacks). | A piece of tape (wraps around an object). |
| **Line Break** | Forced (Starts a new line). | None (Stays in the current line). |
| **Width** | Fills the container (100%). | Shrinks to fit content. |
| **Typical Role** | **Structure** and Layout. | **Detail** and Content modification. |

#### Code Example:
```html
<!-- BLOCK-LEVEL FLOW -->
<div style="border: 1px solid black;">
    <h1>Heading (Block)</h1>
    <p>This is a paragraph (Block).</p>
</div>

<!-- INLINE-LEVEL FLOW -->
<p>
    This is a paragraph containing 
    <span style="color: blue;">blue text (Inline)</span> 
    and <strong>bold words (Inline)</strong>.
</p>
```

---

## Input Elements and Types: Data Collection

Input elements are the primary way users interact with a web application by providing data. This interaction is managed through the `<form>` element, which acts as a container for all user inputs and defines how that data is sent to a server.

### 1. The Importance of the `<label>` (The Accessibility Link)
An `<input>` element by itself is just a "box" on a screen. To make it meaningful, it must be paired with a `<label>`. 

*   **Accessibility:** Screen readers use labels to tell visually impaired users what information is being requested.
*   **Usability:** A properly linked label increases the "click target." If a user clicks the text of the label, the browser automatically focuses on the corresponding input.
*   **The Connection:** You connect a label to an input using the `for` attribute on the label and a matching `id` on the input.

```html
<!-- The CORRECT way to associate a label with an input -->
<label for="user-email">Email Address:</label>
<input type="email" id="user-email" name="email">
```

### 2. Input Types: Defining the Data Contract
The `type` attribute on an `<input>` tag is critical because it tells the browser two things: 
1. **The Keyboard/UI:** On mobile devices, choosing `type="number"` will automatically trigger a numeric keypad instead of a full keyboard.
2. **Validation:** The browser can perform "client-side validation." For example, if `type="email"` is used, the browser will prevent form submission if the input doesn't look like a valid email address.

#### Common Input Types:

**Text-Based Inputs:**
*   `text`: The standard single-line text field.
*   `password`: Masks the characters entered (essential for security).
*   `email`: Expects an email format; triggers specific mobile keyboards.
*   `number`: Restricts input to numeric values; often includes "spinner" arrows.

**Selection-Based Inputs:**
*   `checkbox`: Allows the user to select one or more options from a list (Toggle on/off).
*   `radio`: Allows the user to select **exactly one** option from a group. 
    *   *Crucial:* All radio buttons in the same group must share the same `name` attribute to work correctly.

**Action-Based Inputs:**
*   `submit`: A button that, when clicked, packages the form data and sends it to the destination defined in the `<form>` tag.

### 3. The Anatomy of a Form

A complete, accessible form requires three things: a container (`<form>`), a way to identify the data (`name` attribute), and a way to label the data (`<label>`).

```html
<form action="/submit-data" method="POST">
    <!-- Grouping related inputs is good practice -->
    <div class="form-group">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required>
    </div>

    <div class="form-group">
        <label for="user-pass">Password:</label>
        <input type="password" id="user-pass" name="password" required>
    </div>

    <div class="form-group">
        <input type="checkbox" id="terms" name="terms" required>
        <label for="terms">I agree to the terms and conditions</label>
    </div>

    <input type="submit" value="Create Account">
</form>
```

### Summary Table of Input Logic

| Attribute | Role | Why it matters |
| :--- | :--- | :--- |
| `type` | **Behavior** | Defines the UI (keyboard) and basic validation. |
| `id` | **Linkage** | Connects the input to a `<label>` for accessibility. |
| `name` | **Data Identity** | The "Key" sent to the server (e.g., `username=John`). |
| `value` | **Data Content** | The actual data stored in the field. |
| `required` | **Constraint** | Prevents empty submissions (Client-side validation). |