import React from "react";
import "../../Departments.css";
import { BrowserRouter as Router, Route, Switch, Link } from "react-router-dom";
import CaseStudy from "../../Images/CaseStudy.jpg";
import DataImage from "../../Images/Data.jpg";
import Employees from "../../Images/Employees.jpg";
import BioMechanical from "../../Images/BioMechanical.jpg";
import Points from "../../Images/Points.jpg";

class DepartmentHomePageTemplate extends React.Component<any, any> {
  constructor(props: any) {
    super(props);

    this.state = {
      EmployeeImageClick: () => {
        <Link to={"/departments/:departmentID/employees"}></Link>;
      },
      DataImageClick: () => {
        <Link to={"/departments/:departmentID/datainput"}></Link>;
      },
      CaseStudyImageClick: () => {
        <Link to={"/departments/:departmentID/casestudyPage"}></Link>;
      },
      BioMechImageClick: () => {
        <Link to={"/departments/:departmentID/biomech"}></Link>;
      },
    };
  }

  render() {
    let currDate = new Date();
    let month = currDate.getMonth() + 1;
    let day = currDate.getDate();
    let year = currDate.getFullYear();
    let lastDay = new Date(year, month, 0);
    let date = year + "/" + month + "/" + lastDay.getDate();

    const name = this.props.location.state.name || null;
    const id = this.props.location.state.id || null;

    return (
      <>
        <h1 className="display-1 text-center my-4">{name}</h1>
        <div className="row row-cols-1 row-cols-md-3 g-4 mx-2">
          <div className="col">
            <div className="card h-100">
              <div className="card-body">
                <h5 className="card-title">
                  <p className="card-text">Case Study</p>
                  <Link
                    to={{
                      pathname: `/departments/${id}/casestudyPage`,
                    }}
                  >
                    <img
                      src={CaseStudy}
                      className="card-img-top img-responsive"
                      onClick={this.state.CaseStudyImageClick}
                    />
                  </Link>
                </h5>
                <div className="card-text">
                  Next case study will be due on:
                  <p>{date}</p>
                </div>
              </div>
            </div>
          </div>
          <div className="col">
            <div className="card h-100">
              <div className="card-body">
                <h5 className="card-title">
                  <p className="card-text">Data Input</p>
                  <Link
                    to={{
                      pathname: `/departments/${id}/datainput`,
                      state: {
                        department: name,
                        id: id,
                      },
                    }}
                  >
                    <img
                      src={DataImage}
                      className="card-img-top img-responsive"
                      onClick={this.state.DataImageClick}
                    />
                  </Link>
                </h5>
                <div className="card-text">
                  Next data submission will be due on:
                  <p>{date}</p>
                </div>
              </div>
            </div>
          </div>
          <div className="col">
            <div className="card h-100">
              <div className="card-body">
                <h5 className="card-title">
                  <p className="card-text">Employees</p>
                  {
                    <Link
                      to={{
                        pathname: `/departments/${id}/employees`,
                      }}
                    >
                      <img
                        src={Employees}
                        className="card-img-top img-responsive"
                        onClick={this.state.EmployeeImageClick}
                      />
                    </Link>
                  }
                </h5>
              </div>
            </div>
          </div>
          <div className="col">
            <div className="card h-100">
              <div className="card-body">
                <h5 className="card-title">
                  <p className="card-text">BioMech Support</p>
                  <Link
                    to={{
                      pathname: `/departments/${id}/biomech`,
                    }}
                  >
                    <img
                      src={BioMechanical}
                      className="card-img-top img-responsive"
                      onClick={this.state.BioMechImageClick}
                    />
                  </Link>
                </h5>
                <p className="card-text">No Messages</p>
              </div>
            </div>
          </div>
          <div className="col">
            <div className="card h-100">
              <div className="card-body">
                <h5 className="card-title">
                  <p className="card-text">Your Current Points Tally</p>
                  <img src={Points} className="card-img-top img-responsive" />
                </h5>
                <p className="card-text">Employee's score:</p>
              </div>
            </div>
          </div>
        </div>
      </>
    );
  }
}

export default DepartmentHomePageTemplate;
