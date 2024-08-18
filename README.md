Prerequisite:
1. Kafka and zookeeper in Local
    1. Install the kafka zip from the kafka website
    2. Update the config files for Zookeeper and Kafka
    3. Run bin/zookeeper-server-start.sh config/zookeeper.properties from the kafka folder to start the zookeeper
    4. Run bin/kafka-server-start.sh config/server.properties from the kafka folder to start the kafka broker
    5. mvn test to run the tests. Messages sent and received can be viewed
2. Kafka and Zookeeper in Docker:
    1. Create a docker compose file with Kafka and zookeeper images with respective ports and configuration
    2. docker-compose up -d to build the docker file
    3. mvn test to run the test

Execution:
1. mvn clean install
2. mvn test 
