# Sistema de Gestión de Almacenes

API REST desarrollada con Spring Boot para la gestión de compra y venta de almacenes.

## 🚀 Características

- **CRUD completo** para Sedes, Almacenes y Clientes
- **Principios SOLID** implementados
- **Principios de Seguridad** aplicados
- **Frontend responsive** con HTML, CSS y JavaScript
- **Base de datos MySQL** con JPA/Hibernate
- **Validación de datos** en frontend y backend

## 📋 Principios SOLID Implementados

### 1. Single Responsibility Principle (SRP)
- **ValidationService**: Se encarga únicamente de la validación de datos
- Cada servicio tiene una responsabilidad específica

### 2. Dependency Inversion Principle (DIP)
- Los servicios dependen de abstracciones (interfaces de repositorio)
- Uso de inyección de dependencias con `@Autowired`

## 🔒 Principios de Seguridad Implementados

### 1. Input Validation
- Validación de longitud, formato y tipo de datos
- Sanitización de entradas para prevenir ataques
- Uso de whitelist/blacklist en ValidationService

### 2. Exception Control
- Los errores técnicos no se muestran al usuario final
- Manejo seguro de excepciones en controladores
- Respuestas HTTP apropiadas sin información sensible

### 3. Injection Prevention
- Sanitización de inputs para prevenir XSS
- Uso de consultas parametrizadas con JPA
- Escape de caracteres especiales

### 4. Secured by Default
- Configuración inicial segura
- Ocultación de información sensible en logs
- Configuración de CORS restrictiva

## 🛠️ Tecnologías Utilizadas

- **Spring Boot 3.5.0**
- **Java 17**
- **MySQL**
- **JPA/Hibernate**
- **Lombok**
- **HTML/CSS/JavaScript**

## 📦 Instalación y Configuración

### Prerrequisitos
- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### Pasos de instalación

1. **Clonar el repositorio**
```bash
git clone <tu-repositorio>
cd Actividad3_4
```

2. **Configurar la base de datos**
- Crear una base de datos MySQL llamada `almacenes_db`
- Actualizar credenciales en `application.properties` si es necesario

3. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

4. **Acceder a la aplicación**
- Backend API: `http://localhost:8080/api`
- Frontend: Abrir `index.html` en un navegador

## 🔗 Endpoints de la API

### Sedes
- `GET /api/sedes` - Obtener todas las sedes
- `GET /api/sedes/{id}` - Obtener sede por ID
- `POST /api/sedes` - Crear nueva sede
- `PUT /api/sedes/{id}` - Actualizar sede
- `DELETE /api/sedes/{id}` - Eliminar sede

### Almacenes
- `GET /api/almacenes` - Obtener todos los almacenes
- `GET /api/almacenes/{id}` - Obtener almacén por ID
- `POST /api/almacenes` - Crear nuevo almacén
- `PUT /api/almacenes/{id}` - Actualizar almacén
- `DELETE /api/almacenes/{id}` - Eliminar almacén

### Clientes
- `GET /api/clientes` - Obtener todos los clientes
- `GET /api/clientes/{id}` - Obtener cliente por ID
- `POST /api/clientes` - Crear nuevo cliente
- `PUT /api/clientes/{id}` - Actualizar cliente
- `DELETE /api/clientes/{id}` - Eliminar cliente

## 📊 Estructura de Datos

### Sede
```json
{
  "id": 1,
  "clave": "C1-16062025-1234",
  "estado": "Morelos",
  "municipio": "Cuernavaca"
}
```

### Almacén
```json
{
  "id": 1,
  "clave": "C1-16062025-1234-A1",
  "fechaRegistro": "2025-06-16",
  "precioVenta": 50000.00,
  "precioRenta": 5000.00,
  "tamaño": "G",
  "sede": {
    "id": 1
  }
}
```

### Cliente
```json
{
  "id": 1,
  "nombreCompleto": "Juan Pérez García",
  "numeroTelefono": "7771234567",
  "correoElectronico": "juan.perez@email.com"
}
```

## 🎨 Frontend

El frontend incluye:
- **Interfaz responsiva** con diseño moderno
- **Validación de formularios** en tiempo real
- **Gestión de errores** user-friendly
- **Animaciones CSS** para mejor experiencia de usuario
- **Sanitización de inputs** para prevenir XSS

## 🔧 Configuración Avanzada

### Base de Datos
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/almacenes_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

### Puertos
```properties
server.port=8080
```

## 🧪 Pruebas

Para probar la API puedes usar:
- **Postman** o **Insomnia** para probar endpoints
- **Frontend incluido** para pruebas visuales
- **MySQL Workbench** para verificar datos

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - mira el archivo [LICENSE.md](LICENSE.md) para detalles.

## ✨ Características Adicionales

- **Generación automática de claves** para sedes y almacenes
- **Validación robusta** en frontend y backend
- **Manejo de errores** sin exposición de información técnica
- **Diseño responsive** para diferentes dispositivos
- **Código limpio** siguiendo buenas prácticas