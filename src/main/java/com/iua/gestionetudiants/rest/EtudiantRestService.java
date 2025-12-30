package com.iua.gestionetudiants.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iua.gestionetudiants.model.Etudiant;
import com.iua.gestionetudiants.model.Note;
import com.iua.gestionetudiants.service.EtudiantService;
import com.iua.gestionetudiants.service.NoteService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service REST pour la gestion des étudiants
 * Expose les données au format JSON
 */
@Path("/etudiants")
public class EtudiantRestService {

    private EtudiantService etudiantService = new EtudiantService();
    private NoteService noteService = new NoteService();
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting()
            .create();

    /**
     * GET /api/etudiants
     * Retourne la liste de tous les étudiants au format JSON
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listerTous() {
        try {
            List<Etudiant> etudiants = etudiantService.listerTous();

            // Convertir en format simplifié pour éviter les problèmes de sérialisation
            List<Map<String, Object>> etudiantsSimplifies = etudiants.stream()
                .map(this::convertirEtudiantEnMap)
                .collect(Collectors.toList());

            String json = gson.toJson(etudiantsSimplifies);
            return Response.ok(json).build();

        } catch (Exception e) {
            Map<String, String> erreur = new HashMap<>();
            erreur.put("erreur", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(gson.toJson(erreur))
                    .build();
        }
    }

    /**
     * GET /api/etudiants/{id}
     * Retourne les détails d'un étudiant au format JSON
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response trouverParId(@PathParam("id") Long id) {
        try {
            Etudiant etudiant = etudiantService.trouverParIdAvecNotes(id);

            if (etudiant == null) {
                Map<String, String> erreur = new HashMap<>();
                erreur.put("erreur", "Étudiant introuvable");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(gson.toJson(erreur))
                        .build();
            }

            Map<String, Object> etudiantMap = convertirEtudiantEnMapAvecNotes(etudiant);
            String json = gson.toJson(etudiantMap);
            return Response.ok(json).build();

        } catch (Exception e) {
            Map<String, String> erreur = new HashMap<>();
            erreur.put("erreur", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(gson.toJson(erreur))
                    .build();
        }
    }

    /**
     * GET /api/etudiants/{id}/notes
     * Retourne les notes d'un étudiant au format JSON
     */
    @GET
    @Path("/{id}/notes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listerNotes(@PathParam("id") Long id) {
        try {
            Etudiant etudiant = etudiantService.trouverParId(id);

            if (etudiant == null) {
                Map<String, String> erreur = new HashMap<>();
                erreur.put("erreur", "Étudiant introuvable");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(gson.toJson(erreur))
                        .build();
            }

            List<Note> notes = noteService.listerParEtudiant(id);
            double moyenne = noteService.calculerMoyenne(notes);

            // Créer la réponse
            Map<String, Object> reponse = new HashMap<>();
            reponse.put("etudiant", convertirEtudiantEnMap(etudiant));
            reponse.put("notes", notes.stream()
                .map(this::convertirNoteEnMap)
                .collect(Collectors.toList()));
            reponse.put("moyenne", moyenne);
            reponse.put("nombreNotes", notes.size());

            String json = gson.toJson(reponse);
            return Response.ok(json).build();

        } catch (Exception e) {
            Map<String, String> erreur = new HashMap<>();
            erreur.put("erreur", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(gson.toJson(erreur))
                    .build();
        }
    }

    /**
     * Convertir un étudiant en Map (sans les notes)
     */
    private Map<String, Object> convertirEtudiantEnMap(Etudiant etudiant) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", etudiant.getId());
        map.put("matricule", etudiant.getMatricule());
        map.put("nom", etudiant.getNom());
        map.put("prenom", etudiant.getPrenom());
        map.put("email", etudiant.getEmail());
        map.put("dateNaissance", etudiant.getDateNaissance());
        map.put("dateCreation", etudiant.getDateCreation());
        return map;
    }

    /**
     * Convertir un étudiant en Map (avec les notes)
     */
    private Map<String, Object> convertirEtudiantEnMapAvecNotes(Etudiant etudiant) {
        Map<String, Object> map = convertirEtudiantEnMap(etudiant);
        List<Map<String, Object>> notes = etudiant.getNotes().stream()
            .map(this::convertirNoteEnMap)
            .collect(Collectors.toList());
        map.put("notes", notes);
        map.put("moyenne", noteService.calculerMoyenne(etudiant.getNotes()));
        return map;
    }

    /**
     * Convertir une note en Map
     */
    private Map<String, Object> convertirNoteEnMap(Note note) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", note.getId());
        map.put("matiere", note.getMatiere());
        map.put("valeur", note.getValeur());
        map.put("coefficient", note.getCoefficient());
        map.put("dateCreation", note.getDateCreation());
        return map;
    }
}
