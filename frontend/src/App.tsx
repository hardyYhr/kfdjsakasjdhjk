import React from "react";
import "./App.css";
import Home from "./Components/Home/Home";
import Login from "./Components/Login/Login";
import Employees from "./Components/Employees/Employees";
import Navbar from "./Components/Navbar/Navbar";
import Departments from "./Components/Departments/Departments";
import DepartmentHomePageTemplate from "./Components/Departments/DepartmentHomePageTemplate";
import DataInput from "./Components/Departments/DataInput";
import Messages from "./Components/Messages/Messages";
import Leaderboard from "./Components/Leaderboard/Leaderboard";
import Api from "./API/Api";

import {
  BrowserRouter as Router,
  Route,
  Switch,
  Redirect,
  Link,
} from "react-router-dom";

type DepartmentType = {
    id: number,
    name: string,
    blurb: string
}

class App extends React.Component<any, any> {
  constructor(props: any) {
    super(props);

    this.state = {
      loggedIn: false,
    };


  }

  private handleLoggedIn = () => {
    this.setState({ loggedIn: true });
  };


  render() {

    return (
      <>
        <Router>
          {this.state.loggedIn ? <Navbar /> : <Redirect to="/login" />}
          
          
          <Switch>
            <Route exact path="/">
              <Home />
            </Route>
            <Route exact path="/login">
              <Login onLoggedIn={this.handleLoggedIn} />
            </Route>

            <Route path="/departments/:departmentID/employees">
              <Employees />
            </Route>

            <Route path="/departments/:departmentID/datainput" component={DataInput} />
              
            <Route exact path="/departments">
              <Departments />
            </Route>

            <Route exact path="/departments/:departmentID" component={DepartmentHomePageTemplate} />

            <Route path="/messages" component={Messages} />

            <Route path="/leaderboard" component={Leaderboard} />
            
          </Switch>
        </Router>
      </>
    );
  }
}

export default App;
