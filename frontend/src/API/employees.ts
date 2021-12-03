import axios from 'axios'

const URL = 'http://localhost:8080/api/employees'

export function getAllEmployees() {

    return axios.get(URL).then(res => res.data)

}

