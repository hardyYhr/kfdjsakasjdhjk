import React from 'react'
import '../../Departments.css'
import { BrowserRouter as Router, Route, Switch, Link } from "react-router-dom";
import Api from "../../API/Api";

class Departments extends React.Component<any, any> {

    constructor(props: any) {
        super(props);

        this.state = {
            departments: [],
            name: "",
            blurb: ""
        }
        
    }

    componentDidMount() {
        
        Api.Departments.getDepartments().then(data => {
            this.setState({
                departments: data
            })
        })

    }

    handleDepartmentNameOnChange(e: any) {
        this.setState({
            name: e.target.value
        })
    }

    handleDepartmentBlurbOnChange(e: any) {
        this.setState({
            blurb: e.target.value
        })
    }

    handleSubmit() {
        const name = this.state.name
        const blurb = this.state.blurb
        
        const obj = {
            "name": name,
            "blurb": blurb
        }

        Api.Departments.addDepartment(obj).then((data: any) => {
            ($('#add-departments-modal') as any).modal('hide');
            ($('#complete-modal') as any).modal('show');
        }).catch((error) => console.log(error));
    }

    handleDelete(id: number) {
        Api.Departments.deleteDepartment(id).then((data: any) => {
            ($('#delete-modal') as any).modal('show');
        }).catch((error) => console.log(error))
    }
 

    render() {
        const departments = this.state.departments
        const component = this
        return (
            <>
            <div className="card mx-auto w-75 my-5">
                
                <h1 className="card-header card-title text-center display-4">Departments</h1>
                <div className="card-body">
                <div className="d-grid gap-2 col-6 mx-auto mb-3">
                    <button className="btn btn-primary" type="button" data-bs-toggle="modal" data-bs-target="#add-departments-modal">Add New Department</button>
                </div>
                
                <ul className="list-group">
                    {departments.map(function(d: any, idx: number){ 
                         return (
                            <li className="list-group-item text-center display-6" key={idx}>
                                {
                                <Link to={{
                                    pathname: `/departments/${d.id}`,
                                    state: {
                                        name: d.name,
                                        id: d.id
                                    }
                                }}>
                                    {d.name}
                                </Link> 
                                }
                                
                                <div className="d-grid gap-2 d-md-flex justify-content-md-end">
                                    <button className="btn btn-primary" type="button" onClick={() => component.handleDelete(d.id)}>Delete</button>
                                </div>
                            </li>
                            )
                    })}
                </ul>
                </div>
            </div>

            {/* Forms Modal */}
            <div className="modal fade" id="add-departments-modal" aria-labelledby="modal-label" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title" id="modal-label">Create New Department</h5>
                        <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div className="modal-body">
                        <div className="mb-3">
                            <label htmlFor="department-name-input" className="form-label">Department Name</label>
                            <input className="form-control" id="department-name-input" onChange={this.handleDepartmentNameOnChange.bind(this)} />
                        </div>
                        <div className="mb-3">
                            <label htmlFor="department-blurb-input" className="form-label">Text Blurb</label>
                            <input className="form-control" id="department-blurb-input" onChange={this.handleDepartmentBlurbOnChange.bind(this)} />
                        </div>
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="button" className="btn btn-primary" onClick={this.handleSubmit.bind(this)}>Submit</button>
                    </div>
                    </div>
                </div>
            </div>

            {/* Successful Request Made Modal */}
            <div className="modal fade" id="complete-modal" aria-labelledby="form-complete-modal" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title" id="form-complete-modal">Successfully Added New Department!</h5>
                        <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                    </div>
                </div>
            </div>

            {/* Successfully Deleted Modal */}
            <div className="modal fade" id="delete-modal" aria-labelledby="form-delete-modal" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title" id="form-delete-modal">Successfully Deleted Department!</h5>
                        <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                    </div>
                </div>
            </div>
            </>
        )
    }
}


export default Departments;
