import 'core-js/es6/map';
import 'core-js/es6/set';

import * as React from 'react';
import * as ReactDom from 'react-dom';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import Footer from './components/Footer';
import Page404 from './components/Page404';
import 'bootstrap/dist/css/bootstrap.css';
import { Col } from 'reactstrap';
import InstanceList from './components/instancelist';
import Addinstance from './components/Addinstance';
import Deleteinstance from './components/Deleteinstance';



const routes = (
    <BrowserRouter>
    <div>
    <Footer color="dark"/>
    <br/>
        <Switch>
            <Route path={`/oraclemetricscollector/index`} component={InstanceList} exact={true}/>
            <Route path={`/list`} component={InstanceList} />
            <Route path={`/add`} component={Addinstance} />
            <Route path={`/del`} component={Deleteinstance} />
            <Route render={(props)=><Page404 />} />
        </Switch>
    </div>
    </BrowserRouter>
);
ReactDom.render(
    routes,
    document.getElementById("App")
) 
// path={props.location.pathname}

