# java-agent

Has 2 implementations:
1. Logger
2. Timing

Switch between the 2 in the manifest.

## Logger:
`java -javaagent:java-agent-0.1.0-final.jar -Djava.util.logging.config.file=c:\\temp\\logging.properties <class>`

## Timing:
`java -javaagent:java-agent-0.1.0-final.jar <class>`
