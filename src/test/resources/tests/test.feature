Feature: Sample Test

  Scenario: First Test
    * url 'https://reqres.in/api/users'
    * method GET
    * status 200
    * print response
