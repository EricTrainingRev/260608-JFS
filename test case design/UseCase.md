# UseCase example

## User Story
Users should be able to register an account with valid credentials

### How can a user go about fulfilling this user story?
- happy path (positive tests)
    - User provides valid credentials and their account is created
        - preconditions:
            - no account
            - on the registration page
            - ui supports the portal
            - etc.
        - inputs:
            - valid username
            - valid password
        - actions: (level of testing will change what actions must be taken)
            - user opens registration page
            - user submits credentials for registration process
        - expected result:
            - user is given a success message
- sad path (negative tests)
    - User provides an invalid username and is told why it is invalid
        - preconditions:
            - see happy path conditions
        - inputs:
            - invalid username
            - valid password
        - actions:
            - see happy path
        - expected result:
            - user is informed their username is invalid and prompted to try again
    - User provides an invalid password and is told why it is invalid
        - preconditions:
            - see happy path conditions
        - inputs:
            - valid username
            - invalid password
        - actions:
            - see happy path
        - expected result:
            - user is informed their password is invalid and prompted to try again