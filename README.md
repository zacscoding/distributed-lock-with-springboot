# Distribute lock demo project
this is a demo project for acquiring a lock given task id in distributed application.

## Features  

- [x] `Standalone cluster` : acquire locks depends on property.
- [ ] `Radis cluster` : acquire locks by using redis.
- [ ] `Zookeeper cluster` : acquire locks by using zookeeper.
- [ ] `Raft cluster` : acquire locks by using raft algorithm.

> ## Getting started

> ### Build project  

```aidl
$./gradlew clean build
```  

> ### Running with standalone

```aidl
// start server1
$ java --spring.config.location=classpath:/standalone.yaml \
    -Dserver.port=8080 \
    -Dcluster.id=server1 \ 
    -jar build/libs/demo-0.0.1.jar

// start server2
$ java --spring.config.location=classpath:/standalone.yaml \
    -Dserver.port=8081 \
    -Dcluster.id=server2 \
    -Dcluster.standalone.alwaysAcquire=false \
    -jar build/libs/demo-0.0.1.jar
```  