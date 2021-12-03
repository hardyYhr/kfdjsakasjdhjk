import axios from "axios";

const URL = window.location.protocol + "//" + window.location.hostname;

export type AuthResponse = {
  jwt: string;
};

/**
 *
 * @param creds Login credentials for validation
 * @param port Your server port. Defaults to 8080
 * @returns Axios promise
 */
export function login(username: string, password: string, port?: number) {
  return axios
    .post(URL + (port === undefined ? ":8080" : port) + "/authenticate", {
      username: username,
      password: password,
    })
    .then((res) => res.data);
}
