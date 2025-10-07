package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.text.DecimalFormat;

public class Exp3_S8_Daniel_Caballero {

    // --- CONFIGURACIÓN DE CAPACIDAD Y PRECIOS ---
    
    // Distribución de Asientos por Ubicación (Fijas)
    static final int CAPACIDAD_VIP = 6;
    static final int CAPACIDAD_PLATEA = 6;
    static final int CAPACIDAD_BALCON = 8; 
    
    // La capacidad total será determinada por la suma de las tres capacidades anteriores.
    private final int CAPACIDAD_SALA; 
    
    double precioUnitario = 10000.0;
    
    // Multiplicadores de Precio Base
    static final double MULT_VIP = 1.5;
    static final double MULT_PLATEA = 1.2;

    // --- VARIABLES GLOBALES Y CONTADORES POR SECCIÓN ---
    static int totalEntradasVendidas = 0;
    static double totalIngresos = 0;
    static int vendidasVIP = 0; 
    static int vendidasPLATEA = 0; 
    static int vendidasBALCON = 0; 
    
    static final String NOMBRE_TEATRO = "Teatro Moro";
    static DecimalFormat df = new DecimalFormat("#.##");
    Scanner scanner = new Scanner(System.in);

    // --- ESTRUCTURAS DE DATOS ---

    enum Estado {
        DISPONIBLE, VENDIDO
    }

    // Clase Asiento
    static class Asiento {
        private int numero;
        private Estado estado;
        private String ubicacion; 
        private Integer idVentaAsociada; 

        Asiento(int numero, String ubicacion) {
            this.numero = numero;
            this.estado = Estado.DISPONIBLE;
            this.ubicacion = ubicacion;
            this.idVentaAsociada = null;
        }
        
        // Getters
        public int getNumero() { 
            return numero; 
        }
        public Estado getEstado() { 
            return estado; 
        }
        public String getUbicacion() { 
            return ubicacion; 
        }
        public Integer getIdVentaAsociada() { 
            return idVentaAsociada; 
        }
        
        // Setters
        public void setEstado(Estado estado) { 
            this.estado = estado; 
        }
        public void setUbicacion(String ubicacion) { 
            this.ubicacion = ubicacion; 
        } 
        public void setIdVentaAsociada(Integer idVentaAsociada) { 
            this.idVentaAsociada = idVentaAsociada; 
        }
    }

    // Clase Venta
    static class Venta {
        static int contadorVentas = 1000;
        private final int idVenta; 
        private final int numeroAsiento; 
        private final int idCliente; 
        private final String fecha; 
        private double costoBase;
        private double descuentoAplicadoPorc;
        private double precioFinal;

        Venta(int numeroAsiento, int idCliente, double costoBase, double descuentoAplicadoPorc, double precioFinal) {
            this.idVenta = contadorVentas++;
            this.numeroAsiento = numeroAsiento;
            this.idCliente = idCliente;
            this.fecha = java.time.LocalDate.now().toString();
            this.costoBase = costoBase;
            this.descuentoAplicadoPorc = descuentoAplicadoPorc;
            this.precioFinal = precioFinal;
        }

        // Getters
        public int getIdVenta() { 
            return idVenta; 
        }
        public int getNumeroAsiento() { 
            return numeroAsiento; 
        }
        public double getPrecioFinal() { 
            return precioFinal; 
        }
        public int getIdCliente() { 
            return idCliente; 
        } 
        public double getCostoBase() { 
            return costoBase; 
        }
        public double getDescuentoAplicadoPorc() { 
            return descuentoAplicadoPorc; }
        public String getFecha() { 
            return fecha; 
        } 

        // Setters 
        public void setCostoBase(double costoBase) { 
            this.costoBase = costoBase; 
        }
        public void setDescuentoAplicadoPorc(double descuentoAplicadoPorc) { 
            this.descuentoAplicadoPorc = descuentoAplicadoPorc; 
        }
        public void setPrecioFinal(double precioFinal) { 
            this.precioFinal = precioFinal; 
        }
    }

    // Clase Cliente 
    static class Cliente {
        static int contadorClientes = 1;
        private final int idCliente; 
        private String nombre;
        private String tipoDescuento; 

        Cliente(String nombre, String tipoDescuento) {
            this.idCliente = contadorClientes++;
            this.nombre = nombre;
            this.tipoDescuento = tipoDescuento;
        }

        // Getters
        public int getIdCliente() { 
            return idCliente; 
        }
        public String getNombre() { 
            return nombre; 
        }
        public String getTipoDescuento() { 
            return tipoDescuento; 
        }

        // Setters
        public void setNombre(String nombre) { 
            this.nombre = nombre; 
        }
        public void setTipoDescuento(String tipoDescuento) { 
            this.tipoDescuento = tipoDescuento; 
        }
    }

    // Clase Descuento
    static class Descuento {
        private String tipo;
        private double porcentaje;

        Descuento(String tipo, double porcentaje) {
            this.tipo = tipo;
            this.porcentaje = porcentaje;
        }

        // Getters
        public String getTipo() { 
            return tipo; 
        }
        public double getPorcentaje() { 
            return porcentaje; 
        }
    }

    // Clase Evento
    static class Evento {
        private String nombreEvento;
        private List<Venta> listaVentas; 

        Evento(String nombreEvento) {
            this.nombreEvento = nombreEvento;
            this.listaVentas = new ArrayList<>();
        }

        public String getNombreEvento() { 
            return nombreEvento; 
        }
        public List<Venta> getListaVentas() { 
            return listaVentas; 
        }
    }

    // ESTRUCTURAS PRINCIPALES
    private Asiento[] asientosSala; 
    private List<Cliente> clientesRegistrados = new ArrayList<>(); 
    private List<Venta> ventasTransaccionales = new ArrayList<>(); 
    private List<Descuento> listaDescuentos = new ArrayList<>(); 
    private Evento eventoActual;


    public Exp3_S8_Daniel_Caballero() {
        this.CAPACIDAD_SALA = CAPACIDAD_VIP + CAPACIDAD_PLATEA + CAPACIDAD_BALCON;
        this.asientosSala = new Asiento[CAPACIDAD_SALA];
        
        // --- INICIALIZACIÓN Y ASIGNACIÓN DE UBICACIONES AL ARREGLO ---
        int idx = 0;
        for (int i = 0; i < CAPACIDAD_VIP; i++) {
            this.asientosSala[idx++] = new Asiento(idx, "VIP");
        }
        for (int i = 0; i < CAPACIDAD_PLATEA; i++) {
            this.asientosSala[idx++] = new Asiento(idx, "Platea");
        }
        for (int i = 0; i < CAPACIDAD_BALCON; i++) {
            this.asientosSala[idx++] = new Asiento(idx, "Balcón");
        }
        
        // Inicialización de descuentos
        listaDescuentos.add(new Descuento("ESTUDIANTE", 0.10)); 
        listaDescuentos.add(new Descuento("TERCERA EDAD", 0.15)); 
        listaDescuentos.add(new Descuento("NINGUNO", 0.00)); 

        eventoActual = new Evento("Función Especial Teatro Moro");
    }

    // --- MÉTODOS DE BÚSQUEDA ---

    private Venta buscarVentaPorId(int idVenta) {
        for (Venta v : ventasTransaccionales) {
            if (v.getIdVenta() == idVenta) {
                return v;
            }
        }
        return null;
    }
    
    private Cliente buscarClientePorId(int idCliente) {
        for (Cliente c : clientesRegistrados) {
            if (c.getIdCliente() == idCliente) {
                return c;
            }
        }
        return null;
    }
    
    /**
     * Busca el ID de la primera venta asociada a un Cliente ID.
     * @param idCliente El ID del cliente.
     * @return El ID de Venta o null si no se encuentra.
     */
    private Integer buscarIdVentaPorCliente(int idCliente) {
        for (Venta v : ventasTransaccionales) {
            if (v.getIdCliente() == idCliente) {
                return v.getIdVenta();
            }
        }
        return null;
    }

    private Descuento buscarDescuentoPorTipo(String tipo) {
         for (Descuento d : listaDescuentos) {
            if (d.getTipo().equals(tipo)) {
                return d;
            }
        }
        return null;
    }

    // --- LÓGICA DE RECALCULO DE PRECIO ---

    private double calcularCostoBase(String ubicacion) {
        return switch (ubicacion) {
            case "VIP" -> precioUnitario * MULT_VIP;
            case "Platea" -> precioUnitario * MULT_PLATEA;
            default -> precioUnitario; // Balcón
        };
    }

    private void recalcularPrecioVenta(Venta venta, Asiento asiento, Cliente cliente) {
        double nuevoCostoBase = calcularCostoBase(asiento.getUbicacion());
        double descuentoAplicado = 0.0;
        
        if (cliente != null) {
            Descuento desc = buscarDescuentoPorTipo(cliente.getTipoDescuento());
            if (desc != null) {
                descuentoAplicado = desc.getPorcentaje();
            }
        }
        
        double montoDescuento = nuevoCostoBase * descuentoAplicado;
        double nuevoPrecioFinal = nuevoCostoBase - montoDescuento;

        totalIngresos -= venta.getPrecioFinal(); 
        totalIngresos += nuevoPrecioFinal;      
        
        venta.setCostoBase(nuevoCostoBase);
        venta.setDescuentoAplicadoPorc(descuentoAplicado);
        venta.setPrecioFinal(nuevoPrecioFinal);

        System.out.println("\n[DATOS ACTUALIZADOS] Nuevo Costo Base: $" + df.format(nuevoCostoBase) + 
                           " | Nuevo Descuento: " + df.format(descuentoAplicado * 100) + "%" +
                           " | Nuevo Precio Final: $" + df.format(nuevoPrecioFinal));
    }


    // --- FUNCIONALIDADES DE MANIPULACIÓN DE DATOS ---

    /**
     * Permite la venta de una entrada, comenzando por la selección de la ubicación
     * y mostrando los asientos disponibles.
     */
    void venderEntradas() {
        System.out.println("\n--- Venta de Entrada ---");

        if (totalEntradasVendidas >= CAPACIDAD_SALA) { 
             System.out.println("Sala llena. Venta cancelada.");
             return;
        }
        
        // 1. Mostrar disponibilidad y seleccionar ubicación
        System.out.println("\n--- DISPONIBILIDAD POR SECCIÓN ---");
        System.out.printf("1. VIP: %d/%d vendidos\n", vendidasVIP, CAPACIDAD_VIP);
        System.out.printf("2. Platea: %d/%d vendidos\n", vendidasPLATEA, CAPACIDAD_PLATEA);
        System.out.printf("3. Balcón: %d/%d vendidos\n", vendidasBALCON, CAPACIDAD_BALCON);
        System.out.print("Seleccione la ubicación (1/2/3): ");
        
        String opcionUbicacion = scanner.nextLine().trim();
        String ubicacionElegida;
        int maxCapacidadUbicacion;

        switch (opcionUbicacion) {
            case "1" -> { 
                ubicacionElegida = "VIP"; 
                maxCapacidadUbicacion = CAPACIDAD_VIP; 
                if (vendidasVIP >= maxCapacidadUbicacion) { 
                    System.out.println("La sección VIP está completa."); 
                    return; 
                } 
            }
            case "2" -> { 
                ubicacionElegida = "Platea"; 
                maxCapacidadUbicacion = CAPACIDAD_PLATEA; 
                if (vendidasPLATEA >= maxCapacidadUbicacion) { 
                    System.out.println("La sección Platea está completa."); 
                    return; } 
            }
            case "3" -> { 
                ubicacionElegida = "Balcón"; 
                maxCapacidadUbicacion = CAPACIDAD_BALCON; 
                if (vendidasBALCON >= maxCapacidadUbicacion) { 
                    System.out.println("La sección Balcón está completa."); 
                    return; 
                } 
            }
            default -> { 
                System.out.println("Opción de ubicación inválida."); 
                return; 
            }
        }

        // 2. Filtrar y mostrar asientos DISPONIBLES de la ubicación elegida
        List<Asiento> asientosDisponibles = new ArrayList<>();
        StringBuilder asientosListado = new StringBuilder();

        for (Asiento a : asientosSala) {
            if (a.getUbicacion().equals(ubicacionElegida) && a.getEstado() == Estado.DISPONIBLE) {
                asientosDisponibles.add(a);
                asientosListado.append(a.getNumero()).append(" ");
            }
        }
        
        if (asientosDisponibles.isEmpty()) {
            System.out.println("Error: No quedan asientos disponibles en " + ubicacionElegida + ".");
            return;
        }
        
        // Mostrar los números de asiento disponibles
        System.out.println("\nAsientos DISPONIBLES en " + ubicacionElegida + ":");
        System.out.println("-> " + asientosListado.toString().trim());

        // 3. Elegir asiento
        int numAsiento;
        Asiento asientoActual = null;
        
        System.out.print("Ingrese el número de asiento a vender: ");
        
        try {
            numAsiento = Integer.parseInt(scanner.nextLine().trim());
            
            if (numAsiento < 1 || numAsiento > CAPACIDAD_SALA) {
                System.out.println("Asiento fuera de rango general.");
                return;
            }
            
            asientoActual = asientosSala[numAsiento - 1];
            
            if (asientoActual.getEstado() == Estado.VENDIDO) {
                System.out.println("El Asiento N°" + numAsiento + " ya está vendido.");
                return;
            }
            if (!asientoActual.getUbicacion().equals(ubicacionElegida)) {
                System.out.println("Error: El asiento N°" + numAsiento + " no pertenece a la sección " + ubicacionElegida + ". Venta cancelada.");
                return;
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Número de asiento inválido.");
            return;
        }

        double costoBaseLocal = calcularCostoBase(ubicacionElegida);
        String tipoCliente = "NINGUNO";
        double descuentoAplicado = 0.0;
        
        System.out.println("\nAsiento seleccionado: N°" + numAsiento + " en " + ubicacionElegida + 
                           " (Costo Base: $" + df.format(costoBaseLocal) + ")");


        // 4. Cliente y Descuento
        System.out.print("Ingrese nombre del cliente: ");
        String nombreCliente = scanner.nextLine().trim();

        System.out.println("\n¿Aplica algún descuento?");
        for (int i = 0; i < listaDescuentos.size(); i++) { 
            Descuento d = listaDescuentos.get(i);
            System.out.println((i + 1) + ". " + d.getTipo() + " (" + df.format(d.getPorcentaje() * 100) + "% desc.)");
        }
        System.out.print("Opción: ");
        String tipoOpcion = scanner.nextLine().trim();
        int descIndex = -1;

        try {
            descIndex = Integer.parseInt(tipoOpcion) - 1;
        } catch (NumberFormatException e) {
            // Ignorar
        }

        if (descIndex >= 0 && descIndex < listaDescuentos.size()) {
            Descuento desc = listaDescuentos.get(descIndex);
            tipoCliente = desc.getTipo();
            descuentoAplicado = desc.getPorcentaje();
        }


        double montoDescuento = costoBaseLocal * descuentoAplicado;
        double precioFinalLocal = costoBaseLocal - montoDescuento;

        // 5. Confirmación y Registro 
        System.out.println("\n--- Resumen de la compra ---");
        System.out.println("Ubicación: " + ubicacionElegida + " | Asiento: " + numAsiento);
        System.out.println("Cliente: " + nombreCliente + " | Descuento: " + tipoCliente);
        System.out.printf("Costo Base: $%s | Descuento Aplicado: -$%s\n", df.format(costoBaseLocal), df.format(montoDescuento));
        System.out.println("Total a pagar: $" + df.format(precioFinalLocal));

        System.out.print("\n¿Desea confirmar la compra? (S/N): ");
        String confirmar = scanner.nextLine().trim().toUpperCase();

        if (!confirmar.equals("S")) {
            System.out.println("Compra cancelada.");
        } else {
            Cliente cliente = new Cliente(nombreCliente, tipoCliente);
            clientesRegistrados.add(cliente);

            Venta venta = new Venta(numAsiento, cliente.getIdCliente(), costoBaseLocal, descuentoAplicado, precioFinalLocal);
            ventasTransaccionales.add(venta);
            eventoActual.getListaVentas().add(venta);

            asientoActual.setEstado(Estado.VENDIDO);
            asientoActual.setIdVentaAsociada(venta.getIdVenta());

            // Actualización de contadores por sección
            totalEntradasVendidas++;
            totalIngresos += precioFinalLocal;
            switch (ubicacionElegida) {
                case "VIP" -> vendidasVIP++;
                case "Platea" -> vendidasPLATEA++;
                case "Balcón" -> vendidasBALCON++;
            }

            System.out.println("Compra confirmada. Asiento " + numAsiento + " vendido (ID Venta: " + venta.getIdVenta() + ").");
        }
    }


    void actualizarVenta() {
        System.out.println("\n--- ACTUALIZACIÓN DE VENTA ---");
        System.out.print("Ingrese ID de la venta a modificar: ");
        int idVenta;
        try {
            idVenta = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID de venta inválido.");
            return;
        }

        Venta venta = buscarVentaPorId(idVenta);
        if (venta == null) {
            System.out.println("Venta no encontrada.");
            return;
        }
        
        Cliente cliente = buscarClientePorId(venta.getIdCliente()); 
        Asiento asiento = asientosSala[venta.getNumeroAsiento() - 1];

        if (cliente == null || asiento == null) {
            System.out.println("Error: No se encontró el cliente o asiento asociado. Venta inconsistente.");
            return;
        }

        while (true) {
            System.out.println("\nEDITAR VENTA ID: " + venta.getIdVenta());
            System.out.println("---------------------------------------------");
            System.out.printf("1. Editar Nombre Cliente (Actual: %s)\n", cliente.getNombre());
            System.out.printf("2. Editar Ubicación/Asiento (Actual: %s)\n", asiento.getUbicacion());
            System.out.printf("3. Editar Descuento (Actual: %s - %s%%)\n", cliente.getTipoDescuento(), df.format(venta.getDescuentoAplicadoPorc() * 100));
            System.out.println("4. Recalcular precio (Si ya modificó algo)");
            System.out.println("5. Salir sin Recalcular");
            System.out.print("Opción de Edición: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1" -> {
                    System.out.print("Nuevo nombre del cliente: ");
                    String nuevoNombre = scanner.nextLine().trim();
                    if (!nuevoNombre.isEmpty()) {
                        cliente.setNombre(nuevoNombre); 
                        System.out.println("Nombre actualizado.");
                    }
                }
                case "2" -> {
                    System.out.println("AVISO: La ubicación está fijada por el asiento N°" + asiento.getNumero() + ". No es posible cambiar el asiento en una venta existente.");
                }
                case "3" -> {
                    System.out.println("Elija nuevo Descuento:");
                    for (int i = 0; i < listaDescuentos.size(); i++) {
                        Descuento d = listaDescuentos.get(i);
                        System.out.println((i + 1) + ". " + d.getTipo() + " (" + df.format(d.getPorcentaje() * 100) + "% desc.)");
                    }
                    System.out.print("Opción: ");
                    int descIndex;
                    try {
                        descIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
                    } catch (NumberFormatException e) {
                        System.out.println("Opción inválida."); break;
                    }

                    if (descIndex >= 0 && descIndex < listaDescuentos.size()) {
                        Descuento nuevoDesc = listaDescuentos.get(descIndex);
                        cliente.setTipoDescuento(nuevoDesc.getTipo()); 
                        System.out.println("Descuento actualizado a " + nuevoDesc.getTipo());
                    }
                }
                case "4" -> {
                    recalcularPrecioVenta(venta, asiento, cliente);
                    System.out.println("Venta actualizada y precios recalculados.");
                    return; 
                }
                case "5" -> {
                    System.out.println("Saliendo de la edición sin guardar cambios de precio.");
                    return;
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }


    void eliminarVenta() {
        System.out.println("\n--- ELIMINACIÓN/ANULACIÓN DE VENTA ---");
        System.out.print("Ingrese ID de la venta a anular: ");
        int idVenta;
        try {
            idVenta = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID de venta inválido.");
            return;
        }

        Venta ventaAEliminar = buscarVentaPorId(idVenta);

        if (ventaAEliminar == null) {
            System.out.println("Venta no encontrada.");
            return;
        }

        Asiento asiento = asientosSala[ventaAEliminar.getNumeroAsiento() - 1];
        String ubicacionLiberada = asiento.getUbicacion();
        
        if (asiento.getEstado() == Estado.VENDIDO && asiento.getIdVentaAsociada() != null && asiento.getIdVentaAsociada().equals(idVenta)) {
            asiento.setEstado(Estado.DISPONIBLE);
            asiento.setIdVentaAsociada(null); 
            System.out.println("Asiento " + asiento.getNumero() + " liberado.");
        } else {
             System.out.println("Advertencia de coherencia: El asiento " + asiento.getNumero() + " no estaba marcado correctamente. Se anulará solo la venta.");
        }

        totalIngresos -= ventaAEliminar.getPrecioFinal();
        totalEntradasVendidas--;
        switch (ubicacionLiberada) {
            case "VIP" -> vendidasVIP--;
            case "Platea" -> vendidasPLATEA--;
            case "Balcón" -> vendidasBALCON--;
        }

        ventasTransaccionales.remove(ventaAEliminar);
        eventoActual.getListaVentas().remove(ventaAEliminar);
        
        System.out.println("Venta " + idVenta + " anulada exitosamente.");
    }

    // --- REPORTES ---
    
    void calcularIngresosTotales() {
        System.out.println("\n..:: INGRESOS TOTALES ACUMULADOS ::..");
        
        System.out.println("--- VENDIDAS POR SECCIÓN ---");
        System.out.printf("  VIP: %d de %d\n", vendidasVIP, CAPACIDAD_VIP);
        System.out.printf("  PLATEA: %d de %d\n", vendidasPLATEA, CAPACIDAD_PLATEA);
        System.out.printf("  BALCÓN: %d de %d\n", vendidasBALCON, CAPACIDAD_BALCON);
        System.out.println("------------------------------------");
        System.out.println("Total entradas vendidas: " + totalEntradasVendidas);
        System.out.println("Capacidad Total de Sala: " + CAPACIDAD_SALA); 
        System.out.println("Ingresos Totales: $" + df.format(totalIngresos));
        System.out.println("....................................");
    }

    void visualizarResumenVentas() {
        System.out.println("\n..:: RESUMEN DE TODAS LAS VENTAS ::..");
        if (ventasTransaccionales.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        System.out.printf("%-10s %-10s %-10s %-12s %-15s %-10s %-12s\n", 
                      "ID Venta", "Asiento", "Ubicacion", "ID Cliente", "Fecha", "Costo Base", "Costo Final");
        System.out.println("----------------------------------------------------------------------------------------");
        
        for (Venta venta : ventasTransaccionales) {
            Asiento asiento = asientosSala[venta.getNumeroAsiento() - 1];
            String ubicacionAsiento = asiento.getUbicacion();
            
            System.out.printf("%-10d %-10d %-10s %-12d %-15s %-10s %-12s\n", 
                    venta.getIdVenta(), 
                    venta.getNumeroAsiento(), 
                    ubicacionAsiento, 
                    venta.getIdCliente(), 
                    venta.getFecha(),
                    "$" + df.format(venta.getCostoBase()), 
                    "$" + df.format(venta.getPrecioFinal()));
        }
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("Total entradas vendidas: " + totalEntradasVendidas);
    }
    
    void imprimirBoleta() {
        System.out.println("\n--- IMPRESIÓN DE BOLETA ---");
        System.out.print("Ingrese ID de la venta para imprimir la boleta: ");
        int idVenta;
        try {
            idVenta = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID de venta inválido.");
            return;
        }
        
        Venta venta = buscarVentaPorId(idVenta);
        if (venta == null) {
            System.out.println("Venta ID " + idVenta + " no encontrada.");
            return;
        }
        
        Cliente cliente = buscarClientePorId(venta.getIdCliente());
        Asiento asiento = asientosSala[venta.getNumeroAsiento() - 1];
        
        double costoBase = venta.getCostoBase();
        double descuentoMonto = costoBase * venta.getDescuentoAplicadoPorc();
        double totalAPagar = venta.getPrecioFinal();

        System.out.println("\n=============================================");
        System.out.println("           BOLETA ELECTRÓNICA DE VENTA       ");
        System.out.println("=============================================");
        System.out.println("TEATRO: " + NOMBRE_TEATRO);
        System.out.println("EVENTO: " + eventoActual.getNombreEvento());
        System.out.println("ID VENTA: " + venta.getIdVenta());
        System.out.println("---------------------------------------------");
        
        if (cliente != null) {
            System.out.println("CLIENTE: " + cliente.getNombre());
            System.out.printf("TIPO DESC.: %s (%s%%)\n", cliente.getTipoDescuento(), df.format(venta.getDescuentoAplicadoPorc() * 100));
        } else {
            System.out.println("CLIENTE: [Error de Referencia]");
        }
        System.out.println("---------------------------------------------");
        
        System.out.println("DETALLE DE LA ENTRADA:");
        System.out.printf("  Asiento N°: %d (%s)\n", asiento.getNumero(), asiento.getUbicacion());
        System.out.printf("  Costo Base: $%s\n", df.format(costoBase));
        System.out.printf("  Monto Descuento: -$%s\n", df.format(descuentoMonto));
        System.out.println("---------------------------------------------");
        System.out.printf("  TOTAL PAGADO: $%s\n", df.format(totalAPagar));
        System.out.println("=============================================\n");
    }

    /**
     * Revisa la coherencia entre asientos y ventas, y detecta clientes duplicados con beneficios y/o ubicaciones distintas.
     */
    void validarReferenciasCruzadas() {
        System.out.println("\n--- VALIDACIÓN DE REFERENCIAS CRUZADAS Y DUPLICIDAD DE CLIENTES ---");
        boolean inconsistenciaEncontrada = false;
        boolean duplicidadBeneficio = false;
        boolean duplicidadUbicacion = false;
        
        // --- PARTE 1: VALIDACIÓN DE COHERENCIA ASIENTO-VENTA ---
        System.out.println("\n--- VERIFICACIÓN DE COHERENCIA ASIENTO-VENTA ---");

        for (Asiento asiento : asientosSala) {
            // Chequeo 1: Asiento Vendido debe tener Venta Activa
            if (asiento.getEstado() == Estado.VENDIDO) {
                boolean ventaActiva = false;
                if (asiento.getIdVentaAsociada() != null) { 
                    Venta ventaAsociada = buscarVentaPorId(asiento.getIdVentaAsociada());
                    if (ventaAsociada != null && ventaAsociada.getNumeroAsiento() == asiento.getNumero()) {
                        ventaActiva = true;
                    }
                }
                if (!ventaActiva) {
                    System.err.println("INCONSISTENCIA (1): Asiento " + asiento.getNumero() + " VENDIDO pero la Venta ID " + asiento.getIdVentaAsociada() + " no existe o no coincide.");
                    inconsistenciaEncontrada = true;
                }
            // Chequeo 2: Asiento Disponible no debe tener Venta Asociada
            } else if (asiento.getEstado() == Estado.DISPONIBLE && asiento.getIdVentaAsociada() != null) {
                System.err.println("INCONSISTENCIA (2): Asiento " + asiento.getNumero() + " DISPONIBLE pero tiene una referencia de Venta ID " + asiento.getIdVentaAsociada() + ".");
                inconsistenciaEncontrada = true;
            }
        }
        
        // Chequeo 3: Venta debe referenciar a un Asiento Vendido con su ID
        for (Venta venta : ventasTransaccionales) {
            Asiento asientoRef = asientosSala[venta.getNumeroAsiento() - 1];
            if (asientoRef.getEstado() != Estado.VENDIDO || asientoRef.getIdVentaAsociada() == null || !asientoRef.getIdVentaAsociada().equals(venta.getIdVenta())) {
                 System.err.println("INCONSISTENCIA (3): Venta ID " + venta.getIdVenta() + " referencia al Asiento " + venta.getNumeroAsiento() + ", pero este no está marcado como VENDIDO con su ID.");
                 inconsistenciaEncontrada = true;
            }
        }

        // --- PARTE 2: VERIFICACIÓN DE CLIENTES REPETIDOS CON DATOS DISTINTOS ---
        System.out.println("\n--- VERIFICACIÓN DE DUPLICIDAD DE CLIENTES ---");
        
        for (int i = 0; i < clientesRegistrados.size(); i++) {
            Cliente cliente1 = clientesRegistrados.get(i);
            
            for (int j = i + 1; j < clientesRegistrados.size(); j++) {
                Cliente cliente2 = clientesRegistrados.get(j);
                
                boolean nombresIguales = cliente1.getNombre().trim().equalsIgnoreCase(cliente2.getNombre().trim());
                
                if (nombresIguales) {
                    Integer ventaId1 = buscarIdVentaPorCliente(cliente1.getIdCliente());
                    Integer ventaId2 = buscarIdVentaPorCliente(cliente2.getIdCliente());
                    
                    // Chequeo A: Beneficio Distinto
                    boolean beneficiosDiferentes = !cliente1.getTipoDescuento().equals(cliente2.getTipoDescuento());
                    if (beneficiosDiferentes) {
                        duplicidadBeneficio = true;
                        System.out.println("\nDUPLICIDAD DETECTADA (Beneficio Diferente):");
                        
                        String detalle1 = (ventaId1 != null ? "Venta ID: " + ventaId1 : "Sin Venta");
                        String detalle2 = (ventaId2 != null ? "Venta ID: " + ventaId2 : "Sin Venta");

                        System.out.println("  Cliente A: Nombre: " + cliente1.getNombre() + ", Beneficio: " + cliente1.getTipoDescuento() + " (" + detalle1 + ")");
                        System.out.println("  Cliente B: Nombre: " + cliente2.getNombre() + ", Beneficio: " + cliente2.getTipoDescuento() + " (" + detalle2 + ")");
                    }
                    
                    // Chequeo B: Ubicaciones Distintas
                    if (ventaId1 != null && ventaId2 != null) {
                        Asiento asiento1 = asientosSala[buscarVentaPorId(ventaId1).getNumeroAsiento() - 1];
                        Asiento asiento2 = asientosSala[buscarVentaPorId(ventaId2).getNumeroAsiento() - 1];
                        
                        boolean ubicacionesDiferentes = !asiento1.getUbicacion().equals(asiento2.getUbicacion());
                        
                        if (ubicacionesDiferentes) {
                            duplicidadUbicacion = true;
                            System.out.println("\nDUPLICIDAD DETECTADA (Ubicación Múltiple):");
                            System.out.println("  El cliente '" + cliente1.getNombre() + "' tiene entradas en:");
                            System.out.println("  - Venta ID " + ventaId1 + ": Ubicación " + asiento1.getUbicacion());
                            System.out.println("  - Venta ID " + ventaId2 + ": Ubicación " + asiento2.getUbicacion());
                            System.out.println("  (Esto puede indicar una compra múltiple en diferentes secciones).");
                        }
                    }
                }
            }
        }
        
        if (duplicidadBeneficio) {
            System.out.print("\n¿Desea actualizar una VENTA afectada por BENEFICIO distinto? (S/N): ");
            String opcion = scanner.nextLine().trim().toUpperCase();
            
            if (opcion.equals("S")) {
                System.out.println("Redirigiendo al menú de Actualizar Venta para corregir el beneficio...");
                actualizarVenta(); 
            }
        }
        
        if (duplicidadUbicacion) {
            System.out.println("\nADVERTENCIA: Se detectó que el mismo nombre de cliente compró en distintas ubicaciones. Esto puede ser normal si es una compra múltiple.");
        }


        if (!inconsistenciaEncontrada && !duplicidadBeneficio && !duplicidadUbicacion) {
            System.out.println("\nTodos los datos son consistentes y no se encontraron duplicidades críticas de clientes.");
        } else if (inconsistenciaEncontrada) {
             System.out.println("\nIntegridad de datos verificada con ERRORES. Favor de revisar las inconsistencias reportadas (Asiento/Venta).");
        } else if (duplicidadBeneficio || duplicidadUbicacion) {
            System.out.println("\nIntegridad verificada con ADVERTENCIAS de duplicidad de clientes. (Revise los reportes anteriores).");
        }
    }


    // --- MENÚ PRINCIPAL ---
    public void iniciarSistema() {
        while (true) {
            System.out.println("\n..:: Menú " + NOMBRE_TEATRO + " ::..");
            System.out.println("1. Venta de entradas");
            System.out.println("2. Visualizar resumen de ventas");
            System.out.println("3. Imprimir Boleta");
            System.out.println("4. Actualizar venta"); 
            System.out.println("5. Anular/Eliminar venta");
            System.out.println("6. Calcular Ingresos Totales");
            System.out.println("7. Verificar Integridad de Datos");
            System.out.println("8. Salir del Programa");
            System.out.print("\nSeleccione una opción: ");

            String entrada = scanner.nextLine().trim();
            int opcion;
            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. Ingrese un número.");
                continue;
            }

            switch (opcion) {
                case 1 -> venderEntradas();
                case 2 -> visualizarResumenVentas();
                case 3 -> imprimirBoleta();
                case 4 -> actualizarVenta();
                case 5 -> eliminarVenta();
                case 6 -> calcularIngresosTotales();
                case 7 -> validarReferenciasCruzadas();
                case 8 -> {
                    System.out.println("Gracias por su visita al Teatro Moro.");
                    return;
                }
                default -> System.out.println("Opción no reconocida. Intente de nuevo.");
            }
        }
    }

    // Método principal
    public static void main(String[] args) {
        Exp3_S8_Daniel_Caballero sistema = new Exp3_S8_Daniel_Caballero();
        sistema.iniciarSistema();
    }
}