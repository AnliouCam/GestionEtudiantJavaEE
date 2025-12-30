package com.iua.gestionetudiants.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configuration JAX-RS pour les services REST
 * Les services REST seront accessibles via /api/*
 */
@ApplicationPath("/api")
public class RestApplication extends Application {
    // La configuration par défaut suffit
    // Tous les services REST annotés avec @Path seront automatiquement découverts
}
