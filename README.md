# 2024-tienda-g3
IS2024 - Trabajo Final Integrador - Grupo N° 3

## Integrantes
- Abuin, Juan Jose - 49816
- Medina, Antonio Fabrizio - 46246
- Reinoso, Alvaro - 42308

## Tecnologías utilizadas

### Frontend

Aplicación Android Mobile nativa siguiendo el estado de arte de la industria:
- Lenguaje de programación Kotlin
- Framework Android Jetpack
- Arquitectura de presentación MVVM (Model-View-Viewmodel)
- Arquitectura de aplicación Clean Architecture ( Domain / Data / Presentation )

### Backend

- Lenguaje de programación Javascript
- Runtime environment Node.Js
- Application framework Express.Js
- Base de datos MySQL
- Librería sequelize.js para mapeo objeto-relacional
- Librería passport.js para manejo de autenticación y sesiones

## Features

- Autenticación
- Proceso de Venta:
    - Obtener stock de sucursal
    - Generar lineas de venta
    - Asociar clientes:
        - Genérico (Consumidor Final)
        - Nominal
    - Realizar pago:
	- Efectivo
	- Tarjeta - Autorización con servicio externo
    - Emisión de comprobante:
	- Conexión con servicio de autorización de AFIP
	- Comprobantes factura tipo A y B


