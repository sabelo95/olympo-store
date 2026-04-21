package com.loginservice.Repository;

import com.loginservice.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository <Usuario, Long> {

    public Usuario findByCorreo(String correo);

}
