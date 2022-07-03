   ##startup the three server
export spring_profiles_active=peer3 && ../gradlew  :eureka-server:test --tests "spring.SpringDemoForPeerServer.demo1" --debug
export spring_profiles_active=peer2 && ../gradlew  :eureka-server:test --tests "spring.SpringDemoForPeerServer.demo1" --debug
export spring_profiles_active=peer1 && ../gradlew  :eureka-server:test --tests "spring.SpringDemoForPeerServer.demo1" --debug