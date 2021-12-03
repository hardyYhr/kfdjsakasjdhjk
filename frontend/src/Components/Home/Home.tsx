import React from 'react';
import CaseStudy from '../../Images/CaseStudy.jpg'
import DataImage from '../../Images/Data.jpg'
import Employees from '../../Images/Employees.jpg'
import BioMechanical from '../../Images/BioMechanical.jpg'
import Points from '../../Images/Points.jpg'
import { Link } from 'react-router-dom'

function Home() {
    return (
        <div>
            <div className="container">
                <h1 className="display-1 text-center mt-4"> Welcome to Hope Health Action</h1>
                <div className="row row-cols-1 row-cols-md-2 g-4">
                    <div className="col">
                        <div className="card h-100">
                        {/* <img src="..." className="card-img-top" alt="..."> */}
                        <div className="card-body">
                            <h5 className="card-title"><Link to="/maternity">Maternity</Link></h5>
                            <p className="card-text">Maternity department description</p>
                        </div>
                        </div>
                    </div>
                    <div className="col">
                        <div className="card h-100">
                        {/* <img src="..." className="card-img-top" alt="..."> */}
                        <div className="card-body">
                            <h5 className="card-title"><Link to="/communityhealth">Community Health</Link></h5>
                            <p className="card-text">Community Health description</p>
                        </div>
                        </div>
                    </div>
                    <div className="col">
                        <div className="card">
                        {/* <img src="..." className="card-img-top" alt="..."> */}
                        <div className="card-body">
                            <h5 className="card-title"><Link to="/nicu-paed">Nicu/Paed</Link></h5>
                            <p className="card-text">NICU/PAED description</p>
                        </div>
                        </div>
                    </div>
                    <div className="col">
                        <div className="card">
                        {/* <img src="..." className="card-img-top" alt="..." /> */}
                        <div className="card-body">
                            <h5 className="card-title"><Link to="/rehab">Rehab</Link></h5>
                            <p className="card-text">Rehab description</p>
                        </div>
                        </div>
                    </div>
                </div>

                <div className="card text-center mt-4">
                    <div className="card-header">
                        Leaderboards
                    </div>
                    <div className="card-body">
                        <h5 className="card-title">Click for more information on Leaderboards</h5>
                        <h5 className="card-title"><Link to="/leaderboard">Leaderboards</Link></h5>
                    </div>
                </div>

                <div className="card text-center mt-4">
                    <div className="card-header">
                        MessageBoard
                    </div>
                    <div className="card-body">
                        <h5 className="card-title">Click for more information on MessageBoard</h5>
                        <h5 className="card-title"><Link to="/messages">MessageBoard</Link></h5>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Home;