import React from "react";
import {  Button } from "antd";
import Api from "../../API/Api";


class MessagesSubmit extends React.Component<any, any> {
  form: any;

  constructor(props: any) {
    super(props);

    this.state = {};
  }

  handleSubmit = (e: { preventDefault: () => void }) => {
    e.preventDefault();

    const username = localStorage.getItem("username");
    const name = this.form.name.value.trim();
    const content = this.form.content.value.trim();
    let param = {
      department: name,
      content,
      username,
    };

    Api.Message.newMessage(param)
      .then((data: any) => {
        this.props.getMessages();
      })
      .catch((error) => console.log(error));
  };

  render() {
    return (
      <form
        ref={(e) => {
          this.form = e;
        }}
      >
        <div className="display-0.5 text-center my-3">
          Create New Messages Here
        </div>
        <div className="card h-100">
          <div className="col-xs-4">
            <input
              className="form-control"
              type="text"
              name="name"
              placeholder="Input the title"
              required
            />
          </div>
        </div>
        <div className="card h-100 my-2">
          <div className="col-xs-12">
            <textarea
              className="form-control"
              name="content"
              placeholder="Input the message "
              required
            ></textarea>
          </div>
        </div>
        <div className="SubmitButt">
          <Button type="primary" onClick={this.handleSubmit}>
            Submit
          </Button>
        </div>
      </form>
    );
  }
}

export default MessagesSubmit;

