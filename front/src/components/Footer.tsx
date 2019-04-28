import * as React from 'react';
import {
  Collapse,
  Navbar,
  NavbarToggler,
  NavbarBrand,
  Nav,
  UncontrolledDropdown,
  DropdownToggle,
  DropdownMenu,
  DropdownItem, 
  } from 'reactstrap';
  import {NavLink} from 'react-router-dom'


export default class Footer extends React.Component <{color}, {isOpen:boolean, color:string}> {
  constructor(props) {
    super(props);

    this.toggle = this.toggle.bind(this);
    this.state = {
      isOpen: false,
      color:props.color,
    };
  }
  toggle() {
    this.setState({
      isOpen: !this.state.isOpen
    });
  }
  render() {
    return (
      <div>
        <Navbar color={this.state.color} dark={this.state.color=="dark"} light={this.state.color=="light"}expand="md">
          <NavbarBrand href={`/oraclemetricscollector/index`}>Мониторинг инстансов Oracle</NavbarBrand>
          <NavbarToggler onClick={this.toggle} />
          <Collapse isOpen={this.state.isOpen} navbar>
            <Nav className="ml-auto" navbar>              
              <UncontrolledDropdown nav inNavbar>
                <DropdownToggle nav caret>
                  Управление инстансами
                </DropdownToggle>
                <DropdownMenu right>
                <DropdownItem>
                  <NavLink to={`/list`}> Список инстансов</NavLink>
                  </DropdownItem>
                  <DropdownItem>
                  <NavLink to={`/add`}> Добавить</NavLink>
                  </DropdownItem>
                  <DropdownItem>
                  <NavLink to={`/del`}>Удалить</NavLink>
                  </DropdownItem>
                  <DropdownItem divider />
                  <DropdownItem>
                  <NavLink to={`/oraclemetricscollector/index`}>Главная</NavLink>
                  </DropdownItem>
                  <DropdownItem>
                    Закрыть
                  </DropdownItem>
                </DropdownMenu>
              </UncontrolledDropdown>
            </Nav>
          </Collapse>
        </Navbar>
      </div>
    );
  }
}