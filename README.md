# vantiq-amqp-connector
amqp-connector for VANTIQ


## Usage
1. register AMQP Source with ProtoBuf 
2. create a source in VANTIQ
3. start rabbitMQ server
4. start the connector

## register
Create a config file named *amqpSource.json*:
```
{
   "name" : "AMQPProtoSource",
   "baseType" : "EXTENSION",
   "verticle" : "service:extensionSource",
   "config" : {}
}
```

And run:
```
vantiq -s <profileName> load sourceimpls amqpSource.json
```

## Create source
In VANTIQ, you should see a new Source type named *AMQPProtoSource*, create a new source with this type, and config:
```
{
    "mqs_server_url": "localhost",
    "mqs_user": "username",
    "mqs_password": "thePassword",
    "topics": "topic1"
}
```

## start rabbitMQ
Start rabbitMQ server.

## Package and Start connector
At first, package the connector with:
```
# package
mvn package -Dmaven.test.skip=true 

# and run
java -jar target/amqp-connector-1.0-SNAPSHOT-jar-with-dependencies.jar
```

When you run with jar, the protobuf file should be in home directory, if you didn't set, it is the current directory you work.

For example, in command above, you are in *amqp-connector* project directory. So you should have Face.java file in here.

## Test
To send some message to rabbitMQ to test, you run the unit test *AMQPTest.test1*, it will get protobuf data from file *Face.txt* and send to rabbitMQ.

Then you can see the message in connector log, and in VANTIQ.   