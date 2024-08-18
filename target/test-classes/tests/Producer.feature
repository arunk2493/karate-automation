Feature: Payment processing via Kafka

  Scenario: Place an order and verify payment processing
    * def KafkaUtils = Java.type('com.example.utils.KafkaUtils')
    * def orderTopic = 'orders'
    * def paymentTopic = 'payments'
    * def orderId = 'order-1234'
    * def orderPayload = { id: '#(orderId)', amount: 100.00, currency: 'USD' }
    * def paymentStatusExpected = { orderId: '#(orderId)', status: 'SUCCESS' }

    # Convert the order payload to a JSON string
    * def orderPayloadStr = karate.toJson(orderPayload)

    # Step 1: Publish an order to the orders topic
    * eval KafkaUtils.sendToKafka(orderTopic, orderId, orderPayloadStr)

    # Step 2: Consume the payment status from the payments topic
    * def paymentStatus = KafkaUtils.consumeFromKafka(paymentTopic, 'payment-group-id')

    # Step 3: Validate the payment status
    #* match paymentStatus == karate.toJson(paymentStatusExpected)
