# MIPS Conventions

## Abstract
- When generating the mips code, we use some conventions when naming labels, globals variables etc.
- In this document we'll explicitly state each convention and its usage.
- Sometimes we might not use any convention, and just call `IR.getFreshLabel()`.

## Conventions
- **Functions:**
  - Starting Labels:
    - `func_{name}` - a function named `{name}` declared in global context.
    - `method_{class-name}_{name}` - a method named `{name}` declared in the context of class `{class-name}`.
  - Epilogue Labels:
    - `{f_label}_epilogue` - a label for the epilogue of the function (or method) that was labeled by `{f_lablel}`.


- **Variables:**
  - `global_{name}` - a variable named `{name}` declared in the **global** context.

- **Strings:**
  - TODO @Omer 