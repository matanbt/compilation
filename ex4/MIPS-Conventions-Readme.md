# MIPS Conventions

## Abstract
- When generating the mips code, we use some conventions when naming labels, globals variables etc.
- In this document we'll explicitly state each convention and its usage.
- Sometimes we might not use any convention, and just call `IR.getFreshLabel()`.

## Conventions
- **Labels:**
  -**Functions**:
     - Starting Labels:
        - `func_{name}` - a function named `{name}` declared in global context.
        - `method_{class-name}_{name}` - a method named `{name}` declared in the context of class `{class-name}`.
          Important Note: In the case of `class B extends A`, class B might have method with the prefix `method_A_...`, 
          these were inherited from A.
      - Epilogue Labels:
        - `{f_label}_epilogue` - a label for the epilogue of the function (or method) that was labeled by `{f_lablel}`.
     - *System Labels (const labels that we create):*
       Starts with `Label_`
       e.g.: `Label_string_access_violation`

- **Variables:**
  - `global_{name}` - a variable named `{name}` declared in the **global** context.

- **Strings:**
  - `str_global_{string-name}` - contains the string value of the **global** {string-name} variable
  - `str_{string-name}_{unique-number}` - contains the string value of some **local** {string-name} variable
  - `string_` -  prefix for strings used by the compiler for error messages.
  - `str_class_{class name}_field_{field name}` - contains the string value of {field name} string field in {class name} class 

- **Classes:**
  - `vt_{class name}` - the vtable of class `{class name}`.
