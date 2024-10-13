@tag
Feature: Purchase the order from Ecommerce website

  @tag2
  Scenario Outline: Positive test of submitting the order
    Given I land on Ecommerce page
    And Logged in through username <name> and password <password>
    When I add product <value> to cart
    When I check for the <value> in cart
    When Checkout <value> and submit the order
    Then I verify the <status> in step

  Examples:
    | name             | password    | value   | status  |
    | kanhu@gmail.com  | Kanha@6173  | product | success |
