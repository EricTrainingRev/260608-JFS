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

// if we turn the 2 into a number instead of a string we will get a type error
const solution = useAdditionOperator("one", "2");

console.log(solution);

// TS does have some implicit understanding of your data
let myName = "Billy Bob"; // TS knows this is a string
let myAge = 67; // TS knows this is a number
let isACarpenter = true; // TS knows this is a boolean

/*
    When using more complex types, like arrays that can hold different types of data, you should specify
    the type as part of the reference declaration

    Arrays have two ways of being type hinted: the preferred way is to put [] after the type the array stores,
    but you can also use generics
*/
const myCollection: string[] = []
myCollection.push("Sally");
// myCollection.push(37); this will not work since 37 is not a string

// this works but is not common for array declarations
const myNumbers: Array<number> = []

// Rule of thumb: if working with non strings/numbers/booleans, explicitly declare the type

const myObject = {
    name: "Timmy",
    age: 34
};

const myObject2 = {
    name: "Sarah",
    age: 76
}

/*
    The objects above are perfectly functional, but they have limited type hinting. If we wanted to specify
    that other objects we make about people moving forward have the same properties with the same types we
    would ideally type hint that we are working with the same objects. There are a few ways to do this
*/

const personWithRawTyping: {name: string, age: number} = {
    name: "Vaultus",
    age: 300
}

/* 

    A better way to type hint your objects is to declare an interface and then use the interface as your type 
    hint. By default all properties will be seen as required by the TS compiler, so if any of them are not
    set when you type hint an object you wil get an error. If a property is not always required you can put
    a ? at the end of it to specify the property is optional: if not included when you type hint an object
    you will not get an error

*/
interface Person {
    name: string,
    age: number,
    isCarpenter?: boolean // if we do not define this it will default to the "undefined" value
}

const anotherPerson: Person = {
    name: "Carter",
    age: 45
}

/*
    Interfaces are used to specify the shape of your objects, but they are not ideal for defining custom 
    types for your non-object data. If you ever want to create a custom type for non-objects you can do so
    with the type keyword
*/

// as cool as this would be, unless this is for a videogame people can not have pet raptors. We can use a
// custom type to specify what values a reference may hold
const myPet = "raptor"; // technically valid TS, not valid for our use case

// Here we declare that Pet can mean one of four animals below. Note we are specifying the actual values allowed
type Pet = "dog" | "cat" | "fish" | "horse";

/*
    Now that we are specifying our variable is a Pet we get intelisense telling us what string values we can
    use, and will get an error if we try to provide a value that is not part of the Pet type
*/
const anotherPet: Pet = "horse";

type Identifier = string | number;

// both examples below are valid because their data is either a string or number, which matches the Identifier type
const myId: Identifier = "This is valid";
const myOtherId: Identifier = 1001;

// if you ever need to "narrow" your type you can use the "as" keyword to specify which type to use
function useIdAsString(identifier: Identifier){
    const idAsString = identifier as string;
    console.log(idAsString.toUpperCase());
}

/*
    You can freely mix and match your custom interfaces and types together

    You can also freely redeclare your interfaces: when TS checks your code it will merge all your
    interface declarations together into a single interface
*/

interface Person {
    myPet?: Pet
}

const newPerson: Person = {
    name: "Charles", // this comes from the first Person interface declaration
    age: 12, // this also comes from the first Person interface declaration
    myPet: "fish" // this comes from the second Person interface declaration. Notice we also use our custom Pet type
}

