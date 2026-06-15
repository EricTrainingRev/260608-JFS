// Any code you could write in a JS file is perfectly valid for a TS file
console.log("Hello world!");

/*
    The benefit of typescript is the ability to specify what type of data you
    want to use for your variables, functions, etc. use a colon and then type
    the type you want the reference to be to indicate for the rest of your
    code what type the reference holds

    Keep in mind, the type saftey provided by TypeScript only exists while
    your code is in ts form: as soon as it is compiled into JS all the type
    hints you have added are removed from the code since they are not
    supported in JS. This means the type saftey features of TS are 
    compile-time saftey, not run-time saftey
*/
function useAdditionOperator(paramOne: string, paramTwo: string){
    return paramOne + paramTwo;
}

const solution = useAdditionOperator("one", 2);

console.log(solution);