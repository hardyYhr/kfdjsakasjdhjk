import React from 'react'

 import MessagesSubmit from "./MessagesSubmit";
 import MessageShow from "./MessageShow";

import Api from "../../API/Api";

class MessageBoard extends React.Component<any, any> {
  constructor(props: any) {
    super(props);

    this.state = {
      dataSource: [],
    };
  }

  componentDidMount() {
    this.getMessages();
  }

  getMessages = () => {
    Api.Message.getAllMessage().then((data: any) => {
      this.setState({
        dataSource: data,
      });
    });
  };



  render() {
    const { dataSource } = this.state;
    return (
      <div className="col-xs-8">
        <MessagesSubmit
          getMessages={this.getMessages}
        />
        <MessageShow dataSource={dataSource} getMessages={this.getMessages} />
      </div>
    );
  }
}
    






export default MessageBoard;

