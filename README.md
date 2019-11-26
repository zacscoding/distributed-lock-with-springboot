# Distribute lock demo project
this is a demo project for acquiring a lock given task id in distributed application.

## Features  

- [x] `Standalone cluster` : acquire locks depends on property.
- [ ] `Radis cluster` : acquire locks by using redis.
- [x] `Zookeeper cluster` : acquire locks by using zookeeper.
- [ ] `Raft cluster` : acquire locks by using raft algorithm.  

---  

> ## Getting started  

> ### Running with standalone

```aidl
// start server1
$ ./gradlew bootRun \
    -PjvmArgs="-Dserver.port=8080 -Dcluster.id=server1" \
    -Pargs="--spring.config.location=classpath:/standalone.yaml"

// start server2
$ ./gradlew bootRun \
    -PjvmArgs="-Dserver.port=8081 -Dcluster.id=server2" \
    -Pargs="--spring.config.location=classpath:/standalone.yaml"
```  



---  

> ### Running with zookeeper

> #### Start zookeeper  

```aidl
$ cd compose/zookeeper
$ docker-compose up
```

```aidl
// start server1
$ ./gradlew bootRun \
    -PjvmArgs="-Dserver.port=8080 -Dcluster.id=server1" \
    -Pargs="--spring.config.location=classpath:/zookeeper.yaml"

// start server2
$ ./gradlew bootRun \
      -PjvmArgs="-Dserver.port=8081 -Dcluster.id=server1" \
      -Pargs="--spring.config.location=classpath:/zookeeper.yaml"
```

