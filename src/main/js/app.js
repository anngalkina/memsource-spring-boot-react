'use strict';

// tag::vars[]
const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

// end::vars[]

// tag::app[]
class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {projects: [], configAttributes: [], configuration: {}};
        this.onConfigurationUpdate = this.onConfigurationUpdate.bind(this);
        this.loadProjects = this.loadProjects.bind(this);
	}

    onConfigurationUpdate(configuration) {
            var self = this;
            client({
                    method: 'POST',
                    path: "/updateConfiguration",
                    entity: configuration,
                    headers: {'Content-Type': 'application/json'}
                }).then(response => {
                client({
                    method: 'GET',
                    path: "projects"
                }).done(response => {
                    this.loadProjects(response.entity);
                });
            });

    }

    loadProjects(projects) {
      this.setState({projects: projects})
    }

    loadConfigurationAttributes() {
        client({
            method: 'GET',
            path: '/api/profile/configurations',
            headers: {'Accept': 'application/schema+json'}
            })
            .then(schema => {
                this.schema = schema.entity;
            })
            .done(response => {
                this.setState({configAttributes: Object.keys(this.schema.properties)
                });
            });
    }

    loadConfiguration() {
        client({
            method: 'GET',
            path: "configuration"
        }).done(response => {
            this.setState({configuration: response.entity})
        });
    }

    componentDidMount() {
        this.loadConfigurationAttributes();
        this.loadConfiguration();
    }

	render() {
		return (
            <div>
                <ConfigurationDialog configAttributes={this.state.configAttributes} onConfigurationUpdate={this.onConfigurationUpdate} loadProjects={this.loadProjects}/>
                <ProjectList projects={this.state.projects}/>
            </div>
		)
	}
}
// end::app[]

// tag::configuration[]
class ConfigurationDialog extends React.Component{
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        var configuration = {};
        this.props.configAttributes.forEach(attribute => {
            configuration[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onConfigurationUpdate(configuration);
        // this.props.configAttributes.forEach(attribute => {
        //     ReactDOM.findDOMNode(this.refs[attribute]).value = ''; // clear out the dialog's inputs
        // });
        window.location = "#";
    }

    render() {
        return (
            <div>
                <div id="setUpConfiguration">
                    <div>
                        <h2>Set up configuration</h2>
                        <form>
                            <p key="username">
                                <input type="text" placeholder="username" ref="username" className="field" />
                            </p>
                            <p key="password">
                                <input type="password"  placeholder="password" ref="password" className="field" />
                            </p>

                            <button onClick={this.handleSubmit}>Submit</button>
                        </form>
                    </div>
                </div>
            </div>
        )
    }
}
// end::configuration[]

// tag::project-list[]
class ProjectList extends React.Component{

	render() {
		var projects = this.props.projects.map(project =>
			<Project key={project.id} project={project}/>
		);
		const thHeader = projects.length ? (
                <tr>
                    <th>Name</th>
                    <th>Status</th>
                    <th>Source Language</th>
                    <th>Target Languages</th>
                </tr>
            ) : (
                <tr></tr>
            );

		return (
		    <div>
                <h2>Available projects</h2>
                <table>
                    <tbody>
                        {thHeader}
                        {projects}
                    </tbody>
                </table>
            </div>
		)
	}
}
// end::project-list[]

// tag::project[]
class Project extends React.Component{
	render() {
	    var targetLangs = this.props.project.targetLangs
            ? this.props.project.targetLangs.join(",")
            : "";
		return (
			<tr>
				<td>{this.props.project.name}</td>
                <td>{this.props.project.status}</td>
                <td>{this.props.project.sourceLang}</td>
                <td>{targetLangs}</td>
			</tr>
		)
	}
}
// end::project[]

// tag::render[]
ReactDOM.render(
	<App />,
	document.getElementById('react')
)
// end::render[]

