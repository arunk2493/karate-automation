Feature: Kafka Topic and JSON Payment Message

  Scenario: Create topic with replication factor, produce JSON message, and consume it
    * def kafkaUtils = Java.type('com.example.utils.KafkaUtils')
    
    # Define topic and group ID
    * def topic = 'new_topic'
    * def groupId = 'payment-group'
    
    # Create topic with desired replication factor
    * def createTopic = kafkaUtils.createTopicIfNotExists(topic)
    
    # Produce a JSON payment message to Kafka
    * def paymentMessage = { id: 'order-1234', amount: 100.0, currency: 'USD' }
    * def key = 'payment-key'
    
    # Produce message to Kafka (use map directly)
    * def produceMessage = kafkaUtils.produceToKafka(topic, key, paymentMessage)
    * print produceMessage
    
    # Consume message from Kafka
    * def message = kafkaUtils.consumeFromKafka(topic, groupId)
    
    #
    # Verify the consumed message
    #* def consumedMessage = karate.fromJson(message)
    #* match consumedMessage == { id: 'order-1234', amount: 100.0, currency: 'USD' }
    #
    # Additional assertions to verify individual fields
    #* assert consumedMessage.id == 'order-1234'
    #* assert consumedMessage.amount == 100.0
    #* assert consumedMessage.currency == 'USD'
