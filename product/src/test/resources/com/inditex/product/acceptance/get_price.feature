Feature: Obtener precio de producto

  Scenario: Precio correcto 1
    Given la marca con id 1 y el producto con id 35455
    When consulto el precio para la fecha "2020-06-14 10:00:00"
    Then el sistema debe devolver un precio de 35.5 con la tarifa 1
    Then el sistema debe devolver un codigo de respuesta 200

  Scenario: Precio correcto 2
    Given la marca con id 1 y el producto con id 35455
    When consulto el precio para la fecha "2020-06-14 16:00:00"
    Then el sistema debe devolver un precio de 25.45 con la tarifa 2
    Then el sistema debe devolver un codigo de respuesta 200

  Scenario: Precio correcto 3
    Given la marca con id 1 y el producto con id 35455
    When consulto el precio para la fecha "2020-06-14 21:00:00"
    Then el sistema debe devolver un precio de 35.5 con la tarifa 1
    Then el sistema debe devolver un codigo de respuesta 200

  Scenario: Precio correcto 4
    Given la marca con id 1 y el producto con id 35455
    When consulto el precio para la fecha "2020-06-15 10:00:00"
    Then el sistema debe devolver un precio de 30.5 con la tarifa 3
    Then el sistema debe devolver un codigo de respuesta 200

  Scenario: Precio correcto 5
    Given la marca con id 1 y el producto con id 35455
    When consulto el precio para la fecha "2020-06-16 21:00:00"
    Then el sistema debe devolver un precio de 38.95 con la tarifa 4
    Then el sistema debe devolver un codigo de respuesta 200

  Scenario: Fecha en formato incorrecto
      Given la marca con id 1 y el producto con id 35455
      When consulto el precio para la fecha "2020-06-15T10:00:00Z"
      Then el sistema debe devolver un codigo de respuesta 400

  Scenario: Precio no encontrado
      Given la marca con id 2 y el producto con id 35455
      When consulto el precio para la fecha "2020-06-15 10:00:00"
      Then el sistema debe devolver un codigo de respuesta 404