interface typesObject{
    slot: number,
    type:{
        name: string,
        url: string
    }
}

interface movesObject{
    move:{
        name: string 
    }
}

interface spritesObject{
    back_default:string,
    back_shiny:string,
    front_default:string,
    front_shiny:string
}

interface Pokemon{
    name:string,
    sprites:spritesObject,
    types:Array<typesObject>,
    moves:Array<movesObject>
}