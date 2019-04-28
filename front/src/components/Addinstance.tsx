import * as React from 'react';
import { Form, FormGroup, Label, Col, Row, Input, Button, ButtonGroup } from 'reactstrap';
import * as axios from 'axios';

export default class Addinstance extends React.Component<{}, {
    host: string,
    port: string,
    sid: string,
    user: string,
    pass: string,
    showSubmit: boolean
}> {
    constructor(props: any) {
        super(props);
        this.state = {
            host: "",
            port: "",
            sid: "",
            user: "",
            pass: "",
            showSubmit: false
        }
        this.submitData=this.submitData.bind(this);
    }
    formHandler = (event) => {
        const id = event.target.id;
        switch (id) {
            case "host":
                this.setState({ host: event.target.value }, this.formElementsChecker);
                break;
            case "port":
                this.setState({ port: event.target.value }, this.formElementsChecker);
                break;
            case "sid":
                this.setState({ sid: event.target.value }, this.formElementsChecker);
                break;
            case "user":
                this.setState({ user: event.target.value }, this.formElementsChecker);
                break;
            case "pass":
                this.setState({ pass: event.target.value }, this.formElementsChecker);
                break;
            default:
                break;
        }
    }

    formElementsChecker = () => {
        if (this.state.host != "" && this.state.port != "" && this.state.sid != "" && this.state.user != "" && this.state.pass != "") {
            this.setState(
                {
                    showSubmit: true
                }
            );
        } else {
            this.setState(
                {
                    showSubmit: false
                }
            );
        }
    }
    async submitData () {
        try {
            let response = await axios.post(`/oraclemetricscollector/addinstance`, {
                host: this.state.host,
                port: this.state.port,
                sid: this.state.sid,
                user: this.state.user,
                pass: this.state.pass
            });
            if (response.status == 200) {
                const data = response.data;
                console.log(typeof(data));
                if (data) {
                    alert ('Instanse added!');
                } else {
                    alert('Instans was not added Check parameters!');
                }
            }
        } catch (err) {
            alert(`Исключение: ${err}`);
        }
    }
    render() {
        return (
            <Col>
                <p>Добавить инстанс для мониторинга</p>
                <Form onChange={this.formHandler}>
                    <FormGroup row>
                        <Label sm={2}>Хост</Label>
                        <Col sm={3}>
                            <Input type="text" name="host" id="host" />
                        </Col>
                    </FormGroup>

                    <FormGroup row>
                        <Label sm={2}>Порт</Label>
                        <Col sm={3}>
                            <Input type="text" name="port" id="port" />
                        </Col>
                    </FormGroup>

                    <FormGroup row>
                        <Label sm={2}>Sid</Label>
                        <Col sm={3}>
                            <Input type="text" name="sid" id="sid" />
                        </Col>
                    </FormGroup>

                    <FormGroup row>
                        <Label sm={2}>Пользователь</Label>
                        <Col sm={3}>
                            <Input type="text" name="user" id="user" />
                        </Col>
                    </FormGroup>

                    <FormGroup row>
                        <Label sm={2}>Пароль</Label>
                        <Col sm={3}>
                            <Input type="password" name="pass" id="pass" />
                        </Col>
                    </FormGroup>
                </Form>
                {this.state.showSubmit &&
                    <ButtonGroup>
                        <Col sm={3}>
                            <Button onClick={this.submitData}> Добавить </Button>
                        </Col>
                    </ButtonGroup>
                }
            </Col>

        );
    }
}