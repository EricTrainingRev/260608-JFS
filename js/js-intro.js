// JS provides you with the typical datatypes you would expect to see

// note this is a valid function call, even though myBasicFunction is defined near the bottom of the
// script. The function is hoisted, meaning it is initialized before the rest of the script execution happens
myBasicFunction();

// this is valid due to the variable being hoisted
console.log(myHoistedVariable);

/*
    const should be your default reference declaration keyword: it tells the
    interpreter you are creating a reference variable and that reference should
    not be allowed to change
*/
const myNum = 10;
// myNum = 15; this will cause a TypeError

/*
    If you know your value will need to change use the let keyword instead: this
    still initializes a variable but allows you to change its value
*/

let myName = "Billy";
myName = "Billy Bob";

/*
    There is technically a third way to initialize variables by using the
    var keyword. This is explicitally not recommended in modern JS due to 
    the fact the var keyword makes the interpreter "hoist" the variable
    declaration
*/

var myHoistedVariable = "This gets hoisted";


/*
    JavaScript has 4 scopes that control where a variable can be referenced
    in a valid way:
    - global -> can be referenced anywhere in your script
    - function -> can be referenced in your function
    - block -> can be referenced in a code block (between {})
    - lexical -> an inner function can access the function block of its parent function
*/

// this is a global variable: it can be referenced anywhere after its initialization
const myGlobal = "This can be accessed anywhere";

function myFunction(){
    // this is function scope: it can be accessed anywhere within the function after initialization
    let myFuncVar = 0
    console.log(myGlobal);
    console.log(myFuncVar);
    if(true){
        // this is block scope: it only exists for the duration of the code block
        const myBlockVar = "This can be accessed within the block";
        console.log(myBlockVar);
    }
    // console.log(myBlockVar); this will cause an error

    function innerFunc(){
        // This inner function has access to the function scope of the parent function:
        // this access to the parent is what is known as "lexical scope"
        console.log("myFuncVar access count: " + ++myFuncVar);
    }

    return innerFunc;
}

const extractedFunction = myFunction();

extractedFunction();
extractedFunction();
extractedFunction();


/*
    There are quite a few ways of creating and using functions in JavaScript, we will take a look at some of
    them here
*/

// the most basic way: declar it, write the code to execute, then call it
function myBasicFunction(){
    console.log("This is a basic function");
}
myBasicFunction();

// functions can have parameters
function myFunctionWithParameters(paramOne, paramTwo){
    return paramOne - paramTwo;
}

/*
    The code below is valid JavaScript: the interpreter will auto-coerce the 10 int into a string and then
    concatenate the two string values together. If a non-addtion operation is performed then the values will
    be coerced into numbers and the mathematical operation will be performed. If a value can not be coerced
    into a number then the value NaN (Not a Number) is used. This means the operation will "succeed" but realistcally
    this is a silent failure, and can be very difficult to debug
*/
console.log(myFunctionWithParameters(10,"not a number"));

