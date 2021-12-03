import React from "react";
import { Collapse,  Comment, Form, Input, Button } from "antd";
import moment from "moment";
import { SettingOutlined, DeleteOutlined } from "@ant-design/icons";
import Api from "../../API/Api";

const { Panel } = Collapse;
const Edit = (props: any) => {

  const handleSubmit = (values: any) => {
    const { id } = props;
    Api.Message.updateReplies(id, values)
      .then((data: any) => {
        props.showEditReplies(null)
        props.getMessages();
      })
      .catch((error) => console.log(error));
  };
  return (
    <Form
      name="customized_form_controls"
      layout="inline"
      onFinish={(e) => handleSubmit(e)}
    >
      <Form.Item name="content">
        <Input
          defaultValue={props.content}
          style={{ width: window.innerWidth * 0.4 }}
          placeholder="commit"
        />
      </Form.Item>
      <Form.Item>
        <Button type="primary" htmlType="submit">
          Update
        </Button>
      </Form.Item>
    </Form>
  );
};

class MessageShow extends React.Component<any, any> {
  constructor(props: any) {
    super(props);

    this.state = {
      editIdEnable: "",
    };
  }

  addReplies = (values: any, id: number) => {
    const username = localStorage.getItem("username");

    let param = {
      username,
      ...values,
    };

    Api.Message.addReplies(id, param)
      .then((data: any) => {
        this.props.getMessages();
      })
      .catch((error) => console.log(error));
  };

  deleteReplies = (id: number) => {
    Api.Message.deleteReplies(id)
      .then((data: any) => {
        this.props.getMessages();
      })
      .catch((error) => console.log(error));
  };

  showEditReplies = (id: number) => {
    this.setState({
      editIdEnable: id,
    });
  };

  render() {
    const { dataSource } = this.props;
    const { editIdEnable } = this.state;
    return (
      <div className="MessageList">
        <h1 className="display-0.7 text-center my-4">Messages List</h1>
        <Collapse collapsible={"header"} accordion>
          {dataSource.map((item: any, index: number) => {
            return (
              <Panel
                showArrow={false}
                key={index}
                header={
                  <>
                    <a
                      href="#"
                      className="list-group-item list-group-item-action"
                    >
                      <div className="d-flex w-100 justify-content-between">
                        <h5 className="mb-1">{item.department}</h5>
                        <small>{moment(item.timestamp).fromNow()}</small>
                      </div>
                      <p className="mb-1">{item.content}</p>
                    </a>
                  </>
                }
              >
                <div>
                  <Form
                    name="customized_form_controls"
                    layout="inline"
                    onFinish={(e) => this.addReplies(e, item.id)}
                  >
                    <Form.Item name="content">
                      <Input
                        style={{ width: window.innerWidth * 0.4 }}
                        placeholder="commit"
                      />
                    </Form.Item>
                    <Form.Item>
                      <Button type="primary" htmlType="submit">
                        Submit
                      </Button>
                    </Form.Item>
                  </Form>
                  {item.replies.map((item2: any) => (
                    <Comment
                      author={item2.username}
                      avatar={"https://joeschmoe.io/api/v1/random"}
                      content={
                        editIdEnable === item2.id ? (
                          <Edit
                            id={item2.id}
                            content={item2.content}
                            getMessages={this.props.getMessages}
                            showEditReplies={this.showEditReplies}
                          />
                        ) : (
                          item2.content
                        )
                      }
                      datetime={moment(item2.timestamp).fromNow()}
                      actions={[
                        <SettingOutlined
                          onClick={() => this.showEditReplies(item2.id)}
                        />,
                        <DeleteOutlined
                          onClick={() => this.deleteReplies(item2.id)}
                        />,
                      ]}
                    />
                  ))}
                </div>
              </Panel>
            );
          })}
        </Collapse>
      </div>
    );
  }
}

export default MessageShow;
