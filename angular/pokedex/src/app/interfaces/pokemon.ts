/*
    We know that the PokeAPI returns a significant ammount of information about
    any pokemon queried. For our purposes we only want a bit of that data, the
    name of the pokemon and some of its sprites. We can setup our Pokemon
    interface to store only the data that we want from the PokeAPI, which means
    when we go to query the data from the api we don't have to manually extract
    these specific fields from the response body: the http client can be provided
    this interface and extract the data for us
*/
export interface Pokemon {
    name: string,
    sprites: {
        back_default: string,
        front_default: string
    }
}
