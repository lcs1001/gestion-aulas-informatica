package gestionaulasinformatica.app.security;

import gestionaulasinformatica.backend.entity.Usuario;

@FunctionalInterface
public interface CurrentUser {
	Usuario getUser();
}