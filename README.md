# Exp3Semana8
Resumen del Proyecto
Este proyecto es un sistema de gestión de reservas y ventas de entradas para un evento en el "Teatro Moro". La aplicación simula las funciones clave de un sistema de boletería, incluyendo la asignación de asientos por ubicación (VIP, Platea, Balcón), la aplicación de descuentos y la generación de reportes financieros.

El sistema está desarrollado en Java y utiliza estructuras de datos básicas como Array (para los asientos de la sala) y ArrayList (para clientes, ventas y descuentos).

Estructura y Clases Principales
El sistema se organiza alrededor de la clase principal Exp3_S8_Daniel_Caballero y las siguientes clases internas estáticas que modelan las entidades del negocio:

Asiento: Representa una silla en la sala. Registra el número, la ubicación y el estado.

Venta: Registra una transacción específica. Sus identificadores clave (ID de venta, número de asiento, ID de cliente) son inmutables (final) para asegurar la integridad de la transacción.

Cliente: Representa a la persona que compra la entrada. Su idCliente es inmutable.

Descuento: Define los tipos de beneficios aplicables (e.g., ESTUDIANTE, TERCERA EDAD).

Evento: Contenedor para el evento actual.

Configuración Fija de la Sala
Las capacidades y los precios se definen como constantes (static final) para garantizar la estabilidad del sistema:

Capacidades: VIP (6), Platea (6), Balcón (8).

Precio Base: El precio unitario base es de $10.000 correspondiente al BALCON.

Multiplicadores: Los precios se ajustan usando multiplicadores fijos (ej: VIP es 1.5 veces el precio base y PLATEA 1.2 veces).

Funcionalidades Detalladas del Menú
El menú principal ofrece las siguientes opciones, que cubren el ciclo de vida completo de la gestión de entradas:

1. Vender Entradas
Esta función gestiona la creación de una nueva venta.

Disponibilidad: Muestra los asientos vendidos por sección y la capacidad restante.

Selección: Permite elegir la ubicación (VIP, Platea o Balcón) y un número de asiento disponible.

Datos: Registra el nombre del cliente y aplica un descuento elegible.

Registro: Calcula el costo final, registra un nuevo objeto Venta y marca el Asiento como vendido.

2. Actualizar Venta
Permite modificar los detalles de una venta ya registrada, buscándola por su ID de Venta.

Edición: Permite cambiar el nombre del cliente y/o el tipo de descuento.

Recálculo: Si se modifica el descuento, la opción 4 (Recalcular precio) recalcula el costo final y actualiza automáticamente el totalIngresos global.

(Nota: No permite cambiar el asiento asociado a la venta. En este caso se recomienda eliminar la venta asociada al asiento y volver agregar.)

3. Anular Venta
Permite revertir una transacción buscando por su ID de Venta.

Eliminación: Remueve la Venta de las listas de transacciones.

Liberación: Marca el Asiento asociado de nuevo como DISPONIBLE.

Ajuste: Actualiza los contadores globales: resta el monto de la venta al totalIngresos y disminuye las cuentas de entradas vendidas.

4. Reporte Ingresos Totales
Genera un resumen financiero:

Muestra cuántas entradas se han vendido en cada ubicación vs. su capacidad.

Muestra las estadísticas globales de totalEntradasVendidas y el totalIngresos acumulado.

5. Reporte Resumen de Ventas
Muestra un listado detallado de todas las transacciones realizadas (ventasTransaccionales).

Incluye el ID de Venta, Número de Asiento, Ubicación, ID de Cliente, Fecha, Costo Base y Costo Final.

6. Imprimir Boleta
Busca una venta por su ID de Venta y muestra un formato de boleta electrónica.

Detalla el cliente, el asiento, el costo base, el monto del descuento aplicado (en porcentaje y dinero) y el Total Pagado.

7. Validar Referencias Cruzadas (Integridad de Datos)
Esta función de diagnóstico fue incluida para verificar la coherencia interna del sistema.

Verificación Asiento-Venta: Asegura que los asientos marcados como "VENDIDO" tengan una Venta activa y viceversa.

Duplicidad: Detecta si hay clientes con el mismo nombre que fueron registrados con diferentes tipos de descuentos o asientos.

Ejecución del Proyecto
IDE: El código está diseñado para ejecutarse en Java (se recomienda un IDE como Apache NetBeans).

Inicio: Compile y ejecute la clase principal Exp3_S8_Daniel_Caballero.java.

Interacción: La aplicación se maneja completamente a través de la consola/terminal, solicitando datos al usuario para cada operación.
