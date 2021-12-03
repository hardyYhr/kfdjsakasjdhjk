import * as React from "react";
import { Redirect, RouteComponentProps, withRouter } from "react-router-dom";
import Api from "../../API/Api";
import axios from "axios";
import "./Login.css";
import { width } from "@mui/system";
import { Thermostat } from "@mui/icons-material";

type Property = {
  onLoggedIn?(): void;
} & RouteComponentProps;

type State = {
  username: string;
  password: string;
  text?: string;
  // Used for spinner state control
  loggingIn: boolean;
  loggedIn: boolean;
  authResCode: number;
  redirectDepId: number;
  redirectDepName: string;
  // Used to automatically center login form.
  windowHeight: number;
  onLoggedIn?(): void;
};

class WrappedLogin extends React.Component<Property, State> {
  private mounted: boolean;
  constructor(props: Property) {
    super(props);
    this.mounted = true;

    this.onLoggedIn = props.onLoggedIn;
    this.state = {
      username: "",
      password: "",
      loggingIn: false,
      loggedIn: false,
      authResCode: 0,
      redirectDepId: -1,
      redirectDepName: "",
      windowHeight: window.innerHeight,
    };

    window.onresize = () => {
      this.setState({ windowHeight: window.innerHeight });
    };
  }

  private onLoggedIn?: () => void;

  private handleUsernameChange = (event: React.FormEvent<HTMLInputElement>) => {
    this.setState({ username: event.currentTarget.value });
  };

  private handlePasswordChange = (event: React.FormEvent<HTMLInputElement>) => {
    this.setState({ password: event.currentTarget.value });
  };

  private handleOnClick = (event: React.FormEvent<any>) => {
    event.preventDefault();
    event.stopPropagation();
    this.setState({ authResCode: 0 });
    this.login();
  };

  private login = () => {
    if (this.state.username.length == 0 || this.state.password.length == 0) {
      this.setState({ loggedIn: false, authResCode: 400 });
      return;
    }
    this.validateLogin();
  };

  private validateLogin = () => {
    this.setState({ loggingIn: true });
    Api.Authorization.login(this.state.username, this.state.password)
      .then((data: any) => this.handleSuccessfulLogin(data))
      .catch((error) => this.handleFailedLogin(error));
  };

  private handleSuccessfulLogin = (resJson: any) => {
    if (axios.defaults.headers === undefined) {
      console.error("Axios undefined");
      return;
    }
    axios.defaults.headers.common["Authorization"] = "Bearer " + resJson.jwt;
    const STATE = {
      authResCode: 200,
      loggedIn: true,
      loggingIn: false,
      redirectDepId: resJson.departmentId,
      redirectDepName: resJson.department,
    };
    if (this.onLoggedIn !== undefined) this.onLoggedIn();
    this.setState(STATE);
  };

  private redirect = () => {
    return (
      <>
        {this.state.redirectDepName == "admin" ? (
          <Redirect to="/" />
        ) : (
          <Redirect
            to={{
              pathname: `/departments/${this.state.redirectDepId}`,
              state: {
                name: this.state.redirectDepName,
                id: this.state.redirectDepId,
              },
            }}
          />
        )}
      </>
    );
  };

  private handleFailedLogin = (error: any) => {
    this.setState({
      authResCode: error.response.status,
      loggedIn: false,
      loggingIn: false,
    });
    console.log("Login mounted: " + this.mounted);
  };

  render() {
    const style: any = {
      marginTop: this.state.windowHeight * 0.25 + "px",
    };
    console.log("render");

    return (
      <section className="vh-100 vh-100 gradient-custom">
        <div className="container py-5 h-100">
          <div className="row d-flex justify-content-center align-items-center h-100">
            <div className="col-12 col-md-8 col-lg-6 col-xl-5">
              <div
                className="card bg-dark text-white"
                style={{ borderRadius: "1rem" }}
              >
                <div className="card-body p-5 text-center">
                  {this.renderTitle()}
                  {this.state.authResCode != 200 &&
                  this.state.authResCode != 0 ? (
                    <div className="row justify-content-center">
                      {this.renderAlert()}
                    </div>
                  ) : (
                    <React.Fragment />
                  )}
                  <div className="row justify-content-center">
                    {this.state.loggedIn
                      ? this.redirect()
                      : this.renderLoginForm()}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    );
  }

  private renderAlert = () => {
    return (
      <div className=" alert alert-danger text-center">
        {this.getAlertMessage()}
      </div>
    );
  };

  private getAlertMessage = () => {
    const CODE = this.state.authResCode;
    //  Not an error
    if (CODE < 400) return "";
    if (CODE == 408) return "Request Timeout";
    if (CODE < 500) return "Invalid Credentials";
    else return "Unknown Error";
  };

  private renderLoginForm = () => {
    return <>{this.renderInputs()}</>;
  };

  private renderTitle = (): React.ReactNode => {
    return (
      <div className="d-flex justify-content-center mb-4">
        {this.state.loggingIn ? (
          this.renderSpinner()
        ) : (
          <h3 className="fw-bold text-uppercase">Login</h3>
        )}
      </div>
    );
  };

  private renderSpinner = () => {
    return (
      <>
        <div className="d-flex justify-content-center">
          <div
            className="col-4 spinner-border text-light"
            style={{ width: "3rem", height: "3rem" }}
            role="status"
            aria-hidden="true"
          ></div>
        </div>
      </>
    );
  };

  private renderInputs = () => {
    return (
      <>
        <form onSubmit={this.handleOnClick}>
          {" "}
          <div className="form-outline form-white mb-4">
            <input
              className="form-label form-control form-control-lg"
              type="text"
              name="username"
              disabled={this.state.loggingIn}
              value={this.state.username}
              onChange={this.handleUsernameChange}
              placeholder="Username"
            />
          </div>
          <div className="form-outline form-white mb-4">
            <input
              className="form-label form-control form-control-lg"
              type="password"
              name="password"
              disabled={this.state.loggingIn}
              value={this.state.password}
              onChange={this.handlePasswordChange}
              placeholder="Password"
            />
          </div>
          <p className="small mb-3 pb-lg-2">
            <a className="text-white-50" href="#!">
              Forgot password?
            </a>
          </p>
          <button
            type="submit"
            className="btn btn-outline-light btn-lg px-5"
            disabled={this.state.loggingIn}
            // onClick={this.handleOnClick}
          >
            Login
          </button>{" "}
        </form>
      </>
    );
  };

  componentWillUnmount() {
    this.mounted = false;
  }
}

const Login = withRouter(WrappedLogin);
export default Login;
