package com.example.lukas.monteurs                              ;

public class IncidentModel                                      {
    public int id                                               ;
    public String omschrijving                                  ;
    public String categorie                                     ;            
    public String image_path                                    ;
    public float longitude                                      ;
    public float latitude                                       ;
    public String date                                          ;

    public IncidentModel(int id, String omschrijving, String categorie, String  image_path, float latitude,
                         float longitude, String date)          {
        this.id             =  id                               ;
        this.categorie      =  categorie                        ;
        this.omschrijving   = omschrijving                      ;
        this.image_path     = image_path                        ;
        this.latitude       = latitude                          ;      
        this.longitude      = longitude                         ;
        this.date           = date                              ;
                                                                }
                                                                }
