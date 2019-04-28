import * as React from 'react';
import { Col, Table } from 'reactstrap';
import * as axios from 'axios';

export default class InstanceList extends React.Component<{}, { loading: boolean, instances: Array<JSX.Element> }> {
  constructor(props: any) {
    super(props);
    this.state = {
      loading: true,
      instances: new Array<JSX.Element>()
    }
  }

  async componentDidMount() {
    try {
      let response = await axios.get(`/oraclemetricscollector/instances`);
      if (response.status == 200) {
        const data = response.data; //here comes an array
        const rows = new Array<JSX.Element>();
        for (const key in data) {
          const jsonObject = data[key];
          rows.push(
            <tr key = {jsonObject.host+jsonObject.sid} >
              <td>{jsonObject.host}</td>
              <td>{jsonObject.port}</td>
              <td>{jsonObject.sid}</td>
              <td>{jsonObject.user}</td>
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
