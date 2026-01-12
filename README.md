# START SEEING – Sistema de Gestión de Películas

**START SEEING** es una aplicación de escritorio desarrollada en **Java SE y Swing** que permite la gestión de un catálogo de películas con control de usuarios, roles, bitácora de acciones y generación de reportes en **PDF** y **Excel**.

## Estructura del proyecto

- **src/**  
  Contiene el código fuente del sistema (Java y Swing).

- **src/main/resources/**  
  Contiene recursos como imágenes y reportes realizados con JasperReports.

- **DataBase/**  
  Contiene el script de base de datos  
  `start_seeing_db.sql`

- **.gitignore**  
  Se utiliza para ignorar archivos de compilación y configuraciones locales del IDE.

  
## Funcionalidades principales

- Gestión de usuarios y roles (ADMIN, EDITOR, USER)
- Gestión de categorías, directores y películas
- Registro de acciones en bitácora
- Filtros por usuario y fechas en bitácora
- Exportación de reportes de bitácora en PDF y Excel
- Interfaz gráfica desarrollada con Swing

## Requisitos

- Java 20
- MySQL
- IntelliJ IDEA (o cualquier IDE compatible con Java SE)
