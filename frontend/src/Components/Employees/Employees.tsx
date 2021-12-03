import React from 'react';
import axios from 'axios';
import { getAllEmployees } from '../../API/employees'


type Employee = {
    firstName: string;
    lastName: string;
    id: number;
    department: string;
    isDepartmentHead: boolean;
}

export default class Employees extends React.Component <any, any> {

    constructor(props: any) {
        super(props)

        this.state = {
          employees: []
        };
      }

    componentDidMount() {
        
        getAllEmployees().then(data => {
            this.setState({
                employees: data
            })
        })

    }

    render() {

        return (
            <div className="card mx-auto w-75 my-5">
                <h1 className="card-header card-title text-center display-4">All Employees</h1>
                <div className="card-body">
                    <ul className="list-group">
                        {this.state.employees.map(function(d: any, idx: number){
                            return (
                                <li className="list-group-item text-center display-6" key={idx}>
                                    ID: {d.id} Name: {d.firstName} LastName: {d.lastName} Department: {d.department} Department Head: {d.departmentHead}
                                </li>)
                        })}
                    </ul>
                </div>
            </div>
        )
    }
}
