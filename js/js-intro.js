// JS provides you with the typical datatypes you would expect to see

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