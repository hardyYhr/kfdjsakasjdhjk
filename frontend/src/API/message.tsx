import axios from 'axios'

const BASEURL = "http://localhost:8080/api/messages";

export function newMessage(params: any) {
    return axios.post(BASEURL, params).then((res) => res.data);
}


export function getAllMessage() {
  return axios.get(BASEURL).then((res) => res.data);
}

export function addReplies(id: number, params:object) {
  return axios.post(`${BASEURL}/${id}/replies`, params).then((res) => res.data);
}

export function updateReplies(id: number, params: object) {
  return axios.put(`${BASEURL}/replies/${id}`, params).then((res) => res.data);
}

export function deleteReplies(id: number) {
  return axios.delete(`${BASEURL}/replies/${id}`).then((res) => res.data);
};


