# ThriftProject
Simple Java project using the Thrift API.
The project consists of server/client communication. The server stores data in the <key, value> format using the Java class ConcurrentHashMap.
In order to compile the .thrift file, use

> thrift --gen java ThriftDB.thrift

This will generate the gen-java folder containing all .java and .class files.
Bear in mind to specify, at compilation time, the thrift.jar, slf4j-api-1.7.21.jar and the gen-java generated folder in the classpath.