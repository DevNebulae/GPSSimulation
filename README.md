# Simulation system

## Compiling the jar

To compile the code, run: `gradle build`.

## Executing the jar

There are several command line arguments you need to define before the Java
application will run. Following a list of arguments, specifying which arguments
are required and which are optional:

* `--simulation.delay`: A number between 5 and ∞ specifying the time a car rests
before it starts driving a new route. The delay will be triggered when every
partial of a route has been driven. This argument is required.
* `--simulation.rabbitmq.<country-code>`: An IP address the simulation system
can send the appropriate translocations to. At least one argument must be
defined, otherwise all messages the simulation system produces will be lost.
* `--logging.file`: A file location where you want the logs to be produced. This
argument is optional, but I would recommend defining it for debugging purposes.

An example on how to run the JAR: 

`java -jar rekeningrijden-simulation.jar --logging.file=/var/log/simulation.log --simulation.delay=15 --simulation.rabbitmq.it=localhost`
