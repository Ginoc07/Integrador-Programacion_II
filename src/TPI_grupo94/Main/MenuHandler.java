package TPI_grupo94.Main;

import TPI_grupo94.Models.Libro;
import java.util.List;
import java.util.Scanner;
import TPI_grupo94.Models.FichaBibliografica;
import TPI_grupo94.Service.LibroServiceImpl;

public class MenuHandler {
  
    private final Scanner scanner;
    private final PersonaServiceImpl personaService;

    public MenuHandler(Scanner scanner, PersonaServiceImpl personaService) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner no puede ser null");
        }
        if (personaService == null) {
            throw new IllegalArgumentException("PersonaService no puede ser null");
        }
        this.scanner = scanner;
        this.personaService = personaService;
    }

    public void crearPersona() {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("Apellido: ");
            String apellido = scanner.nextLine().trim();
            System.out.print("DNI: ");
            String dni = scanner.nextLine().trim();

            Domicilio domicilio = null;
            System.out.print("¿Desea agregar un domicilio? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                domicilio = crearDomicilio();
            }

            Persona persona = new Persona(0, nombre, apellido, dni);
            persona.setDomicilio(domicilio);
            personaService.insertar(persona);
            System.out.println("Persona creada exitosamente con ID: " + persona.getId());
        } catch (Exception e) {
            System.err.println("Error al crear persona: " + e.getMessage());
        }
    }

    public void listarPersonas() {
        try {
            System.out.print("¿Desea (1) listar todos o (2) buscar por nombre/apellido? Ingrese opcion: ");
            int subopcion = Integer.parseInt(scanner.nextLine());

            List<Persona> personas;
            if (subopcion == 1) {
                personas = personaService.getAll();
            } else if (subopcion == 2) {
                System.out.print("Ingrese texto a buscar: ");
                String filtro = scanner.nextLine().trim();
                personas = personaService.buscarPorNombreApellido(filtro);
            } else {
                System.out.println("Opcion invalida.");
                return;
            }

            if (personas.isEmpty()) {
                System.out.println("No se encontraron personas.");
                return;
            }

            for (Persona p : personas) {
                System.out.println("ID: " + p.getId() + ", Nombre: " + p.getNombre() +
                        ", Apellido: " + p.getApellido() + ", DNI: " + p.getDni());
                if (p.getDomicilio() != null) {
                    System.out.println("   Domicilio: " + p.getDomicilio().getCalle() +
                            " " + p.getDomicilio().getNumero());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar personas: " + e.getMessage());
        }
    }

    public void actualizarPersona() {
        try {
            System.out.print("ID de la persona a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());
            Persona p = personaService.getById(id);

            if (p == null) {
                System.out.println("Persona no encontrada.");
                return;
            }

            System.out.print("Nuevo nombre (actual: " + p.getNombre() + ", Enter para mantener): ");
            String nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()) {
                p.setNombre(nombre);
            }

            System.out.print("Nuevo apellido (actual: " + p.getApellido() + ", Enter para mantener): ");
            String apellido = scanner.nextLine().trim();
            if (!apellido.isEmpty()) {
                p.setApellido(apellido);
            }

            System.out.print("Nuevo DNI (actual: " + p.getDni() + ", Enter para mantener): ");
            String dni = scanner.nextLine().trim();
            if (!dni.isEmpty()) {
                p.setDni(dni);
            }

            actualizarDomicilioDePersona(p);
            personaService.actualizar(p);
            System.out.println("Persona actualizada exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al actualizar persona: " + e.getMessage());
        }
    }

    public void eliminarPersona() {
        try {
            System.out.print("ID de la persona a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());
            personaService.eliminar(id);
            System.out.println("Persona eliminada exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al eliminar persona: " + e.getMessage());
        }
    }

    public void crearDomicilioIndependiente() {
        try {
            Domicilio domicilio = crearDomicilio();
            personaService.getDomicilioService().insertar(domicilio);
            System.out.println("Domicilio creado exitosamente con ID: " + domicilio.getId());
        } catch (Exception e) {
            System.err.println("Error al crear domicilio: " + e.getMessage());
        }
    }

    public void listarDomicilios() {
        try {
            List<Domicilio> domicilios = personaService.getDomicilioService().getAll();
            if (domicilios.isEmpty()) {
                System.out.println("No se encontraron domicilios.");
                return;
            }
            for (Domicilio d : domicilios) {
                System.out.println("ID: " + d.getId() + ", " + d.getCalle() + " " + d.getNumero());
            }
        } catch (Exception e) {
            System.err.println("Error al listar domicilios: " + e.getMessage());
        }
    }

    public void actualizarDomicilioPorId() {
        try {
            System.out.print("ID del domicilio a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());
            Domicilio d = personaService.getDomicilioService().getById(id);

            if (d == null) {
                System.out.println("Domicilio no encontrado.");
                return;
            }

            System.out.print("Nueva calle (actual: " + d.getCalle() + ", Enter para mantener): ");
            String calle = scanner.nextLine().trim();
            if (!calle.isEmpty()) {
                d.setCalle(calle);
            }

            System.out.print("Nuevo numero (actual: " + d.getNumero() + ", Enter para mantener): ");
            String numero = scanner.nextLine().trim();
            if (!numero.isEmpty()) {
                d.setNumero(numero);
            }

            personaService.getDomicilioService().actualizar(d);
            System.out.println("Domicilio actualizado exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al actualizar domicilio: " + e.getMessage());
        }
    }

    public void eliminarDomicilioPorId() {
        try {
            System.out.print("ID del domicilio a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());
            personaService.getDomicilioService().eliminar(id);
            System.out.println("Domicilio eliminado exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al eliminar domicilio: " + e.getMessage());
        }
    }

    public void actualizarDomicilioPorPersona() {
        try {
            System.out.print("ID de la persona cuyo domicilio desea actualizar: ");
            int personaId = Integer.parseInt(scanner.nextLine());
            Persona p = personaService.getById(personaId);

            if (p == null) {
                System.out.println("Persona no encontrada.");
                return;
            }

            if (p.getDomicilio() == null) {
                System.out.println("La persona no tiene domicilio asociado.");
                return;
            }

            Domicilio d = p.getDomicilio();
            System.out.print("Nueva calle (" + d.getCalle() + "): ");
            String calle = scanner.nextLine().trim();
            if (!calle.isEmpty()) {
                d.setCalle(calle);
            }

            System.out.print("Nuevo numero (" + d.getNumero() + "): ");
            String numero = scanner.nextLine().trim();
            if (!numero.isEmpty()) {
                d.setNumero(numero);
            }

            personaService.getDomicilioService().actualizar(d);
            System.out.println("Domicilio actualizado exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al actualizar domicilio: " + e.getMessage());
        }
    }

    public void eliminarDomicilioPorPersona() {
        try {
            System.out.print("ID de la persona cuyo domicilio desea eliminar: ");
            int personaId = Integer.parseInt(scanner.nextLine());
            Persona p = personaService.getById(personaId);

            if (p == null) {
                System.out.println("Persona no encontrada.");
                return;
            }

            if (p.getDomicilio() == null) {
                System.out.println("La persona no tiene domicilio asociado.");
                return;
            }

            int domicilioId = p.getDomicilio().getId();
            personaService.eliminarDomicilioDePersona(personaId, domicilioId);
            System.out.println("Domicilio eliminado exitosamente y referencia actualizada.");
        } catch (Exception e) {
            System.err.println("Error al eliminar domicilio: " + e.getMessage());
        }
    }

    private Domicilio crearDomicilio() {
        System.out.print("Calle: ");
        String calle = scanner.nextLine().trim();
        System.out.print("Numero: ");
        String numero = scanner.nextLine().trim();
        return new Domicilio(0, calle, numero);
    }

    private void actualizarDomicilioDePersona(Persona p) throws Exception {
        if (p.getDomicilio() != null) {
            System.out.print("¿Desea actualizar el domicilio? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                System.out.print("Nueva calle (" + p.getDomicilio().getCalle() + "): ");
                String calle = scanner.nextLine().trim();
                if (!calle.isEmpty()) {
                    p.getDomicilio().setCalle(calle);
                }

                System.out.print("Nuevo numero (" + p.getDomicilio().getNumero() + "): ");
                String numero = scanner.nextLine().trim();
                if (!numero.isEmpty()) {
                    p.getDomicilio().setNumero(numero);
                }

                personaService.getDomicilioService().actualizar(p.getDomicilio());
            }
        } else {
            System.out.print("La persona no tiene domicilio. ¿Desea agregar uno? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                Domicilio nuevoDom = crearDomicilio();
                personaService.getDomicilioService().insertar(nuevoDom);
                p.setDomicilio(nuevoDom);
            }
        }
    }
}