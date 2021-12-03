import React from 'react';
import axios, { Axios, AxiosError, AxiosRequestConfig, AxiosResponse } from 'axios';
import { METHODS } from 'http';


interface Employee {
    firstName: string;
    lastName: string;
    id: number;
    department: string;
    isDepartmentHead: boolean;
}

export default class Example extends React.Component<any, any> {

    constructor(props: any) {
        super(props)

        this.state = {
            employees: [],
            text: "no jwt"
        };
    }

    componentDidMount() {
        /*
        The fetch API allows for async http request. 
        Axios is built ontop of fetch to provide more funtionalities.
        More on Fetch API here, which explains the concept:
            https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch
        */

        // We make an http request to the authentication endpoint, supplying the body with the login.
        // This is the hardcoded login. You could use your own employees in your db as well. Ensure that 
        // they have the correct authorization. 
        // Note that this entire chain is not executed immidiately; it is async.
        axios.post('http://localhost:8080/authenticate', {
            username: 'dev',
            password: '12345'
        })
            // execute this once an http repsonse is received.
            .then(response => {
                console.log(response.status)
                // extract body from response.
                return response.data;
            })
            // grab the jwt from the server response.
            .then((data: any) => {
                // this will return another promise, continuing the chain.
                // If you do not return a promise, the chain stops.
                this.setState({ text: data.jwt })
                return axios.get('http://localhost:8080/api/employees', {
                    headers: {
                        'Authorization': 'Bearer ' + data.jwt
                    }
                })
            })
            // same as above
            .then(response => response.data)
            .then(data => {
                console.log(data)
                this.setState({employees:data})
            })
            // catching error
            .catch((error: AxiosError) => {
                console.log(error.response?.status)
                this.setState({ text: error.response?.status + ': ' + error.response?.statusText })
            })
    }

    render() {
        console.log(this.state.employees)
        return (
            <div>
                <h1>{this.state.text}</h1>
                <ul>
                    {this.state.employees.map(function (d: any, idx: number) {
                        return (<li key={d.id}>ID: {d.id} Name: {d.firstName} LastName: {d.lastName} Department: {d.department} Department Head: {d.departmentHead}</li>)
                    })}
                </ul>
            </div>
        )
    }
}
