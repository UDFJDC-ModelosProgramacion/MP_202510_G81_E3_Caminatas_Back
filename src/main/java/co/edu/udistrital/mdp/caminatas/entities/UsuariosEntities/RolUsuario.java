package co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities;

public enum RolUsuario {
    // Esta clase puede verse redundante, pero es necesaria para la creación de usuarios
    // y para la asignación de roles en el sistema.
    // Si bien los roles pueden ser gestionados directamente desde los tipos de usuario,
    // es recomendable mantener esta clase para una mejor organización y claridad del código.
    // Por ejemplo el SuperAdmin no puede ser creado desde la interfaz de usuario,
    // sino que debe ser creado internamente por el sistema.
    
    SUPER_ADMIN, // solo se puede crear internamente
    ADMIN_COMENTARIOS,
    NATURAL,
    JURIDICO;
}
