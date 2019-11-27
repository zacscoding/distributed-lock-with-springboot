# Distribute lock demo project
this is a demo project for acquiring a lock given task id in distributed application.

## Features  

- [x] `Standalone cluster` : acquire locks depends on property.
- [x] `Radis cluster` : acquire locks by using redis.
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

> #### Running with zookeeper

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

---  



> ### Running with redis

> #### Start redis  

```aidl
$ cd compose/redis
$ docker-compose up
```  

> #### Running with redis

```aidl
// start server1
$ ./gradlew bootRun \
    -PjvmArgs="-Dserver.port=8080 -Dcluster.id=server1" \
    -Pargs="--spring.config.location=classpath:/redis.yaml"

// start server2
$ ./gradlew bootRun \
      -PjvmArgs="-Dserver.port=8081 -Dcluster.id=server1" \
      -Pargs="--spring.config.location=classpath:/redis.yaml"
```

