import { Routes } from '@angular/router';
import { PokeHome } from './components/poke-home/poke-home';

/*
    This is where we control what routes are available in our application. This
    gets read by the rouer service and gives our application the ability to 
    change the view to accomodate whatever routes need to be viewed
*/

export const routes: Routes = [
    {
        /*
            This is a minimum Route object: you want to specify at least
            the path for the route and the component to be rendered when
            the route is utilized
        */
        path: 'home',
        component: PokeHome
    },
    {
        /*
            This route object provides instructions for Angular to redirect a
            user to the home route when they visit the webpage. Note the pathMatch
            property: without this ANY route we try to visit in our app would
            redirect to the home route
        */
        path:'',
        redirectTo:'home',
        pathMatch:'full'
    }
];
