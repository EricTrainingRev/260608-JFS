# Boundary Value Analysis Example

## User Story
Users should be able to withdraw their money from their bank account
- Users should not be able to overdraw from their account
- Users should not be able to trick the system into giving them money by withdrawing negative ammounts

precondition: account has 100$

|Lower invalid bound|lower valid bound|upper valid bound|upper invalid bound|
|-|-|-|-|
|-1|0|100|101|


As seen above, when doing boundary value analysis you want to test the "boundaries"
- make sure the lower and upper bounds are allowed
- make sure just outside the upper and lower bounds are not allowed

if both those conditions are met, you can be confident the guardrails you put in place are working correctly without the need to test all or many of the values within those "in" and "out" ranges