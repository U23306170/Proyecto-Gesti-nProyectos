package edu.pe.utp.grupo3.hoteldescansoeterno;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import edu.pe.utp.grupo3.hoteldescansoeterno.model.*;
import edu.pe.utp.grupo3.hoteldescansoeterno.repository.*;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class HotelDescansoEternoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelDescansoEternoApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner cargarDatosIniciales(
            HabitacionRepository  habitacionRepo,
            HuespedRepository     huespedRepo,
            ReservaRepository     reservaRepo,
            AmenidadRepository    amenidadRepo) {

        return args -> {
            if (habitacionRepo.count() > 0) {
                System.out.println(">> La BD ya tiene datos — no se insertaron.");
                return;
            }

            // Amenidades
            Amenidad wifi    = amenidadRepo.save(new Amenidad("WiFi"));
            Amenidad tv32    = amenidadRepo.save(new Amenidad("TV 32\""));
            Amenidad tv40    = amenidadRepo.save(new Amenidad("TV 40\""));
            Amenidad tv55    = amenidadRepo.save(new Amenidad("TV 55\""));
            Amenidad agua    = amenidadRepo.save(new Amenidad("Agua caliente"));
            Amenidad ac      = amenidadRepo.save(new Amenidad("A/C"));
            Amenidad minibar = amenidadRepo.save(new Amenidad("Minibar"));
            Amenidad jacuzzi = amenidadRepo.save(new Amenidad("Jacuzzi"));
            Amenidad sala    = amenidadRepo.save(new Amenidad("Sala de mascotas"));

            // Habitaciones
            Habitacion h101 = new Habitacion("101","Simple",120.0,"Disponible",
                "Cama matrimonial, baño privado, vista al jardín, TV 32\" y WiFi.");
            h101.setAmenidades(List.of(wifi, tv32, agua, ac));
            habitacionRepo.save(h101);

            Habitacion h205 = new Habitacion("205","Doble",180.0,"Disponible",
                "Dos camas, vista a la calle, minibar, escritorio y baño privado.");
            h205.setAmenidades(List.of(wifi, tv40, minibar));
            habitacionRepo.save(h205);

            Habitacion h401 = new Habitacion("401","Pet-friendly",450.0,"Disponible",
                "Exclusivamente para amantes de mascotas.");
            h401.setAmenidades(List.of(wifi, tv55, jacuzzi, sala));
            habitacionRepo.save(h401);

            // Huéspedes
            Huesped garcia  = huespedRepo.save(new Huesped("Lucía","García","Quispe",  "72345678","lucia@gmail.com",  "987654321","Activo"));
            Huesped mendoza = huespedRepo.save(new Huesped("José","Mendoza","Torres",  "45678901","jose@gmail.com",   "956123789","Activo"));
            Huesped vargas  = huespedRepo.save(new Huesped("Andrea","Vargas","López",  "61234567","andrea@gmail.com", "945678012","Activo"));

            // Reservas
            reservaRepo.save(new Reserva(garcia, h101,
                LocalDate.of(2026,7,23), LocalDate.of(2026,7,24),
                3, 360.0, "Pendiente", ""));
            reservaRepo.save(new Reserva(mendoza, h205,
                LocalDate.of(2026,7,25), LocalDate.of(2026,7,26),
                3, 540.0, "Pendiente", ""));
            reservaRepo.save(new Reserva(vargas, h401,
                LocalDate.of(2026,7,27), LocalDate.of(2026,7,29),
                3, 1350.0, "Pendiente", ""));

            System.out.println(">> Datos de prueba Hotel El Descanso Eterno cargados.");
        };
    }
}
