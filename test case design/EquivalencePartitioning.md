# Equivalence Partitioning

## User Story
Users should be able to register an account
- usernames should have a lowercase, uppercase, and special character(shift + number on keyboard)

|valid username|missing lowercase|missing uppercase|missing special character|
|-|-|-|-|
|Admin!|ADMIN!|admin!|Admin|

Note this is not exhaustive: we let one value represent the whole of the class. This gives you
a really good indicator if you are accomodating the requirements of your feature/userstory, but 
there is always room to add more tests if you have time. This is particularly useful with automated
testing where you can continuously add on more tests to get more specific information over time when you have the avialability to create more tests