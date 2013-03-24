# Generic crowdsourcing platform

This is a generic crowdsourcin platform developed by [Clara Bayarri](http://www.clarabayarri.com) as part of her bachelor's thesis. This platform serves tasks to registered applications. The first use case is a linguistic problem solving game, and can be found also on [Github](https://github.com/clarabayarri/crowd-game). This platform can be found running during its development phases on [heroku](http://gentle-gorge-9660.herokuapp.com/).

## Running the application locally

First build with:

    $mvn clean package

Then run it with:

    $java -jar target/dependency/jetty-runner.jar target/*.war
