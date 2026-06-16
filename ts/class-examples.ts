/*
    The syntax for working with classes in TypeScript is very similar to
    what you would expect wtih any other programming language: you declare
    the class and give it a name, then in curly brackets declare its 
    fields, methods, and constructor
*/

class MyExampleClass {
    exampleField: string

    constructor(exampleField: string){
        this.exampleField = exampleField;
    }

    getExampleField(){
        return this.exampleField;
    }
}

const myExampleClassObject = new MyExampleClass("the parameter");

console.log(myExampleClassObject.exampleField);
console.log(myExampleClassObject.getExampleField())

/*
    Classes in TS support three access modifiers in classes:
    - public -> this is the default modifier, can be accessed anywhere
    - protected -> can be accessed in the class and sub-classes
    - private -> can be accessed within the class only

    This modifiers can be added to both fields and methods
*/

class AccessModifiersExample {
    // only publicField can be accessed outside of the class
    public publicField: string;
    // if we had a class extend this one this would be available in the child
    protected protectedField: string;
    // must use a getter/setter to access this outside the class
    private privateField: string;

    constructor(){
        this.publicField = "This field is public";
        this.protectedField = "This field is protected";
        this.privateField = "This field is private";
    }
}

const accessExample = new AccessModifiersExample();
accessExample.publicField;
// accessExample.protectedField;
// accessExample.privateField;

/*
    TypeScript offers a convinience feature for declaring and initializing
    fields of a class: in the constructor you can add an access modifier
    to the parameter and TS will recognize that the parameter needs to
    initialize the value for a field of the class. This lets you both
    declare the field while setting the code for your constructor
*/

class ConvinienceClass {

    // when possible, prefer this syntax for your class code
    constructor(private  fieldOne: string, public fieldTwo: string){}

    // note intellisense gives no warnings here: fieldOne is recognized
    getFieldOne(){
        return this.fieldOne;
    }

}

const convinienceObject = new ConvinienceClass("First field", "Second field");

convinienceObject.getFieldOne();
convinienceObject.fieldTwo;

/*
    TS supports Class inheritance just like regular JS. You use the extends
    keyword to indicate what class is inherited from. Single inheritance is supported
*/

class ParentClass {
    constructor(protected parentField: string){}
}

class ChildClass extends ParentClass {

    constructor(public childField: string, parentField: string){
        super(parentField)
    }

    getParentField(){
        return this.parentField;
    }

}

const childObject = new ChildClass("Child value", "Parent value");

childObject.childField;
childObject.getParentField();

/*
    Classes can implement interfaces to determine their starting "shape".
    This can be useful for pre-determing what properties the class should
    have

    Note TS interfaces are a "bottom-out" limit: they define what minimum is needed for your class to
    be implemented correctly. You can allways add on more to your classes that implement interfaces.

    Note multiple interfaces can be implemented
*/

interface BasicShape {
    interfacePropertyOne: string,
    interfacePropertyTwo: string
}

interface SecondInterface {
    // just adding to show multiple interfaces can be implemented
}

class InterfaceImplementor implements BasicShape, SecondInterface {
    classSpecificField: number;
    constructor(public interfacePropertyOne: string, public interfacePropertyTwo: string){
        this.classSpecificField = 0;
    }
    
}