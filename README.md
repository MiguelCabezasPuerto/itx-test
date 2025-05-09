# Inditex Test

## 📌 Descripción
Inditex Test es un servicio REST que ofrece el precio y tarifa que se debe aplicar a un determinado producto de una determinada cadena en una fecha concreta.

## 🛠️ Tecnologías
- **Java 21**
- **Spring Boot 3.4.5**
- **OpenApi**

## 🛠️ Base de datos
- **H2 embebido**

## 🏗️ Arquitectura
Utiliza arquitectura hexagonal con las siguientes capas:
- **Infraestructura**: contiene el controlador de entrada, la configuración de autenticación, la implementación de acceso a H2 y el controlador de excepciones
- **Aplicación**: contiene la lógica de negocio (validador de fechas y buscador de precios) así como el DTO de la información devuelta por API
- **Dominio**: contiene la clase de dominio (Price) así como las excepciones manejadas en el sistema, la interfaz de repositorio y un servicio buscador de precios que podría ubicarse en la capa de aplicación pero he considerado más core y por ello lo introduzco aquí.

## ⚡ Principios de diseño
- **Alta disponibilidad**: hace uso de cachés locales ofrecidas por spring boot para un reducción de los tiempos de consulta. A su vez implementa un circuit breaker para en caso de errores internos no verse saturada y dar una respuesta por defecto hasta que esos errores internos remitan.
- **Alta escalabilidad**: hace uso de una arquitectura hexagonal que la dota de mayor desacoplamiento.
- **Alto rendimiento**: hace uso de cachés así como de índices y fechas sobre la base de datos para reducir el tiempo de consulta. De igual modo contiene test de escalabilidad para verificar su rendimiento tras cada nuevo desarrollo.
- **Mantenibilidad**: sigue una arquitectura limpia (arquitectura hexagonal) que desacopla las responsabilidades, a su vez sigue un patrón factoría para la creación de excepciones lo que la hace más flexible y la generación de parte del modelo y el controlador es automática gracias a OpenApi.
- **Testeable**: incluye una cobertura del 100% entre test unitarios con JUnit5, de aceptación con Gherkin y Cucumber, E2E con RestAssured y de escalabilidad con Artillery. Del mismo modo, se han añadido logs de error con la referencia al producto para facilitar el troubleshooting en caso de fallos.
- **Documentable**: hace uso de OpenApi para facilitar la documentación del API con Swaggger, de igual modo contiene JavaDoc en sus clases y este README de apoyo.

## 🧪 Testing
- **Test unitarios** : cobertura cercana al 100%. Realizados con JUnit5 para probar aisladamente cada clase.
- **Test de aceptación** : realizados con Cucumber y definidos los escenarios con Gherkin. Tienen el propósito de verificar que se cumplen los requisitos funcionales de los siguientes escenarios:
  - Devolución correcta de precios
  - Cortar peticiones mal construidas devolviendo 400
  - Devolver no encontrado 404 para productos no recogidos en la base de datos 
- **Test E2E**: realizados con RestAssured con el fin de simular peticiones HTTP reales como lo haría otro sistema y así verificar la integración completa del sistema. Escenarios probados:
  - Devolución correcta de precios
  - Cortar peticiones mal construidas devolviendo 400
  - Cortar peticiones no autorizadas devolviendo 401
  - Devolver no encontrado 404 para productos no recogidos en la base de datos
- **Test de escalabilidad**: realizados con Artillery para garantizar la escalabilidad del sistema. Se han modelado tres escenario lanzando la misma petición:
  - Smoke: para comprobar que el sistema está operativo
  - Rampa: incremento gradual de las peticiones mandadas al sistema
  - Carga constante: peticiones en un nivel sostenido durante un periodo de tiempo
- **Colección Postman**: se añade colección Postman con peticiones tanto en localhost como al servidor que alberga la aplicación en Render. Prueba los mismos escenarios comentados en los tests E2E. Para el test de devolución correcta de precios se añade fichero test.csv con los datos para lanzar en Collection Runners, esta parte no la explico, se puede ver su funcionamiento en: `https://learning.postman.com/docs/collections/running-collections/intro-to-collection-runs/`

## 🔄 CI/CD
- **Integración continua**: se han creado dos workflows para que, tras hacer push de cambios del proyecto en GitHub, se ejecuten dos actions encadenadas:
  - Primer action (ci/cd.yml): hace un build de la aplicación para que se ejecuten los test unitarios, se compile y se forme la aplicación. Posteriormente lanza los tests de aceptación y los tests E2E.
  - Segundo action(load-test.yml): espera que la aplicación esté correctamente desplegada en el servidor (lanzando petición a su endpoint health) y lanza el escenario smoke contra él. He decidido no lanzar el escenario de rampa ni carga sostenida porque el servidor es gratuito en Render y tiene recursos muy limitados.
- ** Despliegue continuo**: se hace uso de Render integrado con GitHub, de tal forma que cuando se hace push de cualquier cambio al repositorio en su rama main, se lanza un deploy en Render. Está configurado para que lance el deploy a partir del fichero Dockerfile. Para saber cómo integrar Render con GitHub visitar: `https://render.com/docs/github`

## 🐳 Dockerización
- **DockerFile**: se añade un fichero en el cual se construye la aplicación y se empaqueta en un jar (app.jar), no hay que realizar más al tener la base de datos embebida. Este fichero es usado por Render para desplegar la aplicación en su servidor.
- **docker-compose** : añadido para poder levantar la aplicación por ejemplo en local. Hace uso de Dockerfile y expone la aplicación en el puerto 8080.

## 🚀 Instalación y configuración
### Pasos en local
1. Clonar el repositorio
2. Lanzar docker-compose:
   ```
   docker-compose up
   ```
   Si queremos lanzarlo en segundo plano:
   ```
   docker-compose up -d
   ```
3. API estará dispoible en: `http://localhost:8080/prices??brandId=idCadena&productId=idProducto&date=YYYY-MM-DD HH:MM:SS`

### Pasos para render
1. Clonar el repositorio
2. Hacer un push con cualquier cambio (por ejemplo modificar este README)
3. Tras unos minutos el CI/CD lo desplegará en Render.
4. API estará disponible en `https://itx-test-smxc.onrender.com/prices??brandId=idCadena&productId=idProducto&date=YYYY-MM-DD HH:MM:SS`

## 🔮 Mejoras futuras
- **Alta escalabilidad**.
    - **Programación reactiva**: usar enfoque reactivo tanto para la entrada como para la consulta en base de datos. Tengo nociones básicas de Mono y Flux sin embargo no tengo experiencia profesional en ello, es por ello que no lo he utilizado.
    - **Modularizar**: separar las capas de arquitectura hexagonal en diferentes módulos para no solo estar separadas conceptualmente sino también físicamente.
- **Alta disponibilidad**:
    - Usar **base de datos no relacional** replicada como MongoDB para no sobrecargar tanto la base de datos relacional con consultas rápidas no cacheadas.
-  **Rendimiento**:
    - Uso de base de datos no relacional indexada como **MongoDB**
    - **Caché Redis** : usar caché de Redis para que esta no sea solo a nivel local. Montar un sistema donde lea de caché Redis, en caso de no estar tire de caché local, en caso de no estar buscar en MongoDb y por último ya en base de datos relacional.
- **Seguridad**
  - Formular algún mecanismo en el que si se reciben muchas peticiones y se detecta que son fraudulentas, bloquearlas.
  - Credenciales en algún repositorio como **PMP o CyberArk** que no las muestre en texto claro y acceder a ellas por referencias 
- **Concepto**
  - Setear el precio como cantidad + exponente para abarcar más mercados donde el exponente de la moneda no sea solo dos (decimales).
- **Documentable**:
    - **OpenAPI**: improve the OpenAPI documentation with example values, default values, etc.

## 📬 Constribuciones y contacto
- Para contribuir, obtén una **nueva rama** del repositorio y abre una **pull request**.
- Contacto: `miguelcabezaspuerto@gmail.com`

Siempre se puede mejorar pero espero que os guste! 🚀