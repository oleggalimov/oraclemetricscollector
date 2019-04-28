import * as React from 'react';
import { Col, Table, Button } from 'reactstrap';
import * as axios from 'axios';

export default class Deleteinstance extends React.Component<{}, { loading: boolean, instances: Array<JSX.Element> }> {
  constructor(props: any) {
    super(props);
    this.state = {
      loading: true,
      instances: new Array<JSX.Element>()
    }
    this.deleteButtonHandler = this.deleteButtonHandler.bind(this);
    this.listInstances=this.listInstances.bind(this);
  }
  async listInstances() {
    try {
      let response = await axios.get(`/oraclemetricscollector/instances`);
      if (response.status == 200) {
        const data = response.data; //here comes an array
        const rows = new Array<JSX.Element>();
        for (const key in data) {
          const jsonObject = data[key];
          rows.push(
            <tr key={jsonObject.host + jsonObject.sid} >
              <td>{jsonObject.host}</td>
              <td>{jsonObject.port}</td>
              <td>{jsonObject.sid}</td>
              <td>{jsonObject.user}</td>
              <td><Button onClick={(e) => this.deleteButtonHandler(jsonObject)}>Удалить</Button></td>
            </tr>
          );
        }

        this.setState({ instances: rows });
        this.setState({ loading: false });
      }
    } catch (err) {
      alert(`Исключение: ${err}`);

    }
  }
  async deleteButtonHandler(object) {
    try {
      this.setState({ loading: true });
      let response = await axios.post(`/oraclemetricscollector/delinstance`, object);
      if (response.status == 200) {
        const data = response.data;
        console.log(data);
        this.listInstances();
        this.setState({ loading: false });
      }
    } catch (err) {
      alert(`Исключение: ${err}`);
    }
  }
  componentDidMount() {
    this.listInstances();
  }

  render() {
    if (!this.state.loading) {
      return (
        <Col>
          <Table>
            <thead>
              <tr>
                <th>Хост</th>
                <th>Порт</th>
                <th>Sid</th>
                <th>Пользователь</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {this.state.instances}
            </tbody>
          </Table>
        </Col>
      );
    } else {
      return (<div>Загрузка...</div>);
    }

  }
}
