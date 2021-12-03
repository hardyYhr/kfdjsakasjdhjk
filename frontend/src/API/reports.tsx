import axios from 'axios'

const BASEURL = 'http://localhost:8080/api/reports/'

export function newReport(department: string) {
    return axios.post(BASEURL, {"department":department}).then(res => res.data)
}

export function getReportById(id: number) {
    return axios.get(BASEURL + id).then(res => res.data)
}

export function getReportByDeptName(department: string) {
    return axios.get(BASEURL, {params: {department: department}}).then(res => res.data[0])
}

export function addEmptyQuestion(id: number) {
    return axios.post(BASEURL + id + '/questions', {}).then(res => res.data)
}

export function addQuestion(id: number, question: string) {
    return axios.post(BASEURL + id + '/questions', {"question":question}).then(res => res.data)
}

export function deleteQuestion(id: number) {
    return axios.delete(BASEURL + 'questions/' + id)
}

export function updateQuestion(id: number, question: string) {
    return axios.put(BASEURL + 'questions/' + id, {"question":question}).then(res => res.data)
}

export function answerQuestion(id: number, answer: string) {
    return axios.put(BASEURL + 'questions/' + id + '/answer', answer, {
        headers: {
            'Content-Type': 'application/json',
        }}).then(res => res.data)
}

export function addMultipleChoiceQuestion(id: number, question: string) {
    return axios.post(BASEURL + id + '/questions/mcq', {"question":question}).then(res => res.data)
}

export function deleteMultipleChoiceQuestion(id: number) {
    return axios.delete(BASEURL + 'questions/mcq/' + id)
}

export function updateMultipleChoiceQuestion(id: number, question: string) {
    return axios.put(BASEURL + 'questions/mcq/' + id, {"question":question}).then(res => res.data)
}

export function updateMultipleChoiceQuestionChoice(id: number, choices: Map<string, string>) {
    return axios.put(BASEURL + 'questions/mcq/' + id, {"choices":choices}).then(res => res.data)
}

export function answerMultipleChoiceQuestion(id: number, answer: string) {
    return axios.put(BASEURL + 'questions/mcq/' + id + '/answer', answer).then(res => res.data)
}

export function addWrittenQuestion(id: number, question: string) {
    return axios.post(BASEURL + id + '/questions/written', {"question":question}).then(res => res.data)
}

export function deleteWrittenQuestion(id: number) {
    return axios.delete(BASEURL + 'questions/written/' + id)
}

export function updateWrittenQuestion(id: number, question: string) {
    return axios.put(BASEURL + 'questions/written/' + id, {"question":question}).then(res => res.data)
}

export function answerWrittenQuestion(id: number, answer: string) {
    return axios.put(BASEURL + 'questions/written/' + id + '/answer', answer, {
        headers: {
            'Content-Type': 'application/json',
        }}).then(res => res.data)
}

