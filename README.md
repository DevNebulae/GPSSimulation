# Simulation system

## Command line properties

The following command line properties are enabled by providing them as follows:
`-Dsimulation.log=/var/log/simulation.log`. Please read the rules about these
command line properties to ensure which properties are required.

```
# File path to where the Log4J2 will write its log messages.
simuatlion.log=<string>

# These rules all require a valid URL pointing to your RabbitMQ server. If the
# URL cannot be connected to, your messages will be lost. At least supply the
# URL for your own country. If there are no RabbitMQ instances which can be
# connected to, the simulation system will crash.
simulation.rabbitmq.be=<string>
simulation.rabbitmq.de=<string>
simulation.rabbitmq.fi=<string>
simulation.rabbitmq.it=<string>
simulation.rabbitmq.nl=<string>
```