const MESSAGE = "Hello world!";

class Messenger {
    constructor(private parcel: string){}

    deliverMessage(){
        return this.parcel;
    }
}

const messenger = new Messenger(MESSAGE);

console.log(messenger.deliverMessage());